package lilypuree.unearthed.world.feature.gen;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lilypuree.unearthed.util.noise.FastNoiseLite;
import lilypuree.unearthed.world.feature.BiomeBasedReplacer;
import lilypuree.unearthed.world.feature.StoneRegion;
import lilypuree.unearthed.world.feature.StoneReplacerConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.Map;

public class NoiseHolder {
    private static FastNoiseLite regionNoise;

    private Map<Integer, Map<Integer, ChunkFiller.StonetypeGetter>> roughNoiseSamplers = new Int2ObjectOpenHashMap<>(3);
    private Map<Integer, Map<Integer, ChunkFiller.StonetypeGetter>> strataNoiseSamplers = new Int2ObjectArrayMap<>(5);

    private StoneReplacerConfiguration configuration;
    private WorldGenLevel level;
    private int baseX;
    private int baseY;
    private int baseZ;
    private int[][] heights = new int[17][17];
    private int[][] strata_depth = new int[5][5];
    private BiomeBasedReplacer[][] replacers = new BiomeBasedReplacer[5][5];
    private StoneRegion[][] regions = new StoneRegion[5][5];

    public NoiseHolder(StoneReplacerConfiguration cfg, WorldGenLevel worldGenLevel, int posX, int minY, int posZ) {
        this.configuration = cfg;
        this.level = worldGenLevel;
        this.baseX = posX;
        this.baseY = minY;
        this.baseZ = posZ;
    }

    public int[][] getHeights() {
        return heights;
    }

    public int getMaxHeight(ChunkAccess chunkAccess) {
        int maxHeight = Integer.MIN_VALUE;
        for (int x = 0; x < 17; x++) {
            for (int z = 0; z < 17; z++) {
                int height;
                if (x < 16 && z < 16) {
                    height = chunkAccess.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
                } else
                    height = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x + baseX, z + baseZ);
                maxHeight = Math.max(height, maxHeight);
                heights[x][z] = height;
            }
        }
        return maxHeight;
    }

    public StoneType getRoughNoise(int posX, int posY, int posZ) {
        return roughNoiseSamplers
                .computeIfAbsent(posX, i -> new Int2ObjectOpenHashMap<>(3))
                .computeIfAbsent(posZ, i -> getRoughNoiseSampler(posX, posZ)).get(posX, posY, posZ);
    }

    public StoneType getStrataNoise(int posX, int posY, int posZ) {
        return strataNoiseSamplers
                .computeIfAbsent(posX, i -> new Int2ObjectOpenHashMap<>(5))
                .computeIfAbsent(posZ, i -> getPrimaryNoiseSampler(posX, posZ)).get(posX, posY, posZ);
    }

    public StoneType getCombinedNoise(int posX, int posY, int posZ) {
        StoneType state = getRoughNoise(posX, posY, posZ);
        return state == StoneType.EMPTY ? getStrataNoise(posX, posY, posZ) : state;
    }


    public StoneType getBiomeStrata(int posX, int posY, int posZ) {
        int x1 = (posX - baseX) >> 2;
        int z1 = (posZ - baseZ) >> 2;
        BiomeBasedReplacer replacer = replacers[x1][z1];
        int height = heights[posX - baseX][posZ - baseZ];
        int depth = height - posY;
        if (replacer == null) {
            Biome biome = level.getBiome(new BlockPos(posX, baseY + height, posZ));
            replacer = configuration.getForBiome(biome);
            strata_depth[x1][z1] = replacer.getTotalThickness();
            replacers[x1][z1] = replacer;
        }
        if (depth < strata_depth[x1][z1]) {
            return replacer.compute(depth);
        } else return null;
    }

    public StoneRegion getRegion(int posX, int posZ) {
        int x1 = (posX - baseX) >> 2;
        int z1 = (posZ - baseZ) >> 2;
        StoneRegion region = regions[x1][z1];
        if (region == null) {
            region = configuration.getRegions(regionNoise.GetNoise(posX, posZ));
            regions[x1][z1] = region;
        }
        return region;
    }

    private ChunkFiller.StonetypeGetter getRoughNoiseSampler(int posX, int posZ) {
        int height = heights[posX - baseX][posZ - baseZ];
        return new RoughNoiseSampler(getRegion(posX, posZ), posX, posZ, baseY, height);
    }

    private ChunkFiller.StonetypeGetter getPrimaryNoiseSampler(int posX, int posZ) {
        return new PrimaryNoiseSampler(getRegion(posX, posZ).primary(), posX, posZ, baseY);
    }

    public static void setSeed(int seed, float regionFrequency) {
        regionNoise = new FastNoiseLite(seed);
        regionNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        regionNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        regionNoise.SetFrequency(regionFrequency);
    }
}

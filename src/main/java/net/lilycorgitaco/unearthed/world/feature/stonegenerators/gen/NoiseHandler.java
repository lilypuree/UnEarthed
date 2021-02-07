package net.lilycorgitaco.unearthed.world.feature.stonegenerators.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.openhft.hashing.LongHashFunction;
import net.lilycorgitaco.unearthed.util.noise.FastNoiseLite;
import net.lilycorgitaco.unearthed.util.noise.FastNoiseLite.Vector3;
import net.lilycorgitaco.unearthed.util.noise.FastNoiseLite.Vector2;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.CellularOre;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.regions.Region;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.regions.RegionManager;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NoiseHandler {
    private static float regionFreq = 0.0009f;
    private static float primaryFreq = 0.008f;
    private static float orogenFreq = 0.055f;
    private static float secondaryFreq = 0.002f;
    private static float batolithFreq = 0.001f;
    private static float tertiaryFreq = 0.09f;
    private static float stratumFreq = 0.01f;
    public static float orogenWarpAmplitude = 400;

    private static FastNoiseLite regionNoise;
    private static FastNoiseLite primaryNoise;
    private static FastNoiseLite primaryWarp;
    private static FastNoiseLite orogenNoise;
    private static FastNoiseLite orogenWarp;

    private static FastNoiseLite secondaryNoise;
    private static FastNoiseLite secondaryWarp;

    private static FastNoiseLite batolithTypeQF;
    private static FastNoiseLite batolithTypeAP;
    private static FastNoiseLite batolithHeight;

    private static FastNoiseLite tertiaryNoise;

    private static FastNoiseLite stratumBorders;
    private static FastNoiseLite stratumWarp;
    public static float strataWarpAmplitude = 60;
    private static float tertiaryCutoff = 0.992f;

    private StructureWorldAccess world;
    private Biome[][] biomes;
    private Region[][] regions;
    private int[][] maxHeights;
    private int[][] secondaryDepths;
    private int[][] batolithHeights;
    private State[][] batolithStates;
    private BlockPos basePos;
    private Map<BlockPos, Region> regionMap = new HashMap<>();

    public NoiseHandler(StructureWorldAccess world, BlockPos pos) {
        this.world = world;
        this.basePos = pos;
        this.maxHeights = new int[17][17];
        biomes = new Biome[17][17];
        regions = new Region[17][17];
        secondaryDepths = new int[17][17];
        batolithHeights = new int[17][17];
        batolithStates = new State[17][17];
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                secondaryDepths[i][j] = -1;
                batolithHeights[i][j] = -1;
                batolithStates[i][j] = null;
                regions[i][j] = null;
                maxHeights[i][j] = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + i, pos.getZ() + j);
            }
        }
    }

    public State getState(int x, int z, int y) {
        int xPos = basePos.getX() + x;
        int zPos = basePos.getZ() + z;

        float tertiary = tertiaryNoise.GetNoise(xPos, zPos, y);
        tertiary *= tertiary * tertiary;
        if (tertiary > tertiaryCutoff) {
            return RegionManager.getTertiaryState((tertiary - tertiaryCutoff) * 100);
        }
        Region region = getRegionSimple(x, z);
        if (secondaryDepths[x][z] < 0) {
            secondaryDepths[x][z] = getSecondaryDepth(region, xPos, zPos);
        }
        boolean isSecondary = secondaryDepths[x][z] != 0 && (maxHeights[x][z] - y) < secondaryDepths[x][z];
        if (batolithHeights[x][z] < 0) {
            batolithHeights[x][z] = (int) (getBatolithPercentage(region, xPos, zPos) * maxHeights[x][z]);
        }
        boolean isBatolith = y < batolithHeights[x][z] || batolithHeights[x][z] > maxHeights[x][z] * 0.9f;
        if (isSecondary && (!isBatolith || !region.batholithIntrudes())) {
            return region.getSecondaryState();
        } else if (isBatolith && (!isSecondary || region.batholithIntrudes())) {
            if (batolithStates[x][z] == null) {
                batolithStates[x][z] = getBatolithState(xPos, zPos);
            }
            return batolithStates[x][z];
        } else {
            Vector3 originalPos = new Vector3(xPos, zPos, y / 3.0f);
            Vector3 orogenPerturb = new Vector3(xPos, zPos, y / 3.0f);
            orogenWarp.DomainWarp(orogenPerturb);
            if (isOrogen(region, originalPos, orogenPerturb)) {
                return region.getOrogenState((orogenNoise.GetNoise(orogenPerturb.x, orogenPerturb.y, orogenPerturb.z)) / 2 + 0.5f);
            } else {
                Vector3 primaryPerturb = new Vector3(xPos, zPos, y);
                primaryWarp.DomainWarp(primaryPerturb);
                return region.getPrimaryState(primaryNoise.GetNoise(primaryPerturb.x, primaryPerturb.y) / 2 + 0.5f);
            }
        }
    }

    private boolean isOrogen(Region region, Vector3 originalPos, Vector3 perturbed) {
        float xDiff = (perturbed.x - originalPos.x) / orogenWarpAmplitude;
        float yDiff = (perturbed.y - originalPos.y) / orogenWarpAmplitude;
        float zDiff = (perturbed.z - originalPos.z) / orogenWarpAmplitude;
        float scale = (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
        float similarity = ((float) (Math.abs(xDiff + yDiff + zDiff) * Math.sqrt(3)));
        return region.isOrogen(scale, similarity);
    }

    public CellularOre getReplacement(int x, int z, int y, int tier) {
        random.setSeed(NoiseUtil.getSeed(x, z, y, world.getSeed()));
        return getRegionSimple(x - basePos.getX(), z - basePos.getZ())
                .getReplacement(tier, random);
    }

    private Random random = new Random();

    public StratAutomata.StrataState getStrata(int xPos, int zPos, int strataLevel) {
        long worldSeed = world.getSeed();
        Vector2 v = new Vector2(xPos, zPos);
        stratumWarp.SetSeed((int) worldSeed + strataLevel * 100811);
        stratumBorders.SetSeed((int) worldSeed + strataLevel * 100811);
        stratumWarp.DomainWarp(v);
        Pair<Vector2, Float> cenAndDist = stratumBorders.GetCellularCenter2Distance(v.x, v.y);
        Region region = getRegionByMap((int) cenAndDist.getKey().x, (int) cenAndDist.getKey().y);
        random.setSeed(NoiseUtil.getSeed((int) cenAndDist.getKey().x, (int) cenAndDist.getKey().y, worldSeed));
        if (cenAndDist.getValue() * cenAndDist.getValue() > region.getStratumPercentage()) {
            State state = region.getStrataState(random.nextFloat(), strataLevel);
            if (state != null) {
                return new StratAutomata.StrataState(state, region.getStratumDepth(random, strataLevel));
            }
        }
        return null;
    }

    private Region getRegionSimple(int x, int z) {
        if (regions[x][z] == null) {
            int xPos = basePos.getX() + x;
            int zPos = basePos.getZ() + z;
            Biome biome = getBiome(x, z);
            regions[x][z] = RegionManager.getRegion(biome, regionNoise.GetNoise(xPos, zPos) / 2 + 0.5f, world);
        }
        return regions[x][z];
    }

    private Region getRegionByMap(int xPos, int zPos) {
        BlockPos pos = new BlockPos(xPos, 0, zPos);
        return regionMap.computeIfAbsent(pos, p -> {
            Biome biome = world.getBiome(p);
            return RegionManager.getRegion(biome, regionNoise.GetNoise(xPos, zPos) / 2 + 0.5f, world);
        });
    }

    public Biome getBiome(int x, int z) {
        Biome biome = biomes[x][z];
        if (biome == null) {
            int xPos = basePos.getX() + x;
            int zPos = basePos.getZ() + z;
            BlockPos pos = new BlockPos(xPos, 0, zPos);
            biome = world.getBiome(pos);
            biomes[x][z] = biome;
            return biome;
        } else {
            return biome;
        }
    }

    private int getSecondaryDepth(Region region, int xPos, int zPos) {
        Vector2 xzPos = new Vector2(xPos, zPos);
        secondaryWarp.DomainWarp(xzPos);
        return region.getSecondaryDepth(secondaryNoise.GetNoise(xzPos.x, xzPos.y));
    }

    private State getBatolithState(int xPos, int zPos) {
        return RegionManager.getBatolithState(batolithTypeQF.GetNoise(xPos, zPos), batolithTypeAP.GetNoise(xPos, zPos));
    }

    private float getBatolithPercentage(Region region, int xPos, int zPos) {
        return region.getBatolithPercentage(batolithHeight.GetNoise(xPos, zPos));
    }

    public static void setSeed(long seed) {
        if (regionNoise == null) {
            regionNoise = new FastNoiseLite((int) seed + 44472391);
            regionNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            regionNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
            regionNoise.SetFractalType(FastNoiseLite.FractalType.None);
            regionNoise.SetFrequency(regionFreq);
        }
        if (primaryNoise == null) {
            primaryNoise = new FastNoiseLite((int) seed + 39921723);
            primaryNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            primaryNoise.SetFractalType(FastNoiseLite.FractalType.None);
            primaryNoise.SetFrequency(primaryFreq);
            primaryNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        }
        if (orogenNoise == null) {
            orogenNoise = new FastNoiseLite((int) seed + 39921723);
            orogenNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            orogenNoise.SetFractalType(FastNoiseLite.FractalType.None);
            orogenNoise.SetFrequency(orogenFreq);
            orogenNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        }
        if (orogenWarp == null) {
            orogenWarp = new FastNoiseLite((int) seed + 9484585);
            orogenWarp.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            orogenWarp.SetFractalOctaves(2);
            orogenWarp.SetDomainWarpAmp(orogenWarpAmplitude);
            orogenWarp.SetFrequency(primaryFreq / 12);
            orogenWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
        }
        if (primaryWarp == null) {
            primaryWarp = new FastNoiseLite((int) seed + 98941);
            primaryWarp.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            primaryWarp.SetFractalOctaves(3);
            primaryWarp.SetDomainWarpAmp(40);
            primaryWarp.SetFrequency(primaryFreq / 15);
            primaryWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
        }
        if (secondaryNoise == null) {
            secondaryNoise = new FastNoiseLite((int) seed + 7874837);
            secondaryNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            secondaryNoise.SetFractalType(FastNoiseLite.FractalType.None);
            secondaryNoise.SetFrequency(secondaryFreq);
        }
        if (secondaryWarp == null) {
            secondaryWarp = new FastNoiseLite((int) seed + 399918);
            secondaryWarp.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            secondaryWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
            secondaryWarp.SetFractalOctaves(2);
            secondaryWarp.SetDomainWarpAmp(40);
            secondaryWarp.SetFrequency(secondaryFreq * 2f);
        }

        if (batolithTypeQF == null) {
            batolithTypeQF = new FastNoiseLite((int) seed + 211030);
            batolithTypeQF.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            batolithTypeQF.SetFractalOctaves(2);
            batolithTypeQF.SetFrequency(batolithFreq / 2);
        }
        if (batolithTypeAP == null) {
            batolithTypeAP = new FastNoiseLite((int) seed + 22991135);
            batolithTypeAP.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            batolithTypeAP.SetFractalOctaves(2);
            batolithTypeAP.SetFrequency(batolithFreq / 2);
        }
        if (batolithHeight == null) {
            batolithHeight = new FastNoiseLite((int) seed + 6303439);
            batolithHeight.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            batolithHeight.SetCellularReturnType(FastNoiseLite.CellularReturnType.Distance);
            batolithHeight.SetFractalType(FastNoiseLite.FractalType.FBm);
            batolithHeight.SetFractalLacunarity(2.2f);
            batolithHeight.SetFractalOctaves(4);
            batolithHeight.SetFrequency(batolithFreq);
        }
        if (stratumBorders == null) {
            stratumBorders = new FastNoiseLite(((int) seed));
            stratumBorders.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            stratumBorders.SetFractalType(FastNoiseLite.FractalType.None);
            stratumBorders.SetFrequency(stratumFreq);
        }
        if (stratumWarp == null) {
            stratumWarp = new FastNoiseLite((int) seed);
            stratumWarp.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            stratumWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
            stratumWarp.SetDomainWarpAmp(strataWarpAmplitude);
            stratumWarp.SetFrequency(stratumFreq * 2f);
            stratumWarp.SetFractalOctaves(4);
        }
        if (tertiaryNoise == null) {
            tertiaryNoise = new FastNoiseLite((int) seed + 19929411);
            tertiaryNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            tertiaryNoise.SetFractalType(FastNoiseLite.FractalType.None);
            tertiaryNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
            tertiaryNoise.SetFrequency(tertiaryFreq);
        }
    }

    private static final int UINT_MAX = Integer.parseUnsignedInt("4294967295");

    public int randomInt(int bound, int x, int y, int z) {
        return (int) getHash(x, y, z) % bound;
    }

    public float randomFloat(int x, int y, int z, int w) {
        return (int) getHash(x, y, z, w) / (float) UINT_MAX;
    }

    public int randomIntFast(int max, int x, int y, int z) {
        return (int) getHash(x, y, z) & max;
    }

    public long getHash(int x, int y, int z) {
        return LongHashFunction.xx(world.getSeed()).hashInts(new int[]{x, y, z});
    }

    public long getHash(int x, int y, int z, int w) {
        return LongHashFunction.xx(world.getSeed()).hashInts(new int[]{x, y, z, w});
    }
}

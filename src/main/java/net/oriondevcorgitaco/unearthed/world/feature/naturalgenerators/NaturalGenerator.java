package net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators;

import com.mojang.serialization.Codec;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.ConfigBlockReader;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.util.noise.FNVector3f;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoise;
import net.oriondevcorgitaco.unearthed.world.feature.ChunkCoordinatesFeature;

import java.util.Random;

public class NaturalGenerator extends ChunkCoordinatesFeature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> UNDERGROUND_STONE = RegistrationHelper.registerFeature("natural_generator", new NaturalGenerator(DefaultFeatureConfig.CODEC));

    public NaturalGenerator(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    FastNoise selectorNoise = null;
    FastNoise cellNoise1 = null;
    FastNoise cellNoise2 = null;
    FastNoise perturb1 = null;
    FastNoise perturb2 = null;
    FastNoise perturbSmall1 = null;
    FastNoise perturbSmall2 = null;

    double storedNoiseHigh = 0;
    double storedNoiseLow = 0;


    private void getHighestNoisePoint(double noise) {
        if (noise > storedNoiseHigh) {
            storedNoiseHigh = noise;
            Unearthed.LOGGER.info("Highest Noise point: " + storedNoiseHigh);
        }
        if (noise < storedNoiseLow) {
            storedNoiseLow = noise;
            Unearthed.LOGGER.info("Lowest Noise point: " + storedNoiseLow);
        }
    }


    @Override
    public boolean generate(StructureWorldAccess world, Random random, Chunk chunk, int x, int z, DefaultFeatureConfig config) {
        setSeed(world.getSeed());

        int xPos = x & 15;
        int zPos = z & 15;
        BlockPos.Mutable mutable = new BlockPos.Mutable(xPos, 0, zPos);

        int topY = chunk.sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, x, z);

        for (int y = 0; y < topY; y++) {
            if (topY < 2)
                topY = 256;

            FNVector3f perturb3f = new FNVector3f(x, mutable.getY(), z);
            perturb1.GradientPerturb(perturb3f);
            perturbSmall1.GradientPerturb(perturb3f);

            FNVector3f perturb3f2 = new FNVector3f(x, mutable.getY(), z);
            perturb2.GradientPerturb(perturb3f2);
            perturbSmall2.GradientPerturb(perturb3f2);

            double cellNoise1 = this.cellNoise1.GetNoise(perturb3f.x, perturb3f.y, perturb3f.z);
            double cellNoise2 = this.cellNoise2.GetNoise(perturb3f2.x, perturb3f2.y, perturb3f2.z);

            double selectorNoiseValue = selectorNoise.GetValue(x, mutable.getY(), z) * 12 + 0.5;

            double clampedValue = MathHelper.clampedLerp(cellNoise1, cellNoise2, selectorNoiseValue);
            if (FabricLoader.getInstance().isDevelopmentEnvironment())
                getHighestNoisePoint(clampedValue);

            ConfigBlockReader reader = ConfigBlockReader.blocksFromConfig.get((int) ((clampedValue / 2.0 + 0.5) * ConfigBlockReader.blocksFromConfig.size()));

            BlockState mutableState = chunk.getBlockState(mutable);

            if (useStoneTag(chunk, mutable))
                chunk.setBlockState(mutable, reader.getBlock().getDefaultState(), false);

            else if (Unearthed.UE_CONFIG.generation.replaceCobble && mutableState.getBlock() == Blocks.COBBLESTONE)
                chunk.setBlockState(mutable, reader.getCobbleBlock(mutableState).getDefaultState(), false);

            else if (mutableState == Blocks.COAL_ORE.getDefaultState())
                chunk.setBlockState(mutable, reader.getCoalOre(mutableState).getDefaultState(), false);

            else if (mutableState == Blocks.IRON_ORE.getDefaultState())
                chunk.setBlockState(mutable, reader.getIronOre(mutableState).getDefaultState(), false);

            else if (mutableState == Blocks.GOLD_ORE.getDefaultState())
                chunk.setBlockState(mutable, reader.getGoldOre(mutableState).getDefaultState(), false);

            else if (mutableState == Blocks.LAPIS_ORE.getDefaultState())
                chunk.setBlockState(mutable, reader.getLapisOre(mutableState).getDefaultState(), false);

            else if (mutableState == Blocks.REDSTONE_ORE.getDefaultState())
                chunk.setBlockState(mutable, reader.getRedstoneOre(mutableState).getDefaultState(), false);

            else if (mutableState == Blocks.DIAMOND_ORE.getDefaultState())
                chunk.setBlockState(mutable, reader.getDiamondOre(mutableState).getDefaultState(), false);

            else if (mutableState == Blocks.EMERALD_ORE.getDefaultState())
                chunk.setBlockState(mutable, reader.getEmeraldOre(mutableState).getDefaultState(), false);

            mutable.move(Direction.UP);

        }
        return true;
    }

    public static boolean useStoneTag(Chunk chunk, BlockPos mutable) {
        boolean stoneTag = Unearthed.UE_CONFIG.generation.stoneTag;
        if (stoneTag)
            return chunk.getBlockState(mutable).isIn(BlockTags.BASE_STONE_OVERWORLD);
        else
            return chunk.getBlockState(mutable) == Blocks.STONE.getDefaultState();

    }

    private void setSeed(long seed) {
        float scaleFactor = (float) Unearthed.UE_CONFIG.generation.naturalgeneratorv1.stoneVeinScaleFactor;
        float perturbAmpStrength = (float) Unearthed.UE_CONFIG.generation.naturalgeneratorv1.perturbAmpStrength;
        float smallPerturbAmpStrength = (float) Unearthed.UE_CONFIG.generation.naturalgeneratorv1.smallPerturbAmpStrength;
        int perturbOctaves = Unearthed.UE_CONFIG.generation.naturalgeneratorv1.perturbAmpOctaves;
        int smallPerturbOctaves = Unearthed.UE_CONFIG.generation.naturalgeneratorv1.smallPerturbAmpOctaves;

        if (selectorNoise == null) {
            selectorNoise = new FastNoise((int) seed);
            selectorNoise.SetNoiseType(FastNoise.NoiseType.Simplex);
            selectorNoise.SetFrequency(0.0008F * scaleFactor);
        }

        if (cellNoise1 == null) {
            cellNoise1 = new FastNoise((int) seed + 19495485);
            cellNoise1.SetNoiseType(FastNoise.NoiseType.Cellular);
            cellNoise1.SetFrequency(0.004F * scaleFactor);
        }

        if (cellNoise2 == null) {
            cellNoise2 = new FastNoise((int) seed + 9484585);
            cellNoise2.SetNoiseType(FastNoise.NoiseType.Cellular);
            cellNoise2.SetFrequency(0.004F * scaleFactor);
        }

        if (perturb1 == null) {
            perturb1 = new FastNoise((int) seed + 2838495);
            perturb1.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            perturb1.SetFractalOctaves(perturbOctaves);
            perturb1.SetGradientPerturbAmp(perturbAmpStrength);
            perturb1.SetFrequency(0.008F * scaleFactor);
        }

        if (perturb2 == null) {
            perturb2 = new FastNoise((int) seed + 100);
            perturb2.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            perturb2.SetFractalOctaves(perturbOctaves);
            perturb2.SetGradientPerturbAmp(perturbAmpStrength);
            perturb2.SetFrequency(0.008F * scaleFactor);
        }

        if (perturbSmall1 == null) {
            perturbSmall1 = new FastNoise((int) seed + 9475);
            perturbSmall1.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            perturbSmall1.SetFractalOctaves(smallPerturbOctaves);
            perturbSmall1.SetGradientPerturbAmp(smallPerturbAmpStrength);
            perturbSmall1.SetFrequency(0.06F * scaleFactor);
        }

        if (perturbSmall2 == null) {
            perturbSmall2 = new FastNoise((int) seed + 948556);
            perturbSmall2.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            perturbSmall2.SetFractalOctaves(smallPerturbOctaves);
            perturbSmall2.SetGradientPerturbAmp(smallPerturbAmpStrength);
            perturbSmall2.SetFrequency(0.06F * scaleFactor);
        }
    }
}

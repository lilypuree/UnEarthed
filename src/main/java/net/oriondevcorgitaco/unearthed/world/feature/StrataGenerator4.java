package net.oriondevcorgitaco.unearthed.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.util.noise.FNVector3f;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoise;

import java.util.Random;

public class StrataGenerator4 extends Feature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> UNDERGROUND_STONE = RegistrationHelper.registerFeature("strata_gen4", new StrataGenerator4(DefaultFeatureConfig.CODEC));

    public StrataGenerator4(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    FastNoise selectorNoise = null;
    FastNoise cellNoise1 = null;
    FastNoise cellNoise2 = null;
    FastNoise perturb1 = null;
    FastNoise perturb2 = null;
    FastNoise perturbSmall1 = null;
    FastNoise perturbSmall2 = null;

    public static float scaleFactor = 4.5F;
    public static float perturbAmpStrength = 95;
    public static float smallPerturbAmpStrength = 8;
    public static int perturbOctaves = 5;
    public static int smallPerturbOctaves = 3;

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
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig featureConfig) {
        setSeed(world.getSeed());

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(pos.getX() + x, 0, pos.getZ() + z);
                int topY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

                for (int y = 0; y < topY; y++) {
                    if (world.getBlockState(mutable).getBlock().isIn(BlockTags.BASE_STONE_OVERWORLD)) {
                        FNVector3f perturb3f = new FNVector3f(mutable.getX(), mutable.getY(), mutable.getZ());
                        perturb1.GradientPerturb(perturb3f);
                        perturbSmall1.GradientPerturb(perturb3f);

                        FNVector3f perturb3f2 = new FNVector3f(mutable.getX(), mutable.getY(), mutable.getZ());
                        perturb2.GradientPerturb(perturb3f2);
                        perturbSmall2.GradientPerturb(perturb3f2);


                        double cellNoise1 = this.cellNoise1.GetNoise(perturb3f.x, perturb3f.y, perturb3f.z);
                        double cellNoise2 = this.cellNoise2.GetNoise(perturb3f2.x, perturb3f2.y, perturb3f2.z);

                        double selectorNoiseValue = selectorNoise.GetValue(mutable.getX(), mutable.getY(), mutable.getZ()) * 12 + 0.5;

                        double clampedValue = MathHelper.clampedLerp(cellNoise1, cellNoise2, selectorNoiseValue);
                        getHighestNoisePoint(clampedValue);

                            if (clampedValue > 0.8)
                                world.setBlockState(mutable, BlockGeneratorReference.LIMESTONE.getBlock().getDefaultState(), 2);
                            else if (clampedValue > 0.6)
                                world.setBlockState(mutable, BlockGeneratorReference.MARBLE.getBlock().getDefaultState(), 2);
                            else if (clampedValue > 0.4)
                                world.setBlockState(mutable, BlockGeneratorReference.CONGLOMERATE.getBlock().getDefaultState(), 2);
                            else if (clampedValue > 0)
                                world.setBlockState(mutable, BlockGeneratorReference.QUARTZITE.getBlock().getDefaultState(), 2);
                            else if (clampedValue > -0.2)
                                world.setBlockState(mutable, BlockGeneratorReference.GRAY_BASALT.getBlock().getDefaultState(), 2);
                            else if (clampedValue > -0.4)
                                world.setBlockState(mutable, BlockGeneratorReference.RHYOLITE.getBlock().getDefaultState(), 2);
                            else if (clampedValue > -0.6)
                                world.setBlockState(mutable, BlockGeneratorReference.GABBRO.getBlock().getDefaultState(), 2);
                            else if (clampedValue > -0.8)
                                world.setBlockState(mutable, BlockGeneratorReference.SILTSTONE.getBlock().getDefaultState(), 2);
                            else
                                world.setBlockState(mutable, Blocks.ANDESITE.getDefaultState(), 2);
//                        }
                    }
                    mutable.move(Direction.UP);
                }
            }
        }
        return true;
    }


    private void setSeed(long seed) {
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

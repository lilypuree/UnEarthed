package net.oriondevcorgitaco.unearthed.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoise;

import java.util.Random;

public class StrataGenerator extends Feature<NoFeatureConfig> {
    public static final Feature<NoFeatureConfig> UNDERGROUND_STONE = RegistrationHelper.registerFeature("strata_gen1", new StrataGenerator(NoFeatureConfig.CODEC));

    public StrataGenerator(Codec<NoFeatureConfig> configCodec) {
        super(configCodec);
    }

    FastNoise fastNoise3D = null;
    FastNoise fastNoise3D2 = null;

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        setSeed(world.getSeed());

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(pos.getX() + x, 0, pos.getZ() + z);
                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

                for (int y = 0; y < topY; y++) {
                    //Noise range is between -1 and 1.
                    double noise3D = fastNoise3D.GetNoise(mutable.getX(), mutable.getY(), mutable.getZ());

                    if (world.getBlockState(mutable).getBlock().is(BlockTags.BASE_STONE_OVERWORLD)) {
                        if (world.getBiome(mutable).getBiomeCategory() == Biome.Category.ICY) {
                            if (noise3D > 0.5)
                                world.setBlock(mutable, BlockGeneratorReference.RHYOLITE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlock(mutable, Blocks.DIORITE.defaultBlockState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlock(mutable, Blocks.ANDESITE.defaultBlockState(), 2);
                            else if (noise3D > -0.7)
                                world.setBlock(mutable, BlockGeneratorReference.LIMESTONE.getBaseBlock().defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, BlockGeneratorReference.GABBRO.getBaseBlock().defaultBlockState(), 2);

                        } else if (world.getBiome(mutable).getBiomeCategory() == Biome.Category.MESA) {
                            if (noise3D > 0.5)
                                world.setBlock(mutable, Blocks.RED_TERRACOTTA.defaultBlockState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlock(mutable, Blocks.BLACK_TERRACOTTA.defaultBlockState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlock(mutable, Blocks.YELLOW_TERRACOTTA.defaultBlockState(), 2);
                            else if (noise3D > -0.6)
                                world.setBlock(mutable, Blocks.WHITE_TERRACOTTA.defaultBlockState(), 2);
                            else if (noise3D > -0.7)
                                world.setBlock(mutable, Blocks.GRAY_TERRACOTTA.defaultBlockState(), 2);
                            else if (noise3D > -0.8)
                                world.setBlock(mutable, Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState(), 2);
                            else if (noise3D > -0.9)
                                world.setBlock(mutable, Blocks.TERRACOTTA.defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 2);

                        } else if (world.getBiome(mutable).getBiomeCategory() == Biome.Category.EXTREME_HILLS) {
                            if (noise3D > 0.5)
                                world.setBlock(mutable, BlockGeneratorReference.RHYOLITE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlock(mutable, Blocks.GRANITE.defaultBlockState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlock(mutable, Blocks.ANDESITE.defaultBlockState(), 2);
                            else if (noise3D > -0.7)
                                world.setBlock(mutable, BlockGeneratorReference.RHYOLITE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.9)
                                world.setBlock(mutable, BlockGeneratorReference.RHYOLITE.getBaseBlock().defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, BlockGeneratorReference.KIMBERLITE.getBaseBlock().defaultBlockState(), 2);


                        } else if (world.getBiome(mutable).getBiomeCategory() == Biome.Category.EXTREME_HILLS) {
                            if (noise3D > 0.5)
                                world.setBlock(mutable, BlockGeneratorReference.RHYOLITE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlock(mutable, Blocks.DIORITE.defaultBlockState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlock(mutable, Blocks.ANDESITE.defaultBlockState(), 2);
                            else if (noise3D > -0.7)
                                world.setBlock(mutable, BlockGeneratorReference.PILLOW_BASALT.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.9)
                                world.setBlock(mutable, BlockGeneratorReference.KIMBERLITE.getBaseBlock().defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, BlockGeneratorReference.KIMBERLITE.getBaseBlock().defaultBlockState(), 2);

                        } else if (world.getBiome(mutable).getBiomeCategory() == Biome.Category.OCEAN) {
                            if (noise3D > 0.5)
                                world.setBlock(mutable, BlockGeneratorReference.PUMICE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlock(mutable, BlockGeneratorReference.GABBRO.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlock(mutable, BlockGeneratorReference.GABBRO.getBaseBlock().defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, BlockGeneratorReference.PILLOW_BASALT.getBaseBlock().defaultBlockState(), 2);

                        } else if (world.getBiome(mutable).getBiomeCategory() == Biome.Category.DESERT) {
                            if (noise3D > 0.5)
                                world.setBlock(mutable, Blocks.SANDSTONE.defaultBlockState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlock(mutable, BlockGeneratorReference.CONGLOMERATE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlock(mutable, BlockGeneratorReference.LIMESTONE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.7)
                                world.setBlock(mutable, BlockGeneratorReference.LIMESTONE.getBaseBlock().defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, BlockGeneratorReference.SILTSTONE.getBaseBlock().defaultBlockState(), 2);

                        } else if (world.getBiome(mutable).getBiomeCategory() == Biome.Category.SWAMP) {
                            if (noise3D > 0.5)
                                world.setBlock(mutable, BlockGeneratorReference.SILTSTONE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlock(mutable, BlockGeneratorReference.SILTSTONE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlock(mutable, BlockGeneratorReference.LIGNITE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.7)
                                world.setBlock(mutable, BlockGeneratorReference.LIGNITE.getBaseBlock().defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, BlockGeneratorReference.MUDSTONE.getBaseBlock().defaultBlockState(), 2);

                        } else if (world.getBiome(mutable).getBiomeCategory() == Biome.Category.FOREST) {
                            if (noise3D > 0.5)
                                world.setBlock(mutable, BlockGeneratorReference.MARBLE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > 0.0)
                                world.setBlock(mutable, BlockGeneratorReference.MARBLE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.5)
                                world.setBlock(mutable, BlockGeneratorReference.SCHIST.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.7)
                                world.setBlock(mutable, BlockGeneratorReference.PHYLLITE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D > -0.9)
                                world.setBlock(mutable, BlockGeneratorReference.QUARTZITE.getBaseBlock().defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, BlockGeneratorReference.QUARTZITE.getBaseBlock().defaultBlockState(), 2);

                        } else {
                            double noise3D2 = fastNoise3D2.GetNoise(mutable.getX(), mutable.getY(), mutable.getZ());

                            if (noise3D2 > 0.8)
                                world.setBlock(mutable, BlockGeneratorReference.LIMESTONE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D2 > 0.6)
                                world.setBlock(mutable, BlockGeneratorReference.MARBLE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D2 > 0.4)
                                world.setBlock(mutable, BlockGeneratorReference.CONGLOMERATE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D2 > 0)
                                world.setBlock(mutable, BlockGeneratorReference.QUARTZITE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D2 > -0.2)
                                world.setBlock(mutable, BlockGeneratorReference.RHYOLITE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D2 > -0.4)
                                world.setBlock(mutable, BlockGeneratorReference.CONGLOMERATE.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D2 > -0.6)
                                world.setBlock(mutable, BlockGeneratorReference.GABBRO.getBaseBlock().defaultBlockState(), 2);
                            else if (noise3D2 > -0.8)
                                world.setBlock(mutable, BlockGeneratorReference.SILTSTONE.getBaseBlock().defaultBlockState(), 2);
                            else
                                world.setBlock(mutable, Blocks.ANDESITE.defaultBlockState(), 2);
                        }
                    }
                    mutable.move(Direction.UP);
                }
            }
        }
        return true;
    }


    private void setSeed(long seed) {
        if (fastNoise3D == null) {
            fastNoise3D = new FastNoise((int) seed);
            fastNoise3D.SetNoiseType(FastNoise.NoiseType.Simplex);
            fastNoise3D.SetFrequency(0.004F);
        }

        if (fastNoise3D2 == null) {
            fastNoise3D2 = new FastNoise((int) seed);
            fastNoise3D2.SetNoiseType(FastNoise.NoiseType.Simplex);
            fastNoise3D2.SetFrequency(0.004F);
        }
    }
}

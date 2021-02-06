//package net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators;
//
//import com.mojang.serialization.Codec;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.tags.BlockTags;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.ISeedReader;
//import net.minecraft.world.gen.ChunkGenerator;
//import net.minecraft.world.gen.Heightmap;
//import net.minecraft.world.gen.feature.Feature;
//import net.minecraft.world.gen.feature.NoFeatureConfig;
//import net.oriondevcorgitaco.unearthed.Unearthed;
//import net.oriondevcorgitaco.unearthed.block.ConfigBlockReader;
//import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
//import net.oriondevcorgitaco.unearthed.util.noise.FNVector3f;
//import net.oriondevcorgitaco.unearthed.util.noise.FastNoise;
//
//import java.util.Random;
//
//public class NaturalIcyGenerator extends Feature<NoFeatureConfig> {
//
//    public NaturalIcyGenerator(Codec<NoFeatureConfig> configCodec) {
//        super(configCodec);
//    }
//
//    FastNoise selectorNoise = null;
//    FastNoise cellNoise1 = null;
//    FastNoise cellNoise2 = null;
//    FastNoise perturb1 = null;
//    FastNoise perturb2 = null;
//    FastNoise perturbSmall1 = null;
//    FastNoise perturbSmall2 = null;
//
//    double storedNoiseHigh = 0;
//    double storedNoiseLow = 0;
//
//
//    private void getHighestNoisePoint(double noise) {
//        if (noise > storedNoiseHigh) {
//            storedNoiseHigh = noise;
//            Unearthed.LOGGER.info("Highest Noise point: " + storedNoiseHigh);
//        }
//        if (noise < storedNoiseLow) {
//            storedNoiseLow = noise;
//            Unearthed.LOGGER.info("Lowest Noise point: " + storedNoiseLow);
//        }
//    }
//
//
//    @Override
//    public boolean generate(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
//        setSeed(world.getSeed());
//
//        BlockPos.Mutable mutable = new BlockPos.Mutable();
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                mutable.setPos(pos.getX() + x, 0, pos.getZ() + z);
//                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());
//
//                for (int y = 0; y < topY; y++) {
//                    if (topY < 2)
//                        topY = 256;
//
//                    FNVector3f perturb3f = new FNVector3f(mutable.getX(), mutable.getY(), mutable.getZ());
//                    perturb1.GradientPerturb(perturb3f);
//                    perturbSmall1.GradientPerturb(perturb3f);
//
//                    FNVector3f perturb3f2 = new FNVector3f(mutable.getX(), mutable.getY(), mutable.getZ());
//                    perturb2.GradientPerturb(perturb3f2);
//                    perturbSmall2.GradientPerturb(perturb3f2);
//
//                    double cellNoise1 = this.cellNoise1.GetNoise(perturb3f.x, perturb3f.y, perturb3f.z);
//                    double cellNoise2 = this.cellNoise2.GetNoise(perturb3f2.x, perturb3f2.y, perturb3f2.z);
//
//                    double selectorNoiseValue = selectorNoise.GetValue(mutable.getX(), mutable.getY(), mutable.getZ()) * 12 + 0.5;
//
//                    double clampedValue = MathHelper.clampedLerp(cellNoise1, cellNoise2, selectorNoiseValue);
////                        if (FabricLoader.getInstance().isDevelopmentEnvironment())
////                            getHighestNoisePoint(clampedValue);
//
//                    ConfigBlockReader reader = ConfigBlockReader.iceBlocksFromConfig.get((int) ((clampedValue / 2.0 + 0.5) * ConfigBlockReader.iceBlocksFromConfig.size()));
//
//
//                    BlockState mutableState = world.getBlockState(mutable);
//
//                    if (reader.getBlock() != Blocks.STONE) {
//                        if (useStoneTag(world, mutable))
//                            world.setBlockState(mutable, reader.getBlock().getDefaultState(), 2);
//
//                        else if (UnearthedConfig.replaceCobble.get() && mutableState.getBlock() == Blocks.COBBLESTONE)
//                            world.setBlockState(mutable, reader.getCobbleBlock(mutableState).getDefaultState(), 2);
//
//                        else if (mutableState == Blocks.COAL_ORE.getDefaultState())
//                            world.setBlockState(mutable, reader.getCoalOre(mutableState).getDefaultState(), 2);
//
//                        else if (mutableState == Blocks.IRON_ORE.getDefaultState())
//                            world.setBlockState(mutable, reader.getIronOre(mutableState).getDefaultState(), 2);
//
//                        else if (mutableState == Blocks.GOLD_ORE.getDefaultState())
//                            world.setBlockState(mutable, reader.getGoldOre(mutableState).getDefaultState(), 2);
//
//                        else if (mutableState == Blocks.LAPIS_ORE.getDefaultState())
//                            world.setBlockState(mutable, reader.getLapisOre(mutableState).getDefaultState(), 2);
//
//                        else if (mutableState == Blocks.REDSTONE_ORE.getDefaultState())
//                            world.setBlockState(mutable, reader.getRedstoneOre(mutableState).getDefaultState(), 2);
//
//                        else if (mutableState == Blocks.DIAMOND_ORE.getDefaultState())
//                            world.setBlockState(mutable, reader.getDiamondOre(mutableState).getDefaultState(), 2);
//
//                        else if (mutableState == Blocks.EMERALD_ORE.getDefaultState())
//                            world.setBlockState(mutable, reader.getEmeraldOre(mutableState).getDefaultState(), 2);
//                    }
//                    mutable.move(Direction.UP);
//                }
//            }
//        }
//        return true;
//    }
//
//    public static boolean useStoneTag(ISeedReader world, BlockPos mutable) {
//        boolean stoneTag = UnearthedConfig.replaceableTag.get();
//        if (stoneTag)
//            return world.getBlockState(mutable).isIn(BlockTags.BASE_STONE_OVERWORLD);
//        else
//            return world.getBlockState(mutable) == Blocks.STONE.getDefaultState();
//
//    }
//
//    private void setSeed(long seed) {
//        float scaleFactor = UnearthedConfig.stoneVeinScaleFactor.get().floatValue();
//        float perturbAmpStrength = UnearthedConfig.perturbAmpStrength.get().floatValue();
//        float smallPerturbAmpStrength = UnearthedConfig.smallPerturbAmpStrength.get().floatValue();
//        int perturbOctaves = UnearthedConfig.perturbAmpOctaves.get();
//        int smallPerturbOctaves = UnearthedConfig.smallPerturbAmpOctaves.get();
//
//        if (selectorNoise == null) {
//            selectorNoise = new FastNoise((int) seed);
//            selectorNoise.SetNoiseType(FastNoise.NoiseType.Simplex);
//            selectorNoise.SetFrequency(0.0008F * scaleFactor);
//        }
//
//        if (cellNoise1 == null) {
//            cellNoise1 = new FastNoise((int) seed + 19495485);
//            cellNoise1.SetNoiseType(FastNoise.NoiseType.Cellular);
//            cellNoise1.SetFrequency(0.004F * scaleFactor);
//        }
//
//        if (cellNoise2 == null) {
//            cellNoise2 = new FastNoise((int) seed + 9484585);
//            cellNoise2.SetNoiseType(FastNoise.NoiseType.Cellular);
//            cellNoise2.SetFrequency(0.004F * scaleFactor);
//        }
//
//        if (perturb1 == null) {
//            perturb1 = new FastNoise((int) seed + 2838495);
//            perturb1.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
//            perturb1.SetFractalOctaves(perturbOctaves);
//            perturb1.SetGradientPerturbAmp(perturbAmpStrength);
//            perturb1.SetFrequency(0.008F * scaleFactor);
//        }
//
//        if (perturb2 == null) {
//            perturb2 = new FastNoise((int) seed + 100);
//            perturb2.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
//            perturb2.SetFractalOctaves(perturbOctaves);
//            perturb2.SetGradientPerturbAmp(perturbAmpStrength);
//            perturb2.SetFrequency(0.008F * scaleFactor);
//        }
//
//        if (perturbSmall1 == null) {
//            perturbSmall1 = new FastNoise((int) seed + 9475);
//            perturbSmall1.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
//            perturbSmall1.SetFractalOctaves(smallPerturbOctaves);
//            perturbSmall1.SetGradientPerturbAmp(smallPerturbAmpStrength);
//            perturbSmall1.SetFrequency(0.06F * scaleFactor);
//        }
//
//        if (perturbSmall2 == null) {
//            perturbSmall2 = new FastNoise((int) seed + 948556);
//            perturbSmall2.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
//            perturbSmall2.SetFractalOctaves(smallPerturbOctaves);
//            perturbSmall2.SetGradientPerturbAmp(smallPerturbAmpStrength);
//            perturbSmall2.SetFrequency(0.06F * scaleFactor);
//        }
//    }
//}

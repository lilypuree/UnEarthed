package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators;

import com.ibm.icu.impl.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.StoneWrapper;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoiseLite;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen.AutomataRunner;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.States;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.Type;

import java.util.Random;

public class NewGenerator extends Feature<NoFeatureConfig> {
    public NewGenerator(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    FastNoiseLite regionNoise;

    static BlockState air = Blocks.AIR.getDefaultState();
    static BlockState GLASS = Blocks.GLASS.getDefaultState();
    static BlockState DIAMOND = Blocks.DIAMOND_BLOCK.getDefaultState();
    static BlockState EMERALD = Blocks.EMERALD_BLOCK.getDefaultState();
    static BlockState GOLD = Blocks.GOLD_BLOCK.getDefaultState();
    static BlockState IRON = Blocks.IRON_BLOCK.getDefaultState();
    static BlockState NETHERITE = Blocks.NETHERITE_BLOCK.getDefaultState();

    static FastNoiseLite strataHeight;

    @Override
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        setSeed(world.getSeed());
        NoiseHandler.setSeed(world.getSeed());
        boolean isAir = Math.abs(pos.getX() >> 4) % 5 < 2;
//        for (int x = 0; x < 16; x++) {
//            for (int z = 0; z < 16; z++) {
//                int posX = pos.getX() + x;
//                int posZ = pos.getZ() + z;
//                mutable.setPos(posX, 0, posZ);
//                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, posX, posZ);
//                if (topY < 2)
//                    topY = 256;
//                Region region = Regions.getRegion(0);
//
//                Vector2 xzPos = new Vector2(posX, posZ);
//                secondaryWarp.DomainWarp(xzPos);
//                int secondaryDepth = region.getSecondaryDepth(secondaryNoise.GetNoise(xzPos.x, xzPos.y));
//                int batolithHeight = ((int) (region.getBatolithPercentage(batolithNoise.GetNoise(xzPos.x, xzPos.y)) * topY));
//
//                int stratumLevelSize = region.getStratumLevelSize();
//                int stratumHeight = -1;
//                int stratumDepth = 0;
//                for (int y = 0; y < topY; y++) {
//                    BlockState original = world.getBlockState(pos);
//                    boolean blockIsAir = original.isIn(Blocks.AIR) || original.isIn(Blocks.VOID_AIR);
//
//                    if (y % stratumLevelSize == 0) {
//                        stratumWarp.SetSeed((int) world.getSeed() + (y / stratumLevelSize) * 199327);
//                        FastNoiseLite.Vector2 stratumXZ = new Vector2(posX, posZ);
//                        stratumWarp.DomainWarp(stratumXZ);
//                        float vx = (stratumXZ.x - posX) / strataWarpAmplitude / 2;
//                        float vy = (stratumXZ.y - posZ) / strataWarpAmplitude / 2;
//                        stratumDepth = region.getStratumDepth(Math.min(1, Math.sqrt(vx * vx + vy * vy) * 2));
//                        if (stratumDepth > 0) {
//                            stratumHeight = y + (int) ((Math.atan2(vy, vx) / Math.PI / 2 + 0.5f) * stratumLevelSize);
//                        } else {
//                            stratumHeight = -1;
//                        }
//                    }
//                    if (isAir) {
//                        chunk.setBlockState(mutable, GLASS, false);
////                        world.setBlockState(mutable, GLASS, 2);
//                    } else {
//                        boolean isSecondary = (topY - y) <= secondaryDepth;
//                        boolean isBatolith = y < batolithHeight;
//                        boolean isInStrata = stratumHeight != -1 && stratumHeight <= y && y < stratumHeight + stratumDepth;
//                        //                    float regionVal = regionNoise.GetNoise(pos.getX(), pos.getZ());
//                        if (isInStrata) {
//                            chunk.setBlockState(mutable, DIAMOND, false);
//                        } else if (isSecondary && (!isBatolith || !region.batholithIntrudes())) {
//                            chunk.setBlockState(mutable, EMERALD, false);
//                        } else if (isBatolith && (!isSecondary || region.batholithIntrudes())) {
//                            chunk.setBlockState(mutable, GOLD, false);
//                        } else {
//                            Vector3 originalPos = new Vector3(posX, y, posZ);
//                            smallWarp.DomainWarp(originalPos);
//
//                            float dip = 0.998f;
//                            Vector3 orogenPerturb = new Vector3(originalPos.x, originalPos.y * (1 - dip), originalPos.z);
//                            Vector3 cell1Perturb = new Vector3(originalPos.x, originalPos.y, originalPos.z);
////                        primaryWarp.DomainWarp(cell1Perturb);
//                            orogenWarp.DomainWarp(orogenPerturb);
//                            float cellPrimary = primaryNoise.GetNoise(cell1Perturb.x, cell1Perturb.z);
//                            float cellOrogen = primaryNoise.GetNoise(orogenPerturb.x, orogenPerturb.y, orogenPerturb.z);
//
////                            float orogenScore = region.getOrogenScore(originalPos, orogenPerturb);
//                            float orogenScore;
//                            float clampedValue = (float) MathHelper.clampedLerp(cellPrimary, cellOrogen, orogenScore);
//
//                            StoneWrapper areaStone = region.getPrimaryBlock(clampedValue);
//                            if (orogenScore == 1) {
//                                replaceBlock(chunk, mutable, areaStone);
////                            if (clampedValue > 0.8f)
////                                chunk.setBlockState(mutable, DIAMOND, false);
////                            else if (clampedValue > 0.6f)
////                                chunk.setBlockState(mutable, EMERALD, false);
////                            else if (clampedValue > 0.4f)
////                                chunk.setBlockState(mutable, GOLD, false);
////                            else if (clampedValue > 0.2f)
////                                chunk.setBlockState(mutable, IRON, false);
////                            else if (clampedValue > 0.0f)
////                                chunk.setBlockState(mutable, NETHERITE, false);
////                            else if (clampedValue > -0.2f)
////                                chunk.setBlockState(mutable, DIAMOND, false);
////                            else if (clampedValue > -0.4f)
////                                chunk.setBlockState(mutable, EMERALD, false);
////                            else if (clampedValue > -0.6f)
////                                chunk.setBlockState(mutable, GOLD, false);
////                            else if (clampedValue > -0.8f)
////                                chunk.setBlockState(mutable, IRON, false);
////                            else
////                                chunk.setBlockState(mutable, NETHERITE, false);
//                            } else {
////                            if (clampedValue > 0) {
////                                chunk.setBlockState(mutable, GLASS, false);
////                            } else {
////                                chunk.setBlockState(mutable, air, false);
////                            }
////                            replaceBlock(chunk, mutable, areaStone);
//                                chunk.setBlockState(mutable, GLASS, false);
//                            }
//                        }
//                    }
//                    mutable.move(Direction.UP);
//                }
//            }
//        }
        AutomataRunner runner = new AutomataRunner(world, pos);
//        runner.getResults();
        setResults(world, pos, runner.getResults());
        return true;
    }

    private void setResults(ISeedReader world, BlockPos pos, State[][][] result) {
        IChunk chunk = world.getChunk(pos);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                mutable.setPos(posX, 0, posZ);

                int stratumLevelSize = 40;
                int stratumHeight = -1;
                int stratumDepth = 0;
                State stratumState = null;

                int topY = chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR_WG, posX, posZ);
                for (int y = 0; y < topY + 1; y++) {
                    if (y % stratumLevelSize == 0) {
                        strataHeight.SetSeed((int) world.getSeed() + (y / stratumLevelSize) * 199327);
                        Pair<State, Integer> stratum = NoiseHandler.getStrata(posX, posZ, y / stratumLevelSize, world.getSeed());
                        if (stratum == null) {
                            stratumHeight = -1;
                        } else {
                            stratumHeight = y + (int) (Math.sqrt(strataHeight.GetNoise(posX, posZ) / 2 + 0.5) * stratumLevelSize);
                            stratumDepth = stratum.second;
                            stratumState = stratum.first;
                        }
                    }
                    boolean isInStrata = stratumHeight != -1 && stratumHeight <= y && y < stratumHeight + stratumDepth;
                    State state = result[x][z][y];
                    if (isInStrata && (state.getType() != Type.BATOLITH || state.getType() != Type.OROGEN)) {
                        state = stratumState;
                    }
                    BlockState block;


                    if (state == States.DIAMOND) {
                        block = DIAMOND;
                    } else if (state == States.EMERALD) {
                        block = EMERALD;
                    } else if (state == States.GOLD) {
                        block = GOLD;
                    } else if (state == States.IRON) {
                        block = IRON;
                    } else if (state == States.NETHERITE) {
                        block = NETHERITE;
                    } else if (state == States.LAPIS) {
                        block = Blocks.LAPIS_BLOCK.getDefaultState();
                    } else if (state == States.REDSTONE) {
                        block = Blocks.REDSTONE_BLOCK.getDefaultState();
                    } else if (state == States.COAL) {
                        block = Blocks.COAL_BLOCK.getDefaultState();
                    } else {
                        block = GLASS;
                    }
                    chunk.setBlockState(mutable, block, false);
                    mutable.move(Direction.UP);
                }
            }
        }
    }

    private BlockState replaceBlock(IChunk world, BlockPos pos, State state) {
        BlockState original = world.getBlockState(pos);
        if (original.getBlock().isIn(BlockGeneratorReference.REPLACABLE)) {
            return state.getBlock();
        } else {
            for (IOreType oreType : VanillaOreTypes.values()) {
                if (original.isIn(oreType.getBlock())) {
                    return
                }
            }
        }
    }

    private void replaceBlock(IChunk world, BlockPos pos, StoneWrapper wrapper) {
        BlockState original = world.getBlockState(pos);
//        if (wrapper.getBlock() != Blocks.STONE.getDefaultState()) {
//            if (UnearthedConfig.isReplaceableStone(original))
//                world.setBlockState(pos, wrapper.getBlock(), 2);
//            else if (UnearthedConfig.replaceCobble.get() && original.isIn(Blocks.COBBLESTONE))
//                world.setBlockState(pos, wrapper.getCobbleBlock(original), 2);
//            else {
//                IOreType oreType = getOreType(original);
//                if (oreType != null) {
//                    BlockState oreBlock = wrapper.getOre(oreType);
//                    if (oreBlock == null)
//                        oreBlock = wrapper.getBlock();
//                    world.setBlockState(pos, oreBlock, 2);
//                }
//            }
//        }
        world.setBlockState(pos, wrapper.getBlock(), false);
    }


    private IOreType getOreType(BlockState block) {
        if (block.isIn(Blocks.COAL_ORE))
            return VanillaOreTypes.COAL;
        if (block.isIn(Blocks.IRON_ORE))
            return VanillaOreTypes.IRON;
        if (block.isIn(Blocks.GOLD_ORE))
            return VanillaOreTypes.GOLD;
        if (block.isIn(Blocks.LAPIS_ORE))
            return VanillaOreTypes.LAPIS;
        if (block.isIn(Blocks.REDSTONE_ORE))
            return VanillaOreTypes.REDSTONE;
        if (block.isIn(Blocks.DIAMOND_ORE))
            return VanillaOreTypes.DIAMOND;
        if (block.isIn(Blocks.EMERALD_ORE))
            return VanillaOreTypes.EMERALD;
        else
            return null;
    }

    private void setSeed(long seed) {
        if (strataHeight == null) {
            strataHeight = new FastNoiseLite((int) seed + 4568665);
            strataHeight.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
            strataHeight.SetFrequency(0.001f);
            strataHeight.SetFractalOctaves(3);
        }
    }
}

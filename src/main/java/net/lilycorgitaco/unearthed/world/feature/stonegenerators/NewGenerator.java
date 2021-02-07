package net.lilycorgitaco.unearthed.world.feature.stonegenerators;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.lilycorgitaco.unearthed.config.UnearthedConfig;
import net.lilycorgitaco.unearthed.core.UETags;
import net.lilycorgitaco.unearthed.datagen.type.IOreType;
import net.lilycorgitaco.unearthed.datagen.type.VanillaOreTypes;
import net.lilycorgitaco.unearthed.util.noise.FastNoiseLite;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.Cell;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.gen.AutomataRunner;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.Type;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.gen.NoiseHandler;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.gen.StratAutomata;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.gen.StratAutomata.StrataState;

import java.util.Random;

public class NewGenerator extends Feature<DefaultFeatureConfig> {
    public NewGenerator(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    private boolean isSeedSet = false;
    private static FastNoiseLite strataHeight;
    private boolean replaceCobble = false;
    static BlockState air = Blocks.AIR.getDefaultState();
    static BlockState GLASS = Blocks.GLASS.getDefaultState();
    static BlockState DIAMOND = Blocks.DIAMOND_BLOCK.getDefaultState();
    static BlockState EMERALD = Blocks.EMERALD_BLOCK.getDefaultState();
    static BlockState GOLD = Blocks.GOLD_BLOCK.getDefaultState();
    static BlockState IRON = Blocks.IRON_BLOCK.getDefaultState();
    static BlockState NETHERITE = Blocks.NETHERITE_BLOCK.getDefaultState();

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random rand, BlockPos pos, DefaultFeatureConfig config) {
        if (!isSeedSet) {
            setSeed(world.getSeed());
            NoiseHandler.setSeed(world.getSeed());
        }
//        boolean isAir = Math.abs(pos.getX() >> 4) % 5 < 2;
        boolean isAmplified = generator instanceof NoiseChunkGenerator && ((NoiseChunkGenerator) generator).matchesSettings(world.getSeed(), ChunkGeneratorSettings.AMPLIFIED);
        boolean alwaysReplaceDirt = UnearthedConfig.alwaysReplaceDirt.get();
        boolean debugMode = UnearthedConfig.debug.get();
        replaceCobble = UnearthedConfig.replaceCobble.get();
        NoiseHandler noiseHandler = new NoiseHandler(world, pos);
        AutomataRunner runner = new AutomataRunner(world, pos, noiseHandler);
        if (debugMode) {
            setDebugResults(world, pos, runner, noiseHandler, isAmplified || alwaysReplaceDirt);
        } else {
            setResults(world, pos, runner, noiseHandler, isAmplified || alwaysReplaceDirt);
        }
        return true;
    }

    private int stratumLevelSize = 35;
    private static IOreType[] ores = VanillaOreTypes.values();

    private void setResults(StructureWorldAccess world, BlockPos pos, AutomataRunner runner, NoiseHandler noiseHandler, boolean alwaysReplaceDirt) {
        State[][][] result = runner.getResults();
        int[][] maxHeights = runner.getMaxHeights();
        int strataLevels = runner.getMaxHeight() / stratumLevelSize + 1;
        StrataState[][][] strataResults = new StrataState[strataLevels][][];
        StratAutomata stratAutomata = new StratAutomata(world.getSeed(), pos, noiseHandler);
        for (int i = 0; i < strataLevels; i++) {
            stratAutomata.set(i, 8, 3, 3);
            strataResults[i] = stratAutomata.fillInStates().propagate().nextStage().propagate().simpleSelect().nextStage().propagate().getResults();
        }
        Chunk chunk = world.getChunk(pos);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                mutable.set(posX, 0, posZ);
                int stratumHeight = -1;
                int stratumDepth = 0;
                State stratumState = null;
                int topY = maxHeights[x][z];
                for (int y = 0; y < topY; y++) {
                    if (y % stratumLevelSize == 0) {
                        strataHeight.SetSeed((int) world.getSeed() + (y / stratumLevelSize) * 199327);
                        StrataState stratum = strataResults[y / stratumLevelSize][x][z];
                        if (stratum == null) {
                            stratumHeight = -1;
                        } else {
                            stratumHeight = y + (int) (Math.sqrt(strataHeight.GetNoise(posX, posZ) / 2 + 0.5) * stratumLevelSize);
                            stratumDepth = stratum.getThickness();
                            stratumState = stratum.getState();
                        }
                    }
                    boolean isInStrata = stratumHeight != -1 && stratumHeight <= y && y < stratumHeight + stratumDepth;
                    State state = result[x][z][y];
                    if (isInStrata && (state.getType() != Type.BATHOLITH || state.getType() != Type.OROGEN)) {
                        state = stratumState;
                    }
                    BlockState original = chunk.getBlockState(mutable);
                    BlockState replaced = replaceBlock(original, state, alwaysReplaceDirt || isBiomeHilly(noiseHandler.getBiome(x, z)));
                    if (original != replaced && replaced != null) {
                        chunk.getSectionArray()[y >> 4].setBlockState(x, y & 15, z, replaced, false);
//                        chunk.setBlockState(mutable, replaced, false);
                    }  //                        chunk.setBlockState(mutable, original, false);

                    mutable.move(Direction.UP);
                }
            }
        }
    }

    private boolean isBiomeHilly(Biome biome) {
        String biomeName = biome.getRegistryName().getPath();
        return biome.getCategory() == Biome.Category.EXTREME_HILLS || biomeName.contains("hill") || biomeName.contains("plateau") || biomeName.contains("mountain");
    }

    private BlockState replaceBlock(BlockState original, State state, boolean replaceDirt) {
//        return state.getDefaultState();
        if (original.getBlock().isIn(UETags.Blocks.REPLACABLE)) {
            return state.getCell().getDefaultState();
        } else if (original.isOf(Blocks.AIR)) {
            return original;
        } else {
            Cell cell = state.getCell();
            if (state.getType() == Type.TERTIARY) {
                if (original.isIn(UETags.Blocks.REPLACE_DIRT) || original.isIn(UETags.Blocks.REPLACE_GRASS)) {
                    return cell.getDefaultState();
                }
                for (IOreType oreType : ores) {
                    if (original.isOf(oreType.getBlock())) {
                        return cell.getDefaultState();
                    }
                }
            } else {
                if (replaceCobble && original.isOf(Blocks.COBBLESTONE)) {
                    return cell.getCobbleReplacement();
                }
                if (replaceDirt) {
                    if (original.isIn(UETags.Blocks.REPLACE_DIRT)) {
                        return cell.getDirtReplacement();
                    } else if (original.isIn(UETags.Blocks.REPLACE_GRASS)) {
                        return cell.getGrassReplacement(original);
                    }
                }
                if (cell.replacesOre()) {
                    for (IOreType oreType : ores) {
                        if (original.isOf(oreType.getBlock())) {
                            BlockState ore = cell.getOre(oreType);
                            if (ore != null) {
                                return ore;
                            } else return original;
                        }
                    }
                }
            }
        }
        return original;
    }

    private void setSeed(long seed) {
        if (strataHeight == null) {
            strataHeight = new FastNoiseLite((int) seed + 4568665);
            strataHeight.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
            strataHeight.SetFrequency(0.001f);
            strataHeight.SetFractalOctaves(3);
        }
    }


    private void setDebugResults(StructureWorldAccess world, BlockPos pos, AutomataRunner runner, NoiseHandler noiseHandler, boolean alwaysReplaceDirt) {
        State[][][] result = runner.debugResults();
        int[][] maxHeights = runner.getMaxHeights();
        int strataLevels = runner.getMaxHeight() / stratumLevelSize + 1;
        StrataState[][][] strataResults = new StrataState[strataLevels][][];
        StratAutomata stratAutomata = new StratAutomata(world.getSeed(), pos, noiseHandler);
        for (int i = 0; i < strataLevels; i++) {
            stratAutomata.set(i, 8, 3, 3);
            strataResults[i] = stratAutomata.fillInStates().propagate().simpleSelect().nextStage().propagate().nextStage().propagate().getResults();
        }
        Chunk chunk = world.getChunk(pos);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                mutable.set(posX, 0, posZ);

                int stratumHeight = -1;
                int stratumDepth = 0;
                State stratumState = null;
                int topY = maxHeights[x][z];
                for (int y = 0; y < topY; y++) {
                    if (y % stratumLevelSize == 0) {
                        strataHeight.SetSeed((int) world.getSeed() + (y / stratumLevelSize) * 199327);
                        StrataState stratum = null;
                        stratum = strataResults[y / stratumLevelSize][x][z];
                        if (stratum == null) {
                            stratumHeight = -1;
                        } else {
                            stratumHeight = y + (int) (Math.sqrt(strataHeight.GetNoise(posX, posZ) / 2 + 0.5) * stratumLevelSize);
                            stratumDepth = stratum.getThickness();
                            stratumState = stratum.getState();
                        }
                    }
                    boolean isInStrata = stratumHeight != -1 && stratumHeight <= y && y < stratumHeight + stratumDepth;
                    State state = result[x][z][y];
                    if (isInStrata && (state.getType() != Type.BATHOLITH || state.getType() != Type.OROGEN)) {
                        state = stratumState;
                    }
                    BlockState original = chunk.getBlockState(mutable);
                    BlockState replaced;
                    if (state.getType() == Type.PRIMARY) {
                        replaced = GLASS;
                    } else {
                        replaced = replaceBlock(original, state, alwaysReplaceDirt || isBiomeHilly(noiseHandler.getBiome(x, z)));
                    }
                    if (original != replaced && replaced != null) {
                        chunk.setBlockState(mutable, replaced, false);
                    }
                    mutable.move(Direction.UP);
                }
            }
        }
    }
}

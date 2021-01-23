package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.oriondevcorgitaco.unearthed.block.StoneWrapper;
import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
import net.oriondevcorgitaco.unearthed.core.UETags;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoiseLite;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.SimpleState;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.StoneState;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen.AutomataRunner;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.Type;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen.NoiseHandler;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL;

import java.util.Random;

public class NewGenerator extends Feature<NoFeatureConfig> {
    public NewGenerator(Codec<NoFeatureConfig> codec) {
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
    public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (!isSeedSet) {
            setSeed(world.getSeed());
            NoiseHandler.setSeed(world.getSeed());
        }
//        boolean isAir = Math.abs(pos.getX() >> 4) % 5 < 2;
        boolean isAmplified = generator instanceof NoiseChunkGenerator && ((NoiseChunkGenerator) generator).func_236088_a_(world.getSeed(), DimensionSettings.field_242735_d);
        boolean alwaysReplaceDirt = UnearthedConfig.alwaysReplaceDirt.get();
        boolean debugMode = UnearthedConfig.debug.get();
        replaceCobble = UnearthedConfig.replaceCobble.get();
        NoiseHandler noiseHandler = new NoiseHandler(world, pos);
        AutomataRunner runner = new AutomataRunner(world, pos, noiseHandler);
//        runner.getResults();

        if (debugMode) {
            setDebugResults(world, pos, runner, noiseHandler, isAmplified || alwaysReplaceDirt);
        } else {
            setResults(world, pos, runner, noiseHandler, isAmplified || alwaysReplaceDirt);
        }
        return true;
    }

    private int stratumLevelSize = 35;
    private static IOreType[] ores = VanillaOreTypes.values();

    private void setResults(ISeedReader world, BlockPos pos, AutomataRunner runner, NoiseHandler noiseHandler, boolean alwaysReplaceDirt) {
        State[][][] result = runner.getResults();
        int[][] maxHeights = runner.getMaxHeights();
        IChunk chunk = world.getChunk(pos);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                mutable.setPos(posX, 0, posZ);

                int stratumHeight = -1;
                int stratumDepth = 0;
                State stratumState = null;
                Biome biome = noiseHandler.getBiome(x, z);
                int topY = maxHeights[x][z];
                for (int y = 0; y < topY; y++) {
                    if (y % stratumLevelSize == 0) {
                        strataHeight.SetSeed((int) world.getSeed() + (y / stratumLevelSize) * 199327);
                        Pair<State, Integer> stratum = noiseHandler.getStrata(posX, posZ, y / stratumLevelSize);
                        if (stratum == null) {
                            stratumHeight = -1;
                        } else {
                            stratumHeight = y + (int) (Math.sqrt(strataHeight.GetNoise(posX, posZ) / 2 + 0.5) * stratumLevelSize);
                            stratumDepth = stratum.getRight();
                            stratumState = stratum.getLeft();
                        }
                    }
                    boolean isInStrata = stratumHeight != -1 && stratumHeight <= y && y < stratumHeight + stratumDepth;
                    State state = result[x][z][y];
                    if (isInStrata && (state.getType() != Type.BATOLITH || state.getType() != Type.OROGEN)) {
                        state = stratumState;
                    }
                    BlockState original = chunk.getBlockState(mutable);
                    BlockState replaced = replaceBlock(original, state, alwaysReplaceDirt || isBiomeHilly(biome));
                    if (original != replaced && replaced != null) {
                        chunk.getSections()[y >> 4].setBlockState(x, y & 15, z, replaced);
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
            return state.getDefaultState();
        } else if (original.isIn(Blocks.AIR)) {
            return original;
        } else {
            if (state.getType() == Type.TERTIARY) {
                if (original.isIn(Blocks.DIRT) || original.isIn(Blocks.GRASS_BLOCK)) {
                    return state.getDefaultState();
                }
                for (IOreType oreType : ores) {
                    if (original.isIn(oreType.getBlock())) {
                        return state.getDefaultState();
                    }
                }
            } else if (state instanceof StoneState) {
                StoneWrapper wrapper = ((StoneState) state).getWrapper();
                if (replaceCobble && original.isIn(Blocks.COBBLESTONE)) {
                    return wrapper.getCobbleBlock(original);
                }
                if (replaceDirt) {
                    if (original.isIn(Blocks.DIRT)) {
                        return wrapper.getDirtReplacer(original);
                    } else if (original.isIn(Blocks.GRASS_BLOCK)) {
                        return wrapper.getGrassReplacer(original);
                    }
                }
                for (IOreType oreType : ores) {
                    if (original.isIn(oreType.getBlock())) {
                        BlockState ore = wrapper.getOre(oreType);
                        if (ore != null) {
                            return ore;
                        } else return original;
                    }
                }
            } else if (state instanceof SimpleState) {
                if (replaceDirt) {
                    if (original.isIn(Blocks.DIRT)) {
                        return ((SimpleState) state).getDirtReplacement();
                    } else if (original.isIn(Blocks.GRASS_BLOCK)) {
                        return ((SimpleState) state).getGrassReplacement(original);
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

    private void setDebugResults(ISeedReader world, BlockPos pos, AutomataRunner runner, NoiseHandler noiseHandler, boolean alwaysReplaceDirt) {
        State[][][] result = runner.debugResults();
        int[][] maxHeights = runner.getMaxHeights();
        IChunk chunk = world.getChunk(pos);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                mutable.setPos(posX, 0, posZ);
                int stratumHeight = -1;
                int stratumDepth = 0;
                State stratumState = null;
                Biome biome = noiseHandler.getBiome(x, z);
//                int topY = maxHeights[x][z];
                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, posX, posZ);
                for (int y = 0; y < topY; y++) {
                    if (y % stratumLevelSize == 0) {
                        strataHeight.SetSeed((int) world.getSeed() + (y / stratumLevelSize) * 199327);
                        Pair<State, Integer> stratum = noiseHandler.getStrata(posX, posZ, y / stratumLevelSize);
                        if (stratum == null) {
                            stratumHeight = -1;
                        } else {
                            stratumHeight = y + (int) (Math.sqrt(strataHeight.GetNoise(posX, posZ) / 2 + 0.5) * stratumLevelSize);
                            stratumDepth = stratum.getRight();
                            stratumState = stratum.getLeft();
                        }
                    }
                    boolean isInStrata = stratumHeight != -1 && stratumHeight <= y && y < stratumHeight + stratumDepth;
                    State state = result[x][z][y];
                    if (isInStrata && (state.getType() != Type.BATOLITH || state.getType() != Type.OROGEN)) {
                        state = stratumState;
                    }
                    BlockState original = chunk.getBlockState(mutable);
                    BlockState replaced;
                    if (state.getType() == Type.PRIMARY) {
                        replaced = GLASS;
                    } else {
                        replaced = state.getDefaultState();
                    }
                    if (original != replaced && replaced != null) {
                        chunk.setBlockState(mutable, replaced, false);
                    }  //                        chunk.setBlockState(mutable, original, false);
                    mutable.move(Direction.UP);
                }
            }
        }
    }
}

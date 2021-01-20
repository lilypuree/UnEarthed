package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.NoiseHandler;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.States;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.Type;

import java.util.function.Supplier;

public class AutomataRunner {

    static public State DUMMY = new State(Type.BORDER, Blocks.IRON_BLOCK.getDefaultState());
    static public State DUMMY2 = new State(Type.BORDER, Blocks.GOLD_BLOCK.getDefaultState());
    static BlockState GLASS = Blocks.GLASS.getDefaultState();
    static BlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    static BlockState YELLOW_CON = Blocks.YELLOW_CONCRETE.getDefaultState();
    static BlockState GOLD = Blocks.GOLD_BLOCK.getDefaultState();
    static BlockState YELLOW_WOOL = Blocks.YELLOW_WOOL.getDefaultState();
    static BlockState IRON = Blocks.IRON_BLOCK.getDefaultState();
    static BlockState WOOL = Blocks.WHITE_WOOL.getDefaultState();
    static BlockState BONE = Blocks.BONE_BLOCK.getDefaultState();
    static BlockState CONCRETE = Blocks.WHITE_CONCRETE.getDefaultState();

    private ISeedReader world;
    private BlockPos pos;

    public AutomataRunner(ISeedReader world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public State[][][] getResults() {
        int[][] chunkHeights = new int[3][3];
        for (int cx = -1; cx <= 1; cx++) {
            for (int cz = -1; cz <= 1; cz++) {
                int maxHeight = 0;
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        int posX = pos.getX() + x + cx * 16;
                        int posZ = pos.getZ() + z + cz * 16;
                        int height = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, posX, posZ);
                        if (height > maxHeight) maxHeight = height;
                    }
                }
                chunkHeights[cx + 1][cz + 1] = maxHeight;
            }
        }
        NoiseHandler noiseHandler = new NoiseHandler(world, pos);

        AutomataFiller first = new AutomataFiller(world, pos, chunkHeights[1][1]);
        first.setStateGetter((v) -> {
//            return ((v.getX() + v.getZ()) % 3 == 1) ? States.GLASS : States.GOLD;
            return noiseHandler.getState(v.getX() - pos.getX(), v.getZ() - pos.getZ(), v.getY() - pos.getY());
        });
        first.setTargetHeights(chunkHeights);
        first.fillStates();
        first.replaceRandomly((v, state) -> {
            return noiseHandler.getReplaceState(v.getX() - pos.getX(), v.getZ() - pos.getZ(), v.getY() - pos.getY(), state, 1);
        });
        AutomataBase second = new AutomataBase(first);
        second.expandStateArray();
        second.replaceRandomly((v, state) -> {
            return noiseHandler.getReplaceState(v.getX() - pos.getX(), v.getZ() - pos.getZ(), v.getY() - pos.getY(), state, 2);
        });
//        AutomataBase third = new AutomataBase(second);
        AutomataBase third = new AutomataBase(second) {
            @Override
            public State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter) {
                boolean ySame = up.equals(down);
                boolean xSame = x1.equals(x2);
                boolean zSame = z1.equals(z2);
                if (xSame && ySame && zSame) {
                    int i = randomInt(3);
                    if (i == 0) return up;
                    else return (i == 1) ? x1 : z1;
                } else if (!xSame && zSame && ySame) {
                    return selectStates(up, z1);
                } else if (xSame && !zSame && ySame) {
                    return selectStates(up, x1);
                } else if (xSame && zSame && !ySame) {
                    return selectStates(x1, z1);
                } else if (ySame) {
                    randomInt(3);
                    return up;
                } else {
                    return selectStates(x1, x2, z1, z2);
                }
            }
        };
        third.expandStateArray();
        third.replaceRandomly((v, state) -> {
            return noiseHandler.getReplaceState(v.getX() - pos.getX(), v.getZ() - pos.getZ(), v.getY() - pos.getY(), state, 3);
        });
//        AutomataBase fourth = new AutomataBase(third);
        AutomataBase fourth = new AutomataBase(third) {
            @Override
            public State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter) {
                int i = randomInt(6);
                switch (i) {
                    case 0:
                        return up;
                    case 1:
                        return down;
                    case 2:
                        return x1;
                    case 3:
                        return x2;
                    case 4:
                        return z1;
                    default:
                        return z2;
                }
            }
        };
        fourth.expandStateArray();
//        applyResults(world, pos, fourth);
        return fourth.newStateArray;
    }

    private void applyResults(ISeedReader world, BlockPos pos, AutomataBase results) {
        State[][][] result = results.newStateArray;
        IChunk chunk = world.getChunk(pos);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        boolean isXOdd = Math.abs(pos.getX() >> 4) % 2 != 0;
        boolean isZOdd = Math.abs(pos.getZ() >> 4) % 2 != 0;
//        BlockState state = isXOdd ? (isZOdd ? GOLD : IRON) : (isZOdd ? EMERALD : DIAMOND);
        int cellsize2 = results.cellsize / 2;
        for (int x = 0; x < 17; x++) {
            boolean isXCell = x % cellsize2 == 0;
            for (int z = 0; z < 17; z++) {
                boolean isZCell = z % cellsize2 == 0;

                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                mutable.setPos(posX, 0, posZ);
                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, posX, posZ);
//                if (topY < 2)
//                    topY = 256;
                for (int y = 0; y < topY; y++) {
                    boolean isYCell = y % cellsize2 == 0;
                    if (isXCell && isZCell && isYCell) {
                        State realState = result[x / cellsize2][z / cellsize2][y / cellsize2];
                        BlockState state;
//                        if (isXOdd) {
//                            if (isZOdd) {
//                                state = realState == States.GLASS ? SANDSTONE : BONE;
//                            } else {
//                                state = realState == States.GLASS ? GOLD : IRON;
//                            }
//                        } else {
//                            if (isZOdd) {
//                                state = realState == States.GLASS ? YELLOW_CON : CONCRETE;
//                            } else {
//                                state = realState == States.GLASS ? YELLOW_WOOL : WOOL;
//                            }
//                        }
                        state = realState == States.GLASS ? GOLD : IRON;
//                        if (x == 16 && z == 16) {
//                            mutable.move(Direction.UP, 2);
//                            world.setBlockState(mutable, state, 2);
//                            mutable.move(Direction.DOWN, 2);
//                        } else if (x == 16) {
//                            mutable.move(Direction.UP);
//                            world.setBlockState(mutable, state, 2);
//                            mutable.move(Direction.DOWN);
//                        } else if (z == 16 && y > 0) {
//                            mutable.move(Direction.DOWN);
//                            world.setBlockState(mutable, state, 2);
//                            mutable.move(Direction.UP);
//                        } else {
//                            world.setBlockState(mutable, state, 2);
//                        }
                        world.setBlockState(mutable, state, 2);
//                        if (chunkOdd) {
//                            if (x == 16 && z == 16) {
//
//                                chunk.setBlockState(mutable, replaceBlock2(result[x / cellsize2][z / cellsize2][y / cellsize2], originalState), false);
//                                mutable.move(Direction.DOWN, 2);
//                            } else if (x == 16 || z == 16) {
//                                mutable.move(Direction.UP);
//                                chunk.setBlockState(mutable, replaceBlock2(result[x / cellsize2][z / cellsize2][y / cellsize2], originalState), false);
//                                mutable.move(Direction.DOWN);
//                            } else {
//                                chunk.setBlockState(mutable, replaceBlock2(result[x / cellsize2][z / cellsize2][y / cellsize2], originalState), false);
//
//                            }

//                        } else {
//                            chunk.setBlockState(mutable, , originalState), false);
//                        }
                    } else {
                        BlockState originalState = chunk.getBlockState(mutable);
                        if (originalState != IRON && originalState != GOLD
                                && originalState != WOOL && originalState != YELLOW_WOOL
                                && originalState != CONCRETE && originalState != YELLOW_CON
                                && originalState != BONE && originalState != SANDSTONE && x != 16 && z != 16) {
                            world.setBlockState(mutable, GLASS, 2);
                        }
                    }

                    mutable.move(Direction.UP);
                }
            }
        }
    }


    //    private static BlockState replaceBlock(State state, BlockState original) {
    //        return (state == DUMMY) ? DIAMOND : EMERALD;
    //    }
    //
    //    private static BlockState replaceBlock2(State state, BlockState original) {
    //        return (state == DUMMY) ? GOLD : IRON;
    //    }
}

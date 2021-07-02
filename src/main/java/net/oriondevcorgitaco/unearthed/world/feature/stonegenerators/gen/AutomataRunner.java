package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.*;

import java.util.function.Supplier;

public class AutomataRunner {

    static BlockState GLASS = Blocks.GLASS.defaultBlockState();
    static BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
    static BlockState YELLOW_CON = Blocks.YELLOW_CONCRETE.defaultBlockState();
    static BlockState GOLD = Blocks.GOLD_BLOCK.defaultBlockState();
    static BlockState YELLOW_WOOL = Blocks.YELLOW_WOOL.defaultBlockState();
    static BlockState IRON = Blocks.IRON_BLOCK.defaultBlockState();
    static BlockState WOOL = Blocks.WHITE_WOOL.defaultBlockState();
    static BlockState BONE = Blocks.BONE_BLOCK.defaultBlockState();
    static BlockState CONCRETE = Blocks.WHITE_CONCRETE.defaultBlockState();

    private NoiseHandler noiseHandler;
    private ISeedReader world;
    private BlockPos pos;
    private int[][] maxHeights;
    private int maxHeight;

    public AutomataRunner(ISeedReader world, BlockPos pos, NoiseHandler noiseHandler) {
        this.noiseHandler = noiseHandler;
        this.world = world;
        this.pos = pos;
        this.maxHeights = new int[17][17];
        maxHeight = fillMaxHeights();
    }

    public State[][][] getResults() {
        AutomataFiller first = new AutomataFiller(world.getSeed(), pos, maxHeight, noiseHandler);
//        first.setStateGetter((v) -> {
////            return ((v.getX() + v.getZ()) % 3 == 1) ? States.GLASS : States.GOLD;
//            return noiseHandler.getState(v.getX() - pos.getX(), v.getZ() - pos.getZ(), v.getY() - pos.getY());
//        });
        first.fillStates();
        first.replaceRandomly((v) -> {
            return noiseHandler.getReplacement(v.getX(), v.getZ(), v.getY(), 1);
        });
        AutomataBase second = new AutomataBase(first);
        second.expandStateArray();
        second.replaceRandomly((v) -> {
            return noiseHandler.getReplacement(v.getX(), v.getZ(), v.getY(), 2);
        });
//        AutomataBase third = new AutomataBase(second);
        AutomataBase third = new AutomataBase(second) {
            @Override
            protected State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter, int x, int y, int z) {
                boolean ySame = up == down;
                boolean xSame = x1 == x2;
                boolean zSame = z1 == z2;
                if (xSame && ySame && zSame) {
                    int i = randomInt(3, x, y, z);
                    if (i == 0) return up;
                    else return (i == 1) ? x1 : z1;
                } else if (!xSame && zSame && ySame) {
                    return selectStates(up, z1, x, y, z);
                } else if (xSame && !zSame && ySame) {
                    return selectStates(up, x1, x, y, z);
                } else if (xSame && zSame && !ySame) {
                    return selectStates(x1, z1, x, y, z);
                } else if (ySame) {
                    return up;
                } else {
                    return selectStates(x1, x2, z1, z2, x, y, z);
                }
            }
        };
        third.expandStateArray();
        third.replaceRandomly((v) -> {
            return noiseHandler.getReplacement(v.getX(), v.getZ(), v.getY(), 3);
        });


        AutomataBase fourth = new AutomataBase(third) {
            @Override
            public State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter, int x, int y, int z) {
                int i = randomInt(6, x, y, z);
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
        };           //for the fourth automata, just using a linear congruential generator might prove more performant (xoroshiro??)
                     //bc the requirement for the chunk borders to be equal do not hold anymore.
                    //however I am not sure if this is worth it so just noting it in case performace is getting crucial again
        fourth.expandStateArray();
//        applyResults(world, pos, fourth);
        return fourth.newStateArray;
    }

    private int fillMaxHeights() {
        int maxHeight = 0;
        for (int x = 0; x < 17; x++) {
            for (int z = 0; z < 17; z++) {
                int posX = pos.getX() + x;
                int posZ = pos.getZ() + z;
                int height = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, posX, posZ);
                if (height > maxHeight) maxHeight = height;
                maxHeights[x][z] = height;
            }
        }
        return maxHeight;
    }

    public State[][][] debugResults() {
        AutomataFiller first = new AutomataFiller(world.getSeed(), pos, maxHeight, noiseHandler);
//        first.setStateGetter((v) -> {
//            return noiseHandler.getState(v.getX() - pos.getX(), v.getZ() - pos.getZ(), v.getY() - pos.getY());
//        });
        first.fillStates();
        first.replaceRandomly((v) -> {
            CellularOre ore = noiseHandler.getReplacement(v.getX(), v.getZ(), v.getY(), 1);
            return ore != null ? new DebugReplacer(ore) : null;
        });
        AutomataBase second = new AutomataBase(first);
        second.expandStateArray();
        second.replaceRandomly((v) -> {
            CellularOre ore = noiseHandler.getReplacement(v.getX(), v.getZ(), v.getY(), 2);
            return ore != null ? new DebugReplacer(ore) : null;
        });
        AutomataBase third = new AutomataBase(second) {
            @Override
            protected State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter, int x, int y, int z) {
                boolean ySame = up == down;
                boolean xSame = x1 == x2;
                boolean zSame = z1 == z2;
                if (xSame && ySame && zSame) {
                    int i = randomInt(3, x, y, z);
                    if (i == 0) return up;
                    else return (i == 1) ? x1 : z1;
                } else if (!xSame && zSame && ySame) {
                    return selectStates(up, z1, x, y, z);
                } else if (xSame && !zSame && ySame) {
                    return selectStates(up, x1, x, y, z);
                } else if (xSame && zSame && !ySame) {
                    return selectStates(x1, z1, x, y, z);
                } else if (ySame) {
                    return up;
                } else {
                    return selectStates(x1, x2, z1, z2, x, y, z);
                }
            }
        };
        third.expandStateArray();
        third.replaceRandomly((v) -> {
            CellularOre ore = noiseHandler.getReplacement(v.getX(), v.getZ(), v.getY(), 3);
            return ore != null ? new DebugReplacer(ore) : null;
        });
        AutomataBase fourth = new AutomataBase(third) {
            @Override
            public State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter, int x, int y, int z) {
                int i = randomInt(6, x, y, z);
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
        return fourth.newStateArray;
    }

//    private void applyResults(ISeedReader world, BlockPos pos, AutomataBase results) {
//        State[][][] result = results.newStateArray;
//        IChunk chunk = world.getChunk(pos);
//        BlockPos.Mutable mutable = new BlockPos.Mutable();
//        boolean isXOdd = Math.abs(pos.getX() >> 4) % 2 != 0;
//        boolean isZOdd = Math.abs(pos.getZ() >> 4) % 2 != 0;
////        BlockState state = isXOdd ? (isZOdd ? GOLD : IRON) : (isZOdd ? EMERALD : DIAMOND);
//        int cellsize2 = results.cellsize2;
//        for (int x = 0; x < 17; x++) {
//            boolean isXCell = x % cellsize2 == 0;
//            for (int z = 0; z < 17; z++) {
//                boolean isZCell = z % cellsize2 == 0;
//
//                int posX = pos.getX() + x;
//                int posZ = pos.getZ() + z;
//                mutable.setPos(posX, 0, posZ);
//                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, posX, posZ);
////                if (topY < 2)
////                    topY = 256;
//                for (int y = 0; y < topY; y++) {
//                    boolean isYCell = y % cellsize2 == 0;
//                    if (isXCell && isZCell && isYCell) {
//                        State realState = result[x / cellsize2][z / cellsize2][y / cellsize2];
//                        BlockState state;
////                        if (isXOdd) {
////                            if (isZOdd) {
////                                state = realState == States.GLASS ? SANDSTONE : BONE;
////                            } else {
////                                state = realState == States.GLASS ? GOLD : IRON;
////                            }
////                        } else {
////                            if (isZOdd) {
////                                state = realState == States.GLASS ? YELLOW_CON : CONCRETE;
////                            } else {
////                                state = realState == States.GLASS ? YELLOW_WOOL : WOOL;
////                            }
////                        }
//                        state = realState == States.GLASS ? GOLD : IRON;
////                        if (x == 16 && z == 16) {
////                            mutable.move(Direction.UP, 2);
////                            world.setBlockState(mutable, state, 2);
////                            mutable.move(Direction.DOWN, 2);
////                        } else if (x == 16) {
////                            mutable.move(Direction.UP);
////                            world.setBlockState(mutable, state, 2);
////                            mutable.move(Direction.DOWN);
////                        } else if (z == 16 && y > 0) {
////                            mutable.move(Direction.DOWN);
////                            world.setBlockState(mutable, state, 2);
////                            mutable.move(Direction.UP);
////                        } else {
////                            world.setBlockState(mutable, state, 2);
////                        }
//                        world.setBlockState(mutable, state, 2);
////                        if (chunkOdd) {
////                            if (x == 16 && z == 16) {
////
////                                chunk.setBlockState(mutable, replaceBlock2(result[x / cellsize2][z / cellsize2][y / cellsize2], originalState), false);
////                                mutable.move(Direction.DOWN, 2);
////                            } else if (x == 16 || z == 16) {
////                                mutable.move(Direction.UP);
////                                chunk.setBlockState(mutable, replaceBlock2(result[x / cellsize2][z / cellsize2][y / cellsize2], originalState), false);
////                                mutable.move(Direction.DOWN);
////                            } else {
////                                chunk.setBlockState(mutable, replaceBlock2(result[x / cellsize2][z / cellsize2][y / cellsize2], originalState), false);
////
////                            }
//
////                        } else {
////                            chunk.setBlockState(mutable, , originalState), false);
////                        }
//                    } else {
//                        BlockState originalState = chunk.getBlockState(mutable);
//                        if (originalState != IRON && originalState != GOLD
//                                && originalState != WOOL && originalState != YELLOW_WOOL
//                                && originalState != CONCRETE && originalState != YELLOW_CON
//                                && originalState != BONE && originalState != SANDSTONE && x != 16 && z != 16) {
//                            world.setBlockState(mutable, GLASS, 2);
//                        }
//                    }
//
//                    mutable.move(Direction.UP);
//                }
//            }
//        }
//    }

    public int[][] getMaxHeights() {
        return maxHeights;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    //    private static BlockState replaceBlock(State state, BlockState original) {
    //        return (state == DUMMY) ? DIAMOND : EMERALD;
    //    }
    //
    //    private static BlockState replaceBlock2(State state, BlockState original) {
    //        return (state == DUMMY) ? GOLD : IRON;
    //    }

}

package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;

import com.ibm.icu.impl.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AutomataBase implements IAutomata {
    protected ISeedReader world;
    protected BlockPos basePos;
    protected int cellsize;
    protected int xWidth;
    protected int zWidth;
    protected int yHeight;
    protected int xWidth2;
    protected int zWidth2;
    protected int yHeight2;

    protected TargetedRandom[][] randomArray = new TargetedRandom[3][3];
    //    protected boolean[][] fixedBorder;
    protected Random random;

    protected State[][][] stateArray;
    protected State[][][] newStateArray = null;
    protected Function<Vector3i, State> stateGetter = null;

    public AutomataBase(ISeedReader world, BlockPos basePos, int cellsize, int xWidth, int zWidth, int yHeight) {
        this.cellsize = cellsize;
        this.xWidth = xWidth;
        this.xWidth2 = xWidth * 2 - 1;
        this.zWidth = zWidth;
        this.zWidth2 = zWidth * 2 - 1;
        this.yHeight = yHeight;
        this.yHeight2 = yHeight * 2 - 1;
        this.stateArray = new State[xWidth][zWidth][yHeight];
        this.random = world.getRandom();
        this.world = world;
        this.basePos = basePos;
    }

    public AutomataBase(AutomataBase parent) {
        this(parent.world, parent.basePos, parent.cellsize / 2, parent.xWidth2, parent.zWidth2, parent.yHeight2);
        this.setStates(parent.newStateArray);
//        this.setFixedBorder(parent.fixedBorder);
        this.setStateGetter(parent.stateGetter);
        this.randomArray = parent.randomArray;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                randomArray[x][z].multiply();
            }
        }
    }

    protected void setTargetHeights(final int[][] maxHeights) {
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                maxHeights[x][z] = convertMaxHeightToCells(maxHeights[x][z]);
            }
        }
        int curY = maxHeights[1][1];
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                randomArray[x][z] = new TargetedRandom(getSeed(x - 1, z - 1), Math.max(curY, maxHeights[x][z]), curY);
            }
        }
    }

    protected int convertMaxHeightToCells(int height) {
        return (int) Math.ceil((height - 1.0f) / (cellsize / 2.0f)) + 1;
    }

//    public void setFixedBorder(boolean[][] fixedBorder) {
//        this.fixedBorder = fixedBorder;
//    }

    public void setStates(State[][][] stateArray) {
        this.stateArray = stateArray;
    }

    public void setStateGetter(Function<Vector3i, State> stateGetter) {
        this.stateGetter = stateGetter;
    }

//    public void expandStateArray() {
//        for (int x = 0; x < xWidth2; x++) {
//            int borderX = x != 0 ? (x == xWidth2 - 1 ? 2 : 1) : 0;
//            for (int z = 0; z < zWidth2; z++) {
//                int borderZ = z != 0 ? (z == zWidth2 - 1 ? 2 : 1) : 0;
//                setRandom(randomArray[borderX][borderZ]);
//                boolean isBorderFixed = fixedBorder[borderX][borderZ];  //is the area already covered by a generated chunk
//                boolean xMid = x % 2 == 1;
//                boolean zMid = z % 2 == 1;
//                int x1 = x / 2;
//                int z1 = z / 2;
//                for (int y = 0; y < yHeight2; y++) {
//                    boolean yMid = y % 2 == 1;
//                    int y1 = y / 2;
//                    State state = null;
//
//                    if (!xMid && !yMid && !zMid) {                      //check if point is in the corners of the expanded cubes
//                        newStateArray[x][z][y] = stateArray[x1][z1][y1];//the original points carry over
//                        continue;
//                    } else if (isBorderFixed) {
//                        state = getBorderState(x, z, y);                //if the borders have been generated, we use those instead
//                        if (state != null) {                            //if the chunk boundaries do not match, we have to run the CA
//                            newStateArray[x][z][y] = state;
//                            continue;
//                        }
//                    }
//                    if (xMid && !zMid && !yMid || !xMid && zMid && !yMid || !xMid && !zMid && yMid) {   //is on edge of expanded cubes
//                        if (xMid) {
//                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1]);   //select either point randomly
//                        } else if (zMid) {
//                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1 + 1][y1]);
//                        } else if (yMid) {
//                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1][y1 + 1]);
//                        }
//                    } else if (!(xMid && yMid && zMid)) {                                               //if point is on faces of expanded cubes
//                        if (!xMid) {
//                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1 + 1][y1], stateArray[x1][z1][y1 + 1], stateArray[x1][z1 + 1][y1 + 1]); //select one from four corners
//                        } else if ((!yMid)) {
//                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], stateArray[x1][z1 + 1][y1], stateArray[x1 + 1][z1 + 1][y1]);
//                        } else if (!zMid) {
//                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], stateArray[x1][z1][y1 + 1], stateArray[x1 + 1][z1][y1 + 1]);
//                        }
//                    }
//                    newStateArray[x][y][z] = state;
//                }
//                for (int y = 0; y < yHeight2; y++) {                    //the center points are not handled, as they require all face points to exist beforehand
//                    boolean yMid = y % 2 == 1;
//                    if (xMid && yMid && zMid) {                         //if there isn't at least 2 axis that have the same state, getState() is called to determine;
//                        int finalX = x;
//                        int finalY = y;
//                        int finalZ = z;
//                        newStateArray[x][y][z] = determineState(newStateArray[x][z][y + 1], newStateArray[x][z][y - 1], newStateArray[x - 1][z][y], newStateArray[x + 1][z][y], newStateArray[x][z - 1][y], newStateArray[x][z + 1][y],
//                                () -> {
////                                    return getState(((int) ((x1 + 0.5) * cellSize)), ((int) ((z1 + 0.5) * cellSize)), ((int) ((y1 + 0.5) * cellSize)));
//                                    return getState(finalX, finalY, finalZ);
//                                });
//                    }
//                }
//            }
//        }
//    }

    public void expandStateArray() {
        if (newStateArray == null) {
            newStateArray = new State[xWidth2][zWidth2][yHeight2];
        }
        fillInCorners();
        fillInEdgesAndFaces();
        fillInCenters();
    }

    public void replaceRandomly(BiFunction<Vector3i, State, Pair<State, Float>> replacer) {
        for (int x = 0; x < xWidth2; x++) {
            int borderX = x != 0 ? (x == xWidth2 - 1 ? 2 : 1) : 0;
            for (int z = 0; z < zWidth2; z++) {
                int borderZ = z != 0 ? (z == zWidth2 - 1 ? 2 : 1) : 0;
                setRandom(randomArray[borderX][borderZ]);
                for (int y = 0; y < yHeight2; y++) {
                    Pair<State, Float> result = replacer.apply(getBlockPosExpanded(x, z, y), newStateArray[x][z][y]);
                    if (random.nextFloat() < result.second) {
                        newStateArray[x][z][y] = result.first;
                    }
                }
            }
        }
    }

    private void fillInCorners() {
        for (int x = 0; x < xWidth; x++) {
            for (int z = 0; z < zWidth; z++) {
                for (int y = 0; y < yHeight; y++) {
                    newStateArray[2 * x][2 * z][2 * y] = stateArray[x][z][y];
                }
            }
        }
    }

    private void fillInEdgesAndFaces() {
        for (int x = 0; x < xWidth2; x++) {
            int borderX = x != 0 ? (x == xWidth2 - 1 ? 2 : 1) : 0;
            for (int z = 0; z < zWidth2; z++) {
                int borderZ = z != 0 ? (z == zWidth2 - 1 ? 2 : 1) : 0;
                setRandom(randomArray[borderX][borderZ]);
                boolean xMid = x % 2 == 1;
                boolean zMid = z % 2 == 1;
                int x1 = x / 2;
                int z1 = z / 2;
                for (int y = 0; y < yHeight2; y++) {
                    boolean yMid = y % 2 == 1;
                    int y1 = y / 2;
                    State state = null;
                    if (xMid && !zMid && !yMid || !xMid && zMid && !yMid || !xMid && !zMid && yMid) {   //is on edge of expanded cubes
                        if (xMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1]);   //select either point randomly
                        } else if (zMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1 + 1][y1]);
                        } else if (yMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1][y1 + 1]);
                        }
                    } else if (!xMid && zMid && yMid || xMid && !zMid && yMid || xMid && zMid && !yMid) {                                               //if point is on faces of expanded cubes
                        if (!xMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1 + 1][y1], stateArray[x1][z1][y1 + 1], stateArray[x1][z1 + 1][y1 + 1]); //select one from four corners
                        } else if (!yMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], stateArray[x1][z1 + 1][y1], stateArray[x1 + 1][z1 + 1][y1]);
                        } else if (!zMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], stateArray[x1][z1][y1 + 1], stateArray[x1 + 1][z1][y1 + 1]);
                        }
                    } else {
                        randomInt(2);        //ensure random calls
                        continue;
                    }
                    if (state != null) {
                        newStateArray[x][z][y] = state;
                    }
                }
            }
        }
    }

    private void fillInCenters() {
        for (int x = 0; x < xWidth2; x++) {
            int borderX = x != 0 ? (x == xWidth2 - 1 ? 2 : 1) : 0;
            for (int z = 0; z < zWidth2; z++) {
                int borderZ = z != 0 ? (z == zWidth2 - 1 ? 2 : 1) : 0;
                setRandom(randomArray[borderX][borderZ]);
                boolean xMid = x % 2 == 1;
                boolean zMid = z % 2 == 1;
                for (int y = 0; y < yHeight2; y++) {
                    boolean yMid = y % 2 == 1;
                    if (xMid && yMid && zMid) {                         //if there isn't at least 2 axis that have the same state, getState() is called to determine;
                        int finalX = x;
                        int finalY = y;
                        int finalZ = z;
                        newStateArray[x][z][y] = determineState(newStateArray[x][z][y + 1], newStateArray[x][z][y - 1], newStateArray[x - 1][z][y], newStateArray[x + 1][z][y], newStateArray[x][z - 1][y], newStateArray[x][z + 1][y],
                                () -> {
//                                    return getState(((int) ((x1 + 0.5) * cellSize)), ((int) ((z1 + 0.5) * cellSize)), ((int) ((y1 + 0.5) * cellSize)));
                                    return getState(finalX, finalY, finalZ);
                                });
                    } else {
                        randomInt(2);       //ensure random calls
                    }
                }
            }
        }
    }

//    /**
//     * get the state of the border, in the expanded array coordinates
//     */
//    protected State getBorderState(int x, int z, int y) {
//        return State.getStateFromBlockState(world.getBlockState(getBlockPosExpanded(x, z, y)));
//    }

    /**
     * expanded array coordinates --> blockpos
     */
    protected BlockPos getBlockPosExpanded(int x, int z, int y) {
        return new BlockPos(basePos.getX() + cellsize / 2 * x, y * cellsize / 2, basePos.getZ() + cellsize / 2 * z);
    }

    /**
     * evaluate state of the given coordinates in the expanded array
     */
    protected State getState(int x, int z, int y) {
        BlockPos pos = getBlockPosExpanded(x, z, y);
//        return stateGetter.apply(new Vector3i(x * cellsize/2, z * cellsize/2, y * cellsize/2));
        return stateGetter.apply(pos);
    }

    private long getSeed(int chunkXOff, int chunkZOff) {
        int chunkX = basePos.getX() >> 4;
        int chunkZ = basePos.getZ() >> 4;
        return NoiseUtil.getSeed(chunkX * 2 + chunkXOff, chunkZ * 2 + chunkZOff, world.getSeed());
    }

    protected void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public int randomInt(int bound) {
        return random.nextInt() % bound;
    }


}

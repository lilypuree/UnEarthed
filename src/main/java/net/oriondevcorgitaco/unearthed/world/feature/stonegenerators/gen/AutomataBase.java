package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.CellularOre;

import java.util.Random;
import java.util.function.Function;

public class AutomataBase implements IAutomata {
    protected long seed;
    protected BlockPos basePos;
    protected int cellsize;
    protected int cellsize2;
    protected int xWidth;
    protected int zWidth;
    protected int yHeight;
    protected int xWidth2;
    protected int zWidth2;
    protected int yHeight2;

    protected TargetedRandom[][] randomArray = new TargetedRandom[3][3];
    protected TargetedRandom random;

    protected State[][][] stateArray;
    protected State[][][] newStateArray = null;
    protected Function<Vector3i, State> stateGetter = null;

    public AutomataBase(long seed, BlockPos basePos, int cellsize, int xWidth, int zWidth, int yHeight) {
        this.seed = seed;
        this.cellsize = cellsize;
        this.cellsize2 = cellsize / 2;
        this.xWidth = xWidth;
        this.xWidth2 = xWidth * 2 - 1;
        this.zWidth = zWidth;
        this.zWidth2 = zWidth * 2 - 1;
        this.yHeight = yHeight;
        this.yHeight2 = yHeight * 2 - 1;
        this.stateArray = new State[xWidth][zWidth][yHeight];
        this.basePos = basePos;
    }

    public AutomataBase(AutomataBase parent) {
        this(parent.seed, parent.basePos, parent.cellsize2, parent.xWidth2, parent.zWidth2, parent.yHeight2);
        this.setStates(parent.newStateArray);
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
        return (int) Math.ceil((height - 1.0f) / cellsize2) + 1;
    }

    public void setStates(State[][][] stateArray) {
        this.stateArray = stateArray;
    }

    public void setStateGetter(Function<Vector3i, State> stateGetter) {
        this.stateGetter = stateGetter;
    }

    public void expandStateArray() {
        if (newStateArray == null) {
            newStateArray = new State[xWidth2][zWidth2][yHeight2];
        }
        fillInCorners();
        fillInEdgesAndFaces();
        fillInCenters();
    }

    public void replaceRandomly(Function<Vector3i, CellularOre> replacer) {
        for (int x = 0; x < xWidth2; x++) {
            for (int z = 0; z < zWidth2; z++) {
                for (int y = 0; y < yHeight2; y++) {
                    CellularOre result = replacer.apply(getBlockPosExpanded(x, z, y));
                    if (result != null && result.canReplace(newStateArray[x][z][y])) {
                        newStateArray[x][z][y] = result.apply(newStateArray[x][z][y]);
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
        boolean xMid = false;
        int borderX = 0;
        for (int x = 0; x < xWidth2; x++, xMid = !xMid) {
            boolean zMid = false;
            int borderZ = 0;
            int x1 = x / 2;
            for (int z = 0; z < zWidth2; z++, zMid = !zMid) {
                setRandom(randomArray[borderX][borderZ]);
                int z1 = z / 2;
                boolean yMid = false;
                for (int y = 0; y < yHeight2; y++, yMid = !yMid) {
                    int y1 = y / 2;
                    State state = null;
                    if (xMid && !yMid) {
                        if (zMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], stateArray[x1][z1 + 1][y1], stateArray[x1 + 1][z1 + 1][y1]);
                        } else {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1]);   //select either point randomly
                        }
                    } else if (yMid && !zMid) {
                        if (xMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], stateArray[x1][z1][y1 + 1], stateArray[x1 + 1][z1][y1 + 1]);
                        } else {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1][y1 + 1]);
                        }
                    } else if (zMid && !xMid) {
                        if (yMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1 + 1][y1], stateArray[x1][z1][y1 + 1], stateArray[x1][z1 + 1][y1 + 1]); //select one from four corners
                        } else {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1 + 1][y1]);
                        }
                    } else {
                        random.skip();        //ensure random calls
                        continue;
                    }
                    if (state != null) {
                        newStateArray[x][z][y] = state;
                    }
                }
                borderZ = z == zWidth2 - 2 ? 2 : 1;
            }
            borderX = x == xWidth2 - 2 ? 2 : 1;
        }
    }

    private void fillInCenters() {
        int borderX = 0;
        boolean xMid = false;
        for (int x = 0; x < xWidth2; x++, xMid = !xMid) {
            int borderZ = 0;
            boolean zMid = false;
            for (int z = 0; z < zWidth2; z++, zMid = !zMid) {
                setRandom(randomArray[borderX][borderZ]);
                boolean yMid = false;
                for (int y = 0; y < yHeight2; y++, yMid = !yMid) {
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
                        random.skip();      //ensure random calls
                    }
                }
                borderZ = z == zWidth2 - 2 ? 2 : 1;
            }
            borderX = x == xWidth2 - 2 ? 2 : 1;
        }
    }

    /**
     * expanded array coordinates --> blockpos
     */
    protected BlockPos getBlockPosExpanded(int x, int z, int y) {
        return new BlockPos(basePos.getX() + cellsize2 * x, y * cellsize2, basePos.getZ() + cellsize2 * z);
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
        return NoiseUtil.getSeed(chunkX * 2 + chunkXOff, chunkZ * 2 + chunkZOff, seed);
    }

    protected void setRandom(TargetedRandom random) {
        this.random = random;
    }

    @Override
    public int randomInt(int bound) {
        long hash = 
        return random.nextInt() % bound;
    }


}

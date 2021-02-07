package net.lilycorgitaco.unearthed.world.feature.stonegenerators.gen;

import com.google.common.base.Functions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.openhft.hashing.LongHashFunction;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.CellularOre;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutomataBase {
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

    protected State[][][] stateArray;
    protected State[][][] newStateArray = null;
    protected Function<Vec3i, State> stateGetter = null;
    protected NoiseHandler noiseHandler;

    public AutomataBase(long seed, BlockPos basePos, int cellsize, int xWidth, int zWidth, int yHeight, NoiseHandler noiseHandler) {
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
        this.noiseHandler = noiseHandler;
    }

    public AutomataBase(AutomataBase parent) {
        this(parent.seed, parent.basePos, parent.cellsize2, parent.xWidth2, parent.zWidth2, parent.yHeight2, parent.noiseHandler);
        this.setStates(parent.newStateArray);
        this.setStateGetter(parent.stateGetter);
    }

    public void setStates(State[][][] stateArray) {
        this.stateArray = stateArray;
    }

    public void setStateGetter(Function<Vec3i, State> stateGetter) {
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

    public void replaceRandomly(Function<Vec3i, CellularOre> replacer) {
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
        for (int x = 0; x < xWidth2; x++, xMid = !xMid) {
            boolean zMid = false;
            int x1 = x / 2;
            for (int z = 0; z < zWidth2; z++, zMid = !zMid) {
                int z1 = z / 2;
                boolean yMid = false;
                for (int y = 0; y < yHeight2; y++, yMid = !yMid) {
                    int y1 = y / 2;
                    State state = null;
                    if (xMid && !yMid) {
                        if (zMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], stateArray[x1][z1 + 1][y1], stateArray[x1 + 1][z1 + 1][y1], basePos.getX() + cellsize2 * x, y * cellsize2, basePos.getZ() + cellsize2 * z);
                        } else {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], basePos.getX() + cellsize2 * x, y * cellsize2, basePos.getZ() + cellsize2 * z);   //select either point randomly
                        }
                    } else if (yMid && !zMid) {
                        if (xMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1 + 1][z1][y1], stateArray[x1][z1][y1 + 1], stateArray[x1 + 1][z1][y1 + 1], basePos.getX() + cellsize2 * x, y * cellsize2, basePos.getZ() + cellsize2 * z);
                        } else {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1][y1 + 1], basePos.getX() + cellsize2 * x, y * cellsize2, basePos.getZ() + cellsize2 * z);
                        }
                    } else if (zMid && !xMid) {
                        if (yMid) {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1 + 1][y1], stateArray[x1][z1][y1 + 1], stateArray[x1][z1 + 1][y1 + 1], basePos.getX() + cellsize2 * x, y * cellsize2, basePos.getZ() + cellsize2 * z); //select one from four corners
                        } else {
                            state = selectStates(stateArray[x1][z1][y1], stateArray[x1][z1 + 1][y1], basePos.getX() + cellsize2 * x, y * cellsize2, basePos.getZ() + cellsize2 * z);
                        }
                    } else {
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
        boolean xMid = false;
        for (int x = 0; x < xWidth2; x++, xMid = !xMid) {
            boolean zMid = false;
            for (int z = 0; z < zWidth2; z++, zMid = !zMid) {
                boolean yMid = false;
                for (int y = 0; y < yHeight2; y++, yMid = !yMid) {
                    if (xMid && yMid && zMid) {                         //if there isn't at least 2 axis that have the same state, getState() is called to determine;
                        BlockPos pos = getBlockPosExpanded(x, z, y);
                        newStateArray[x][z][y] = determineState(newStateArray[x][z][y + 1], newStateArray[x][z][y - 1], newStateArray[x - 1][z][y], newStateArray[x + 1][z][y], newStateArray[x][z - 1][y], newStateArray[x][z + 1][y],
                                () -> stateGetter.apply(pos), pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            }
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
//        BlockPos pos = getBlockPosExpanded(x, z, y);
//        return stateGetter.apply(pos);
        return noiseHandler.getState(x * cellsize2, z * cellsize2, y * cellsize2);
    }

    private long getSeed(int chunkXOff, int chunkZOff) {
        int chunkX = basePos.getX() >> 4;
        int chunkZ = basePos.getZ() >> 4;
        return NoiseUtil.getSeed(chunkX * 2 + chunkXOff, chunkZ * 2 + chunkZOff, seed);
    }

    public int randomInt(int bound, int x, int y, int z) {
        return Math.floorMod((int) getHash(x, y, z), bound);
    }

    public int randomIntFast(int max, int x, int y, int z) {
        return (int) getHash(x, y, z) & max;
    }

    public long getHash(int x, int y, int z) {
        return LongHashFunction.xx(seed).hashInts(new int[]{x, y, z});
    }

    protected State selectStates(State state1, State state2, int x, int y, int z) {
        if (state1 == state2) return state1;
        return randomIntFast(1, x, y, z) == 0 ? state1 : state2;
    }

    protected State selectStates(State state1, State state2, State state3, State state4, int x, int y, int z) {
        if (state1 == state2 && state2 == state3 & state3 == state4) {
            return state1;
        }
        int i = randomIntFast(3, x, y, z);
        if (i == 0) {
            return state1;
        } else if (i == 1) {
            return state2;
        } else {
            return i == 2 ? state3 : state4;
        }
    }
    protected State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter, int x, int y, int z) {
//        boolean ySame = up.equals(down);
//        boolean xSame = x1.equals(x2);
//        boolean zSame = z1.equals(z2);
//        if (xSame && ySame && zSame) {
//            int i = randomInt(3);
//            if (i == 0) return up;
//            else return (i == 1) ? x1 : z1;
//        } else if (!xSame && zSame && ySame) {
//            return selectStates(up, z1);
//        } else if (xSame && !zSame && ySame) {
//            return selectStates(up, x1);
//        } else if (xSame && zSame && !ySame) {
//            return selectStates(x1, z1);
//        } else {
//            randomInt(3);
//            return stateGetter.get();
//        }
        Optional<State> state = Stream.of(up, down, x1, x2, z1, z2)
//                .sorted()
                .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
        return state.orElseGet(stateGetter::get);
    }
}

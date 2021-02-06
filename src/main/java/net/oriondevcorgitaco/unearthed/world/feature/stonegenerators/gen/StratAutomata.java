package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;

import net.minecraft.util.math.BlockPos;
import net.openhft.hashing.LongHashFunction;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;

public class StratAutomata {
    protected long worldSeed;
    protected long seed;
    protected BlockPos basePos;
    protected int cellsize;
    protected int cellsize2;
    protected int xWidth;
    protected int zWidth;
    protected int xWidth2;
    protected int zWidth2;

    protected StrataState[][] stateArray = null;
    protected StrataState[][] newStateArray = null;
    private boolean simpleSelect = false;
    private NoiseHandler noiseHandler;
    private int level;

    public StratAutomata(long seed, BlockPos basePos, NoiseHandler noiseHandler) {
        this.worldSeed = seed;
        this.basePos = basePos;
        this.noiseHandler = noiseHandler;
    }

    public void set(int level, int cellsize, int xWidth, int zWidth) {
        this.seed = worldSeed * (level + 1);
        this.level = level;
        this.cellsize = cellsize;
        this.xWidth = xWidth;
        this.zWidth = zWidth;
        setNextVars();
    }

    public StratAutomata nextStage() {
        this.cellsize = cellsize2;
        this.xWidth = xWidth2;
        this.zWidth = zWidth2;
        this.stateArray = newStateArray;
        setNextVars();
        return this;
    }

    private void setNextVars() {
        this.cellsize2 = cellsize / 2;
        this.xWidth2 = xWidth * 2 - 1;
        this.zWidth2 = zWidth * 2 - 1;
    }

    public StratAutomata fillInStates() {
        this.stateArray = new StrataState[xWidth][zWidth];
        for (int x = 0; x < xWidth; x++) {
            for (int z = 0; z < zWidth; z++) {
                stateArray[x][z] = getState(basePos.getX() + x * cellsize, basePos.getZ() + z * cellsize);
            }
        }
        return this;
    }

    public StratAutomata propagate() {
        this.newStateArray = new StrataState[xWidth2][zWidth2];
        boolean xMid = false;
        for (int x = 0; x < xWidth2; x++, xMid = !xMid) {
            boolean zMid = false;
            int x1 = x / 2;
            for (int z = 0; z < zWidth2; z++, zMid = !zMid) {
                int z1 = z / 2;
                StrataState state = null;
                if (xMid) {
                    if (zMid) {
                        state = selectState(stateArray[x1][z1], stateArray[x1 + 1][z1], stateArray[x1][z1 + 1], stateArray[x1 + 1][z1 + 1], basePos.getX() + cellsize2 * x, basePos.getZ() + cellsize2 * z);
                    } else {
                        state = selectState(stateArray[x1][z1], stateArray[x1 + 1][z1], basePos.getX() + cellsize2 * x, basePos.getZ() + cellsize2 * z);
                    }
                } else if (zMid) {
                    state = selectState(stateArray[x1][z1], stateArray[x1][z1 + 1], basePos.getX() + cellsize2 * x, basePos.getZ() + cellsize2 * z);
                } else {
                    state = stateArray[x1][z1];
                }
                newStateArray[x][z] = state;
            }
        }
        return this;
    }

    public StratAutomata simpleSelect() {
        this.simpleSelect = true;
        return this;
    }

    public StrataState[][] getResults() {
        return newStateArray;
    }

    protected StrataState selectState(StrataState strata1, StrataState strata2, int x, int z) {
        if (strata1 == null) {
            if (strata2 == null) return null;
            else {
                int t = randomInt(strata2.getThickness() + 1, x, z);
                if (t == 0) return null;
                else return new StrataState(strata2.getState(), t);
            }
        } else {
            State cell1 = strata1.getState();
            if (strata2 == null) {
                int t = randomInt(strata1.getThickness() + 1, x, z);
                return t == 0 ? null : new StrataState(cell1, t);
            } else {
                State cell2 = strata2.getState();
                int t1 = strata1.getThickness();
                int t2 = strata2.getThickness();
                State cell = (cell1 == cell2) ? cell1 : ((randomIntFast(1, x, z) == 0) ? cell1 : cell2);
                if (t1 == t2) return new StrataState(cell, t1);
                else if (t1 < t2) return new StrataState(cell, randomInt(t1, t2 + 1, x, z));
                else return new StrataState(cell, randomInt(t2, t1 + 1, x, z));
            }
        }
    }

    protected StrataState selectState(StrataState state1, StrataState state2, StrataState state3, StrataState state4, int x, int z) {
        boolean b1 = state1 == null;
        boolean b2 = state2 == null;
        boolean b3 = state3 == null;
        boolean b4 = state4 == null;
        if (b1 & b2) {
            if (b3 && b4) return null;
            else {
                return selectState(state3, state4, x, z);
            }
        } else {
            if (b3 && b4) {
                return selectState(state1, state2, x, z);
            } else {
                if (simpleSelect) {
                    int i = randomIntFast(3, x, z);
                    if (i == 0) {
                        return state1;
                    } else if (i == 1) {
                        return state2;
                    } else if (i == 2) {
                        return state3;
                    } else {
                        return state4;
                    }
                } else {
                    return getState(x, z);
                }
            }
        }
    }

    private StrataState getState(int x, int z) {
        return noiseHandler.getStrata(x, z, level);
    }

    public int randomIntFast(int max, int x, int z) {
        return (int) getHash(x, z) & max;
    }

    public int randomInt(int bound, int x, int z) {
        return Math.floorMod((int) getHash(x, z), bound);
    }

    public int randomInt(int min, int max, int x, int z) {
        return Math.floorMod((int) getHash(x, z), max - min) + min;
    }

    private long getHash(int x, int z) {
        return LongHashFunction.xx(seed).hashInts(new int[]{x, z});
    }

    public int getCellsize2() {
        return cellsize2;
    }

    public static class StrataState {
        private int thickness;
        private State state;

        public StrataState(State state, int thickness) {
            this.state = state;
            this.thickness = thickness;
        }

        public State getState() {
            return state;
        }

        public int getThickness() {
            return thickness;
        }
    }
}

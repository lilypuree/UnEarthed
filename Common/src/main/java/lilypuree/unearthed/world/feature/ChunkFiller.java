package lilypuree.unearthed.world.feature;

import com.google.common.base.Functions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import net.openhft.hashing.LongHashFunction;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChunkFiller {
    protected final long seed;
    protected final BlockPos pos;
    protected final State[][][] stateArray;
    protected final int ySize;

    public static record State(BlockState block) {

    }

    public ChunkFiller(long seed, BlockPos minPos, int ySize) {
        this.seed = seed;
        this.pos = minPos;
        this.ySize = ((int) Math.ceil(ySize / 8.0)) * 8 + 1;
        this.stateArray = new State[17][17][this.ySize];
    }

    public void run(Function<Vec3i, State> stateGetter) {
        fillIn(2, stateGetter);
        fillInEdgesAndFaces(8);
        fillInCenters(8, stateGetter);
        fillInEdgesAndFaces(4);
        fillInCenters(4, stateGetter);
        fillInEdgesAndFaces(2);
        fillInCenters(2);
        fillInEdgesAndFaces(1);
        fillInCenters(1);
    }

    private void fillIn(int s, Function<Vec3i, State> stateGetter) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 17; x += s) {
            mutable.setX(pos.getX() + x);
            for (int z = 0; z < 17; z += s) {
                mutable.setZ(pos.getZ() + z);
                for (int y = 0; y < ySize; y += s) {
                    mutable.setY(pos.getY() + y);
                    stateArray[x][z][y] = stateGetter.apply(mutable);
                }
            }
        }
    }

    //stepsize could be 1, 2, 4, 8
    //s : step size
    private void fillInEdgesAndFaces(int s) {
        boolean xMid = false;
        for (int x = 0; x < 17; x += s, xMid = !xMid) {
            int posX = pos.getX() + x;
            boolean zMid = false;
            for (int z = 0; z < 17; z += s, zMid = !zMid) {
                int posZ = pos.getZ() + z;
                boolean yMid = false;
                for (int y = 0; y < ySize; y += s, yMid = !yMid) {
                    State state = null;
                    if (xMid && !yMid) {
//                        if (stateArray[x][y][z] != null) continue;
                        if (zMid) {
                            state = selectStates(stateArray[x - s][z - s][y], stateArray[x + s][z - s][y], stateArray[x - s][z + s][y], stateArray[x + s][z + s][y], posX, y, posZ);
                        } else {
                            state = selectStates(stateArray[x - s][z][y], stateArray[x + s][z][y], posX, y, posZ);   //select either point randomly
                        }
                    } else if (yMid && !zMid) {
//                        if (stateArray[x][y][z] != null) continue;
                        if (xMid) {
                            state = selectStates(stateArray[x - s][z][y - s], stateArray[x + s][z][y - s], stateArray[x - s][z][y + s], stateArray[x + s][z][y + s], posX, y, posZ);
                        } else {
                            state = selectStates(stateArray[x][z][y - s], stateArray[x][z][y + s], posX, y, posZ);
                        }
                    } else if (zMid && !xMid) {
//                        if (stateArray[x][y][z] != null) continue;
                        if (yMid) {
                            state = selectStates(stateArray[x][z - s][y - s], stateArray[x][z + s][y - s], stateArray[x][z - s][y + s], stateArray[x][z + s][y + s], posX, y, posZ); //select one from four corners
                        } else {
                            state = selectStates(stateArray[x][z - s][y], stateArray[x][z + s][y], posX, y, posZ);
                        }
                    } else {
                        continue;
                    }

                    if (state != null) {
                        stateArray[x][z][y] = state;
                    }
                }
            }
        }
    }

    private void fillInCenters(int s) {
        boolean xMid = false;
        for (int x = 0; x < 17; x += s, xMid = !xMid) {
            int posX = pos.getX() + x;
            boolean zMid = false;
            for (int z = 0; z < 17; z += s, zMid = !zMid) {
                int posZ = pos.getZ() + z;
                boolean yMid = false;
                for (int y = 0; y < ySize; y += s, yMid = !yMid) {
                    if (xMid && yMid && zMid) {
//                        if (stateArray[x][y][z] != null) continue;
                        int posY = pos.getY() + y;
                        stateArray[x][z][y] = selectRandom(stateArray[x][z][y + s], stateArray[x][z][y - s], stateArray[x - s][z][y], stateArray[x + s][z][y], stateArray[x][z - s][y], stateArray[x][z + s][y], posX, posY, posZ);
                    }
                }
            }
        }
    }

    private void fillInCenters(int s, Function<Vec3i, State> stateGetter) {
        boolean xMid = false;
        for (int x = 0; x < 17; x += s, xMid = !xMid) {
            int posX = pos.getX() + x;
            boolean zMid = false;
            for (int z = 0; z < 17; z += s, zMid = !zMid) {
                int posZ = pos.getZ() + z;
                boolean yMid = false;
                for (int y = 0; y < ySize; y += s, yMid = !yMid) {
                    if (xMid && yMid && zMid) {
//                        if (stateArray[x][y][z] != null) continue;
                        int posY = pos.getY() + y;
                        stateArray[x][z][y] = determineState(stateArray[x][z][y + s], stateArray[x][z][y - s], stateArray[x - s][z][y], stateArray[x + s][z][y], stateArray[x][z - s][y], stateArray[x][z + s][y],
                                () -> stateGetter.apply(new Vec3i(posX, posY, posZ)), posX, posY, posZ);
                    }
                }
            }
        }
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

    public int randomIntFast(int max, int x, int y, int z) {
        return (int) getHash(x, y, z) & max;
    }

    public int randomInt(int bound, int x, int y, int z) {
        return Math.floorMod((int) getHash(x, y, z), bound);
    }

    public long getHash(int x, int y, int z) {
        return LongHashFunction.xx(seed).hashInts(new int[]{x, y, z});
    }

    protected State selectRandom(State up, State down, State x1, State x2, State z1, State z2, int x, int y, int z) {
        int i = randomInt(6, x, y, z);
        return switch (i) {
            case 0 -> up;
            case 1 -> down;
            case 2 -> x1;
            case 3 -> x2;
            case 4 -> z1;
            default -> z2;
        };
    }

    protected State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter, int x, int y, int z) {
        Optional<State> state = Stream.of(up, down, x1, x2, z1, z2)
//                .sorted()
                .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
        return state.orElseGet(stateGetter);
    }
}

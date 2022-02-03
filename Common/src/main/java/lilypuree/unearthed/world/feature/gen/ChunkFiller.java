package lilypuree.unearthed.world.feature.gen;

import com.google.common.base.Functions;
import lilypuree.unearthed.world.feature.BiomeBasedReplacer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.openhft.hashing.LongHashFunction;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChunkFiller {
    protected final long seed;
    protected final BlockPos pos;
    protected final StoneType[][][] stoneTypeArray;
    protected final int ySize;

    @FunctionalInterface
    public static interface StonetypeGetter {
        StoneType get(int posX, int posY, int posZ);
    }

    public ChunkFiller(long seed, BlockPos minPos, int ySize) {
        this.seed = seed;
        this.pos = minPos;
        this.ySize = ((int) Math.ceil(ySize / 16.0)) * 16 + 1;
        this.stoneTypeArray = new StoneType[17][17][this.ySize];
    }

    public StoneType[][][] results() {
        return stoneTypeArray;
    }

    protected class Iterator {
        protected boolean xMid = false, yMid = false, zMid = false;
        protected int posX = pos.getX(), posZ = pos.getZ();
        protected int x = 0, y = 0, z = 0;
        protected int s;

        public Iterator(int s) {
            this.s = s;
        }

        public boolean next() {
            y += s;
            if (y >= ySize) {
                yMid = false;
                y = 0;
                z += s;

                if (z >= 17) {
                    zMid = false;
                    z = 0;
                    x += s;


                    if (x >= 17) {
                        return false;
                    }
                    xMid = !xMid;
                    posX = pos.getX() + x;
                    return true;
                }
                posZ = pos.getZ() + z;
                zMid = !zMid;
                return true;
            }
            yMid = !yMid;
            return true;
        }

        private StoneType edgeX() {
            return select(stoneTypeArray[x - s][z][y], stoneTypeArray[x + s][z][y], posX, y, posZ);
        }

        private StoneType edgeZ() {
            return select(stoneTypeArray[x][z - s][y], stoneTypeArray[x][z + s][y], posX, y, posZ);
        }

        private StoneType edgeY() {
            return select(stoneTypeArray[x][z][y - s], stoneTypeArray[x][z][y + s], posX, y, posZ);
        }

        private StoneType faceX() {
            return select(stoneTypeArray[x][z - s][y - s], stoneTypeArray[x][z + s][y - s], stoneTypeArray[x][z - s][y + s], stoneTypeArray[x][z + s][y + s], posX, y, posZ);
        }

        private StoneType faceZ() {
            return select(stoneTypeArray[x - s][z][y - s], stoneTypeArray[x + s][z][y - s], stoneTypeArray[x - s][z][y + s], stoneTypeArray[x + s][z][y + s], posX, y, posZ);
        }

        private StoneType faceY() {
            return select(stoneTypeArray[x - s][z - s][y], stoneTypeArray[x + s][z - s][y], stoneTypeArray[x - s][z + s][y], stoneTypeArray[x + s][z + s][y], posX, y, posZ);
        }

        private StoneType randomCenter() {
            return selectRandom(stoneTypeArray[x][z][y + s], stoneTypeArray[x][z][y - s], stoneTypeArray[x - s][z][y], stoneTypeArray[x + s][z][y], stoneTypeArray[x][z - s][y], stoneTypeArray[x][z + s][y], posX, y, posZ);
        }

        private StoneType center(StonetypeGetter getter) {
            return determineState(stoneTypeArray[x][z][y + s], stoneTypeArray[x][z][y - s], stoneTypeArray[x - s][z][y], stoneTypeArray[x + s][z][y], stoneTypeArray[x][z - s][y], stoneTypeArray[x][z + s][y],
                    () -> getter.get(posX, pos.getY() + y, posZ));
        }

        private StoneType fastCenter() {
            return determineState(stoneTypeArray[x][z][y + s], stoneTypeArray[x][z][y - s], stoneTypeArray[x - s][z][y], stoneTypeArray[x + s][z][y], stoneTypeArray[x][z - s][y], stoneTypeArray[x][z + s][y], posX, y, posZ);
        }

        private void set(StoneType state) {
            stoneTypeArray[x][z][y] = state;
        }
    }

    public void fillIn(int s, StonetypeGetter stonetypeGetter) {
        for (int x = 0; x < 17; x += s) {
            int posX = x + pos.getX();
            for (int z = 0; z < 17; z += s) {
                int posZ = z + pos.getZ();
                for (int y = 0; y < ySize; y += s) {
                    int posY = y + pos.getY();
                    stoneTypeArray[x][z][y] = stonetypeGetter.get(posX, posY, posZ);
                }
            }
        }
    }

    public void fillEmpty(int s, StonetypeGetter stonetypeGetter) {
        for (int x = 0; x < 17; x += s) {
            int posX = x + pos.getX();
            for (int z = 0; z < 17; z += s) {
                int posZ = z + pos.getZ();
                for (int y = 0; y < ySize; y += s) {
                    int posY = y + pos.getY();
                    if (stoneTypeArray[x][z][y] == StoneType.EMPTY)
                        stoneTypeArray[x][z][y] = stonetypeGetter.get(posX, posY, posZ);
                }
            }
        }
    }

    public void fillBiome(int s, StonetypeGetter stonetypeGetter) {
        for (int x = 0; x < 17; x += s) {
            int posX = x + pos.getX();
            for (int z = 0; z < 17; z += s) {
                int posZ = z + pos.getZ();
                for (int y = 0; y < ySize; y += s) {
                    int posY = y + pos.getY();
                    if (!stoneTypeArray[x][z][y].ignoresBiome()) {
                        StoneType stoneType = stonetypeGetter.get(posX, posY, posZ);
                        if (stoneType != null)
                            stoneTypeArray[x][z][y] = stoneType;
                    }
                }
            }
        }
    }

    //stepsize could be 1, 2, 4, 8
    //s : step size
    public void fillInEdgesAndFaces(int s) {
        Iterator i = new Iterator(s);
        do {
            if (i.xMid && !i.yMid) {
//                        if (stateArray[x][y][z] != null) continue;
                if (i.zMid) {
                    i.set(i.faceY());
                } else {
                    i.set(i.edgeX());
                }
            } else if (i.yMid && !i.zMid) {
//                        if (stateArray[x][y][z] != null) continue;
                if (i.xMid) {
                    i.set(i.faceZ());
                } else {
                    i.set(i.edgeY());
                }
            } else if (i.zMid && !i.xMid) {
//                        if (stateArray[x][y][z] != null) continue;
                if (i.yMid) {
                    i.set(i.faceX());
                } else {
                    i.set(i.edgeZ());
                }
            }
        } while (i.next());
    }

    public void fillInCentersRandom(int s) {
        Iterator i = new Iterator(s);
        do {
            if (i.xMid && i.yMid && i.zMid) {
                i.set(i.randomCenter());
            }
        } while (i.next());
    }

    public void fillInCentersFast(int s) {
        Iterator i = new Iterator(s);
        do {
            if (i.xMid && i.yMid && i.zMid) {
                i.set(i.fastCenter());
            }
        } while (i.next());
    }

    public void fillInCenters(int s, StonetypeGetter stonetypeGetter) {
        Iterator i = new Iterator(s);
        do {
            if (i.xMid && i.yMid && i.zMid) {
                i.set(i.center(stonetypeGetter));
            }
        } while (i.next());
    }


    private StoneType select(StoneType state1, StoneType state2, int x, int y, int z) {
        if (state1 == state2) return state1;
        return randomIntFast(1, x, y, z) == 0 ? state1 : state2;
    }

    private StoneType select(StoneType state1, StoneType state2, StoneType state3, StoneType state4, int x, int y, int z) {
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

    private int randomIntFast(int max, int x, int y, int z) {
        return (int) getHash(x, y, z) & max;
    }

    private int randomInt(int bound, int x, int y, int z) {
        return Math.floorMod((int) getHash(x, y, z), bound);
    }

    private long getHash(int x, int y, int z) {
        return LongHashFunction.xx(seed).hashInts(new int[]{x, y, z});
    }

    private StoneType selectRandom(StoneType up, StoneType down, StoneType x1, StoneType x2, StoneType z1, StoneType z2, int x, int y, int z) {
        boolean horizontalEquals = x1 == x2 & z1 == z2 & x1 == z1;
        if (horizontalEquals && up == x1 || horizontalEquals && down == x1) {
            return x1;
        }
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

    private StoneType determineState(StoneType up, StoneType down, StoneType x1, StoneType x2, StoneType z1, StoneType z2, Supplier<StoneType> stateGetter) {
        Optional<StoneType> state = Stream.of(up, down, x1, x2, z1, z2)
//                .sorted()
                .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
        return state.orElseGet(stateGetter);
    }

    private StoneType determineState(StoneType up, StoneType down, StoneType x1, StoneType x2, StoneType z1, StoneType z2, int x, int y, int z) {
        boolean ySame = up == down;
        boolean xSame = x1 == x2;
        boolean zSame = z1 == z2;
        if (xSame && ySame && zSame) {
            int i = randomInt(3, x, y, z);
            if (i == 0) return up;
            else return (i == 1) ? x1 : z1;
        } else if (!xSame && zSame && ySame) {
            return select(up, z1, x, y, z);
        } else if (xSame && !zSame && ySame) {
            return select(up, x1, x, y, z);
        } else if (xSame && zSame && !ySame) {
            return select(x1, z1, x, y, z);
        } else if (ySame) {
            return up;
        } else {
            return select(x1, x2, z1, z2, x, y, z);
        }
    }
}

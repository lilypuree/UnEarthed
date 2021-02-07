package net.lilycorgitaco.unearthed.world.feature.stonegenerators.gen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.State;

public class AutomataFiller extends AutomataBase {

    private State[][] subChunkStates = new State[5][yHeight];

    public AutomataFiller(long seed, BlockPos basePos, final int maxHeight, NoiseHandler noiseHandler) {
        super(seed, basePos, 16, 2, 2, (maxHeight + 30) / 16, noiseHandler);
    }

    //fill the initial stateArray
    public void fillStates() {
        if (newStateArray == null) {
            newStateArray = new State[xWidth2][zWidth2][yHeight2];
        }
//        for (int x = -1; x <= 1; x++) {
//            for (int z = -1; z <= 1; z++) {
//                //nearby chunk generation check
////                fixedBorder[x + 1][z + 1] = !(x == 0 && z == 0) && hasChunkGenerated(basePos.getX() + x * 16, basePos.getZ() + z * 16);
//                if (x * z == 0) {                               //only check adjacent chunks
//                    for (int y = 0; y < yHeight; y++) {
//                        subChunkStates[2 + x * 2 + z][y] = getSubChunkState(x, z, y);
//                    }
//                }
//            }
//        }
//        boolean[] subchunkContinuity = new boolean[yHeight];
//        for (int y = 0; y < yHeight; y++) {
//            subchunkContinuity[y] = isSubChunkContinuous(y);
//        }
        for (int y = 0; y < yHeight2; y++) {
//            boolean yMid = y % 2 == 1;
            //check if the surrounding chunks have the same state
//            if (subchunkContinuity[y / 2] && (!yMid || subchunkContinuity[y / 2 + 1])) {
//                fillInLayerWithState(y, subChunkStates[2][y / 2]);
//            } else {
            //use noise to figure out the layer
            calculateLayerStates(y);
//            }
        }
    }


//    private boolean hasChunkGenerated(int posX, int posZ) {
//        return State.isMarker(world.getBlockState(new BlockPos(posX << 4, 0, posZ << 4)));
//    }

    Direction[] bottom = new Direction[]{Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.UP};
    Direction[] top = new Direction[]{Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.DOWN};

    private boolean isSubChunkContinuous(int y) {
        State center = subChunkStates[2][y];
        Direction[] directions = (y != 0) ? ((y != yHeight - 1) ? Direction.values() : top) : bottom;
        for (Direction dir : directions) {
            Vec3i vi = dir.getVector();
            if (!center.equals(subChunkStates[2 + vi.getX() * 2 + vi.getZ()][y + vi.getY()])) {
                return false;
            }
        }
        return true;
    }

    private void fillInLayerWithState(int y, State state) {
        for (int x = 0; x < xWidth2; x++) {
            for (int z = 0; z < zWidth2; z++) {
                newStateArray[x][z][y] = state;
            }
        }
    }

    private void calculateLayerStates(int y) {
        for (int x = 0; x < xWidth2; x++) {
            for (int z = 0; z < zWidth2; z++) {
                newStateArray[x][z][y] = getState(x, z, y);
            }
        }
    }
//
//    @Override
//    protected State getState(int x, int z, int y) {
//        return stateGetter.apply(new Vector3i(x * 4, z * 4, y * 4));
//    }
//
//    /**
//     * x and z are chunk position offsets
//     */
//    private State getSubChunkState(int xOff, int zOff, int yOff) {
//        return stateGetter.apply(new FastNoiseLite.Vector3(basePos.getX() + 8 * (1 + xOff), basePos.getZ() + 8 * (1 + zOff), 8 * yOff));
//    }
}

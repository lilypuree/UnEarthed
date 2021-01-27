//package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;
//
//import net.minecraft.block.BlockState;
//
//import java.util.Random;
//import java.util.function.Supplier;
//
//public abstract class Automata extends AutomataBase implements IAutomata {
//    private int maxHeight;
//    private State[][][] stateArray = new State[xWidth][zWidth][yHeight];
//    private boolean[][] fixedBorder;
//    private Random random;
//
//    private void expandArray() {
//        State[][][] newArray = new State[xWidth2][zWidth2][yHeight2];   //create a larger array
//        for (int x = 0; x < xWidth2; x++) {
//            int borderX = x != 0 ? (x != xWidth2 ? 1 : 2) : 0;
//            for (int z = 0; z < zWidth2; z++) {
//                int borderZ = z != 0 ? (z != zWidth2 ? 1 : 2) : 0;
//                boolean isBorderFixed = fixedBorder[borderX][borderZ];  //is the area already covered by a generated chunk
//
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
//                        newArray[x][z][y] = stateArray[x1][z1][y1];     //the original points carry over
//                        continue;
//                    } else if (isBorderFixed) {
//                        state = getBorderState(x, z, y);                //if the borders have been generated, we use those instead
//                        if (state != null) {                            //if the chunk boundaries do not match, we have to run the CA
//                            newArray[x][z][y] = state;
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
//                    newArray[x][y][z] = state;
//                }
//                for (int y = 0; y < yHeight2; y++) {                    //the center points are not handled, as they require all face points to exist beforehand
//                    boolean yMid = y % 2 == 1;
//                    if (xMid && yMid && zMid) {                         //if there isn't at least 2 axis that have the same state, getState() is called to determine;
//                        int finalX = x;
//                        int finalY = y;
//                        int finalZ = z;
//                        newArray[x][y][z] = determineState(newArray[x][z][y + 1], newArray[x][z][y - 1], newArray[x - 1][z][y], newArray[x + 1][z][y], newArray[x][z - 1][y], newArray[x][z + 1][y],
//                                () -> {
////                                    return getState(((int) ((x1 + 0.5) * cellSize)), ((int) ((z1 + 0.5) * cellSize)), ((int) ((y1 + 0.5) * cellSize)));
//                                    return getState(finalX, finalY, finalZ);
//                                });
//                    }
//                }
//            }
//        }
//    }
//
//    public abstract void replaceBlocks();
//
//
//}

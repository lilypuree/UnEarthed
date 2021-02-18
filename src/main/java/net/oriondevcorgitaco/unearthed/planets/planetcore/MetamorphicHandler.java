package net.oriondevcorgitaco.unearthed.planets.planetcore;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetamorphicHandler {
    private MantleCoreTile parent;
    private World world;
    private BlockPos corePos;
    private boolean isActive;
    List<BlockCount> existingStates;
    Set<Long> visited;
    List<LinkedList<Pair<BlockPos, BlockCount>>> queues;
    float changeChance = 0.1f;

    public MetamorphicHandler(MantleCoreTile mantleCore) {
        this.parent = mantleCore;
        this.world = mantleCore.getWorld();
        this.corePos = mantleCore.getPos();
    }

    public void init() {
        this.world = parent.getWorld();
        this.corePos = parent.getPos();

    }

    public boolean isActive() {
        return isActive;
    }

    public boolean tick() {
        if (queues.size() <= 0) {
            this.isActive = false;
            existingStates.clear();
            visited.clear();
            return true;
        } else {
            Random rand = world.rand;
            for (int i = queues.size() - 1; i >= 0; i--) {
                LinkedList<Pair<BlockPos, BlockCount>> queue = queues.get(i);
                if (queue.size() <= 0) {
                    queues.remove(i);
                } else {
                    Pair<BlockPos, BlockCount> pair = queue.poll();
                    BlockPos currentPos = pair.getKey();
                    BlockCount blockCount = pair.getValue();
                    //As long as there's other states to change into, each branch has a change to turn into some other block
                    if (!existingStates.isEmpty()) {
                        if (rand.nextFloat() < changeChance) {
                            blockCount = existingStates.get(rand.nextInt(existingStates.size()));
                        }
                    }
                    if (parent.canSetBlock(currentPos, true, false)) {
                        parent.setBlock(currentPos, blockCount.state);
                        //remove depleted states, and replace with intrusive(default) blocks
                        if (blockCount.count-- == 0) {
                            existingStates.remove(blockCount);
                            //since these have a negative count they will not be handled here
                            blockCount = new BlockCount(parent.getIntrusiveBlock(), -1);
                        }
                    }
                    for (Direction dir : Direction.values()) {
                        BlockPos nextBlockPos = currentPos.offset(dir);
                        if (!visited.contains(nextBlockPos.toLong())) {
                            visited.add(nextBlockPos.toLong());
                            if (parent.isInsideSphere(nextBlockPos) && parent.isGeneratedBlock(world.getBlockState(nextBlockPos))) {
                                queue.add(Pair.of(nextBlockPos, blockCount));
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    public void startMetamorphicEvent() {
        Random rand = world.rand;

        isActive = true;
        //get a list of the count of each blockstate in the planet, removing any non generated blocks
        existingStates = findAllBlocks().stream().filter(blockCount -> parent.isGeneratedBlock(blockCount.state)).collect(Collectors.toList());
        //metamorphose the existing blocks
        existingStates.forEach(blockCount -> {
            blockCount.state = parent.getMetamorphosedBlock(blockCount.state);
        });
        //BFS from the starting positions
        visited = new LongArraySet();
        queues = new ArrayList<>();

        // add 3~5 starting positions
        int approxR = (int) Math.ceil(parent.getRadius());
        for (int i = 0; i < 3 + rand.nextInt(2); i++) {
            int tries = 0;
            BlockPos.Mutable startPos = new BlockPos.Mutable();
            do {
                do {
                    int x = corePos.getX() - approxR + rand.nextInt(approxR * 2 + 1);
                    int y = corePos.getY() - approxR + rand.nextInt(approxR * 2 + 1);
                    int z = corePos.getZ() - approxR + rand.nextInt(approxR * 2 + 1);
                    startPos = startPos.setPos(x, y, z);
                } while (!parent.isInsideSphere(startPos));
            } while (!parent.isGeneratedBlock(world.getBlockState(startPos)) && !visited.contains(startPos.toLong()) && tries++ < 25);
            if (tries < 25) {
                visited.add(startPos.toLong());
                LinkedList<Pair<BlockPos, BlockCount>> queue = new LinkedList<>();
                queue.add(Pair.of(startPos.toImmutable(), existingStates.get(rand.nextInt(existingStates.size()))));
                queues.add(queue);
            }
        }
    }

    private List<BlockCount> findAllBlocks() {
        return parent.forEachPlanetBlock().map(pos -> world.getBlockState(pos))
                .collect(ArrayList::new, (list, state) -> {
                    for (BlockCount blockCount : list) {
                        if (blockCount.state == state) {
                            blockCount.count++;
                            return;
                        }
                    }
                    list.add(new BlockCount(state, 1));
                }, (list1, list2) -> {
                    list2.forEach(blockCount -> {
                        int index = list1.indexOf(blockCount);
                        if (index >= 0) {
                            list1.get(index).count += blockCount.count;
                        } else {
                            list1.add(blockCount);
                        }
                    });
                });
    }

    private static class BlockCount {
        BlockState state;
        int count;

        public BlockCount(BlockState state, int count) {
            this.state = state;
            this.count = count;
        }

        public BlockCount(CompoundNBT nbt) {
            this.state = NBTUtil.readBlockState(nbt.getCompound("count"));
            this.count = nbt.getInt("count");
        }

        public CompoundNBT getTag() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.put("state", NBTUtil.writeBlockState(state));
            nbt.putInt("count", count);
            return nbt;
        }
    }

    public void read(CompoundNBT nbt) {
        isActive = nbt.getBoolean("active");
        BlockCount intrusive = new BlockCount(parent.getIntrusiveBlock(), -1);
        if (isActive) {
            visited = new LongArraySet(nbt.getLongArray("visited"));
            ListNBT states = nbt.getList("existingStates", 10);
            existingStates = new ArrayList<>();
            states.forEach(inbt -> {
                existingStates.add(new BlockCount((CompoundNBT) inbt));
            });
            ListNBT nbtQueues = nbt.getList("queues", 9);
            queues = new ArrayList<>();
            nbtQueues.forEach(inbt -> {
                ListNBT queueList = (ListNBT) inbt;
                LinkedList<Pair<BlockPos, BlockCount>> queue = new LinkedList<>();
                queueList.forEach(inbt1 -> {
                    CompoundNBT compound = (CompoundNBT) inbt1;
                    BlockPos pos = BlockPos.fromLong(compound.getLong("pos"));
                    int index = compound.getInt("type");
                    BlockCount blockCount;
                    if (index == -1) {
                        blockCount = intrusive;
                    } else {
                        blockCount = existingStates.get(index);
                    }
                    queue.addLast(Pair.of(pos, blockCount));
                });
                queues.add(queue);
            });
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("active", isActive);
        if (isActive) {
            compound.putLongArray("visited", new ArrayList<>(visited));
            ListNBT states = new ListNBT();
            ListNBT nbtQueues = new ListNBT();
            existingStates.forEach(blockCount -> states.add(blockCount.getTag()));
            queues.forEach(queue -> {
                ListNBT queueList = new ListNBT();
                queue.forEach(pair -> {
                    CompoundNBT nbt = new CompoundNBT();
                    nbt.putLong("pos", pair.getKey().toLong());
                    nbt.putInt("type", existingStates.indexOf(pair.getValue()));
                    queueList.add(nbt);
                });
                nbtQueues.add(queueList);
            });
            compound.put("existingStates", states);
            compound.put("queues", nbtQueues);
        }
        return compound;
    }
}

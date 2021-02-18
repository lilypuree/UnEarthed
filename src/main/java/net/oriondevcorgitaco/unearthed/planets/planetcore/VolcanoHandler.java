package net.oriondevcorgitaco.unearthed.planets.planetcore;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

public class VolcanoHandler {
    private MantleCoreTile parent;
    private World world;
    private BlockPos corePos;

    private boolean isActive;
    private boolean isExtrusionStage;
    private int remainingAmount;

    private Vector3d target;
    private int eruptions = 0;

    private BlockPos.Mutable currentPos;
    private Set<Long> visited;
    private LinkedList<BlockPos> queue;

    public VolcanoHandler(MantleCoreTile mantleCore) {
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


    public void volcanicExplosion(BlockPos volcano, int amount) {
        this.isActive = true;
        target = new Vector3d(volcano.getX() - corePos.getX(), volcano.getY() - corePos.getY(), volcano.getZ() - corePos.getZ());
        currentPos = corePos.toMutable();
        remainingAmount = amount;
        isExtrusionStage = false;
    }

    public void tick() {
        if (remainingAmount <= 0) {
            this.isActive = false;
            return;
        }
        if (isExtrusionStage) {
            flowLava();
        } else {
            makePipe();
        }
    }

    private void makePipe() {
        double xOff = currentPos.getX() - corePos.getX();
        double yOff = currentPos.getY() - corePos.getY();
        double zOff = currentPos.getZ() - corePos.getZ();
        Direction offsetDir = Direction.getFacingFromVector(target.x + xOff, target.y + yOff, target.z + zOff);
        currentPos.move(offsetDir);
        if (parent.canSetBlock(currentPos, false, false)) {
            parent.setBlock(currentPos, parent.getIntrusiveBlock());
            remainingAmount--;
        } else {
            currentPos.move(offsetDir.getOpposite());
            prepareFlowLava();
        }
    }

    private void prepareFlowLava() {
        if (remainingAmount > 0) {
            isExtrusionStage = true;
            visited = new LongArraySet();
            queue = new LinkedList<>();
            visited.add(currentPos.toLong());
            queue.add(currentPos.toImmutable());
        }
    }

    private void flowLava() {
        if (queue.size() <= 0) {
            isActive = false;
            return;
        }
        BlockPos pos = queue.poll();
        if (parent.canSetBlock(pos, true, true)) {
            parent.addLavaBlockIfExposed(pos, parent.getExtrusiveBlock());
            remainingAmount--;
            eruptions++;
        }

        for (Direction dir : Direction.values()) {
            BlockPos nextBlockPos = pos.offset(dir);
            if (!visited.contains(nextBlockPos.toLong())) {
                visited.add(nextBlockPos.toLong());
                if (parent.isReplaceable(world.getBlockState(nextBlockPos)) && parent.isInsideSphere(nextBlockPos) && isSurfacePlanetBlock(nextBlockPos)) {
                    queue.add(nextBlockPos);
                }
            }
        }
    }

    private boolean isSurfacePlanetBlock(BlockPos pos) {
        boolean hasAir = false;
        boolean hasBlock = false;
        for (BlockPos other : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            BlockState block = world.getBlockState(other);
            if (block.isAir() || block.isIn(UEBlocks.PLANET_WATER)) {
                hasAir = true;
            } else if (parent.isGeneratedBlock(block)) {
                hasBlock = true;
            }
            if (hasAir && hasBlock) return true;
        }
        return false;
    }

    public int getEruptions() {
        return eruptions;
    }


    public void read(CompoundNBT nbt) {
        isActive = nbt.getBoolean("active");
        if (isActive) {
            isExtrusionStage = nbt.getBoolean("extrusion");
            remainingAmount = nbt.getInt("remaining");
            eruptions = nbt.getInt("eruptions");
            long[] t = nbt.getLongArray("target");
            target = new Vector3d(Double.longBitsToDouble(t[0]),
                    Double.longBitsToDouble(t[1]),
                    Double.longBitsToDouble(t[2]));
            visited = new LongArraySet(nbt.getLongArray("visited"));
            ListNBT queueList = nbt.getList("queue", 10);
            queue = new LinkedList<>();
            queueList.forEach(inbt -> {
                queue.addLast(NBTUtil.readBlockPos((CompoundNBT) inbt));
            });
        }
    }

    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("active", isActive);
        if (isActive) {
            compound.putBoolean("extrusion", isExtrusionStage);
            compound.putInt("remaining", remainingAmount);
            compound.putInt("eruptions", eruptions);
            long[] targetArray = new long[]{
                    Double.doubleToLongBits(target.x), Double.doubleToLongBits(target.y), Double.doubleToLongBits(target.z)
            };
            compound.putLongArray("target", targetArray);
            compound.put("currentPos", NBTUtil.writeBlockPos(currentPos));
            compound.putLongArray("visited", new ArrayList<>(visited));
            ListNBT queueList = new ListNBT();
            queue.forEach(pos -> queueList.add(NBTUtil.writeBlockPos(pos)));
            compound.put("queue", queueList);
        }
        return compound;
    }
}

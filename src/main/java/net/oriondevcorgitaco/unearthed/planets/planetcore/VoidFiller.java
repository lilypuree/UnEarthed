package net.oriondevcorgitaco.unearthed.planets.planetcore;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.planets.block.PlanetLavaBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class VoidFiller {
    private MantleCoreTile parent;
    private World world;
    private BlockPos corePos;
    private int minShores;

    private boolean fillOceans = false;
    private boolean fillStone = false;
    private int oceansToFill = 0;
    private int filledOceans = 0;
    private PriorityQueue<DistancedBlockPos> airs;
    private PriorityQueue<DistancedBlockPos> oceans;

    private List<BlockPos> exposedBlocks;
    private List<BlockPos> shoreBlocks;

    private int count;
    private boolean initStone = false;
    private boolean initOcean = false;

    public VoidFiller(MantleCoreTile mantleCore) {
        this.parent = mantleCore;
        this.world = mantleCore.getWorld();
        this.corePos = mantleCore.getPos();
        minShores = (int) (mantleCore.getRadius() * mantleCore.getRadius());
    }

    public int getFilledAndReset() {
        int returnVal = filledOceans;
        this.filledOceans = 0;
        return returnVal;
    }

    public void init(){
        this.world = parent.getWorld();
        this.corePos = parent.getPos();
        if (initOcean){
            scanForVoids();
            initOcean = false;
            initStone = false;
        }else if (initStone){
            scanForAir();
            initStone = false;
        }
    }

    public boolean stoneTick(){
        if (fillStone) {
            parent.markDirty();
            if (!airs.isEmpty()) {
                DistancedBlockPos pos = airs.poll();
                if (world.isAirBlock(pos)) {
                    PlanetLavaBlock.setPlanetLavaBlock(world, pos, parent.getIntrusiveBlock());
                }
            } else {
                fillStone = false;
                return true;
            }
        }
        return false;
    }

    public boolean oceanTick(){
        if (fillOceans) {
            if (filledOceans < oceansToFill && count++ < oceansToFill * 2) {
                if (!airs.isEmpty()) {
                    DistancedBlockPos pos = airs.poll();
                    tryAddOceans(pos);
                } else {
                    if (shoreBlocks.size() < minShores) {
                        tryAddOceans(exposedBlocks.get(world.rand.nextInt(exposedBlocks.size())));
                    } else {
                        tryAddOceans(shoreBlocks.get(world.rand.nextInt(shoreBlocks.size())));
                    }
                }
            } else {
                fillOceans = false;
                oceansToFill = 0;
                return true;
            }
        }
        return false;
    }


    private void tryAddOceans(BlockPos pos) {
        if (parent.canSetBlock(pos, false, true)) {
            parent.setBlock(pos, UEBlocks.PLANET_WATER.getDefaultState());
            filledOceans++;
            oceans.add(new DistancedBlockPos(pos));
            exposedBlocks.remove(pos);
            shoreBlocks.remove(pos);
            addShoresAndExposed(pos);
        }
    }

    private void addShoresAndExposed(BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos newPos = pos.offset(direction);
            if (parent.isInsideSphere(newPos) && parent.isGeneratedBlock(world.getBlockState(newPos))) {
                shoreBlocks.add(newPos);
                exposedBlocks.add(newPos);
            }
        }
    }

    public void startFillingVoids() {
        count = 0;
        scanForAir();
        fillStone = true;
    }

    public void startFillingOceans(int amount) {
        count = 0;
        scanForVoids();
        oceansToFill += amount;
        fillOceans = true;
    }

    private void scanForAir() {
        airs = new PriorityQueue<>();
        parent.forEachPlanetBlock().forEach(planetPos -> {
            BlockState block = world.getBlockState(planetPos);
            if (block.isAir()) {
                airs.add(new DistancedBlockPos(planetPos));
            }
        });
    }

    private void scanForVoids() {
        airs = new PriorityQueue<>();
        oceans = new PriorityQueue<>();
        exposedBlocks = new ArrayList<>();
        shoreBlocks = new ArrayList<>();
        parent.forEachPlanetBlock().forEach(planetPos -> {
            BlockState block = world.getBlockState(planetPos);
            if (block.isAir()) {
                airs.add(new DistancedBlockPos(planetPos));
            } else if (block.isIn(UEBlocks.PLANET_WATER)) {
                oceans.add(new DistancedBlockPos(planetPos));
            } else if (parent.isGeneratedBlock(block)) {
                BlockPos.Mutable mutablePos = planetPos.toMutable();
                boolean isExposed = false;
                for (Direction dir : Direction.values()) {
                    mutablePos.setAndMove(planetPos, dir);
                    if (world.getBlockState(mutablePos).isIn(UEBlocks.PLANET_WATER)) {
                        shoreBlocks.add(mutablePos);
                        if (!isExposed) exposedBlocks.add(mutablePos);
                        break;
                    }
                    if (!isExposed && !world.getBlockState(mutablePos).isAir()) {
                        exposedBlocks.add(mutablePos);
                        isExposed = true;
                    }
                }
            }
        });
    }

    public List<BlockPos> getExposedBlocks() {
        return exposedBlocks;
    }

    public BlockPos removeBottomMostOcean() {
        return oceans.poll();
    }

    private class DistancedBlockPos extends BlockPos {
        float centerDistance;

        public DistancedBlockPos(int x, int y, int z) {
            super(x, y, z);
            this.centerDistance = (float) this.distanceSq(corePos);
        }

        public DistancedBlockPos(BlockPos pos) {
            super(pos);
            this.centerDistance = (float) this.distanceSq(corePos);
        }

        @Override
        public int compareTo(@NotNull Vector3i compareTo) {
            if (compareTo instanceof DistancedBlockPos) {
                return Float.compare(centerDistance, ((DistancedBlockPos) compareTo).centerDistance);
            } else {
                return super.compareTo(compareTo);
            }
        }
    }

    public void read(CompoundNBT nbt) {
        fillOceans = nbt.getBoolean("fillOceans");
        fillStone = nbt.getBoolean("fillStone");
        if (fillOceans) {
            initOcean = true;
            oceansToFill = nbt.getInt("oceansToFill");
            filledOceans = nbt.getInt("filledOceans");
        } else if (fillStone) {
            initStone = true;
        }
    }

    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putBoolean("fillOceans", fillOceans);
        nbt.putBoolean("fillStone", fillStone);
        if (fillOceans) {
            nbt.putInt("oceansToFill", oceansToFill);
            nbt.putInt("filledOceans", filledOceans);
        }
        return nbt;
    }
}

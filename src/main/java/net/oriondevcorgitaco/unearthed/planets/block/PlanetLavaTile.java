package net.oriondevcorgitaco.unearthed.planets.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEEntities;
import org.jetbrains.annotations.Nullable;

public class PlanetLavaTile extends TileEntity implements ITickableTileEntity {
    private int ticks;
    private boolean synced = false;
    private BlockState actualBlock;

    public PlanetLavaTile() {
        super(UEEntities.PLANET_LAVA);
        this.actualBlock = Blocks.STONE.getDefaultState();
        this.ticks = getMaxTicks();
    }

    public PlanetLavaTile(BlockState actualBlock) {
        this();
        setActualBlock(actualBlock);
    }

    public void setActualBlock(BlockState actualBlock) {
        this.actualBlock = actualBlock;
    }

    public BlockState getActualBlock() {
        return actualBlock;
    }

    @Override
    public void tick() {
        if (!world.isRemote()) {
            if (ticks-- < 0 && world != null) {
                world.setBlockState(pos, actualBlock);
            }
        } else {
            ticks--;
        }
    }

//    @Override
//    public void read(BlockState state, CompoundNBT nbt) {
//        super.read(state, nbt);
//        ticks = nbt.getInt("ticks");
//        actualBlock = NBTUtil.readBlockState(nbt.getCompound("block"));
//        synced = false;
//    }
//
//    @Override
//    public CompoundNBT write(CompoundNBT compound) {
//        compound = super.write(compound);
//        compound.putInt("ticks", ticks);
//        compound.put("block", NBTUtil.writeBlockState(actualBlock));
//        return compound;
//    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("block", NBTUtil.writeBlockState(actualBlock));
        return new SUpdateTileEntityPacket(getPos(), -1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbt = pkt.getNbtCompound();
        actualBlock = NBTUtil.readBlockState(nbt.getCompound("block"));
    }

    public static int getMaxTicks() {
        return 15;
    }

    public int getTicks() {
        return ticks;
    }
}

package net.oriondevcorgitaco.unearthed.planets.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import org.jetbrains.annotations.Nullable;

public class PlanetLavaBlock extends Block {

    public PlanetLavaBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PlanetLavaTile();
    }

    public static void setPlanetLavaBlock(World world, BlockPos pos, BlockState actualBlock) {
        BlockState lavaBlock = UEBlocks.PLANET_LAVA.getDefaultState();
        world.setBlockState(pos, lavaBlock, 16);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof PlanetLavaTile) {
            ((PlanetLavaTile) tileEntity).setActualBlock(actualBlock);
        }
        world.notifyBlockUpdate(pos, lavaBlock, lavaBlock, 3);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }
}

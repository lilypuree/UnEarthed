package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class SixwaySlabBlock extends Block implements IWaterLoggable {
    public static final BooleanProperty DOUBLE = ModBlockProperties.DOUBLE;
    public static final DirectionProperty FACE = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape EAST_SHAPE = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);

    public SixwaySlabBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(super.getDefaultState().with(FACE, Direction.DOWN).with(WATERLOGGED, false).with(DOUBLE, false));
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACE, WATERLOGGED, DOUBLE);
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(FACE);
        if (state.get(DOUBLE)) {
            return VoxelShapes.fullCube();
        }
        switch (direction) {
            case UP:
                return TOP_SHAPE;
            default:
            case DOWN:
                return BOTTOM_SHAPE;
            case NORTH:
                return NORTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
        }
    }

    //partly copied from slabblock
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState ifluidstate = context.getWorld().getFluidState(blockpos);
        BlockState currentState = context.getWorld().getBlockState(blockpos);
        Direction direction = context.getFace().getOpposite();
        if (currentState.isIn(this)) {
            return currentState.with(WATERLOGGED, false).with(DOUBLE, true);
        }
        BlockState baseState = super.getStateForPlacement(context).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            if (direction.getAxis() != Direction.Axis.Y) {
                return baseState.with(FACE, (context.getHitVec().y - (double) blockpos.getY() > 0.5D) ? Direction.UP : Direction.DOWN);
            } else {
                Direction lookDir = context.getPlacementHorizontalFacing();
                if (lookDir.getAxis() == Direction.Axis.X) {
                    return baseState.with(FACE, (context.getHitVec().x - (double) blockpos.getX() > 0.5D) ? Direction.EAST : Direction.WEST);
                } else if (lookDir.getAxis() == Direction.Axis.Z) {
                    return baseState.with(FACE, (context.getHitVec().z - (double) blockpos.getZ() > 0.5D) ? Direction.SOUTH : Direction.NORTH);
                }
            }
        }
        return baseState.with(FACE, direction);
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        ItemStack itemstack = useContext.getItem();
        if (!state.get(DOUBLE) && itemstack.getItem() == this.asItem()) {
            if (useContext.replacingClickedOnBlock()) {
                Direction direction = useContext.getFace();
                if (state.get(FACE) == direction.getOpposite()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return !state.get(DOUBLE) && IWaterLoggable.super.receiveFluid(worldIn, pos, state, fluidStateIn);
    }

    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !state.get(DOUBLE) && IWaterLoggable.super.canContainFluid(worldIn, pos, state, fluidIn);
    }


    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        switch (type) {
            case LAND:
                return false;
            case WATER:
                return worldIn.getFluidState(pos).isTagged(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }
}

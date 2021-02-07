package net.lilycorgitaco.unearthed.block;

import net.minecraft.block.*;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class SixwaySlabBlock extends Block implements Waterloggable {
    public static final BooleanProperty DOUBLE = ModBlockProperties.DOUBLE;
    public static final DirectionProperty FACE = Properties.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    protected static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);

    public SixwaySlabBlock(AbstractBlock.Settings properties) {
        super(properties);
        this.setDefaultState(super.getDefaultState().with(FACE, Direction.DOWN).with(WATERLOGGED, false).with(DOUBLE, false));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACE, WATERLOGGED, DOUBLE);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
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
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockPos blockpos = context.getBlockPos();
        FluidState ifluidstate = context.getWorld().getFluidState(blockpos);
        BlockState currentState = context.getWorld().getBlockState(blockpos);
        Direction direction = context.getSide().getOpposite();
        if (currentState.isOf(this)) {
            return currentState.with(WATERLOGGED, false).with(DOUBLE, true);
        }
        BlockState baseState = super.getPlacementState(context).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            if (direction.getAxis() != Direction.Axis.Y) {
                return baseState.with(FACE, (context.getHitPos().y - (double) blockpos.getY() > 0.5D) ? Direction.UP : Direction.DOWN);
            } else {
                Direction lookDir = context.getPlayerFacing();
                if (lookDir.getAxis() == Direction.Axis.X) {
                    return baseState.with(FACE, (context.getHitPos().x - (double) blockpos.getX() > 0.5D) ? Direction.EAST : Direction.WEST);
                } else if (lookDir.getAxis() == Direction.Axis.Z) {
                    return baseState.with(FACE, (context.getHitPos().z - (double) blockpos.getZ() > 0.5D) ? Direction.SOUTH : Direction.NORTH);
                }
            }
        }
        return baseState.with(FACE, direction);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext useContext) {
        ItemStack itemstack = useContext.getStack();
        if (!state.get(DOUBLE) && itemstack.getItem() == this.asItem()) {
            if (useContext.canReplaceExisting()) {
                Direction direction = useContext.getSide();
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
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public boolean tryFillWithFluid(WorldAccess worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return !state.get(DOUBLE) && Waterloggable.super.tryFillWithFluid(worldIn, pos, state, fluidStateIn);
    }

    public boolean canFillWithFluid(BlockView worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !state.get(DOUBLE) && Waterloggable.super.canFillWithFluid(worldIn, pos, state, fluidIn);
    }


    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getFluidTickScheduler().schedule(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.getStateForNeighborUpdate(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean canPathfindThrough(BlockState state, BlockView worldIn, BlockPos pos, NavigationType type) {
        switch (type) {
            case LAND:
                return false;
            case WATER:
                return worldIn.getFluidState(pos).isIn(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }
}

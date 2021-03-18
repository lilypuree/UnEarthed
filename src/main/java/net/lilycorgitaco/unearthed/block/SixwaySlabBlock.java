package net.lilycorgitaco.unearthed.block;

import net.lilycorgitaco.unearthed.block.properties.ModBlockProperties;
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

    public static final DirectionProperty FACE = Properties.FACING;
    public static final DirectionProperty SECONDARY_FACING = ModBlockProperties.SECONDARY_FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    //Down, Up, North, South, West, East (order of Direction enum)
    protected static final VoxelShape[] directionShapes = new VoxelShape[]{
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.createCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D),
            Block.createCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D),
            Block.createCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D),
            Block.createCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };
    protected static VoxelShape[][] shapeArray;

    public SixwaySlabBlock(AbstractBlock.Settings properties) {
        super(properties);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FACE, Direction.DOWN).with(WATERLOGGED, false).with(SECONDARY_FACING, Direction.DOWN));
        shapeArray = getShapeArray();
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACE, WATERLOGGED, SECONDARY_FACING);
    }

    protected static VoxelShape[][] getShapeArray() {
        VoxelShape[][] shapes = new VoxelShape[6][6];
        for (Direction direction : Direction.values()) {
            for (Direction otherDir : Direction.values()) {
                int directionIndex = direction.ordinal();
                int directionIndex2 = otherDir.ordinal();
                shapes[directionIndex][directionIndex2] = VoxelShapes.union(directionShapes[directionIndex], directionShapes[directionIndex2]);
            }
        }
        return shapes;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return shapeArray[state.get(FACE).ordinal()][state.get(SECONDARY_FACING).ordinal()];
    }

    //partly copied from slabblock
    public BlockState getPlacementState(ItemPlacementContext context) {
//        BlockPos blockpos = context.getBlockPos();
//        FluidState ifluidstate = context.getWorld().getFluidState(blockpos);
//        BlockState currentState = context.getWorld().getBlockState(blockpos);
//        Direction direction = context.getSide().getOpposite();
//        if (currentState.isOf(this)) {
//            return currentState.with(WATERLOGGED, false).with(DOUBLE, true);
//        }
//        BlockState baseState = super.getPlacementState(context).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
//        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
//            if (direction.getAxis() != Direction.Axis.Y) {
//                return baseState.with(FACE, (context.getHitPos().y - (double) blockpos.getY() > 0.5D) ? Direction.UP : Direction.DOWN);
//            } else {
//                Direction lookDir = context.getPlayerFacing();
//                if (lookDir.getAxis() == Direction.Axis.X) {
//                    return baseState.with(FACE, (context.getHitPos().x - (double) blockpos.getX() > 0.5D) ? Direction.EAST : Direction.WEST);
//                } else if (lookDir.getAxis() == Direction.Axis.Z) {
//                    return baseState.with(FACE, (context.getHitPos().z - (double) blockpos.getZ() > 0.5D) ? Direction.SOUTH : Direction.NORTH);
//                }
//            }
//        }
//        return baseState.with(FACE, direction);


        BlockPos blockpos = context.getBlockPos();
        FluidState ifluidstate = context.getWorld().getFluidState(blockpos);
        BlockState currentState = context.getWorld().getBlockState(blockpos);
        Direction placeDir = getPlaceDir(context);
        boolean waterLoggedFlag;

        if (currentState.isOf(this)) {
            waterLoggedFlag = currentState.get(WATERLOGGED) && placeDir != currentState.get(FACE);
            return currentState.with(WATERLOGGED, waterLoggedFlag).with(FACE, currentState.get(FACE)).with(SECONDARY_FACING, placeDir);
        } else {
            waterLoggedFlag = ifluidstate.getFluid() == Fluids.WATER;
            return getDefaultState().with(WATERLOGGED, waterLoggedFlag).with(FACE, placeDir).with(SECONDARY_FACING, placeDir);
        }
    }

    public boolean hasTwoSides(BlockState state) {
        return state.get(FACE) != state.get(SECONDARY_FACING);
    }

    public boolean isFullBlock(BlockState state) {
        return state.get(FACE) == state.get(SECONDARY_FACING).getOpposite();
    }


    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext useContext) {
        ItemStack itemstack = useContext.getStack();
        if (!hasTwoSides(state) && itemstack.getItem() == this.asItem()) {
            if (useContext.canReplaceExisting()) {
                if (useContext.getSide() == state.get(FACE).getOpposite()) {
                    return true;
                }

                return false;
            } else {
                Direction placeDir = getPlaceDir(useContext);
                if (state.get(SECONDARY_FACING) == placeDir) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    protected Direction getPlaceDir(ItemPlacementContext context) {
        BlockPos blockpos = context.getBlockPos();
        BlockState currentState = context.getWorld().getBlockState(blockpos);
        Direction placeDir = context.getSide().getOpposite();
        if (currentState.isOf(this)) {
            if (placeDir == currentState.get(FACE)) {
                placeDir = placeDir.getOpposite();
            }
        }

        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            if (placeDir.getAxis() != Direction.Axis.Y) {
                placeDir = (context.getHitPos().y - (double) blockpos.getY() > 0.5D) ? Direction.UP : Direction.DOWN;
            } else {
                Direction lookDir = context.getPlayerFacing();
                if (lookDir.getAxis() == Direction.Axis.X) {
                    placeDir = (context.getHitPos().x - (double) blockpos.getX() > 0.5D) ? Direction.EAST : Direction.WEST;
                } else if (lookDir.getAxis() == Direction.Axis.Z) {
                    placeDir = (context.getHitPos().z - (double) blockpos.getZ() > 0.5D) ? Direction.SOUTH : Direction.NORTH;
                }
            }
        }
        return placeDir;
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    public boolean tryFillWithFluid(WorldAccess worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return !isFullBlock(state) && Waterloggable.super.tryFillWithFluid(worldIn, pos, state, fluidStateIn);
    }

    public boolean canFillWithFluid(BlockView worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !isFullBlock(state) && Waterloggable.super.canFillWithFluid(worldIn, pos, state, fluidIn);
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

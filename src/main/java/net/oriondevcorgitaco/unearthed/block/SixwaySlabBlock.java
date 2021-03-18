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
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.oriondevcorgitaco.unearthed.block.properties.ModBlockProperties;

public class SixwaySlabBlock extends Block implements IWaterLoggable {

    public static final DirectionProperty FACE = BlockStateProperties.FACING;
    public static final DirectionProperty SECONDARY_FACING = ModBlockProperties.SECONDARY_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    //Down, Up, North, South, West, East (order of Direction enum)
    protected static final VoxelShape[] directionShapes = new VoxelShape[]{
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D),
            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D),
            Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };
    protected static VoxelShape[][] shapeArray;

    public SixwaySlabBlock(Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACE, Direction.DOWN).with(WATERLOGGED, false).with(SECONDARY_FACING, Direction.DOWN));
        shapeArray = getShapeArray();
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FACE, WATERLOGGED, SECONDARY_FACING);
    }

    protected static VoxelShape[][] getShapeArray() {
        VoxelShape[][] shapes = new VoxelShape[6][6];
        for (Direction direction : Direction.values()) {
            for (Direction otherDir : Direction.values()) {
                int directionIndex = direction.ordinal();
                int directionIndex2 = otherDir.ordinal();
                shapes[directionIndex][directionIndex2] = VoxelShapes.or(directionShapes[directionIndex], directionShapes[directionIndex2]);
            }
        }
        return shapes;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shapeArray[state.get(FACE).ordinal()][state.get(SECONDARY_FACING).ordinal()];
    }

    //partly copied from slabblock
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        FluidState ifluidstate = context.getWorld().getFluidState(blockpos);
        BlockState currentState = context.getWorld().getBlockState(blockpos);
        Direction placeDir = getPlaceDir(context);
        boolean waterLoggedFlag;

        if (currentState.isIn(this)) {
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
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        ItemStack itemstack = useContext.getItem();
        if (!hasTwoSides(state) && itemstack.getItem() == this.asItem()) {
            if (useContext.replacingClickedOnBlock()) {
                if (useContext.getFace() == state.get(FACE).getOpposite()) {
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

    protected Direction getPlaceDir(BlockItemUseContext context) {
        BlockPos blockpos = context.getPos();
        BlockState currentState = context.getWorld().getBlockState(blockpos);
        Direction placeDir = context.getFace().getOpposite();
        if (currentState.isIn(this)) {
            if (placeDir == currentState.get(FACE)) {
                placeDir = placeDir.getOpposite();
            }
        }

        if (context.getPlayer() != null && context.getPlayer().isSneaking()) {
            if (placeDir.getAxis() != Direction.Axis.Y) {
                placeDir = (context.getHitVec().y - (double) blockpos.getY() > 0.5D) ? Direction.UP : Direction.DOWN;
            } else {
                Direction lookDir = context.getPlacementHorizontalFacing();
                if (lookDir.getAxis() == Direction.Axis.X) {
                    placeDir = (context.getHitVec().x - (double) blockpos.getX() > 0.5D) ? Direction.EAST : Direction.WEST;
                } else if (lookDir.getAxis() == Direction.Axis.Z) {
                    placeDir = (context.getHitVec().z - (double) blockpos.getZ() > 0.5D) ? Direction.SOUTH : Direction.NORTH;
                }
            }
        }
        return placeDir;
    }

    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return !isFullBlock(state) && IWaterLoggable.super.receiveFluid(worldIn, pos, state, fluidStateIn);
    }

    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !isFullBlock(state) && IWaterLoggable.super.canContainFluid(worldIn, pos, state, fluidIn);
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

package lilypuree.unearthed.block;

import lilypuree.unearthed.block.properties.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SixwaySlabBlock extends Block implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACE = BlockStateProperties.FACING;
    public static final DirectionProperty SECONDARY_FACING = ModBlockProperties.SECONDARY_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    //Down, Up, North, South, West, East (order of Direction enum)
    protected static final VoxelShape[] directionShapes = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D),
            Block.box(0.0D, 0.0D, 8.0D, 16.0D, 16.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D),
            Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    };
    protected static VoxelShape[][] shapeArray;

    public SixwaySlabBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACE, Direction.DOWN).setValue(WATERLOGGED, false).setValue(SECONDARY_FACING, Direction.DOWN));
        shapeArray = getShapeArray();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACE, WATERLOGGED, SECONDARY_FACING);
    }

    protected static VoxelShape[][] getShapeArray() {
        VoxelShape[][] shapes = new VoxelShape[6][6];
        for (Direction direction : Direction.values()) {
            for (Direction otherDir : Direction.values()) {
                int directionIndex = direction.ordinal();
                int directionIndex2 = otherDir.ordinal();
                shapes[directionIndex][directionIndex2] = Shapes.or(directionShapes[directionIndex], directionShapes[directionIndex2]);
            }
        }
        return shapes;
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return shapeArray[state.getValue(FACE).ordinal()][state.getValue(SECONDARY_FACING).ordinal()];
    }

    //partly copied from slabblock
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        FluidState ifluidstate = context.getLevel().getFluidState(blockpos);
        BlockState currentState = context.getLevel().getBlockState(blockpos);
        Direction placeDir = getPlaceDir(context);
        boolean waterLoggedFlag;

        if (currentState.is(this)) {
            waterLoggedFlag = currentState.getValue(WATERLOGGED) && placeDir != currentState.getValue(FACE);
            return currentState.setValue(WATERLOGGED, waterLoggedFlag).setValue(FACE, currentState.getValue(FACE)).setValue(SECONDARY_FACING, placeDir);
        } else {
            waterLoggedFlag = ifluidstate.getType() == Fluids.WATER;
            return defaultBlockState().setValue(WATERLOGGED, waterLoggedFlag).setValue(FACE, placeDir).setValue(SECONDARY_FACING, placeDir);
        }
    }

    public boolean hasTwoSides(BlockState state) {
        return state.getValue(FACE) != state.getValue(SECONDARY_FACING);
    }

    public boolean isFullBlock(BlockState state) {
        return state.getValue(FACE) == state.getValue(SECONDARY_FACING).getOpposite();
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        ItemStack itemstack = useContext.getItemInHand();
        if (!hasTwoSides(state) && itemstack.getItem() == this.asItem()) {
            if (useContext.replacingClickedOnBlock()) {
                if (useContext.getClickedFace() == state.getValue(FACE).getOpposite()) {
                    return true;
                }

                return false;
            } else {
                Direction placeDir = getPlaceDir(useContext);
                if (state.getValue(SECONDARY_FACING) == placeDir) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    protected Direction getPlaceDir(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        BlockState currentState = context.getLevel().getBlockState(blockpos);
        Direction placeDir = context.getClickedFace().getOpposite();
        if (currentState.is(this)) {
            if (placeDir == currentState.getValue(FACE)) {
                placeDir = placeDir.getOpposite();
            }
        }

        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            if (placeDir.getAxis() != Direction.Axis.Y) {
                placeDir = (context.getClickLocation().y - (double) blockpos.getY() > 0.5D) ? Direction.UP : Direction.DOWN;
            } else {
                Direction lookDir = context.getHorizontalDirection();
                if (lookDir.getAxis() == Direction.Axis.X) {
                    placeDir = (context.getClickLocation().x - (double) blockpos.getX() > 0.5D) ? Direction.EAST : Direction.WEST;
                } else if (lookDir.getAxis() == Direction.Axis.Z) {
                    placeDir = (context.getClickLocation().z - (double) blockpos.getZ() > 0.5D) ? Direction.SOUTH : Direction.NORTH;
                }
            }
        }
        return placeDir;
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return !isFullBlock(state) && SimpleWaterloggedBlock.super.placeLiquid(worldIn, pos, state, fluidStateIn);
    }

    public boolean canPlaceLiquid(BlockGetter worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return !isFullBlock(state) && SimpleWaterloggedBlock.super.canPlaceLiquid(worldIn, pos, state, fluidIn);
    }


    /**
     * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
     * returns its solidified counterpart.
     * Note that this method should ideally consider only the specific face passed in.
     */
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        switch (type) {
            case LAND:
                return false;
            case WATER:
                return worldIn.getFluidState(pos).is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }
}

package net.oriondevcorgitaco.unearthed.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VanillaLichenParentBlock extends Block {

    private static final VoxelShape UP = Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape EAST = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST = Block.makeCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SOUTH = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape NORTH = Block.makeCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP;
    private static final Map<Direction, VoxelShape> directionToShapeMap;
    //field_28421
    private static final Direction[] directions;
    private final ImmutableMap<BlockState, VoxelShape> stateToShapeMap;
    private final boolean field_28423;
    private final boolean field_28424;
    private final boolean field_28425;

    public VanillaLichenParentBlock(Properties settings) {
        super(settings);
        this.setDefaultState(getDefaultState(this.getStateContainer()));
        this.stateToShapeMap = getStateToShapeMap(this.getStateContainer());
        this.field_28423 = Direction.Plane.HORIZONTAL.getDirectionValues().allMatch(this::hasFace);
        this.field_28424 = Direction.Plane.HORIZONTAL.getDirectionValues().filter(Direction.Axis.X).filter(this::hasFace).count() % 2L == 0L;
        this.field_28425 = Direction.Plane.HORIZONTAL.getDirectionValues().filter(Direction.Axis.Z).filter(this::hasFace).count() % 2L == 0L;
    }

    protected boolean hasFace(Direction direction) {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
           for (Direction direction : directions) {
            if (this.hasFace(direction)) {
                builder.add(getPropertyFor(direction));
            }
        }
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return isFaceFilled(stateIn, facing) && !canAttachTo(worldIn, facing, facingPos, facingState) ? removeProperty(stateIn, getPropertyFor(facing)) : stateIn;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return (VoxelShape) this.stateToShapeMap.get(state);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        boolean bl = false;
        for (Direction direction : directions) {
            if (isFaceFilled(state, direction)) {
                BlockPos blockPos = pos.offset(direction);
                if (!canAttachTo(worldIn, direction, blockPos, worldIn.getBlockState(blockPos))) {
                    return false;
                }
                bl = true;
            }
        }
        return bl;
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        return isNotFull(state);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getPos();
        BlockState blockState = world.getBlockState(blockPos);
        return Arrays.stream(context.getNearestLookingDirections()).map((direction) -> {
            return this.tryPlaceOnto(blockState, world, blockPos, direction);
        }).filter(Objects::nonNull).findFirst().orElse(null);
    }

    //method_33362
    @Nullable
    public BlockState tryPlaceOnto(BlockState blockState, IWorld world, BlockPos blockPos, Direction direction) {
        if (!this.hasFace(direction)) {
            return null;
        } else {
            BlockState newState;
            if (blockState.isIn(this)) {
                if (isFaceFilled(blockState, direction)) {
                    return null;
                }
                newState = blockState;
            } else if (this.canBeWaterlogged() && blockState.getFluidState().isSource()) {
                newState = this.getDefaultState().with(BlockStateProperties.WATERLOGGED, true);
            } else {
                newState = this.getDefaultState();
            }
            BlockPos newPos = blockPos.offset(direction);
            return canAttachTo(world, direction, newPos, world.getBlockState(newPos)) ? newState.with(getPropertyFor(direction), true) : null;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (!this.field_28423) {
            return state;
        } else {
            rot.getClass();
            return this.applyTransformation(state, rot::rotate);
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        if (mirrorIn == Mirror.FRONT_BACK && !this.field_28424) {
            return state;
        } else if (mirrorIn == Mirror.LEFT_RIGHT && !this.field_28425) {
            return state;
        } else {
            mirrorIn.getClass();
            return this.applyTransformation(state, mirrorIn::mirror);
        }
    }

    //method_33367
    private BlockState applyTransformation(BlockState blockState, Function<Direction, Direction> function) {
        BlockState newState = blockState;
        for (Direction direction : directions) {
            if (this.hasFace(direction)) {
                newState = newState.with(getPropertyFor(function.apply(direction)), blockState.get(getPropertyFor(direction)));
            }
        }
        return newState;
    }

    //method_33375
    public boolean tryGrowth(BlockState blockState, ServerWorld serverWorld, BlockPos blockPos, Random random) {
        List<Direction> list = Lists.newArrayList(directions);
        Collections.shuffle(list);
        return list.stream().filter((dir) -> isFaceFilled(blockState, dir))
                .anyMatch((dir) -> this.tryGrowFrom(blockState, serverWorld, blockPos, dir, random));
    }


    //method_33364
    public boolean tryGrowFrom(BlockState blockState, IWorld worldAccess, BlockPos blockPos, Direction direction, Random random) {
        List<Direction> list = Arrays.asList(directions);
        Collections.shuffle(list, random);
        return list.stream().anyMatch((dir) -> this.tryGrowOn(blockState, worldAccess, blockPos, direction, dir));
    }

    //method_33363
    public boolean tryGrowOn(BlockState blockState, IWorld world, BlockPos blockPos, Direction direction, Direction newDirection) {
        if (newDirection.getAxis() != direction.getAxis() && isFaceFilled(blockState, direction) && !isFaceFilled(blockState, newDirection)) {
            if (this.tryGrowInto(world, blockPos, newDirection)) {
                return true;
            } else {
                return this.tryGrowInto(world, blockPos.offset(newDirection), direction) ? true
                        : this.tryGrowInto(world, blockPos.offset(newDirection).offset(direction), newDirection.getOpposite());
            }
        } else {
            return false;
        }
    }

    //method_33359
    private boolean tryGrowInto(IWorld worldAccess, BlockPos blockPos, Direction direction) {
        BlockState blockState = worldAccess.getBlockState(blockPos);
        if (!this.canGrowInto(blockState)) {
            return false;
        } else {
            BlockState blockState2 = this.tryPlaceOnto(blockState, worldAccess, blockPos, direction);
            return blockState2 != null ? worldAccess.setBlockState(blockPos, blockState2, 2) : false;
        }
    }

    //
    private boolean canGrowInto(BlockState blockState) {
        return blockState.isAir() || blockState.isIn(this) || blockState.isIn(Blocks.WATER) && blockState.getFluidState().isSource();
    }

    //method_33366
    private static boolean isFaceFilled(BlockState blockState, Direction direction) {
        BooleanProperty directionProperty = getPropertyFor(direction);
        return blockState.hasProperty(directionProperty) && blockState.get(directionProperty);
    }

    //method_33358
    private static boolean canAttachTo(IBlockReader blockReader, Direction direction, BlockPos blockPos, BlockState blockState) {
        return Block.doesSideFillSquare(blockState.getCollisionShape(blockReader, blockPos), direction.getOpposite());
    }

    //method_33378
    private boolean canBeWaterlogged() {
        return this.stateContainer.getProperties().contains(BlockStateProperties.WATERLOGGED);
    }

    //method_33365
    private static BlockState removeProperty(BlockState blockState, BooleanProperty booleanProperty) {
        BlockState blockState2 = blockState.with(booleanProperty, false);
        if (isNotEmpty(blockState2)) {
            return blockState2;
        } else {
            return blockState.hasProperty(BlockStateProperties.WATERLOGGED)
                    && (Boolean) blockState.get(BlockStateProperties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
        }
    }

    //method_33374
    public static BooleanProperty getPropertyFor(Direction direction) {
        return FACING_TO_PROPERTY_MAP.get(direction);
    }

    private static BlockState getDefaultState(StateContainer<Block, BlockState> stateContainer) {
        BlockState blockState = stateContainer.getBaseState();
        for (BooleanProperty booleanProperty : FACING_TO_PROPERTY_MAP.values()) {
            if (blockState.hasProperty(booleanProperty)) {
                blockState = blockState.with(booleanProperty, false);
            }
        }
        return blockState;
    }

    //method_33373
    private static ImmutableMap<BlockState, VoxelShape> getStateToShapeMap(StateContainer<Block, BlockState> stateManager) {
        Map<BlockState, VoxelShape> map = stateManager.getValidStates().stream()
                .collect(Collectors.toMap(Function.identity(), VanillaLichenParentBlock::getShapeForState));
        return ImmutableMap.copyOf(map);
    }

    private static VoxelShape getShapeForState(BlockState blockState) {
        VoxelShape voxelShape = VoxelShapes.empty();
        for (Direction direction : directions) {
            if (isFaceFilled(blockState, direction)) {
                voxelShape = VoxelShapes.or(voxelShape, directionToShapeMap.get(direction));
            }
        }
        return voxelShape;
    }

    //method_33381
    private static boolean isNotEmpty(BlockState blockState) {
        return Arrays.stream(directions).anyMatch((direction) -> {
            return isFaceFilled(blockState, direction);
        });
    }

    //method_33382
    private static boolean isNotFull(BlockState blockState) {
        return Arrays.stream(directions).anyMatch((direction) -> {
            return !isFaceFilled(blockState, direction);
        });
    }

    static {
        FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP;
        directionToShapeMap = Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
            enumMap.put(Direction.NORTH, SOUTH);
            enumMap.put(Direction.EAST, WEST);
            enumMap.put(Direction.SOUTH, NORTH);
            enumMap.put(Direction.WEST, EAST);
            enumMap.put(Direction.UP, UP);
            enumMap.put(Direction.DOWN, DOWN);
        });
        directions = Direction.values();
    }
}

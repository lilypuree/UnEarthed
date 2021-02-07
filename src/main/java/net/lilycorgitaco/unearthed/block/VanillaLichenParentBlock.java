package net.lilycorgitaco.unearthed.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VanillaLichenParentBlock extends Block {

    private static final VoxelShape UP = Block.createCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape EAST = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST = Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SOUTH = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape NORTH = Block.createCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP;
    private static final Map<Direction, VoxelShape> directionToShapeMap;
    //field_28421
    private static final Direction[] directions;
    private final ImmutableMap<BlockState, VoxelShape> stateToShapeMap;
    private final boolean field_28423;
    private final boolean field_28424;
    private final boolean field_28425;

    public VanillaLichenParentBlock(Settings settings) {
        super(settings);
        this.setDefaultState(getDefaultState(this.getStateManager()));
        this.stateToShapeMap = getStateToShapeMap(this.getStateManager());
        this.field_28423 = Direction.Type.HORIZONTAL.stream().allMatch(this::hasFace);
        this.field_28424 = Direction.Type.HORIZONTAL.stream().filter(Direction.Axis.X).filter(this::hasFace).count() % 2L == 0L;
        this.field_28425 = Direction.Type.HORIZONTAL.stream().filter(Direction.Axis.Z).filter(this::hasFace).count() % 2L == 0L;
    }

    protected boolean hasFace(Direction direction) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
           for (Direction direction : directions) {
            if (this.hasFace(direction)) {
                builder.add(getPropertyFor(direction));
            }
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        return isFaceFilled(stateIn, facing) && !canAttachTo(worldIn, facing, facingPos, facingState) ? removeProperty(stateIn, getPropertyFor(facing)) : stateIn;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return (VoxelShape) this.stateToShapeMap.get(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
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
    public boolean canReplace(BlockState state, ItemPlacementContext useContext) {
        return isNotFull(state);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        return Arrays.stream(context.getPlacementDirections()).map((direction) -> {
            return this.tryPlaceOnto(blockState, world, blockPos, direction);
        }).filter(Objects::nonNull).findFirst().orElse(null);
    }

    //method_33362
    @Nullable
    public BlockState tryPlaceOnto(BlockState blockState, WorldAccess world, BlockPos blockPos, Direction direction) {
        if (!this.hasFace(direction)) {
            return null;
        } else {
            BlockState newState;
            if (blockState.isOf(this)) {
                if (isFaceFilled(blockState, direction)) {
                    return null;
                }
                newState = blockState;
            } else if (this.canBeWaterlogged() && blockState.getFluidState().isStill()) {
                newState = this.getDefaultState().with(Properties.WATERLOGGED, true);
            } else {
                newState = this.getDefaultState();
            }
            BlockPos newPos = blockPos.offset(direction);
            return canAttachTo(world, direction, newPos, world.getBlockState(newPos)) ? newState.with(getPropertyFor(direction), true) : null;
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rot) {
        if (!this.field_28423) {
            return state;
        } else {
            rot.getClass();
            return this.applyTransformation(state, rot::rotate);
        }
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirrorIn) {
        if (mirrorIn == BlockMirror.FRONT_BACK && !this.field_28424) {
            return state;
        } else if (mirrorIn == BlockMirror.LEFT_RIGHT && !this.field_28425) {
            return state;
        } else {
            mirrorIn.getClass();
            return this.applyTransformation(state, mirrorIn::apply);
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
    public boolean tryGrowFrom(BlockState blockState, WorldAccess worldAccess, BlockPos blockPos, Direction direction, Random random) {
        List<Direction> list = Arrays.asList(directions);
        Collections.shuffle(list, random);
        return list.stream().anyMatch((dir) -> this.tryGrowOn(blockState, worldAccess, blockPos, direction, dir));
    }

    //method_33363
    public boolean tryGrowOn(BlockState blockState, WorldAccess world, BlockPos blockPos, Direction direction, Direction newDirection) {
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
    private boolean tryGrowInto(WorldAccess worldAccess, BlockPos blockPos, Direction direction) {
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
        return blockState.isAir() || blockState.isOf(this) || blockState.isOf(Blocks.WATER) && blockState.getFluidState().isStill();
    }

    //method_33366
    private static boolean isFaceFilled(BlockState blockState, Direction direction) {
        BooleanProperty directionProperty = getPropertyFor(direction);
        return blockState.contains(directionProperty) && blockState.get(directionProperty);
    }

    //method_33358
    private static boolean canAttachTo(BlockView blockReader, Direction direction, BlockPos blockPos, BlockState blockState) {
        return Block.isFaceFullSquare(blockState.getCollisionShape(blockReader, blockPos), direction.getOpposite());
    }

    //method_33378
    private boolean canBeWaterlogged() {
        return this.stateManager.getProperties().contains(Properties.WATERLOGGED);
    }

    //method_33365
    private static BlockState removeProperty(BlockState blockState, BooleanProperty booleanProperty) {
        BlockState blockState2 = blockState.with(booleanProperty, false);
        if (isNotEmpty(blockState2)) {
            return blockState2;
        } else {
            return blockState.contains(Properties.WATERLOGGED)
                    && (Boolean) blockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
        }
    }

    //method_33374
    public static BooleanProperty getPropertyFor(Direction direction) {
        return FACING_TO_PROPERTY_MAP.get(direction);
    }

    private static BlockState getDefaultState(StateManager<Block, BlockState> stateContainer) {
        BlockState blockState = stateContainer.getDefaultState();
        for (BooleanProperty booleanProperty : FACING_TO_PROPERTY_MAP.values()) {
            if (blockState.contains(booleanProperty)) {
                blockState = blockState.with(booleanProperty, false);
            }
        }
        return blockState;
    }

    //method_33373
    private static ImmutableMap<BlockState, VoxelShape> getStateToShapeMap(StateManager<Block, BlockState> stateManager) {
        Map<BlockState, VoxelShape> map = stateManager.getStates().stream()
                .collect(Collectors.toMap(Function.identity(), VanillaLichenParentBlock::getShapeForState));
        return ImmutableMap.copyOf(map);
    }

    private static VoxelShape getShapeForState(BlockState blockState) {
        VoxelShape voxelShape = VoxelShapes.empty();
        for (Direction direction : directions) {
            if (isFaceFilled(blockState, direction)) {
                voxelShape = VoxelShapes.union(voxelShape, directionToShapeMap.get(direction));
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
        FACING_TO_PROPERTY_MAP = ConnectingBlock.FACING_PROPERTIES;
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

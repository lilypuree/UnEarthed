package net.lilycorgitaco.unearthed.block;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.lilycorgitaco.unearthed.core.UEBlocks;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LichenBlockOld extends Block {
    public static BooleanProperty WET = ModBlockProperties.WET;
    public static final BooleanProperty UP = ConnectingBlock.UP;
    public static final BooleanProperty DOWN = ConnectingBlock.DOWN;
    public static final BooleanProperty NORTH = ConnectingBlock.NORTH;
    public static final BooleanProperty EAST = ConnectingBlock.EAST;
    public static final BooleanProperty SOUTH = ConnectingBlock.SOUTH;
    public static final BooleanProperty WEST = ConnectingBlock.WEST;
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = ConnectingBlock.FACING_PROPERTIES;
    private static final VoxelShape UP_AABB = Block.createCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape NORTH_AABB = Block.createCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> stateToShapeMap;

    private static Map<Block, Block> lichenErosionMap = new Object2ObjectOpenHashMap<>();

    public LichenBlockOld(Settings properties) {
        super(properties);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(UP, false).with(DOWN, false).with(WEST, false).with(EAST, false).with(NORTH, false).with(SOUTH, false));
        this.stateToShapeMap = ImmutableMap.copyOf(this.stateManager.getStates().stream().collect(Collectors.toMap(Function.identity(), LichenBlockOld::getShapeForState)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(WET, UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    private static VoxelShape getShapeForState(BlockState state) {
        boolean hasValue = false;
        VoxelShape voxelshape = VoxelShapes.empty();
        if (state.get(UP)) {
            voxelshape = UP_AABB;
            hasValue = true;
        }
        if (state.get(DOWN)) {
            voxelshape = VoxelShapes.union(voxelshape, DOWN_AABB);
            hasValue = true;
        }
        if (state.get(NORTH)) {
            voxelshape = VoxelShapes.union(voxelshape, SOUTH_AABB);
            hasValue = true;
        }
        if (state.get(SOUTH)) {
            voxelshape = VoxelShapes.union(voxelshape, NORTH_AABB);
            hasValue = true;
        }
        if (state.get(EAST)) {
            voxelshape = VoxelShapes.union(voxelshape, WEST_AABB);
            hasValue = true;
        }
        if (state.get(WEST)) {
            voxelshape = VoxelShapes.union(voxelshape, EAST_AABB);
            hasValue = true;
        }
        if (!hasValue) {
            return VoxelShapes.fullCube();
        }

        return voxelshape;
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return this.stateToShapeMap.get(state);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
        return this.getBlocksAttachedTo(this.updateState(state, worldIn, pos));
    }

    private boolean getBlocksAttachedTo(BlockState state) {
        return countBlocksLichenIsAttachedTo(state) > 0;
    }

    public static int countBlocksLichenIsAttachedTo(BlockState state) {
        int i = 0;
        for (BooleanProperty booleanproperty : FACING_TO_PROPERTY_MAP.values()) {
            if (state.get(booleanproperty)) {
                ++i;
            }
        }
        return i;
    }

    private BlockState updateState(BlockState state, BlockView blockReader, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (state.get(getPropertyFor(direction))) {
                BlockPos neighbor = pos.offset(direction);
                state = state.with(getPropertyFor(direction), canAttachTo(blockReader, neighbor, direction));
            }
        }
        return state;
    }

    public static boolean canAttachTo(BlockView blockReader, BlockPos posIn, Direction neighborPos) {
        BlockState blockstate = blockReader.getBlockState(posIn);
        return Block.isFaceFullSquare(blockstate.getCollisionShape(blockReader, posIn), neighborPos.getOpposite());
    }

    public static boolean canGrowOn(BlockView blockReader, BlockPos posIn) {
        BlockState blockstate = blockReader.getBlockState(posIn);
        return lichenErosionMap.containsKey(blockstate.getBlock()) || lichenErosionMap.containsValue(blockstate.getBlock());
//        return Block.doesSideFillSquare(blockstate.getCollisionShape(blockReader, posIn), neighborPos.getOpposite());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState blockstate = this.updateState(stateIn, worldIn, currentPos);
        return !this.getBlocksAttachedTo(blockstate) ? Blocks.AIR.getDefaultState() : blockstate;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction face = context.getSide();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState originalState = world.getBlockState(pos);

        boolean isLichen = originalState.isOf(this);
        BlockState blockstate = isLichen ? originalState : this.getDefaultState();

        for (Direction direction : context.getPlacementDirections()) {
            BooleanProperty booleanProperty = getPropertyFor(direction);
            boolean exists = isLichen && originalState.get(booleanProperty);
            if (!exists && canAttachTo(world, pos.offset(direction), direction)) {
                return blockstate.with(booleanProperty, true);
            }
        }
        return isLichen ? blockstate : null;
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext useContext) {
        BlockState blockstate = useContext.getWorld().getBlockState(useContext.getBlockPos());
        if (blockstate.isOf(this)) {
            return this.countBlocksLichenIsAttachedTo(blockstate) < FACING_TO_PROPERTY_MAP.size();
        } else {
            return super.canReplace(state, useContext);
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        boolean isWet = state.get(WET);
        if (!hasWater(worldIn, pos) && !worldIn.hasRain(pos.up())) {
            worldIn.setBlockState(pos, state.with(WET, false), 2);
            isWet = false;
        } else if (!isWet) {
            worldIn.setBlockState(pos, state.with(WET, true), 2);
            isWet = true;
        }
        if (isWet) {
            if (random.nextInt(4) == 0) {
                growToConnectedBlockFace(state, worldIn, pos, random);
            }
            if (random.nextInt(5) == 0) {
                for (Direction dir : Direction.values()) {
                    if (state.get(getPropertyFor(dir))) {
                        tryErodeBlock(worldIn, pos.offset(dir));
                    }
                }
            }
        }
    }

    private static boolean hasWater(WorldView worldIn, BlockPos pos) {
        for (BlockPos blockpos : BlockPos.iterate(pos.add(-2, -2, -2), pos.add(2, 2, 2))) {
            if (worldIn.getFluidState(blockpos).isIn(FluidTags.WATER) || worldIn.getBlockState(blockpos).isOf(UEBlocks.PUDDLE)) {
                return true;
            }
        }
        return false;
    }

    private static void growToConnectedBlockFace(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int count = 0;
        Direction face;
        do {
            face = Direction.random(random);
            if (count++ > 20) {
                return;
            }
        } while (!state.get(getPropertyFor(face)));

        Direction growthDir;
        do {
            growthDir = Direction.random(random);
        } while (growthDir == face || growthDir == face.getOpposite());

        BlockPos newPos = pos.offset(growthDir);
        if (canGrowOn(world, newPos)) {
            world.setBlockState(pos, state.with(getPropertyFor(growthDir), true), 2);
        } else if (!tryGrowIntoBlock(world, newPos, face, false)) {
            if (world.getBlockState(newPos).isAir()) {
                tryGrowIntoBlock(world, newPos.offset(face), growthDir.getOpposite(), false);
            }
        }

    }

    public static boolean tryGrowIntoBlock(World world, BlockPos pos, Direction side, boolean fromPuddle) {
        BlockState block = world.getBlockState(pos);
        BlockState newBlock = null;
        if (block.isAir()) {
            newBlock = UEBlocks.LICHEN.getDefaultState();
        } else if (block.isOf(UEBlocks.LICHEN)) {
            newBlock = block;
        }
        if (newBlock != null && (fromPuddle ? canAttachTo(world, pos.offset(side), side) : canGrowOn(world, pos.offset(side)))) {
            world.setBlockState(pos, newBlock.with(getPropertyFor(side), true));
            return true;
        }
        return false;
    }

    public static boolean hasEnoughLichen(BlockView blockReader, BlockPos pos, int max, int range, int vertRange) {
        Iterable<BlockPos> iterable = BlockPos.iterate(pos.getX() - range, pos.getY() - vertRange, pos.getZ() - range, pos.getX() + range, pos.getY() + vertRange, pos.getZ() + range);
        int j = max;
        for (BlockPos blockpos : iterable) {
            BlockState state = blockReader.getBlockState(blockpos);
            if (state.isOf(UEBlocks.LICHEN)) {
                j -= LichenBlockOld.countBlocksLichenIsAttachedTo(state);
                if (j <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void addErosionMap(Block block, Block eroded) {
        lichenErosionMap.put(block, eroded);
    }

    public void tryErodeBlock(World world, BlockPos pos) {
        BlockState block = world.getBlockState(pos);
        if (lichenErosionMap.containsKey(block.getBlock())) {
            int coveredSides = 0;
            for (Direction dir : Direction.values()) {
                BlockState newBlock = world.getBlockState(pos.offset(dir));
                if (newBlock.isOf(this) && newBlock.get(getPropertyFor(dir.getOpposite()))) {
                    coveredSides++;
                }
            }
            if (coveredSides >= 5) {
                world.setBlockState(pos, lichenErosionMap.get(block.getBlock()).getDefaultState());
            }
        }
    }

    public static BooleanProperty getPropertyFor(Direction side) {
        return FACING_TO_PROPERTY_MAP.get(side);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rot) {
        return state.with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.NORTH)), state.get(NORTH))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.SOUTH)), state.get(SOUTH))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.EAST)), state.get(EAST))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.WEST)), state.get(WEST))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.UP)), state.get(UP))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.DOWN)), state.get(DOWN));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirrorIn) {
        return state.with(FACING_TO_PROPERTY_MAP.get(mirrorIn.apply(Direction.NORTH)), state.get(NORTH))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.apply(Direction.SOUTH)), state.get(SOUTH))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.apply(Direction.EAST)), state.get(EAST))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.apply(Direction.WEST)), state.get(WEST))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.apply(Direction.UP)), state.get(UP))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.apply(Direction.DOWN)), state.get(DOWN));
    }


}

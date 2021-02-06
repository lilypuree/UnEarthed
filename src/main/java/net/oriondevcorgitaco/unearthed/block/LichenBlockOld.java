package net.oriondevcorgitaco.unearthed.block;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SixWayBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LichenBlockOld extends Block {
    public static BooleanProperty WET = ModBlockProperties.WET;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP;
    private static final VoxelShape UP_AABB = Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape EAST_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_AABB = Block.makeCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private final Map<BlockState, VoxelShape> stateToShapeMap;

    private static Map<Block, Block> lichenErosionMap = new Object2ObjectOpenHashMap<>();

    public LichenBlockOld(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState()
                .with(UP, false).with(DOWN, false).with(WEST, false).with(EAST, false).with(NORTH, false).with(SOUTH, false));
        this.stateToShapeMap = ImmutableMap.copyOf(this.stateContainer.getValidStates().stream().collect(Collectors.toMap(Function.identity(), LichenBlockOld::getShapeForState)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
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
            voxelshape = VoxelShapes.or(voxelshape, DOWN_AABB);
            hasValue = true;
        }
        if (state.get(NORTH)) {
            voxelshape = VoxelShapes.or(voxelshape, SOUTH_AABB);
            hasValue = true;
        }
        if (state.get(SOUTH)) {
            voxelshape = VoxelShapes.or(voxelshape, NORTH_AABB);
            hasValue = true;
        }
        if (state.get(EAST)) {
            voxelshape = VoxelShapes.or(voxelshape, WEST_AABB);
            hasValue = true;
        }
        if (state.get(WEST)) {
            voxelshape = VoxelShapes.or(voxelshape, EAST_AABB);
            hasValue = true;
        }
        if (!hasValue) {
            return VoxelShapes.fullCube();
        }

        return voxelshape;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return this.stateToShapeMap.get(state);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
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

    private BlockState updateState(BlockState state, IBlockReader blockReader, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (state.get(getPropertyFor(direction))) {
                BlockPos neighbor = pos.offset(direction);
                state = state.with(getPropertyFor(direction), canAttachTo(blockReader, neighbor, direction));
            }
        }
        return state;
    }

    public static boolean canAttachTo(IBlockReader blockReader, BlockPos posIn, Direction neighborPos) {
        BlockState blockstate = blockReader.getBlockState(posIn);
        return Block.doesSideFillSquare(blockstate.getCollisionShape(blockReader, posIn), neighborPos.getOpposite());
    }

    public static boolean canGrowOn(IBlockReader blockReader, BlockPos posIn) {
        BlockState blockstate = blockReader.getBlockState(posIn);
        return lichenErosionMap.containsKey(blockstate.getBlock()) || lichenErosionMap.containsValue(blockstate.getBlock());
//        return Block.doesSideFillSquare(blockstate.getCollisionShape(blockReader, posIn), neighborPos.getOpposite());
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState blockstate = this.updateState(stateIn, worldIn, currentPos);
        return !this.getBlocksAttachedTo(blockstate) ? Blocks.AIR.getDefaultState() : blockstate;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction face = context.getFace();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState originalState = world.getBlockState(pos);

        boolean isLichen = originalState.isIn(this);
        BlockState blockstate = isLichen ? originalState : this.getDefaultState();

        for (Direction direction : context.getNearestLookingDirections()) {
            BooleanProperty booleanProperty = getPropertyFor(direction);
            boolean exists = isLichen && originalState.get(booleanProperty);
            if (!exists && canAttachTo(world, pos.offset(direction), direction)) {
                return blockstate.with(booleanProperty, true);
            }
        }
        return isLichen ? blockstate : null;
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        BlockState blockstate = useContext.getWorld().getBlockState(useContext.getPos());
        if (blockstate.isIn(this)) {
            return this.countBlocksLichenIsAttachedTo(blockstate) < FACING_TO_PROPERTY_MAP.size();
        } else {
            return super.isReplaceable(state, useContext);
        }
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        boolean isWet = state.get(WET);
        if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
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

    private static boolean hasWater(IWorldReader worldIn, BlockPos pos) {
        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-2, -2, -2), pos.add(2, 2, 2))) {
            if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER) || worldIn.getBlockState(blockpos).isIn(UEBlocks.PUDDLE)) {
                return true;
            }
        }
        return false;
    }

    private static void growToConnectedBlockFace(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int count = 0;
        Direction face;
        do {
            face = Direction.getRandomDirection(random);
            if (count++ > 20) {
                return;
            }
        } while (!state.get(getPropertyFor(face)));

        Direction growthDir;
        do {
            growthDir = Direction.getRandomDirection(random);
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
        } else if (block.isIn(UEBlocks.LICHEN)) {
            newBlock = block;
        }
        if (newBlock != null && (fromPuddle ? canAttachTo(world, pos.offset(side), side) : canGrowOn(world, pos.offset(side)))) {
            world.setBlockState(pos, newBlock.with(getPropertyFor(side), true));
            return true;
        }
        return false;
    }

    public static boolean hasEnoughLichen(IBlockReader blockReader, BlockPos pos, int max, int range, int vertRange) {
        Iterable<BlockPos> iterable = BlockPos.getAllInBoxMutable(pos.getX() - range, pos.getY() - vertRange, pos.getZ() - range, pos.getX() + range, pos.getY() + vertRange, pos.getZ() + range);
        int j = max;
        for (BlockPos blockpos : iterable) {
            BlockState state = blockReader.getBlockState(blockpos);
            if (state.isIn(UEBlocks.LICHEN)) {
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
                if (newBlock.isIn(this) && newBlock.get(getPropertyFor(dir.getOpposite()))) {
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
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.NORTH)), state.get(NORTH))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.SOUTH)), state.get(SOUTH))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.EAST)), state.get(EAST))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.WEST)), state.get(WEST))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.UP)), state.get(UP))
                .with(FACING_TO_PROPERTY_MAP.get(rot.rotate(Direction.DOWN)), state.get(DOWN));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.with(FACING_TO_PROPERTY_MAP.get(mirrorIn.mirror(Direction.NORTH)), state.get(NORTH))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.mirror(Direction.SOUTH)), state.get(SOUTH))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.mirror(Direction.EAST)), state.get(EAST))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.mirror(Direction.WEST)), state.get(WEST))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.mirror(Direction.UP)), state.get(UP))
                .with(FACING_TO_PROPERTY_MAP.get(mirrorIn.mirror(Direction.DOWN)), state.get(DOWN));
    }


}

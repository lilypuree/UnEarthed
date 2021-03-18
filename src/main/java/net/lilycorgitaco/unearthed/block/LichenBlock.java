package net.lilycorgitaco.unearthed.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.lilycorgitaco.unearthed.block.properties.ModBlockProperties;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import net.lilycorgitaco.unearthed.core.UEItems;

import java.util.Map;
import java.util.Random;

public class LichenBlock extends VanillaLichenParentBlock implements Waterloggable {
    public static BooleanProperty WET = ModBlockProperties.WET;
    public static BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = ConnectingBlock.FACING_PROPERTIES;

    public static Map<Block, Block> lichenErosionMap = new Object2ObjectOpenHashMap<>();

    public LichenBlock(AbstractBlock.Settings properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(WET, true).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WET, WATERLOGGED);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.get(WATERLOGGED)) {
            worldIn.getFluidTickScheduler().schedule(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.getStateForNeighborUpdate(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext useContext) {
        return !(useContext.getStack().getItem() == UEItems.LICHEN) || super.canReplace(state, useContext);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
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
                if (!hasEnoughLichen(worldIn, pos, 8, 2, 2)) {
                    tryGrowth(state, worldIn, pos, random);
                }
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

    public static boolean hasEnoughLichen(BlockView blockReader, BlockPos pos, int max, int range, int vertRange) {
        Iterable<BlockPos> iterable = BlockPos.iterate(pos.getX() - range, pos.getY() - vertRange, pos.getZ() - range, pos.getX() + range, pos.getY() + vertRange, pos.getZ() + range);
        int j = max;
        for (BlockPos blockpos : iterable) {
            BlockState state = blockReader.getBlockState(blockpos);
            if (state.isOf(UEBlocks.LICHEN)) {
                j -= LichenBlock.countBlocksLichenIsAttachedTo(state);
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
                Block eroded = lichenErosionMap.get(block.getBlock());
                if (eroded != null){
                    world.setBlockState(pos, lichenErosionMap.get(block.getBlock()).getDefaultState());
                }
            }
        }
    }
}

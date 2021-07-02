package net.oriondevcorgitaco.unearthed.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
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
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.block.properties.ModBlockProperties;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEItems;

import java.util.Map;
import java.util.Random;

public class LichenBlock extends VanillaLichenParentBlock implements IWaterLoggable {
    public static BooleanProperty WET = ModBlockProperties.WET;
    public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.PROPERTY_BY_DIRECTION;

    public static Map<Block, Block> lichenErosionMap = new Object2ObjectOpenHashMap<>();

    public LichenBlock(AbstractBlock.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(WET, true).setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WET, WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.getValue(WATERLOGGED)) {
            worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockItemUseContext useContext) {
        return !(useContext.getItemInHand().getItem() == UEItems.LICHEN) || super.canBeReplaced(state, useContext);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public static int countBlocksLichenIsAttachedTo(BlockState state) {
        int i = 0;
        for (BooleanProperty booleanproperty : FACING_TO_PROPERTY_MAP.values()) {
            if (state.getValue(booleanproperty)) {
                ++i;
            }
        }
        return i;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        boolean isWet = state.getValue(WET);
        if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.above())) {
            worldIn.setBlock(pos, state.setValue(WET, false), 2);
            isWet = false;
        } else if (!isWet) {
            worldIn.setBlock(pos, state.setValue(WET, true), 2);
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
                    if (state.getValue(getPropertyFor(dir))) {
                        tryErodeBlock(worldIn, pos.relative(dir));
                    }
                }
            }
        }
    }

    private static boolean hasWater(IWorldReader worldIn, BlockPos pos) {
        for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-2, -2, -2), pos.offset(2, 2, 2))) {
            if (worldIn.getFluidState(blockpos).is(FluidTags.WATER) || worldIn.getBlockState(blockpos).is(UEBlocks.PUDDLE)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEnoughLichen(IBlockReader blockReader, BlockPos pos, int max, int range, int vertRange) {
        Iterable<BlockPos> iterable = BlockPos.betweenClosed(pos.getX() - range, pos.getY() - vertRange, pos.getZ() - range, pos.getX() + range, pos.getY() + vertRange, pos.getZ() + range);
        int j = max;
        for (BlockPos blockpos : iterable) {
            BlockState state = blockReader.getBlockState(blockpos);
            if (state.is(UEBlocks.LICHEN)) {
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
                BlockState newBlock = world.getBlockState(pos.relative(dir));
                if (newBlock.is(this) && newBlock.getValue(getPropertyFor(dir.getOpposite()))) {
                    coveredSides++;
                }
            }
            if (coveredSides >= 5) {
                Block eroded =  lichenErosionMap.get(block.getBlock());
                if (eroded != null){
                    world.setBlockAndUpdate(pos,eroded.defaultBlockState());
                }
            }
        }
    }
}

package net.oriondevcorgitaco.unearthed.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;

import java.util.Map;
import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class RegolithGrassBlock extends GrassBlock {
    public static Map<Block, Block> regolithToGrassMap = new Object2ObjectOpenHashMap<>();
    private Block regolithBlock;

    static {
        regolithToGrassMap.put(Blocks.DIRT, Blocks.GRASS_BLOCK);
    }

    public RegolithGrassBlock(Block regolithBlock, Properties properties) {
        super(properties);
        this.regolithBlock = regolithBlock;
        regolithToGrassMap.put(regolithBlock, this);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!canBeGrass(state, worldIn, pos)) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            worldIn.setBlockAndUpdate(pos, regolithBlock.defaultBlockState());
        } else {
            if (worldIn.getMaxLocalRawBrightness(pos.above()) >= 9) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 2, random.nextInt(3) - 1);
                    BlockState blockState = worldIn.getBlockState(blockpos);
                    if (regolithToGrassMap.containsKey(blockState.getBlock()) && canPropagate(blockState, worldIn, blockpos)) {
                        worldIn.setBlockAndUpdate(blockpos, regolithToGrassMap.get(blockState.getBlock()).defaultBlockState());
                        worldIn.setBlockAndUpdate(blockpos.above(), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public static boolean canBeGrass(BlockState state, IWorldReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = worldReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowBlock.LAYERS) == 1 || blockstate.is(UEBlocks.LICHEN)) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(worldReader, blockpos));
            return i < worldReader.getMaxLightLevel();
        }
    }

    public static boolean canPropagate(BlockState state, IWorldReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState coverBlock = worldReader.getBlockState(blockpos);
        return coverBlock.is(UEBlocks.LICHEN) && coverBlock.getValue(LichenBlock.getPropertyFor(Direction.DOWN));
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return plantable.getPlantType(world, pos) == PlantType.PLAINS;
    }

    public Block getRegolithBlock() {
        return regolithBlock;
    }
}

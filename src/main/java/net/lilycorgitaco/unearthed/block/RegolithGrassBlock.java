package net.lilycorgitaco.unearthed.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.lilycorgitaco.unearthed.core.UEBlocks;

import java.util.Map;
import java.util.Random;

public class RegolithGrassBlock extends GrassBlock {
    public static Map<Block, Block> regolithToGrassMap = new Object2ObjectOpenHashMap<>();
    private Block regolithBlock;

    static {
        regolithToGrassMap.put(Blocks.DIRT, Blocks.GRASS_BLOCK);
    }

    public RegolithGrassBlock(Block regolithBlock, Settings properties) {
        super(properties);
        this.regolithBlock = regolithBlock;
        regolithToGrassMap.put(regolithBlock, this);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!canBeGrass(state, worldIn, pos)) {
            worldIn.setBlockState(pos, regolithBlock.getDefaultState());
        } else {
            if (worldIn.getLightLevel(pos.up()) >= 9) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 2, random.nextInt(3) - 1);
                    BlockState blockState = worldIn.getBlockState(blockpos);
                    if (regolithToGrassMap.containsKey(blockState.getBlock()) && canPropagate(blockState, worldIn, blockpos)) {
                        worldIn.setBlockState(blockpos, regolithToGrassMap.get(blockState.getBlock()).getDefaultState());
                        worldIn.setBlockState(blockpos.up(), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
    }

    public static boolean canBeGrass(BlockState state, WorldView worldReader, BlockPos pos) {
        BlockPos blockpos = pos.up();
        BlockState blockstate = worldReader.getBlockState(blockpos);
        if (blockstate.isOf(Blocks.SNOW) && blockstate.get(SnowBlock.LAYERS) == 1 || blockstate.isOf(UEBlocks.LICHEN)) {
            return true;
        } else if (blockstate.getFluidState().getLevel() == 8) {
            return false;
        } else {
            int i = ChunkLightProvider.getRealisticOpacity(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(worldReader, blockpos));
            return i < worldReader.getMaxLightLevel();
        }
    }

    public static boolean canPropagate(BlockState state, WorldView worldReader, BlockPos pos) {
        BlockPos blockpos = pos.up();
        BlockState coverBlock = worldReader.getBlockState(blockpos);
        return coverBlock.isOf(UEBlocks.LICHEN) && coverBlock.get(LichenBlock.getPropertyFor(Direction.DOWN));
    }

    public Block getRegolithBlock() {
        return regolithBlock;
    }

}

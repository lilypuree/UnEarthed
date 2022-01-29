package lilypuree.unearthed.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lilypuree.unearthed.core.UEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;

import java.util.Map;
import java.util.Random;

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
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
        if (!canBeGrass(state, worldIn, pos)) {
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

    public static boolean canBeGrass(BlockState state, LevelReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = worldReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1 || blockstate.is(UEBlocks.LICHEN)) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LayerLightEngine.getLightBlockInto(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(worldReader, blockpos));
            return i < worldReader.getMaxLightLevel();
        }
    }

    public static boolean canPropagate(BlockState state, LevelReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState coverBlock = worldReader.getBlockState(blockpos);
        return coverBlock.is(UEBlocks.LICHEN) && coverBlock.getValue(LichenBlock.getFaceProperty(Direction.DOWN));
    }

    public Block getRegolithBlock() {
        return regolithBlock;
    }
}

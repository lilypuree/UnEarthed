package lilypuree.unearthed.block;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lilypuree.unearthed.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LayerLightEngine;
import org.jetbrains.annotations.Nullable;

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
                    if (blockState.getBlock() instanceof RegolithBlock && regolithToGrassMap.containsKey(blockState.getBlock()) && canPropagate(blockState, worldIn, blockpos)) {
                        worldIn.setBlockAndUpdate(blockpos, regolithToGrassMap.get(blockState.getBlock()).defaultBlockState());
                        worldIn.setBlockAndUpdate(blockpos.above(), Blocks.AIR.defaultBlockState());
                    } else if (blockState.is(Blocks.DIRT) && canPropagate(blockState, worldIn, blockpos)) {
                        worldIn.setBlockAndUpdate(blockpos, Blocks.GRASS_BLOCK.defaultBlockState());
                        worldIn.setBlockAndUpdate(blockpos.above(), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState $$3, @Nullable BlockEntity $$4, ItemStack stack) {
        super.playerDestroy(level, player, pos, $$3, $$4, stack);
        if (regolithBlock instanceof RegolithBlock && Services.PLATFORM.isDiggingHoe(stack)) {
            level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 2);
        }
    }

    public static boolean canBeGrass(BlockState state, LevelReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = worldReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
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
        return canBeGrass(state, worldReader, pos) && !worldReader.getFluidState(blockpos).is(FluidTags.WATER);
    }

    public Block getRegolithBlock() {
        return regolithBlock;
    }
}

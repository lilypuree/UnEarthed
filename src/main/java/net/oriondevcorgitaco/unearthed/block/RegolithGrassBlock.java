package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.*;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;

import java.util.Random;

public class RegolithGrassBlock extends GrassBlock {

    private Block regolithBlock;

    public RegolithGrassBlock(Block regolithBlock, Properties properties) {
        super(properties);
        this.regolithBlock = regolithBlock;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!canBeGrass(state, worldIn, pos)) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            worldIn.setBlockState(pos, regolithBlock.getDefaultState());
        } else {
            if (worldIn.getLight(pos.up()) >= 9) {
                BlockState blockstate = this.getDefaultState();
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 2, random.nextInt(3) - 1);
                    BlockState blockState = worldIn.getBlockState(blockpos);
                    if (blockState.isIn(Blocks.DIRT) && canPropagate(blockstate, worldIn, blockpos)) {
                        worldIn.setBlockState(blockpos, blockstate);
                    }
                    if (blockState.isIn(BlockGeneratorReference.REGOLITH_TAG) && canPropagate(blockstate, worldIn, blockpos)) {
                        ResourceLocation regolithName = blockState.getBlock().getRegistryName();
                        Block grassyRegolithBlock = Registry.BLOCK.getOrDefault(new ResourceLocation(regolithName.getNamespace(), regolithName.getPath().replaceAll("regolith", "grassy_regolith")));
                        if (grassyRegolithBlock != Blocks.AIR) {
                            worldIn.setBlockState(blockpos, grassyRegolithBlock.getDefaultState());
                            worldIn.setBlockState(blockpos.up(), Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    public static boolean canBeGrass(BlockState state, IWorldReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.up();
        BlockState blockstate = worldReader.getBlockState(blockpos);
        if (blockstate.isIn(Blocks.SNOW) && blockstate.get(SnowBlock.LAYERS) == 1 || blockstate.isIn(UEBlocks.LICHEN)) {
            return true;
        } else if (blockstate.getFluidState().getLevel() == 8) {
            return false;
        } else {
            int i = LightEngine.func_215613_a(worldReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(worldReader, blockpos));
            return i < worldReader.getMaxLightLevel();
        }
    }

    public static boolean canPropagate(BlockState state, IWorldReader worldReader, BlockPos pos) {
        BlockPos blockpos = pos.up();
        BlockState coverBlock = worldReader.getBlockState(blockpos);
        return coverBlock.isIn(UEBlocks.LICHEN) && coverBlock.get(LichenBlock.getPropertyFor(Direction.DOWN));
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        return plantable.getPlantType(world, pos) == PlantType.PLAINS;
    }
}

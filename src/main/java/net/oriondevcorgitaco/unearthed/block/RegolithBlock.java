package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.Block;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

public class RegolithBlock extends Block {
    public RegolithBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        PlantType plantType = plantable.getPlantType(world, pos);

        if (plantType == PlantType.PLAINS || plantType == PlantType.DESERT) {
            return true;
        }
        return super.canSustainPlant(state, world, pos, facing, plantable);
    }
}

package net.oriondevcorgitaco.unearthed.planets.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.planets.data.CorePositionSavedData;
import net.oriondevcorgitaco.unearthed.planets.planetcore.MantleCoreTile;

import java.util.Optional;

public class PlanetVolcanoBlock extends Block {
    public PlanetVolcanoBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        if (worldIn instanceof ServerWorld) {
            CorePositionSavedData.get((ServerWorld) worldIn).findCore(((ServerWorld) worldIn).getWorld(), pos).forEach(coreTile -> {
                coreTile.volcanicExplosion(pos);
            });
        }
        super.onPlayerDestroy(worldIn, pos, state);
    }
}

package net.oriondevcorgitaco.unearthed.planets.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.block.properties.ModBlockProperties;
import net.oriondevcorgitaco.unearthed.planets.data.CorePositionSavedData;

import java.util.Random;

public class PlanetSurfaceBlock extends Block {
    public static final EnumProperty<SurfaceTypes> SURFACE_TYPE = ModBlockProperties.SURFACE_TYPE;

    public PlanetSurfaceBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SURFACE_TYPE);
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        if (worldIn instanceof ServerWorld) {
            onTick(((ServerWorld) worldIn), pos, state);
        }
        super.onPlayerDestroy(worldIn, pos, state);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        onTick(worldIn, pos, state);
        super.tick(state, worldIn, pos, rand);
    }

    private void onTick(ServerWorld world, BlockPos pos, BlockState state) {
        CorePositionSavedData.get(world).findCore(world, pos).forEach(coreTile -> {
            if (coreTile.addSediment(state.get(SURFACE_TYPE)))
                world.setBlockState(pos, coreTile.getIntrusiveBlock());
            for (Direction dir : Direction.values()) {
                world.getPendingBlockTicks().scheduleTick(pos.offset(dir), this, 5);
            }
        });
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        switch (state.get(SURFACE_TYPE)) {
            case PLAIN:
                return SoundType.GROUND;
            case DESERT:
                return SoundType.SAND;
            case POLAR:
                return SoundType.SNOW;
            case FOREST:
                return SoundType.PLANT;
        }
        return super.getSoundType(state);
    }
}

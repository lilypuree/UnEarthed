package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;

public interface State{

    Type getType();

    BlockState getDefaultState();

}

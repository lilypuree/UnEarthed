package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;

import java.util.Map;

public interface Cell2 {
    BlockState getDefaultState();

    BlockState getCobbleReplacement();

    BlockState getDirtReplacement();

    BlockState getGrassReplacementDefault();

    default BlockState getGrassReplacement(BlockState grassBlock) {
        if (getGrassReplacementDefault() == grassBlock) return grassBlock;
        else if (getGrassReplacementDefault().hasProperty(BlockStateProperties.SNOWY)) {
            return getGrassReplacementDefault().with(BlockStateProperties.SNOWY, grassBlock.get(BlockStateProperties.SNOWY));
        }
        return getGrassReplacementDefault();
    }

    boolean replacesOre();

    BlockState getOre(IOreType oreType);
}

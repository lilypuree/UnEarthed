package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;

public class SimpleState extends BaseState {

    private final BlockState dirtReplacement;
    private final boolean isGrass;
    private final BlockState grassReplacement;

    public SimpleState(Type type, BlockState block, BlockState dirtReplacement, BlockState grassReplacement) {
        super(type, block);
        this.dirtReplacement = dirtReplacement;
        this.grassReplacement = grassReplacement;
        this.isGrass = grassReplacement.hasProperty(BlockStateProperties.SNOWY);
    }

    public BlockState getDirtReplacement() {
        return dirtReplacement;
    }

    public BlockState getGrassReplacement(BlockState original) {
        if (isGrass) {
            return grassReplacement.with(BlockStateProperties.SNOWY, original.get(BlockStateProperties.SNOWY));
        } else {
            return grassReplacement;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SimpleState) {
            SimpleState state = (SimpleState) obj;
            return getType() == ((SimpleState) obj).getType()
                    && getDefaultState() == state.getDefaultState()
                    && getDirtReplacement() == state.getDirtReplacement()
                    && this.grassReplacement == state.grassReplacement;
        }
        return false;
    }
}

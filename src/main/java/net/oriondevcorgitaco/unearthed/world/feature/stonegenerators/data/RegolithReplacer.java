package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class RegolithReplacer implements CellularOre {
    private float accWeight;

    public RegolithReplacer() {
    }

    @Override
    public boolean canReplace(State existing) {
        if (existing instanceof StoneState) {
            return ((StoneState) existing).getWrapper().getDirtReplacer(Blocks.DIRT.getDefaultState()) != Blocks.DIRT.getDefaultState();
        }
        return false;
    }

    @Override
    public State apply(State existing) {
        BlockState state = ((StoneState) existing).getWrapper().getDirtReplacer(Blocks.DIRT.getDefaultState());
        return new BaseState(existing.getType(), state);
    }

    @Override
    public float getAccumulatedWeight() {
        return accWeight;
    }

    @Override
    public void setAccumulatedWeight(float weight) {
        this.accWeight = weight;
    }
}

package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;

public class OreReplacer implements CellularOre {
    private IOreType oreType;
    private float accWeight;

    public OreReplacer(IOreType oreType) {
        this.oreType = oreType;
    }

    @Override
    public boolean canReplace(State existing) {
        if (existing instanceof StoneState) {
            return (((StoneState) existing).getWrapper().getOre(oreType)) != null;
        }
        return false;
    }

    @Override
    public State apply(State existing) {
        BlockState state = ((StoneState) existing).getWrapper().getOre(oreType);
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

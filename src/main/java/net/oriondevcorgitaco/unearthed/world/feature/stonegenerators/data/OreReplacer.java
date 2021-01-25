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
        return existing.getCell().replacesOre();
    }

    @Override
    public State apply(State existing) {
        BlockState state = existing.getCell().getOre(oreType);
        return new State(existing.getType(), new Cell(state, existing.getCell().getDirtReplacement(), existing.getCell().getGrassReplacementDefault(), true));
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

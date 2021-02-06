package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class RegolithReplacer implements CellularOre {
    private float accWeight;

    public RegolithReplacer() {
    }

    @Override
    public boolean canReplace(State existing) {
        return existing.getCell().getDirtReplacement() != Blocks.DIRT.getDefaultState();
    }

    @Override
    public State apply(State existing) {
        return new State(existing.getType(), new Cell(existing.getCell().getDirtReplacement(), existing.getCell().getDirtReplacement(), existing.getCell().getGrassReplacementDefault(), true));
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

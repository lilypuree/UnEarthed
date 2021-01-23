package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

public class DebugReplacer implements CellularOre {
    CellularOre ore;

    public DebugReplacer(CellularOre ore) {
        this.ore = ore;
    }

    @Override
    public boolean canReplace(State existing) {
        return ore.canReplace(existing);
    }

    @Override
    public State apply(State existing) {
        return new BaseState(Type.TERTIARY, ore.apply(existing).getDefaultState());
    }

    @Override
    public float getAccumulatedWeight() {
        return ore.getAccumulatedWeight();
    }

    @Override
    public void setAccumulatedWeight(float weight) {
        ore.setAccumulatedWeight(weight);
    }
}

package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class BasicReplacer implements CellularOre {
    private State state;
    private List<State> replaceable;
    private float accWeight;

    public BasicReplacer(State state, State... replaceable) {
        this.state = state;
        this.replaceable = ImmutableList.copyOf(replaceable);
    }

    @Override
    public boolean canReplace(State existing) {
        return replaceable.contains(existing);
    }

    @Override
    public State apply(State existing) {
        return state;
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

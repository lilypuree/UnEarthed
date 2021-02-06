package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;

import java.util.List;

public interface CellularOre {
    boolean canReplace(State existing);

    State apply(State existing);

    float getAccumulatedWeight();

    void setAccumulatedWeight(float weight);
}

package net.lilycorgitaco.unearthed.world.feature.stonegenerators.data;

public interface CellularOre {
    boolean canReplace(State existing);

    State apply(State existing);

    float getAccumulatedWeight();

    void setAccumulatedWeight(float weight);
}

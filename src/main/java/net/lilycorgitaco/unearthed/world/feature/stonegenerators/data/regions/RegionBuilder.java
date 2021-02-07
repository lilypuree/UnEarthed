package net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.regions;

import net.lilycorgitaco.unearthed.datagen.type.IOreType;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionBuilder {
    private List<State> primaryStates;
    private List<State> orogenyStates;
    private List<State> strataStates;
    private State secondaryState;
    private boolean batolithIntrudes;

    private Map<CellularOre, Float> largeOres;
    private Map<CellularOre, Float> mediumOres;
    private Map<CellularOre, Float> smallOres;
    private float largeOreProbability;
    private float mediumOreProbability;
    private float smallOreProbability;
    private float strataCutoff;
    private int maxStrata;
    private int minStrata;

    public RegionBuilder() {
        primaryStates = new ArrayList<>();
        orogenyStates = new ArrayList<>();
        strataStates = new ArrayList<>();
        largeOres = new HashMap<>();
        mediumOres = new HashMap<>();
        smallOres = new HashMap<>();
        batolithIntrudes = true;
        secondaryState = null;
    }

    public RegionBuilder addStates(State... states) {
        for (State state : states) {
            switch (state.getType()) {
                case PRIMARY:
                    primaryStates.add(state);
                    break;
                case OROGEN:
                    orogenyStates.add(state);
                    break;
                case SECONDARY:
                    secondaryState = state;
                    break;
                case STRATA:
                    strataStates.add(state);
                    break;
            }
        }
        return this;
    }

    public RegionBuilder addPrimary(Cell... cells) {
        for (Cell cell : cells) {
            primaryStates.add(cell.getState(Type.PRIMARY));
        }
        return this;
    }

    public RegionBuilder addOrogen(Cell... cells) {
        for (Cell cell : cells) {
            orogenyStates.add(cell.getState(Type.OROGEN));
        }
        return this;
    }

    public RegionBuilder setSecondary(Cell cell) {
        this.secondaryState = cell.getState(Type.SECONDARY);
        return this;
    }

    public RegionBuilder addStrata(Cell... cells) {
        for (Cell cell : cells) {
            strataStates.add(cell.getState(Type.STRATA));
        }
        return this;
    }

    public RegionBuilder batolithIntrudes(boolean flag) {
        this.batolithIntrudes = flag;
        return this;
    }

    public RegionBuilder setOreProbability(float largeOreProbability, float mediumOreProbability, float smallOreProbability) {
        this.largeOreProbability = largeOreProbability;
        this.mediumOreProbability = mediumOreProbability;
        this.smallOreProbability = smallOreProbability;
        return this;
    }

    public RegionBuilder addOre(int tier, CellularOre ore, float weight) {
        switch (tier) {
            case 1:
                largeOres.put(ore, weight);
                break;
            case 2:
                mediumOres.put(ore, weight);
                break;
            case 3:
                smallOres.put(ore, weight);
        }
        return this;
    }

    public RegionBuilder addOre(int tier, IOreType oreType, float weight) {
        switch (tier) {
            case 1:
                largeOres.put(new OreReplacer(oreType), weight);
                break;
            case 2:
                mediumOres.put(new OreReplacer(oreType), weight);
                break;
            case 3:
                smallOres.put(new OreReplacer(oreType), weight);
        }
        return this;
    }

    public RegionBuilder addRegolith(int tier, float weight) {
        switch (tier) {
            case 1:
                largeOres.put(new RegolithReplacer(), weight);
                break;
            case 2:
                mediumOres.put(new RegolithReplacer(), weight);
                break;
            case 3:
                smallOres.put(new RegolithReplacer(), weight);
        }
        return this;
    }


    public RegionBuilder setStrataParams(float strataCutoff, int maxStrata, int minStrata) {
        this.strataCutoff = strataCutoff;
        this.maxStrata = maxStrata;
        this.minStrata = minStrata;
        return this;
    }

    public Region build() {
        Region region = new Region(primaryStates, orogenyStates, secondaryState, strataStates, batolithIntrudes);
        region.setLargeOres(largeOres, largeOreProbability);
        region.setMediumOres(mediumOres, mediumOreProbability);
        region.setSmallOres(smallOres, smallOreProbability);
        region.setStrataParams(strataCutoff, maxStrata, minStrata);
        return region;
    }
}

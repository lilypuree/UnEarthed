package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.regions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.oriondevcorgitaco.unearthed.block.StoneWrapper;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.*;

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

    public RegionBuilder addPrimaryStoneType(StoneWrapper wrapper) {
        primaryStates.add(new StoneState(Type.PRIMARY, wrapper));
        return this;
    }


    public RegionBuilder addOrogenyStoneType(StoneWrapper wrapper) {
        orogenyStates.add(new StoneState(Type.OROGEN, wrapper));
        return this;
    }

    public RegionBuilder addSimpleStoneType(Type type, BlockState base, BlockState dirtReplacement, BlockState grassReplacement) {
        SimpleState state = new SimpleState(type, base, dirtReplacement, grassReplacement);
        switch (type) {
            case PRIMARY:
                primaryStates.add(state);
                return this;
            case OROGEN:
                orogenyStates.add(state);
                return this;
            case STRATA:
                strataStates.add(state);
                return this;
        }
        return this;
    }

    public RegionBuilder setSecondaryStoneType(StoneWrapper wrapper) {
        this.secondaryState = new StoneState(Type.SECONDARY, wrapper);
        return this;
    }

    public RegionBuilder addStrataStoneType(StoneWrapper wrapper) {
        strataStates.add(new StoneState(Type.STRATA, wrapper));
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

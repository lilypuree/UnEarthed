package net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.regions;

import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.CellularOre;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.State;

import java.util.*;

public class Region {
    private List<State> primaryStates;
    private List<State> orogenStates;
    private State secondaryState;
    private List<State> strataStates;

    private List<CellularOre> largeOres = new ArrayList<>();
    private List<CellularOre> mediumOres = new ArrayList<>();
    private List<CellularOre> smallOres = new ArrayList<>();
    private float largeOreProbability = 0;
    private float mediumOreProbability = 0;
    private float smallOreProbability = 0;
    private float accumulatedWeightLarge = 0;
    private float accumulatedWeightMedium = 0;
    private float accumulatedWeightSmall = 0;

    private float strataCutoff = 0.2f;
    private int minStrata;
    private int maxStrata;

    private boolean batolithIntrudes = true;

    public Region(List<State> primaries, List<State> orgenies, State secondary, List<State> stratum, boolean batolithIntrudes) {
        this.primaryStates = primaries;
        this.orogenStates = orgenies;
        this.secondaryState = secondary;
        this.strataStates = stratum;
        this.batolithIntrudes = batolithIntrudes;
    }

    public void setLargeOres(Map<CellularOre, Float> largeOres, float largeOreProbability) {
        for (Map.Entry<CellularOre, Float> entry : largeOres.entrySet()) {
            accumulatedWeightLarge += entry.getValue();
            entry.getKey().setAccumulatedWeight(accumulatedWeightLarge);
            this.largeOres.add(entry.getKey());
        }
        this.largeOreProbability = largeOreProbability;
    }

    public void setMediumOres(Map<CellularOre, Float> mediumOres, float mediumOreProbability) {
        for (Map.Entry<CellularOre, Float> entry : mediumOres.entrySet()) {
            accumulatedWeightMedium += entry.getValue();
            entry.getKey().setAccumulatedWeight(accumulatedWeightMedium);
            this.mediumOres.add(entry.getKey());
        }
        this.mediumOreProbability = mediumOreProbability;
    }

    public void setSmallOres(Map<CellularOre, Float> smallOres, float smallOreProbability) {
        for (Map.Entry<CellularOre, Float> entry : smallOres.entrySet()) {
            accumulatedWeightSmall += entry.getValue();
            entry.getKey().setAccumulatedWeight(accumulatedWeightSmall);
            this.smallOres.add(entry.getKey());
        }
        this.smallOreProbability = smallOreProbability;
    }

    public void setStrataParams(float strataCutoff, int maxStrata, int minStrata) {
        this.strataCutoff = strataCutoff;
        this.maxStrata = maxStrata;
        this.minStrata = minStrata;
    }

    public boolean isOrogen(float warpScale, float similarity) {
        if (warpScale < 0.07f || similarity > 0.95f) {
            return true;
        } else {
            return false;
        }
    }

    public State getSecondaryState() {
        return secondaryState;
    }

    public State getPrimaryState(float level) {
        int index = ((int) (level * primaryStates.size() * 3));
        return primaryStates.get(index % primaryStates.size());
    }

    public State getOrogenState(float level) {
        int index = ((int) (level * orogenStates.size() * 10));
        return orogenStates.get(index % orogenStates.size());
    }

    public State getStrataState(float value, int strataLevel) {
        if (value < 0.9f) {
            return null;
        }
        int index = ((int) ((value - 0.9f) * 10 * strataStates.size()));
        return strataStates.get(index);
    }

    public CellularOre getReplacement(int tier, Random rand) {
        switch (tier) {
            default:
            case 1:
                if (rand.nextFloat() < largeOreProbability) {
                    float targetWeight = accumulatedWeightLarge * rand.nextFloat();
                    for (CellularOre ore : largeOres) {
                        if (ore.getAccumulatedWeight() >= targetWeight) {
                            return ore;
                        }
                    }
                }
                return null;
            case 2:
                if (rand.nextFloat() < mediumOreProbability) {
                    float targetWeight = accumulatedWeightMedium * rand.nextFloat();
                    for (CellularOre ore : mediumOres) {
                        if (ore.getAccumulatedWeight() >= targetWeight) {
                            return ore;
                        }
                    }
                }
                return null;
            case 3:
                if (rand.nextFloat() < smallOreProbability) {
                    float targetWeight = accumulatedWeightSmall * rand.nextFloat();
                    for (CellularOre ore : smallOres) {
                        if (ore.getAccumulatedWeight() >= targetWeight) {
                            return ore;
                        }
                    }
                }
                return null;
        }
    }

    public int getSecondaryDepth(float noiseValue) {
        noiseValue = noiseValue * noiseValue * noiseValue;
        if (noiseValue > 0.6) {
            return ((int) ((noiseValue - 0.6f) / 0.4f * 120));
        } else if (noiseValue < -0.8) {
            return (int) ((-0.8f - noiseValue) / 0.2f * 80);
        } else {
            return 0;
        }
    }

    public float getBatolithPercentage(float noiseValue) {
        float cutOff = -0.2f;
        noiseValue = noiseValue * noiseValue * noiseValue;
        if (noiseValue < -0.75f) {
            noiseValue = (-0.75f - noiseValue) / 0.25f;
            return ((float) Math.sqrt(noiseValue));
        } else if (noiseValue > cutOff) {
            noiseValue = (noiseValue - cutOff) / 0.2f + 0.05f;
            return (float) Math.sqrt(noiseValue) * 1.3f;
        } else {
            return 0;
        }
    }


    public float getStratumPercentage() {
        return strataCutoff;
    }

    public int getStratumDepth(Random rand, int strataLevel) {
        return minStrata + rand.nextInt(maxStrata - minStrata);
    }

    public boolean batholithIntrudes() {
        return batolithIntrudes;
    }

}

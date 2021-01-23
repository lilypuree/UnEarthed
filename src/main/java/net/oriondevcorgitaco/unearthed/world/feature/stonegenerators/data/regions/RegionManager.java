package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.regions;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.StoneWrapper;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.SimpleState;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.StoneState;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.Type;

import java.util.*;

public class RegionManager {
    private static List<Region> regions = new ArrayList<>();
    private static Map<Biome.Category, Region> specialRegions = Collections.emptyMap();
    private static Map<ResourceLocation, Region> biomeRegion = Collections.emptyMap();
    private static List<BatolithicState> batolithicStates;
    private static List<State> tertiaries = new ArrayList<>();

    public static Region getRegion(Biome biome, float value) {
        if (specialRegions.containsKey(biome.getCategory())) {
            return specialRegions.get(biome.getCategory());
        } else if (biomeRegion.containsKey(biome.getRegistryName())) {
            return biomeRegion.get(biome.getRegistryName());
        } else {
            return regions.get(((int) (value * regions.size())));
        }
    }

    public static State getBatolithState(float qf, float ap) {
        return batolithicStates.stream().min(Comparator.comparing(state -> state.getDistance(qf, ap))).get().state;
    }

    public static State getTertiaryState(float level) {
        return tertiaries.get(((int) (tertiaries.size() * level)));
    }

    public static void addTertiary(StoneWrapper stoneWrapper) {
        tertiaries.add(new StoneState(Type.TERTIARY, stoneWrapper));
    }

    public static void addTertiary(State state){
        tertiaries.add(state);
    }

    static class BatolithicState {
        public State state;
        public float qf;
        public float ap;

        public BatolithicState(StoneWrapper stone, float qf, float ap) {
            this.state = new StoneState(Type.BATOLITH, stone);
            this.qf = qf;
            this.ap = ap;
        }

        public float getDistance(float qf, float ap) {
            return (float) Math.sqrt(qf * this.qf + ap + this.ap);
        }
    }

    static {
        batolithicStates = new ArrayList<>();
        batolithicStates.add(new BatolithicState(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.GRANITE), 0.8f, 0.0f));
        batolithicStates.add(new BatolithicState(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.DIORITE), 0.0f, 0.9f));
        batolithicStates.add(new BatolithicState(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.GRANODIORITE), -0.1f, 0.9f));
        batolithicStates.add(new BatolithicState(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.ANDESITE), -0.5f, 0.0f));

        tertiaries = new ArrayList<>();
        addTertiary(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.MARBLE));
        addTertiary(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.DOLOMITE));
        addTertiary(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.DACITE));
        addTertiary(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.PUMICE));
        addTertiary(StoneWrapper.fromGenerationHelper(BlockGeneratorReference.PILLOW_BASALT));
//        addTertiary(StoneWrapper.getOrCreate(new ResourceLocation(Unearthed.MOD_ID, "weathered_rhyolite")));

        regions.add(Regions.DEFAULT);
        regions.add(Regions.LIMESTONE);
        regions.add(Regions.SEDIMENTARY);
    }
}

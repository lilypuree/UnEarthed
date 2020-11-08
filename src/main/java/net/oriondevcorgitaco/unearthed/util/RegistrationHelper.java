package net.oriondevcorgitaco.unearthed.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.oriondevcorgitaco.unearthed.Unearthed;

import java.util.ArrayList;
import java.util.List;

public class RegistrationHelper {

    public static List<Feature<?>> features = new ArrayList<>();

    public static ConfiguredFeature<?, ?> newConfiguredFeature(String registryName, ConfiguredFeature<?, ?> configuredFeature) {
        if (!WorldGenRegistries.field_243653_e.keySet().contains(new ResourceLocation(Unearthed.MOD_ID, registryName)))
            Registry.register(WorldGenRegistries.field_243653_e, new ResourceLocation(Unearthed.MOD_ID, registryName), configuredFeature);
        return configuredFeature;
    }

    public static <T extends IFeatureConfig, G extends Feature<T>> G registerFeature(String registryName, G feature) {
        if (!Registry.FEATURE.keySet().contains(new ResourceLocation(Unearthed.MOD_ID, registryName)))
            feature.setRegistryName(new ResourceLocation(Unearthed.MOD_ID, registryName));
        features.add(feature);
        return feature;
    }
}

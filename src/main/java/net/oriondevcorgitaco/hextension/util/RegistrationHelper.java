package net.oriondevcorgitaco.hextension.util;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.oriondevcorgitaco.hextension.Hextension;

public class RegistrationHelper {

    public static ConfiguredFeature<?, ?> newConfiguredFeature(String registryName, ConfiguredFeature<?, ?> configuredFeature) {
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(Hextension.MOD_ID, registryName), configuredFeature);
        return configuredFeature;
    }

    public static <T extends FeatureConfig, G extends Feature<T>> G registerFeature(String registryName, G feature) {
        Registry.register(Registry.FEATURE, new Identifier(Hextension.MOD_ID, registryName), feature);
        return feature;
    }
}

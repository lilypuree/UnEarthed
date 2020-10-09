package net.oriondevcorgitaco.unearthed.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.registries.ForgeRegistries;
import net.oriondevcorgitaco.unearthed.Unearthed;

public class RegistrationHelper {

    public static ConfiguredFeature<?, ?> newConfiguredFeature(String registryName, ConfiguredFeature<?, ?> configuredFeature) {
        if (!WorldGenRegistries.field_243653_e.containsKey(new ResourceLocation(Unearthed.MOD_ID, registryName)))
            Registry.register(WorldGenRegistries.field_243653_e, new ResourceLocation(Unearthed.MOD_ID, registryName), configuredFeature);
        return configuredFeature;
    }

    public static <T extends IFeatureConfig, G extends Feature<T>> G registerFeature(String registryName, G feature) {
        if (!ForgeRegistries.FEATURES.containsKey(new ResourceLocation(Unearthed.MOD_ID, registryName)))
            feature.setRegistryName(new ResourceLocation(Unearthed.MOD_ID, registryName));
        return feature;
    }
}

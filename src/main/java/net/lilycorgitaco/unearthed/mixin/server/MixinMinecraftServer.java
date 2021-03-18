package net.lilycorgitaco.unearthed.mixin.server;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.JsonOps;
import net.lilycorgitaco.unearthed.Unearthed;
import net.lilycorgitaco.unearthed.config.UEConfig;
import net.lilycorgitaco.unearthed.interfaces.GenerationSettingsHelper;
import net.lilycorgitaco.unearthed.mixin.access.GenerationSettingsAccess;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.level.storage.LevelStorage;
import net.lilycorgitaco.unearthed.UEFeatures;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow
    @Final
    protected DynamicRegistryManager.Impl registryManager;

    @Inject(at = @At("RETURN"), method = "<init>(Ljava/lang/Thread;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/SaveProperties;Lnet/minecraft/resource/ResourcePackManager;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/resource/ServerResourceManager;Lcom/mojang/authlib/minecraft/MinecraftSessionService;Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/util/UserCache;Lnet/minecraft/server/WorldGenerationProgressListenerFactory;)V")
    private void implementUnearthedStones(Thread thread, DynamicRegistryManager.Impl impl, LevelStorage.Session session, SaveProperties saveProperties, ResourcePackManager resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        if (this.registryManager.getOptional(Registry.BIOME_KEY).isPresent()) {
            for (Biome biome : registryManager.getOptional(Registry.BIOME_KEY).get()) {
                if (biome.getCategory() == Biome.Category.NETHER) {
                    if (!Unearthed.CONFIG.disableNetherGeneration) {
                        removeFeatureFromBiome(biome, GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_GRAVEL_NETHER);
                    }
                } else if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NONE) {
                 if (biome.getCategory() == Biome.Category.EXTREME_HILLS) {
                        addFeatureToBiome(biome, GenerationStep.Feature.VEGETAL_DECORATION, UEFeatures.LICHEN_FEATURE);
                    }
                    if (Unearthed.CONFIG.disableGeneration) {

                    } else {
                        addFeatureToBiome(biome, GenerationStep.Feature.TOP_LAYER_MODIFICATION, UEFeatures.NEW_GENERATOR);
                        removeFeatureFromBiome(biome, GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.ORE_DIRT, ConfiguredFeatures.ORE_GRANITE, ConfiguredFeatures.ORE_DIORITE, ConfiguredFeatures.ORE_ANDESITE);
                    }
                }
            }
        }
    }

    private static void addFeatureToBiome(Biome biome, GenerationStep.Feature feature, ConfiguredFeature<?, ?> configuredFeature) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = ((GenerationSettingsAccess) biome.getGenerationSettings()).getFeatures();
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(feature.ordinal()).add(() -> configuredFeature);
    }

    private static void removeFeatureFromBiome(Biome biome, GenerationStep.Feature feature, ConfiguredFeature<?, ?>... configuredFeatures) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = biome.getGenerationSettings().getFeatures();
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(feature.ordinal()).removeIf(supplier -> {
            return supplier.get().getConfig() instanceof DecoratedFeatureConfig && Arrays.stream(configuredFeatures).anyMatch(configuredFeature -> serializeAndCompareFeature(configuredFeature, supplier.get()));
        });
    }

    private static void ConvertImmutableFeatures(Biome biome) {
        if (((GenerationSettingsAccess) biome.getGenerationSettings()).getFeatures() instanceof ImmutableList) {
            ((GenerationSettingsHelper) biome.getGenerationSettings()).setGenerationSettings(((GenerationSettingsAccess) biome.getGenerationSettings()).getFeatures().stream().map(Lists::newArrayList).collect(Collectors.toList()));
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERAL UTILITIES // from TelepathicGrunt's Repurposed Structures Repo
    // https://github.com/TelepathicGrunt/RepurposedStructures

    /**
     * Will serialize (if possible) both features and check if they are the same feature.
     * If cannot serialize, compare the feature itself to see if it is the same.
     */
    private static boolean serializeAndCompareFeature(ConfiguredFeature<?, ?> configuredFeature1, ConfiguredFeature<?, ?> configuredFeature2) {

        Optional<JsonElement> configuredFeatureJSON1 = ConfiguredFeature.CODEC.encode(configuredFeature1, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();
        Optional<JsonElement> configuredFeatureJSON2 = ConfiguredFeature.CODEC.encode(configuredFeature2, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

        // One of the configuredfeatures cannot be serialized
        if (!configuredFeatureJSON1.isPresent() || !configuredFeatureJSON2.isPresent()) {
            return false;
        }

        // Compare the JSON to see if it's the same ConfiguredFeature in the end.
        return configuredFeatureJSON1.equals(configuredFeatureJSON2);
    }
}

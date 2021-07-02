package net.oriondevcorgitaco.unearthed.mixin.server;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.ResourcePackList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.listener.IChunkStatusListenerFactory;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.storage.IServerConfiguration;
import net.minecraft.world.storage.SaveFormat;
import net.oriondevcorgitaco.unearthed.UEFeatures;
import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
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

//    private static final ConfiguredFeature<?, ?> NATURAL_GENERATOR = RegistrationHelper.newConfiguredFeature("natural_generator", UEFeatures.UNDERGROUND_STONE.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(new NoPlacementConfig())));
//    private static final ConfiguredFeature<?, ?> NATURAL_ICY_GENERATOR = RegistrationHelper.newConfiguredFeature("natural_icy_generator", UEFeatures.ICY_GENERATOR.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(new NoPlacementConfig())));
//    private static final ConfiguredFeature<?, ?> NATURAL_DESERT_GENERATOR = RegistrationHelper.newConfiguredFeature("natural_desert_generator", UEFeatures.UNDERGROUND_SANDSTONE.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(new NoPlacementConfig())));
//    private static final ConfiguredFeature<?, ?> NATURAL_LAYERS_GENERATOR = RegistrationHelper.newConfiguredFeature("natural_layers_generator", UEFeatures.LAYERED_GENERATOR.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(new NoPlacementConfig())));
//    private final ConfiguredFeature<?, ?> MESA_GENERATOR = RegistrationHelper.newConfiguredFeature("true_mesa_generator", UEFeatures.MESA.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(new NoPlacementConfig())));

//    private static final ConfiguredFeature<?, ?> NEW_GENERATOR = RegistrationHelper.newConfiguredFeature("new_generator", UEFeatures.NEW_STONE.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(new NoPlacementConfig())));


    @Shadow
    @Final
    protected DynamicRegistries.Impl registryHolder;

    @Inject(at = @At("RETURN"), method = "<init>(Ljava/lang/Thread;Lnet/minecraft/util/registry/DynamicRegistries$Impl;Lnet/minecraft/world/storage/SaveFormat$LevelSave;Lnet/minecraft/world/storage/IServerConfiguration;Lnet/minecraft/resources/ResourcePackList;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/resources/DataPackRegistries;Lcom/mojang/authlib/minecraft/MinecraftSessionService;Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/server/management/PlayerProfileCache;Lnet/minecraft/world/chunk/listener/IChunkStatusListenerFactory;)V", cancellable = true)
    private void implementUnearthedStones(Thread thread, DynamicRegistries.Impl impl, SaveFormat.LevelSave session, IServerConfiguration saveProperties, ResourcePackList resourcePackManager, Proxy proxy, DataFixer dataFixer, DataPackRegistries serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, PlayerProfileCache userCache, IChunkStatusListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        if (this.registryHolder.registry(Registry.BIOME_REGISTRY).isPresent()) {
            for (Biome biome : registryHolder.registry(Registry.BIOME_REGISTRY).get()) {
                if (biome.getBiomeCategory() == Biome.Category.NETHER) {
                    if (!UnearthedConfig.disableNetherGeneration.get()) {
                        removeFeatureFromBiome(biome, GenerationStage.Decoration.UNDERGROUND_ORES, Features.ORE_GRAVEL_NETHER);
                    }
                } else if (biome.getBiomeCategory() != Biome.Category.THEEND && biome.getBiomeCategory() != Biome.Category.NONE) {
//                    if (useDesertCaves(biome))
//                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, NATURAL_DESERT_GENERATOR);
//                    else if (useIceCaves(biome))
//                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, NATURAL_ICY_GENERATOR);
//                    else if (useTrueMesas(biome))
//                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MESA_GENERATOR);
//                    else
//                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, NATURAL_GENERATOR);
                    if (biome.getBiomeCategory() == Biome.Category.EXTREME_HILLS) {
                        addFeatureToBiome(biome, GenerationStage.Decoration.VEGETAL_DECORATION, UEFeatures.LICHEN_FEATURE);
                    }
                    if (UnearthedConfig.disableGeneration.get()) {

                    } else {
                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, UEFeatures.NEW_GENERATOR);
                        removeFeatureFromBiome(biome, GenerationStage.Decoration.UNDERGROUND_ORES, Features.ORE_DIRT, Features.ORE_GRANITE, Features.ORE_DIORITE, Features.ORE_ANDESITE);
                    }
                }
            }
        }
    }

    private static void addFeatureToBiome(Biome biome, GenerationStage.Decoration feature, ConfiguredFeature<?, ?> configuredFeature) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = biome.getGenerationSettings().features;
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(feature.ordinal()).add(() -> configuredFeature);
    }

    private static void removeFeatureFromBiome(Biome biome, GenerationStage.Decoration feature, ConfiguredFeature<?, ?>... configuredFeatures) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = biome.getGenerationSettings().features;
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(feature.ordinal()).removeIf(supplier -> {
            return supplier.get().config() instanceof DecoratedFeatureConfig && Arrays.stream(configuredFeatures).anyMatch(configuredFeature -> serializeAndCompareFeature(configuredFeature, supplier.get()));
        });
    }

    private static void ConvertImmutableFeatures(Biome biome) {
        if (biome.getGenerationSettings().features instanceof ImmutableList) {
            biome.getGenerationSettings().features = biome.getGenerationSettings().features.stream().map(Lists::newArrayList).collect(Collectors.toList());
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

        Optional<JsonElement> configuredFeatureJSON1 = ConfiguredFeature.DIRECT_CODEC.encode(configuredFeature1, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();
        Optional<JsonElement> configuredFeatureJSON2 = ConfiguredFeature.DIRECT_CODEC.encode(configuredFeature2, JsonOps.INSTANCE, JsonOps.INSTANCE.empty()).get().left();

        // One of the configuredfeatures cannot be serialized
        if (!configuredFeatureJSON1.isPresent() || !configuredFeatureJSON2.isPresent()) {
            return false;
        }

        // Compare the JSON to see if it's the same ConfiguredFeature in the end.
        return configuredFeatureJSON1.equals(configuredFeatureJSON2);
    }

//    private static boolean useTrueMesas(Biome biome) {
//        boolean trueMesas = UnearthedConfig.trueMesas.get();
//        if (trueMesas) {
//            return biome.getCategory() == Biome.Category.MESA;
//        } else
//            return false;
//    }
//
//
//    private static boolean useIceCaves(Biome biome) {
//        boolean icyCaves = UnearthedConfig.icyCaves.get();
//        if (icyCaves) {
//            return biome.getCategory() == Biome.Category.ICY;
//        } else
//            return false;
//    }
//
//    private static boolean useDesertCaves(Biome biome) {
//        boolean desertCaves = UnearthedConfig.desertCaves.get();
//        if (desertCaves) {
//            return biome.getCategory() == Biome.Category.DESERT;
//        } else
//            return false;
//    }
}

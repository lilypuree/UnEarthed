package net.lilycorgitaco.unearthed.mixin.server;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
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
import net.minecraft.world.level.storage.LevelStorage;
import net.lilycorgitaco.unearthed.UEFeatures;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;
import java.util.List;
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
    protected DynamicRegistryManager.Impl registryManager;

    @Inject(at = @At("RETURN"), method = "<init>(Ljava/lang/Thread;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/SaveProperties;Lnet/minecraft/resource/ResourcePackManager;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/resource/ServerResourceManager;Lcom/mojang/authlib/minecraft/MinecraftSessionService;Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/util/UserCache;Lnet/minecraft/server/WorldGenerationProgressListenerFactory;)V", cancellable = true)
    private void implementUnearthedStones(Thread thread, DynamicRegistryManager.Impl impl, LevelStorage.Session session, SaveProperties saveProperties, ResourcePackManager resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        if (this.registryManager.getOptional(Registry.BIOME_KEY).isPresent()) {
            for (Biome biome : registryManager.getOptional(Registry.BIOME_KEY).get()) {
                if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NONE) {
//                    if (useDesertCaves(biome))
//                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, NATURAL_DESERT_GENERATOR);
//                    else if (useIceCaves(biome))
//                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, NATURAL_ICY_GENERATOR);
//                    else if (useTrueMesas(biome))
//                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, MESA_GENERATOR);
//                    else
//                        addFeatureToBiome(biome, GenerationStage.Decoration.TOP_LAYER_MODIFICATION, NATURAL_GENERATOR);
                    addFeatureToBiome(biome, GenerationStep.Feature.TOP_LAYER_MODIFICATION, UEFeatures.NEW_GENERATOR);
                    if (biome.getCategory() == Biome.Category.EXTREME_HILLS) {
                        addFeatureToBiome(biome, GenerationStep.Feature.VEGETAL_DECORATION, UEFeatures.LICHEN_FEATURE);
                    }
                }
            }
        }
    }

    private static void addFeatureToBiome(Biome biome, GenerationStep.Feature feature, ConfiguredFeature<?, ?> configuredFeature) {
        ConvertImmutableFeatures(biome);
        List<List<Supplier<ConfiguredFeature<?, ?>>>> biomeFeatures = biome.getGenerationSettings().features;
        while (biomeFeatures.size() <= feature.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(feature.ordinal()).add(() -> configuredFeature);
    }

    private static void ConvertImmutableFeatures(Biome biome) {
        if (biome.getGenerationSettings().features instanceof ImmutableList) {
            biome.getGenerationSettings().features = biome.getGenerationSettings().features.stream().map(Lists::newArrayList).collect(Collectors.toList());
        }
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

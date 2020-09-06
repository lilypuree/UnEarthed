package net.oriondevcorgitaco.unearthed.mixin.server;


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
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.level.storage.LevelStorage;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators.TrueMesaGenerator;
import net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators.NaturalDesertGenerator;
import net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators.NaturalGenerator;
import net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators.NaturalIcyGenerator;
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

    private final ConfiguredFeature<?, ?> NATURAL_GENERATOR = RegistrationHelper.newConfiguredFeature("natural_generator", NaturalGenerator.UNDERGROUND_STONE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));
    private final ConfiguredFeature<?, ?> NATURAL_ICY_GENERATOR = RegistrationHelper.newConfiguredFeature("natural_icy_generator", NaturalIcyGenerator.UNDERGROUND_STONE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));
    private final ConfiguredFeature<?, ?> NATURAL_DESERT_GENERATOR = RegistrationHelper.newConfiguredFeature("natural_desert_generator", NaturalDesertGenerator.UNDERGROUND_STONE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));

    private final ConfiguredFeature<?, ?> MESA_GENERATOR = RegistrationHelper.newConfiguredFeature("true_mesa_generator", TrueMesaGenerator.MESA.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));


    @Shadow @Final protected DynamicRegistryManager.Impl registryManager;

    @Inject(at = @At("RETURN"), method = "<init>(Ljava/lang/Thread;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/SaveProperties;Lnet/minecraft/resource/ResourcePackManager;Ljava/net/Proxy;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/resource/ServerResourceManager;Lcom/mojang/authlib/minecraft/MinecraftSessionService;Lcom/mojang/authlib/GameProfileRepository;Lnet/minecraft/util/UserCache;Lnet/minecraft/server/WorldGenerationProgressListenerFactory;)V", cancellable = true)
    private void implementUnearthedStones(Thread thread, DynamicRegistryManager.Impl impl, LevelStorage.Session session, SaveProperties saveProperties, ResourcePackManager resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, CallbackInfo ci) {
        if(this.registryManager.getOptional(Registry.BIOME_KEY).isPresent()) {
            for (Biome biome : registryManager.getOptional(Registry.BIOME_KEY).get()) {
                if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NONE) {
                    if (useDesertCaves(biome))
                        addFeatureToBiome(biome, GenerationStep.Feature.TOP_LAYER_MODIFICATION, NATURAL_DESERT_GENERATOR);
                    else if (useIceCaves(biome))
                        addFeatureToBiome(biome, GenerationStep.Feature.TOP_LAYER_MODIFICATION, NATURAL_ICY_GENERATOR);
                    else if (useTrueMesas(biome))
                        addFeatureToBiome(biome, GenerationStep.Feature.TOP_LAYER_MODIFICATION, MESA_GENERATOR);
                    else
                        addFeatureToBiome(biome, GenerationStep.Feature.TOP_LAYER_MODIFICATION, NATURAL_GENERATOR);

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


    private static boolean useTrueMesas(Biome biome) {
        boolean trueMesas = Unearthed.UE_CONFIG.generation.trueMesas;
        if (trueMesas) {
            return biome.getCategory() == Biome.Category.MESA;
        }
        else
            return false;
    }


    private static boolean useIceCaves(Biome biome) {
        boolean icyCaves = Unearthed.UE_CONFIG.generation.icyCaves;
        if (icyCaves) {
            return biome.getCategory() == Biome.Category.ICY;
        }
        else
            return false;
    }

    private static boolean useDesertCaves(Biome biome) {
        boolean desertCaves = Unearthed.UE_CONFIG.generation.desertCaves;
        if (desertCaves) {
            return biome.getCategory() == Biome.Category.DESERT;
        }
        else
            return false;
    }
}

package net.lilycorgitaco.unearthed.mixin;

import net.lilycorgitaco.unearthed.interfaces.GenerationSettingsHelper;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Supplier;

@Mixin(GenerationSettings.class)
public class MixinBiomeGenerationSettings implements GenerationSettingsHelper {


    @Mutable
    @Shadow @Final private List<List<Supplier<ConfiguredFeature<?, ?>>>> features;

    @Override
    public void setGenerationSettings(List<List<Supplier<ConfiguredFeature<?, ?>>>> feature) {
        this.features = feature;
    }
}

package net.lilycorgitaco.unearthed.interfaces;

import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.List;
import java.util.function.Supplier;

public interface GenerationSettingsHelper {

    void setGenerationSettings(List<List<Supplier<ConfiguredFeature<?, ?>>>> feature);
}

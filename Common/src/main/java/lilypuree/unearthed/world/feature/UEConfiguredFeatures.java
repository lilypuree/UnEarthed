package lilypuree.unearthed.world.feature;

import lilypuree.unearthed.core.UENames;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class UEConfiguredFeatures {

    public static ConfiguredFeature<?, ?> STONE_REPLACER = UEFeatures.STONE_REPLACER.configured(NoneFeatureConfiguration.INSTANCE);
    public static PlacedFeature STONE_REPLACER_PLACED = PlacementUtils.register(UENames.STONE_REPLACER.toString(), STONE_REPLACER.placed());

    public static void register() {
        Registry<ConfiguredFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_FEATURE;
        Registry.register(registry, UENames.STONE_REPLACER, STONE_REPLACER);
    }
}

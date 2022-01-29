package lilypuree.unearthed.world.feature;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class UEFeatures {
    public static Feature<NoneFeatureConfiguration> STONE_REPLACER;

    public static void init() {
        STONE_REPLACER = new UEStoneReplacer();
    }
}

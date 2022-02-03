package lilypuree.unearthed.core;

import lilypuree.unearthed.world.feature.StoneReplacerConfiguration;
import lilypuree.unearthed.world.feature.UEStoneReplacer;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class UEFeatures {
    public static Feature<StoneReplacerConfiguration> STONE_REPLACER;

    public static void init() {
        STONE_REPLACER = new UEStoneReplacer();
    }
}

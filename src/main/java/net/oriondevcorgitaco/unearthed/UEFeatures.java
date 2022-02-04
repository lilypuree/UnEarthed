package net.oriondevcorgitaco.unearthed;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.world.feature.LichenConfig;
import net.oriondevcorgitaco.unearthed.world.feature.LichenFeature;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.NewGenerator;

public class UEFeatures {
    public static final Feature<NoFeatureConfig> NEW_STONE = RegistrationHelper.registerFeature("new_generator", new NewGenerator(NoFeatureConfig.CODEC));
    public static final Feature<LichenConfig> LICHEN = RegistrationHelper.registerFeature("lichen", new LichenFeature(LichenConfig.CODEC));

    public static final ConfiguredFeature<?, ?> NEW_GENERATOR = RegistrationHelper.newConfiguredFeature("new_generator", UEFeatures.NEW_STONE.configured(NoFeatureConfig.NONE).decorated(Placement.NOPE.configured(new NoPlacementConfig())));
   public static final  ConfiguredFeature<?,?>  LICHEN_FEATURE =
           RegistrationHelper.newConfiguredFeature("lichen",
                  ((LICHEN.configured(
                          new LichenConfig(10, true, true, true, 0.6F,
                                  ImmutableList.of(Blocks.STONE.defaultBlockState(), Blocks.ANDESITE.defaultBlockState(), Blocks.DIORITE.defaultBlockState(), Blocks.GRANITE.defaultBlockState()))
                  ).squared()).decorated(Placement.RANGE.configured(new TopSolidRangeConfig(60, 20, 150)))).count(FeatureSpread.of(12, 6)));


}

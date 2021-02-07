package net.lilycorgitaco.unearthed;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.lilycorgitaco.unearthed.util.RegistrationHelper;
import net.lilycorgitaco.unearthed.world.feature.LichenConfig;
import net.lilycorgitaco.unearthed.world.feature.LichenFeature;
import net.lilycorgitaco.unearthed.world.feature.stonegenerators.NewGenerator;

public class UEFeatures {
    public static final Feature<DefaultFeatureConfig> NEW_STONE = RegistrationHelper.registerFeature("new_generator", new NewGenerator(DefaultFeatureConfig.CODEC));
    public static final Feature<LichenConfig> LICHEN = RegistrationHelper.registerFeature("lichen", new LichenFeature(LichenConfig.CODEC));

    public static final ConfiguredFeature<?, ?> NEW_GENERATOR = RegistrationHelper.newConfiguredFeature("new_generator", UEFeatures.NEW_STONE.configure(DefaultFeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));
    public static final ConfiguredFeature<?, ?> LICHEN_FEATURE =
            RegistrationHelper.newConfiguredFeature("lichen",
                    ((LICHEN.configure(
                            new LichenConfig(10, true, true, true, 0.6F,
                                    ImmutableList.of(Blocks.STONE.getDefaultState(), Blocks.ANDESITE.getDefaultState(), Blocks.DIORITE.getDefaultState(), Blocks.GRANITE.getDefaultState()))
                    ).spreadHorizontally()).decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(60, 20, 150)))).repeat(UniformIntDistribution.of(12, 6)));


}

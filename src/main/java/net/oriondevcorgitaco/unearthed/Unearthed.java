package net.oriondevcorgitaco.unearthed;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.util.BlockAssetHelper;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.world.feature.StrataGenerator4;
import net.oriondevcorgitaco.unearthed.world.feature.StrataGenerator2;
import net.oriondevcorgitaco.unearthed.world.feature.StrataGenerator3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Unearthed implements ModInitializer {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ConfiguredFeature<?, ?> STRATA_GENERATOR = RegistrationHelper.newConfiguredFeature("strata_generator", StrataGenerator4.UNDERGROUND_STONE.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));
    public static final ConfiguredFeature<?, ?> STRATA_GENERATOR2 = RegistrationHelper.newConfiguredFeature("strata_generator2", StrataGenerator2.UNDERGROUND_STONE2.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));
    public static final ConfiguredFeature<?, ?> STRATA_GENERATOR3 = RegistrationHelper.newConfiguredFeature("strata_generator3", StrataGenerator3.UNDERGROUND_STONE3.configure(FeatureConfig.DEFAULT).decorate(Decorator.NOPE.configure(new NopeDecoratorConfig())));


    @Override
    public void onInitialize() {
        BlockGeneratorReference.init();
//        FeatureAdder.strataGeneratorForAllBiomes();
        BlockAssetHelper.jsonPrinter();
        BlockAssetHelper.printBlockIDs();
    }
}

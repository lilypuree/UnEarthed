package net.oriondevcorgitaco.unearthed;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.world.feature.layergenerators.LayeredGenerator;
import net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators.NaturalDesertGenerator;
import net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators.NaturalGenerator;
import net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators.NaturalIcyGenerator;
import net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators.TrueMesaGenerator;

public class UEFeatures {
    public static final Feature<NoFeatureConfig> UNDERGROUND_SANDSTONE = RegistrationHelper.registerFeature("natural_desert_generator", new NaturalDesertGenerator(NoFeatureConfig.field_236558_a_));
    public static final Feature<NoFeatureConfig> UNDERGROUND_STONE = RegistrationHelper.registerFeature("natural_generator", new NaturalGenerator(NoFeatureConfig.field_236558_a_));
    public static final Feature<NoFeatureConfig> LAYERED_GENERATOR = RegistrationHelper.registerFeature("layered_generator", new LayeredGenerator(NoFeatureConfig.field_236558_a_));
    public static final Feature<NoFeatureConfig> ICY_GENERATOR = RegistrationHelper.registerFeature("natural_icy_generator", new NaturalIcyGenerator(NoFeatureConfig.field_236558_a_));
    public static final Feature<NoFeatureConfig> MESA = RegistrationHelper.registerFeature("true_mesa_generator", new TrueMesaGenerator(NoFeatureConfig.field_236558_a_));


}

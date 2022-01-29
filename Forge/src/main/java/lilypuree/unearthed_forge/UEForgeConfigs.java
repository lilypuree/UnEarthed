package lilypuree.unearthed_forge;

import lilypuree.unearthed.CommonConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class UEForgeConfigs implements CommonConfig {

    public static ForgeConfigSpec COMMON_CONFIG;
    private ForgeConfigSpec.BooleanValue disablePassive;
    private ForgeConfigSpec.ConfigValue<String> blackListedMobs;
    private ForgeConfigSpec.DoubleValue chance;

    public UEForgeConfigs() {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
//        COMMON_BUILDER.comment("Unearthed Configs");

//        COMMON_CONFIG = COMMON_BUILDER.build();
    }

}

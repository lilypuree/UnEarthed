package lilypuree.unearthed;

import net.minecraftforge.common.ForgeConfigSpec;

public class UEForgeConfigs implements CommonConfig {

    public static ForgeConfigSpec COMMON_CONFIG;
    private ForgeConfigSpec.BooleanValue enableDebug;
    private ForgeConfigSpec.BooleanValue disableReplacement;
    private ForgeConfigSpec.BooleanValue disableCobbleReplacement;
    private ForgeConfigSpec.BooleanValue disableDirtReplacement;
    private ForgeConfigSpec.BooleanValue disableOreReplacement;

    public UEForgeConfigs() {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("Unearthed Configs");

        COMMON_BUILDER.push("generation settings");
        enableDebug = COMMON_BUILDER.define("enable_debug", false);
        disableReplacement = COMMON_BUILDER.define("disable_all", false);
        disableCobbleReplacement = COMMON_BUILDER.define("disable_cobble", false);
        disableDirtReplacement = COMMON_BUILDER.define("disable_dirt", false);
        disableOreReplacement = COMMON_BUILDER.define("disable_ore", false);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @Override
    public boolean enableDebug() {
        return enableDebug.get();
    }

    @Override
    public boolean disableReplacement() {
        return disableReplacement.get();
    }

    @Override
    public boolean disableCobbleReplacement() {
        return disableCobbleReplacement.get();
    }

    @Override
    public boolean disableDirtReplacement() {
        return disableDirtReplacement.get();
    }

    @Override
    public boolean disableOreReplacement() {
        return disableOreReplacement.get();
    }
}

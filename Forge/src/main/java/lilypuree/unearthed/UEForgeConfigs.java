package lilypuree.unearthed;

import net.minecraftforge.common.ForgeConfigSpec;

public class UEForgeConfigs implements CommonConfig {

    public static ForgeConfigSpec COMMON_CONFIG;
    private ForgeConfigSpec.BooleanValue enableRegolithToDirt;

    public UEForgeConfigs() {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("Unearthed Configs\n");

        COMMON_BUILDER.push("general settings");
        enableRegolithToDirt = COMMON_BUILDER.define("enable_regolith_to_dirt_recipe", false);
        COMMON_BUILDER.pop();
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @Override
    public boolean enableRegolithToDirt() {
        return enableRegolithToDirt.get();
    }
}

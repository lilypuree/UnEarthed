package net.lilycorgitaco.unearthed.config;

import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

public class UEConfig {





    public static class GeneratorSettings {
        @Comment(value = "\nUse Stone Block Tag? Could have performance impact!\nDefault: false")
        public boolean stoneTag = true;

        @Comment(value = "\nReplace cobblestone? Replaces dungeon cobble stone for example.\nDefault: true")
        public boolean replaceCobble = true;
    }
}

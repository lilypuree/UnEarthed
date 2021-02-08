package net.lilycorgitaco.unearthed.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "unearthed")
public class UEConfig implements ConfigData {
    @Comment(value = "\nUse Stone Block Tag? Could have performance impact!\nDefault: false")
    public boolean replaceableTag = true;

    @Comment(value = "\nReplace cobblestone? Replaces dungeon cobble stone for example.\nDefault: true")
    public boolean replaceCobble = true;

    @Comment(value = "\nAlways replace dirt regardless of biome?\nDefault: false")
    public boolean alwaysReplaceDirt = false;

    @Comment(value = "\nReplace cobblestone? Replaces dungeon cobble stone for example.\nDefault: true")
    public boolean debug = false;
}

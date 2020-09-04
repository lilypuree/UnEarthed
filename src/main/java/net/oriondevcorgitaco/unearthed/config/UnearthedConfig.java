package net.oriondevcorgitaco.unearthed.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;

@Config(name = "unearthed-common")
public class UnearthedConfig implements ConfigData {


    @ConfigEntry.Category("generation")
    @ConfigEntry.Gui.TransitiveObject
    public GeneratorConfig generation = new GeneratorConfig();
}

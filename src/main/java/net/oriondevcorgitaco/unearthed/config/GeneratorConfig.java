package net.oriondevcorgitaco.unearthed.config;

import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@Config(name = "generation")
public class GeneratorConfig {
    @ConfigEntry.Gui.CollapsibleObject
    public NaturalGeneratorV1 naturalgeneratorv1 = new NaturalGeneratorV1();

    public static class NaturalGeneratorV1 {
        @ConfigEntry.Gui.PrefixText
        @Comment(value = "\nStone vein scale factor. Multiplies the vein size. \nDefault: 4.5")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 10000)
        public double stoneVeinScaleFactor = 4.5;

        @ConfigEntry.Gui.PrefixText
        @Comment(value = "\nPerturb Strength \nDefault: 95")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
        public double perturbAmpStrength = 95;

        @ConfigEntry.Gui.PrefixText
        @Comment(value = "\nPerturb Octaves. Bigger values = slower world gen\nDefault: 8")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
        public int perturbAmpOctaves = 5;

        @ConfigEntry.Gui.PrefixText
        @Comment(value = "\nSmall Perturb Strength \nDefault: 8")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
        public double smallPerturbAmpStrength = 8;

        @ConfigEntry.Gui.PrefixText
        @Comment(value = "\nPerturb Octaves. Bigger values = slower world gen\nDefault: 8")
        @ConfigEntry.BoundedDiscrete(min = 1, max = 10000)
        public int smallPerturbAmpOctaves = 3;

        @ConfigEntry.Gui.PrefixText
        @Comment(value = "\nList of blocks to use in world generation.")
        public String blocksForGeneration = "unearthed:gray_basalt,unearthed:gabbro,unearthed:pumice,unearthed:kimberlite,unearthed:rhyolite,unearthed:slate,unearthed:quartzite,unearthed:phyllite,unearthed:schist,unearthed:limestone,unearthed:lignite,unearthed:conglomerate";


        @ConfigEntry.Gui.PrefixText
        @Comment(value = "\nList of blocks to use in icy biome world generation.")
        public String iceBlocksForGeneration = "unearthed:marble,minecraft:packed_ice";

        @ConfigEntry.Gui.PrefixText
        @Comment(value = "\nList of blocks to use in desert biome world generation.")
        public String desertBlocksForGeneration = "unearthed:siltstone,unearthed:mudstone,minecraft:smooth_sandstone,minecraft:sandstone";


    }


    @ConfigEntry.Gui.PrefixText
    @Comment(value = "\nUse Stone Block Tag? Could have performance impact!\nDefault: false")
    public boolean stoneTag = true;

    @ConfigEntry.Gui.PrefixText
    @Comment(value = "\nAre vanilla Mesas/badlands layered down to bedrock? Ores will still be kept.\nDefault: true")
    public boolean trueMesas = true;

    @ConfigEntry.Gui.PrefixText
    @Comment(value = "\nDo deserts use different blocks for cave generation?\nDefault: true")
    public boolean desertCaves = true;

    @ConfigEntry.Gui.PrefixText
    @Comment(value = "\nDo icy/frozen biomes use different blocks for cave generation?\nDefault: true")
    public boolean icyCaves = true;
}

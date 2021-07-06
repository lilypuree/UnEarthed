package net.oriondevcorgitaco.unearthed.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.oriondevcorgitaco.unearthed.Unearthed;

import java.nio.file.Path;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnearthedConfig {

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    //    public static ForgeConfigSpec.DoubleValue stoneVeinScaleFactor;
//    public static ForgeConfigSpec.DoubleValue perturbAmpStrength;
//    public static ForgeConfigSpec.IntValue perturbAmpOctaves;
//    public static ForgeConfigSpec.DoubleValue smallPerturbAmpStrength;
//    public static ForgeConfigSpec.IntValue smallPerturbAmpOctaves;
//
//    public static ForgeConfigSpec.ConfigValue<String> blocksForGeneration;
//    public static ForgeConfigSpec.ConfigValue<String> iceBlocksForGeneration;
//    public static ForgeConfigSpec.ConfigValue<String> desertBlocksForGeneration;
    public static ForgeConfigSpec.BooleanValue replaceableTag;
    public static ForgeConfigSpec.BooleanValue replaceCobble;
    public static ForgeConfigSpec.BooleanValue replaceOres;
    public static ForgeConfigSpec.EnumValue<DirtReplacement> dirtReplacement;
    public static ForgeConfigSpec.BooleanValue disableGeneration;
    public static ForgeConfigSpec.BooleanValue disableNetherGeneration;
    public static ForgeConfigSpec.BooleanValue debug;
//    public static ForgeConfigSpec.BooleanValue trueMesas;
//    public static ForgeConfigSpec.BooleanValue desertCaves;
//    public static ForgeConfigSpec.BooleanValue icyCaves;

    static {
        COMMON_BUILDER.comment("Generation Setting").push("Generation_Settings").push("Natural_Generator");
//        stoneVeinScaleFactor = COMMON_BUILDER.comment("Stone vein scale factor. Multiplies the vein size. \nDefault: 4.5").defineInRange("stoneVeinScaleFactor", 4.5, 0, 100000);
//        perturbAmpStrength = COMMON_BUILDER.comment("Perturb Strength \nDefault: 95").defineInRange("perturbAmpStrength", 95.0, 0, 100000);
//        perturbAmpOctaves = COMMON_BUILDER.comment("Perturb Octaves. Bigger values = slower world gen\nDefault: 8").defineInRange("perturbAmpOctaves", 5, 0, 100000);
//        smallPerturbAmpStrength = COMMON_BUILDER.comment("Small Perturb Strength \nDefault: 8").defineInRange("smallPerturbAmpStrength", 8.0, 0, 100000);
//        smallPerturbAmpOctaves = COMMON_BUILDER.comment("Small Perturb Octaves. Bigger values = slower world gen\nDefault: 3").defineInRange("smallPerturbAmpOctaves", 3, 0, 100000);
//        blocksForGeneration = COMMON_BUILDER.comment("List of blocks to use in world generation.\nDefault: \"unearthed:gray_basalt,unearthed:gabbro,unearthed:pumice,unearthed:kimberlite,unearthed:rhyolite,unearthed:slate,unearthed:quartzite,unearthed:phyllite,unearthed:schist,unearthed:limestone,unearthed:lignite,unearthed:conglomerate,minecraft:stone\"").define("blocksForGeneration", "unearthed:gray_basalt,unearthed:gabbro,unearthed:pumice,unearthed:kimberlite,unearthed:rhyolite,unearthed:slate,unearthed:quartzite,unearthed:phyllite,unearthed:schist,unearthed:limestone,unearthed:lignite,unearthed:conglomerate,minecraft:stone");
//        iceBlocksForGeneration = COMMON_BUILDER.comment("List of blocks to use in icy biome world generation.\nDefault: \"unearthed:marble,minecraft:packed_ice\"").define("iceBlocksForGeneration", "unearthed:marble,minecraft:packed_ice");
//        desertBlocksForGeneration = COMMON_BUILDER.comment("List of blocks to use in desert biome world generation.\nDefault: \"unearthed:siltstone,unearthed:mudstone,minecraft:smooth_sandstone,minecraft:sandstone\"").define("desertBlocksForGeneration", "unearthed:siltstone,unearthed:mudstone,minecraft:smooth_sandstone,minecraft:sandstone");
        COMMON_BUILDER.pop();
        replaceableTag = COMMON_BUILDER.comment("Use Replaceable Block Tag? Could have performance impact!\nDefault: true").define("replaceableTag", true);
        replaceCobble = COMMON_BUILDER.comment("Replace cobblestone? Replaces dungeon cobble stone for example.\nDefault: true").define("replaceCobble", true);
        replaceOres = COMMON_BUILDER.comment("Replace ores? \nDefault: true").define("replaceOres", true);
        dirtReplacement = COMMON_BUILDER.comment("Replacement behavior of dirt.\nNONE: does not replace dirt\nHILL: only replace on hilly biomes(default)\nALL: replace all dirt regardless of biome")
                .defineEnum("dirtReplacement", DirtReplacement.HILLS);

        disableGeneration = COMMON_BUILDER.comment("Disable rock replacement\nDefault: false").define("disableGeneration", false);
        disableNetherGeneration = COMMON_BUILDER.comment("Disable nether generation\nDefault: false").define("disableNetherGeneration", false);
        debug = COMMON_BUILDER.comment("Enables debug mode. Takes more time to generate.\nDefault : false").define("debug", false);
        //        trueMesas = COMMON_BUILDER.comment("Are vanilla Mesas/badlands layered down to bedrock? Ores will still be kept.\nDefault: true").define("trueMesas", false);
//        desertCaves = COMMON_BUILDER.comment("Do deserts use different blocks for cave generation?\nDefault: true").define("desertCaves", false);
//        icyCaves = COMMON_BUILDER.comment("Do icy/frozen biomes use different blocks for cave generation?\nDefault: true").define("icyCaves", false);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec config, Path path) {
        Unearthed.LOGGER.info("Loading config: " + path);
        CommentedFileConfig file = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {

    }

    public static boolean isReplaceableStone(BlockState block) {
        boolean stoneTag = UnearthedConfig.replaceableTag.get();
        if (stoneTag)
            return block.is(BlockTags.BASE_STONE_OVERWORLD);
        else
            return block == Blocks.STONE.defaultBlockState();
    }


    public static enum DirtReplacement {
        NONE, HILLS, ALL
    }
}
package net.oriondevcorgitaco.unearthed;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UnearthedConfig {

    private static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue replaceableTag;
    public static ForgeConfigSpec.BooleanValue replaceCobble;
    public static ForgeConfigSpec.BooleanValue replaceOres;
    public static ForgeConfigSpec.EnumValue<DirtReplacement> dirtReplacement;
    public static ForgeConfigSpec.BooleanValue disableGeneration;
    public static ForgeConfigSpec.BooleanValue disableNetherGeneration;
    public static ForgeConfigSpec.BooleanValue debug;


    static {
        COMMON_BUILDER.comment("Generation Setting").push("Generation_Settings").push("Natural_Generator");
       COMMON_BUILDER.pop();
        replaceableTag = COMMON_BUILDER.comment("Use Replaceable Block Tag? Could have performance impact!\nDefault: true").define("replaceableTag", true);
        replaceCobble = COMMON_BUILDER.comment("Replace cobblestone? Replaces dungeon cobble stone for example.\nDefault: true").define("replaceCobble", true);
        replaceOres = COMMON_BUILDER.comment("Replace ores? \nDefault: true").define("replaceOres", true);
        dirtReplacement = COMMON_BUILDER.comment("Replacement behavior of dirt.\nNONE: does not replace dirt\nHILL: only replace on hilly biomes(default)\nALL: replace all dirt regardless of biome")
                .defineEnum("dirtReplacement", DirtReplacement.HILLS);

        disableGeneration = COMMON_BUILDER.comment("Disable rock replacement\nDefault: false").define("disableGeneration", false);
        disableNetherGeneration = COMMON_BUILDER.comment("Disable nether generation\nDefault: false").define("disableNetherGeneration", false);
        debug = COMMON_BUILDER.comment("Enables debug mode. Takes more time to generate.\nDefault : false").define("debug", false);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static boolean isReplaceableStone(BlockState block) {
        boolean stoneTag = UnearthedConfig.replaceableTag.get();
        if (stoneTag)
            return block.is(BlockTags.BASE_STONE_OVERWORLD);
        else
            return block == Blocks.STONE.defaultBlockState();
    }


    public enum DirtReplacement {
        NONE, HILLS, ALL
    }
}
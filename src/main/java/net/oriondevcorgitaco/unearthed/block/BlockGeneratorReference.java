package net.oriondevcorgitaco.unearthed.block;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.oriondevcorgitaco.unearthed.Unearthed;

import java.util.ArrayList;
import java.util.List;

public class BlockGeneratorReference {
    public static List<BlockGeneratorHelper> ROCK_TYPES = new ArrayList<>();
    //igneous
    public static final BlockGeneratorHelper GRAY_BASALT = new BlockGeneratorHelper("gray_basalt", true, false, true, true, true);
    public static final BlockGeneratorHelper GABBRO = new BlockGeneratorHelper("gabbro", true, true, true, true, true);
    public static final BlockGeneratorHelper PUMICE = new BlockGeneratorHelper("pumice", false, false, false, false, false);
    public static final BlockGeneratorHelper KIMBERLITE = new BlockGeneratorHelper("kimberlite", true, true, true, true, true);
    public static final BlockGeneratorHelper RHYOLITE = new BlockGeneratorHelper("rhyolite", true, false, true, true, true);
//    public static final BlockGeneratorHelper PERIDOTITE = new BlockGeneratorHelper("peridotite", true, true, true, true, true);
//    public static final BlockGeneratorHelper DOLOMITE = new BlockGeneratorHelper("dolomite", true, true, true, true, true);
//    public static final BlockGeneratorHelper BLUE_GRANITE = new BlockGeneratorHelper("blue_granite", true, false, false, false, false);

    //metamorphic
    public static final BlockGeneratorHelper SLATE = new BlockGeneratorHelper("slate", true, true, true, true, true);
    public static final BlockGeneratorHelper MARBLE = new BlockGeneratorHelper("marble", true, true, true, true, true);
    public static final BlockGeneratorHelper QUARTZITE = new BlockGeneratorHelper("quartzite", true, false, true, true, true);
    public static final BlockGeneratorHelper PHYLLITE = new BlockGeneratorHelper("phyllite", true, false, true, true, true);
    public static final BlockGeneratorHelper SCHIST = new BlockGeneratorHelper("schist", true, false, true, true, true);
//    public static final BlockGeneratorHelper HORNFEL = new BlockGeneratorHelper("hornfel", true, true, true, true, true);
//    public static final BlockGeneratorHelper GNIESS = new BlockGeneratorHelper("gniess", true, true, true, true, true);
//    public static final BlockGeneratorHelper SKARN = new BlockGeneratorHelper("skarn", true, true, true, true, true);

    //sedimentary
    public static final BlockGeneratorHelper LIMESTONE = new BlockGeneratorHelper("limestone", true, true, true, true, true);
    public static final BlockGeneratorHelper LIGNITE = new BlockGeneratorHelper("lignite", false, false, false, false, false);
    public static final BlockGeneratorHelper SILTSTONE = new BlockGeneratorHelper("siltstone", true, false, true, true, true);
    public static final BlockGeneratorHelper MUDSTONE = new BlockGeneratorHelper("mudstone", true, false, true, true, true);
    public static final BlockGeneratorHelper CONGLOMERATE = new BlockGeneratorHelper("conglomerate", true, false, true, true, true);
//    public static final BlockGeneratorHelper CHERT = new BlockGeneratorHelper("chert", true, true, true, true, true);
//    public static final BlockGeneratorHelper CHALK = new BlockGeneratorHelper("chalk", true, true, true, true, true);
//    public static final BlockGeneratorHelper SHALE = new BlockGeneratorHelper("shale", true, true, true, true, true);
    public static void init() {
    }


    public static final Tag<Block> IRON_ORE_TAG = blockTagRegister("iron_ores");
    public static final Tag<Block> DIAMOND_ORE_TAG = blockTagRegister("diamond_ores");
    public static final Tag<Block> LAPIS_ORE_TAG = blockTagRegister("lapis_ores");
    public static final Tag<Block> REDSTONE_ORE_TAG = blockTagRegister("redstone_ores");
    public static final Tag<Block> COAL_ORE_TAG = blockTagRegister("coal_ores");
    public static final Tag<Block> EMERALD_ORE_TAG = blockTagRegister("emerald_ores");


    public static Tag<Block> blockTagRegister(String id) {
        return TagRegistry.block(new Identifier(Unearthed.MOD_ID, id));

    }
}
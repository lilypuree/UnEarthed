package net.oriondevcorgitaco.unearthed.block;

import java.util.ArrayList;
import java.util.List;

public class BlockGeneratorReference {
    public static List<BlockGeneratorHelper> ROCK_TYPES = new ArrayList<>();
    //igneous
    public static final BlockGeneratorHelper GRAY_BASALT = new BlockGeneratorHelper("gray_basalt", true, true, true, true, true);
    public static final BlockGeneratorHelper GABBRO = new BlockGeneratorHelper("gabbro", true, true, true, true, true);
    public static final BlockGeneratorHelper PUMICE = new BlockGeneratorHelper("pumice", false, false, false, false, false);
    public static final BlockGeneratorHelper KIMBERLITE = new BlockGeneratorHelper("kimberlite", true, true, true, true, true);
    public static final BlockGeneratorHelper RHYOLITE = new BlockGeneratorHelper("rhyolite", true, true, true, true, true);
    public static final BlockGeneratorHelper PERIDOTITE = new BlockGeneratorHelper("peridotite", true, true, true, true, true);
    public static final BlockGeneratorHelper DOLOMITE = new BlockGeneratorHelper("dolomite", true, true, true, true, true);
    public static final BlockGeneratorHelper BLUE_GRANITE = new BlockGeneratorHelper("blue_granite", true, false, false, false, false);

    //metamorphic
    public static final BlockGeneratorHelper SLATE = new BlockGeneratorHelper("slate", true, true, true, true, true);
    public static final BlockGeneratorHelper MARBLE = new BlockGeneratorHelper("marble", true, true, true, true, true);
    public static final BlockGeneratorHelper QUARTZITE = new BlockGeneratorHelper("quartzite", true, true, true, true, true);
    public static final BlockGeneratorHelper PHYLLITE = new BlockGeneratorHelper("phyllite", true, true, true, true, true);
    public static final BlockGeneratorHelper SCHIST = new BlockGeneratorHelper("schist", true, true, true, true, true);
    public static final BlockGeneratorHelper HORNFEL = new BlockGeneratorHelper("hornfel", true, true, true, true, true);
    public static final BlockGeneratorHelper GNIESS = new BlockGeneratorHelper("gniess", true, true, true, true, true);
    public static final BlockGeneratorHelper SKARN = new BlockGeneratorHelper("skarn", true, true, true, true, true);

    //sedimentary
    public static final BlockGeneratorHelper LIMESTONE = new BlockGeneratorHelper("limestone", true, true, true, true, true);
    public static final BlockGeneratorHelper LIGNITE = new BlockGeneratorHelper("lignite", false, false, false, false, false);
    public static final BlockGeneratorHelper SILTSTONE = new BlockGeneratorHelper("siltstone", true, true, true, true, true);
    public static final BlockGeneratorHelper MUDSTONE = new BlockGeneratorHelper("mudstone", true, true, true, true, true);
    public static final BlockGeneratorHelper CONGLOMERATE = new BlockGeneratorHelper("conglomerate", true, true, true, true, true);
    public static final BlockGeneratorHelper CHERT = new BlockGeneratorHelper("chert", true, true, true, true, true);
    public static final BlockGeneratorHelper CHALK = new BlockGeneratorHelper("chalk", true, true, true, true, true);
    public static final BlockGeneratorHelper SHALE = new BlockGeneratorHelper("shale", true, true, true, true, true);
    public static void init() {
    }
}
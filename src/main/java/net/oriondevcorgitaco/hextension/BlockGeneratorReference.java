package net.oriondevcorgitaco.hextension;

import java.util.ArrayList;
import java.util.List;

public class BlockGeneratorReference {
    public static List<BlockGeneratorHelper> ROCK_TYPES = new ArrayList<>();
    public static final BlockGeneratorHelper LIMESTONE = new BlockGeneratorHelper("limestone", true, true, true, true, true);
    public static final BlockGeneratorHelper SLATE = new BlockGeneratorHelper("slate", true, true, true, true, true);
    public static final BlockGeneratorHelper MARBLE = new BlockGeneratorHelper("marble", true, true, true, true, true);
    public static final BlockGeneratorHelper PUMICE = new BlockGeneratorHelper("pumice", false, false, false, false, false);
    public static final BlockGeneratorHelper KIMBERLITE = new BlockGeneratorHelper("kimberlite", false, false, false, false, false);

    public static void init() {
    }
}
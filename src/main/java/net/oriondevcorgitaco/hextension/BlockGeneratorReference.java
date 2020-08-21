package net.oriondevcorgitaco.hextension;

import java.util.ArrayList;
import java.util.List;

public class BlockGeneratorReference {
    public static List<BlockGeneratorHelper> ROCK_TYPES = new ArrayList<>();
    public static final BlockGeneratorHelper LIMESTONE = new BlockGeneratorHelper("limestone", true, true, true, true, true);
//    public static final BlockGeneratorHelper SOAPSTONE = new BlockGeneratorHelper("soapstone", true, true, true, true, true);


    public static void init() {

    }
}
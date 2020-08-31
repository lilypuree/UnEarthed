package net.oriondevcorgitaco.unearthed.util;

import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;

public class BlockAssetHelper {
    public static final String[] TYPES = {"_stairs", "_slab", "_button"/*, "_wall"*/};

    public static void jsonPrinter() {
        Unearthed.LOGGER.info("Starting Json Printer...");
        BlockGeneratorHelper.blockIdList.forEach(id -> {
            String blockPath = "\"block." + Unearthed.MOD_ID + ".";
            String itemPath = "\"item." + Unearthed.MOD_ID + ".";

            Unearthed.LOGGER.info(blockPath + id + "\" : \"" + capitalizeWord(id.replace("_", " ")) + "\",");

            for (String type : TYPES) {
                String idForType = id + type + "\"";
                Unearthed.LOGGER.info(blockPath + idForType + " : \"" + capitalizeWord(idForType.replace("_", " ")) + ",");
            }

            for (String type : TYPES) {
                String idForType = id + type + "\"";
                Unearthed.LOGGER.info(itemPath + idForType + " : \"" + capitalizeWord(idForType.replace("_", " ")) + ",");
            }
        });
    }

    public static void printBlockIDs() {
        BlockGeneratorHelper.blockIdList.forEach(id -> {
            Unearthed.LOGGER.info("unearthed:" + id);
        });
    }


    private static String capitalizeWord(String str){
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder();
        for(String w : words){
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterfirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }
}

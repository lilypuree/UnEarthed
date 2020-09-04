package net.oriondevcorgitaco.unearthed.util;

import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;

public class BlockAssetHelper {
    public static final String[] BASE_TYPES = {"_stairs", "_slab", "_button", "_pressure_plate", "_wall"};
    public static final String[] VANILLA_ORE_TYPES = {"_coal_ore" ,"_iron_ore", "_gold_ore", "_lapis_ore", "_redstone_ore", "_diamond_ore", "_emerald_ore"};
    public static final String[] BYG_ORE_TYPES = {"_ametrine_ore" ,"_pendorite_ore"};

    public static void jsonPrinter() {
        Unearthed.LOGGER.info("Starting Json Printer...");

        //Base blocks
        BlockGeneratorHelper.baseBlockIdList.forEach(id -> {
            String blockPath = "\"block." + Unearthed.MOD_ID + ".";
            String itemPath = "\"item." + Unearthed.MOD_ID + ".";

            Unearthed.LOGGER.info(blockPath + id + "\" : \"" + capitalizeWord(id.replace("_", " ")) + "\",");

            for (String type : BASE_TYPES) {
                String idForType = id + type + "\"";
                Unearthed.LOGGER.info(blockPath + idForType + " : \"" + capitalizeWord(idForType.replace("_", " ")) + ",");
            }

            for (String type : BASE_TYPES) {
                String idForType = id + type + "\"";
                Unearthed.LOGGER.info(itemPath + idForType + " : \"" + capitalizeWord(idForType.replace("_", " ")) + ",");
            }
        });
        //Cobble blocks
        BlockGeneratorHelper.cobbleBlockIdList.forEach(id -> {
            String blockPath = "\"block." + Unearthed.MOD_ID + ".";
            String itemPath = "\"item." + Unearthed.MOD_ID + ".";

            Unearthed.LOGGER.info(blockPath + id + "\" : \"" + capitalizeWord(id.replace("_", " ")) + "\",");

            for (String type : BASE_TYPES) {
                String idForType = id + type + "\"";
                Unearthed.LOGGER.info(blockPath + idForType + " : \"" + capitalizeWord(idForType.replace("_", " ")) + ",");
            }

            for (String type : BASE_TYPES) {
                String idForType = id + type + "\"";
                Unearthed.LOGGER.info(itemPath + idForType + " : \"" + capitalizeWord(idForType.replace("_", " ")) + ",");
            }
        });

        //Ore blocks
        BlockGeneratorHelper.baseBlockIdList.forEach(id -> {
            String blockPath = "\"block." + Unearthed.MOD_ID + ".";
            String itemPath = "\"item." + Unearthed.MOD_ID + ".";

            Unearthed.LOGGER.info(blockPath + id + "\" : \"" + capitalizeWord(id.replace("_", " ")) + "\",");

            for (String type : VANILLA_ORE_TYPES) {
                String idForType = id + type + "\"";
                Unearthed.LOGGER.info(blockPath + idForType + " : \"" + capitalizeWord(idForType.replace("_", " ")) + ",");
            }

            for (String type : VANILLA_ORE_TYPES) {
                String idForType = id + type + "\"";
                Unearthed.LOGGER.info(itemPath + idForType + " : \"" + capitalizeWord(idForType.replace("_", " ")) + ",");
            }
        });
    }

    public static void printBlockIDs() {
        BlockGeneratorHelper.baseBlockIdList.forEach(id -> {
            Unearthed.LOGGER.info("unearthed:" + id);
        });

        BlockGeneratorHelper.cobbleBlockIdList.forEach(id -> {
            Unearthed.LOGGER.info("unearthed:" + id);
        });

        BlockGeneratorHelper.oreBlockIdList.forEach(id -> {
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

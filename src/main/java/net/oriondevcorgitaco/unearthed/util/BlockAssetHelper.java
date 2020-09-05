package net.oriondevcorgitaco.unearthed.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockAssetHelper {
    public static final String[] BASE_TYPES = {"_stairs", "_slab", "_button", "_pressure_plate", "_wall"};
    public static final String[] VANILLA_ORE_TYPES = {"_coal_ore", "_iron_ore", "_gold_ore", "_lapis_ore", "_redstone_ore", "_diamond_ore", "_emerald_ore"};
    public static final String[] BYG_ORE_TYPES = {"_ametrine_ore", "_pendorite_ore"};

    public static void createUnearthedLangFile() {
        List<String> unearthedBlockItemIDs = new ArrayList<>();
        for (Block block : Registry.BLOCK) {
            String blockID = Registry.BLOCK.getId(block).toString();
            if (blockID.contains(Unearthed.MOD_ID))
                unearthedBlockItemIDs.add(blockID.replace(Unearthed.MOD_ID + ":", ""));
            createLangFile("D:\\Coding\\Hextension-Fabric 1.16.X\\src\\main\\resources\\assets\\unearthed\\lang\\en_us.json", unearthedBlockItemIDs, true, true);
        }
    }

    public static void createUnearthedCraftingRecipes() {
        List<String> unearthedBlockItemIDs = new ArrayList<>();
        for (Block block : Registry.BLOCK) {
            String blockID = Registry.BLOCK.getId(block).toString();
            if (blockID.contains(Unearthed.MOD_ID))
                if (blockID.contains("_slab") || blockID.contains("_button") || blockID.contains("_pressure_plate") || blockID.contains("_wall"))
                unearthedBlockItemIDs.add(blockID.replace(Unearthed.MOD_ID + ":", ""));
            createRecipeFiles("D:\\Coding\\Hextension-Fabric 1.16.X\\src\\main\\resources\\data\\unearthed\\recipes", unearthedBlockItemIDs);
        }
    }


    public static void createLangFile(String langPath, List<String> idList, boolean isBlockList, boolean isItemList) {

        try {
            FileWriter fileWriter = new FileWriter(langPath);
            Gson prettyPrinting = new GsonBuilder().setPrettyPrinting().create();

            fileWriter.write(StringEscapeUtils.unescapeJava("{\n\""));
            for (int idx = 0; idx < idList.size(); idx++) {
                String id = idList.get(idx);

                String blockPath = "\"block." + Unearthed.MOD_ID + "." + id;
                String itemPath = "\"item." + Unearthed.MOD_ID + "." + id;
                if (isBlockList) {
                    String blockLangLine = blockPath + "\":\"" + capitalizeWord(id.replace("_", " ")) + "\",\n";
                    if (idx == idList.size() - 1)
                        blockLangLine = itemPath + "\":\"" + capitalizeWord(id.replace("_", " ")) + "\"\n";
                    String blockTranslation = prettyPrinting.toJson(new JsonPrimitive(blockLangLine));
                    blockTranslation = StringEscapeUtils.unescapeJava(blockTranslation);
                    blockTranslation = StringEscapeUtils.unescapeJava(blockTranslation.replace("\"\"", ""));

                    try {
                        fileWriter.write(blockTranslation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (isItemList) {
                    String itemLangLine = itemPath + "\":\"" + capitalizeWord(id.replace("_", " ")) + "\",\n";
                    if (idx == idList.size() - 1)
                        itemLangLine = itemPath + "\":\"" + capitalizeWord(id.replace("_", " ")) + "\"\n";
                    String itemTranslation = prettyPrinting.toJson(new JsonPrimitive(itemLangLine));
                    itemTranslation = StringEscapeUtils.unescapeJava(itemTranslation);


                    itemTranslation = StringEscapeUtils.unescapeJava(itemTranslation.replace("\"\"", ""));
                    try {
                        fileWriter.write(itemTranslation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            String endBracket = StringEscapeUtils.unescapeJava("}");

            fileWriter.write(endBracket);

            //close the writer
            fileWriter.close();

        } catch (IOException e) {
            Unearthed.LOGGER.error("Lang file failed to generate.");
        }
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


    private static String capitalizeWord(String str) {
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord = new StringBuilder();
        for (String w : words) {
            String first = w.substring(0, 1);
            String afterfirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterfirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }

    public static void createRecipeFiles(String path, List<String> idList) {
        idList.forEach(id -> {
            try {
                FileWriter fileWriter = new FileWriter(path + "\\" + id + ".json");
                Gson prettyPrinting = new GsonBuilder().setPrettyPrinting().create();

                if (id.contains("_wall")) {
                    String recipe = "{\n" +
                        "  \"type\": \"minecraft:crafting_shaped\"\n" +
                        "  \"pattern\": [\n" +
                        "    \"###\",\n" +
                        "    \"###\"\n" +
                        "  ],\n" +
                        "  \"key\": {\n" +
                        "    \"#\": {\n" +
                        "      \"item\": \"" + Unearthed.MOD_ID + ":" + id.replace("_wall", "") + "\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"result\": {\n" +
                        "    \"item\": \"" + Unearthed.MOD_ID + ":" + id + "\"\n" +
                        "    \"count\": 6\n" +
                        "  }\n" +
                        "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                }
                else if (id.contains("_button")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:crafting_shapeless\",\n" +
                            "  \"group\": \"stone_button\",\n" +
                            "  \"ingredients\": [\n" +
                            "    {\n" +
                            "      \"item\": \"" + Unearthed.MOD_ID + ":" + id.replace("_button", "") + "\"\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"result\": {\n" +
                            "    \"item\": \"" + Unearthed.MOD_ID + ":" + id + "\"\n" +
                            "  }\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                }
                else if (id.contains("_pressure_plate")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:crafting_shaped\",\n" +
                            "  \"pattern\": [\n" +
                            "    \"##\"\n" +
                            "  ],\n" +
                            "  \"key\": {\n" +
                            "    \"#\": {\n" +
                            "      \"item\": \"" + Unearthed.MOD_ID + ":" + id.replace("_pressure_plate", "") + "\"\n" +
                            "    }\n" +
                            "  },\n" +
                            "  \"result\": {\n" +
                            "    \"item\": \"" + Unearthed.MOD_ID + ":" + id + "\",\n" +
                            "  }\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                }
                else if (id.contains("_slab")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:stonecutting\",\n" +
                            "  \"ingredient\": {\n" +
                            "    \"item\": \"" + Unearthed.MOD_ID + ":" + id.replace("_slab", "") + "\"\n" +
                            "  },\n" +
                            "  \"result\": \"" + Unearthed.MOD_ID + ":" + id + "\",\n" +
                            "  \"count\": 1\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                }

                //close the writer
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        });

    }
}

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
        List<String> unearthedSlabItemIDs = new ArrayList<>();
        for (Block block : Registry.BLOCK) {
            String blockID = Registry.BLOCK.getId(block).toString();
            if (blockID.contains(Unearthed.MOD_ID)) {
                if (blockID.contains("_slab") || blockID.contains("_button") || blockID.contains("_pressure_plate") || blockID.contains("_wall"))
                    unearthedBlockItemIDs.add(blockID.replace("unearthed:", ""));
                createRecipeFiles("D:\\Coding\\Hextension-Fabric 1.16.X\\src\\main\\resources\\data\\unearthed\\recipes", unearthedBlockItemIDs);

                if (blockID.contains("_slab"))
                    unearthedSlabItemIDs.add(blockID.replace("unearthed:", ""));
                createStoneCutterRecipes("D:\\Coding\\Hextension-Fabric 1.16.X\\src\\main\\resources\\data\\unearthed\\recipes", unearthedSlabItemIDs);

            }
        }
    }

    public static void createUnearthedOreLootTableRecipes() {
        List<String> unearthedBlockItemIDs = new ArrayList<>();
        for (Block block : Registry.BLOCK) {
            String blockID = Registry.BLOCK.getId(block).toString();
            if (blockID.contains(Unearthed.MOD_ID))
                if (blockID.contains("diamond") || blockID.contains("redstone") || blockID.contains("lapis") || blockID.contains("emerald") || blockID.contains("coal"))
                    unearthedBlockItemIDs.add(blockID.replace("unearthed:", ""));
            createOreLootTables("D:\\Coding\\Hextension-Fabric 1.16.X\\src\\main\\resources\\data\\unearthed\\loot_tables\\blocks", unearthedBlockItemIDs);
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
                            "  \"type\": \"minecraft:crafting_shaped\",\n" +
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
                            "    \"item\": \"" + Unearthed.MOD_ID + ":" + id + "\",\n" +
                            "    \"count\": 6\n" +
                            "  }\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                } else if (id.contains("_button")) {
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
                } else if (id.contains("_pressure_plate")) {
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
                            "    \"item\": \"" + Unearthed.MOD_ID + ":" + id + "\"\n" +
                            "  }\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                } else if (id.contains("_slab")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:crafting_shaped\",\n" +
                            "  \"pattern\": [\n" +
                            "    \"###\"\n" +
                            "  ],\n" +
                            "  \"key\": {\n" +
                            "    \"#\": {\n" +
                            "    \"item\": \"" + Unearthed.MOD_ID + ":" + id.replace("_slab", "") + "\"\n" +
                            "    }\n" +
                            "  },\n" +
                            "  \"result\": {\n" +
                            "    \"item\": \"" + Unearthed.MOD_ID + ":" + id + "\",\n" +
                            "    \"count\": 6\n" +
                            "  }\n" +
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


    public static void createStoneCutterRecipes(String path, List<String> idList) {
        idList.forEach(id -> {
            try {
                FileWriter fileWriter = new FileWriter(path + "\\" + id + "_from_" + id.replace("slab", "") + "stonecutting.json");
                Gson prettyPrinting = new GsonBuilder().setPrettyPrinting().create();
                if (id.contains("_slab")) {
                    String stoneCutterRecipe = "{\n" +
                            "  \"type\": \"minecraft:stonecutting\",\n" +
                            "  \"ingredient\": {\n" +
                            "    \"item\": \"" + Unearthed.MOD_ID + ":" + id.replace("_slab", "") + "\"\n" +
                            "  },\n" +
                            "  \"result\": \"" + Unearthed.MOD_ID + ":" + id + "\",\n" +
                            "  \"count\": 1\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(stoneCutterRecipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                }

                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        });

    }


    public static void createOreLootTables(String path, List<String> idList) {
        idList.forEach(id -> {
            try {
                FileWriter fileWriter = new FileWriter(path + "\\" + id + ".json");
                Gson prettyPrinting = new GsonBuilder().setPrettyPrinting().create();

                if (id.contains("_lapis_ore")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:block\",\n" +
                            "  \"pools\": [\n" +
                            "    {\n" +
                            "      \"rolls\": 1,\n" +
                            "      \"entries\": [\n" +
                            "        {\n" +
                            "          \"type\": \"minecraft:alternatives\",\n" +
                            "          \"children\": [\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"conditions\": [\n" +
                            "                {\n" +
                            "                  \"condition\": \"minecraft:match_tool\",\n" +
                            "                  \"predicate\": {\n" +
                            "                    \"enchantments\": [\n" +
                            "                      {\n" +
                            "                        \"enchantment\": \"minecraft:silk_touch\",\n" +
                            "                        \"levels\": {\n" +
                            "                          \"min\": 1\n" +
                            "                        }\n" +
                            "                      }\n" +
                            "                    ]\n" +
                            "                  }\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"" + Unearthed.MOD_ID + ":" + id + "\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"functions\": [\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:set_count\",\n" +
                            "                  \"count\": {\n" +
                            "                    \"min\": 4.0,\n" +
                            "                    \"max\": 9.0,\n" +
                            "                    \"type\": \"minecraft:uniform\"\n" +
                            "                  }\n" +
                            "                },\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:apply_bonus\",\n" +
                            "                  \"enchantment\": \"minecraft:fortune\",\n" +
                            "                  \"formula\": \"minecraft:ore_drops\"\n" +
                            "                },\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:explosion_decay\"\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"minecraft:lapis_lazuli\"\n" +
                            "            }\n" +
                            "          ]\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                } else if (id.contains("_diamond_ore")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:block\",\n" +
                            "  \"pools\": [\n" +
                            "    {\n" +
                            "      \"rolls\": 1,\n" +
                            "      \"entries\": [\n" +
                            "        {\n" +
                            "          \"type\": \"minecraft:alternatives\",\n" +
                            "          \"children\": [\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"conditions\": [\n" +
                            "                {\n" +
                            "                  \"condition\": \"minecraft:match_tool\",\n" +
                            "                  \"predicate\": {\n" +
                            "                    \"enchantments\": [\n" +
                            "                      {\n" +
                            "                        \"enchantment\": \"minecraft:silk_touch\",\n" +
                            "                        \"levels\": {\n" +
                            "                          \"min\": 1\n" +
                            "                        }\n" +
                            "                      }\n" +
                            "                    ]\n" +
                            "                  }\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"" + Unearthed.MOD_ID + ":" + id + "\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"functions\": [\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:apply_bonus\",\n" +
                            "                  \"enchantment\": \"minecraft:fortune\",\n" +
                            "                  \"formula\": \"minecraft:ore_drops\"\n" +
                            "                },\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:explosion_decay\"\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"minecraft:diamond\"\n" +
                            "            }\n" +
                            "          ]\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                } else if (id.contains("_redstone_ore")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:block\",\n" +
                            "  \"pools\": [\n" +
                            "    {\n" +
                            "      \"rolls\": 1,\n" +
                            "      \"entries\": [\n" +
                            "        {\n" +
                            "          \"type\": \"minecraft:alternatives\",\n" +
                            "          \"children\": [\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"conditions\": [\n" +
                            "                {\n" +
                            "                  \"condition\": \"minecraft:match_tool\",\n" +
                            "                  \"predicate\": {\n" +
                            "                    \"enchantments\": [\n" +
                            "                      {\n" +
                            "                        \"enchantment\": \"minecraft:silk_touch\",\n" +
                            "                        \"levels\": {\n" +
                            "                          \"min\": 1\n" +
                            "                        }\n" +
                            "                      }\n" +
                            "                    ]\n" +
                            "                  }\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"" + Unearthed.MOD_ID + ":" + id + "\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"functions\": [\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:set_count\",\n" +
                            "                  \"count\": {\n" +
                            "                    \"min\": 4.0,\n" +
                            "                    \"max\": 5.0,\n" +
                            "                    \"type\": \"minecraft:uniform\"\n" +
                            "                  }\n" +
                            "                },\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:apply_bonus\",\n" +
                            "                  \"enchantment\": \"minecraft:fortune\",\n" +
                            "                  \"formula\": \"minecraft:uniform_bonus_count\",\n" +
                            "                  \"parameters\": {\n" +
                            "                    \"bonusMultiplier\": 1\n" +
                            "                  }\n" +
                            "                },\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:explosion_decay\"\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"minecraft:redstone\"\n" +
                            "            }\n" +
                            "          ]\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                } else if (id.contains("_coal_ore")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:block\",\n" +
                            "  \"pools\": [\n" +
                            "    {\n" +
                            "      \"rolls\": 1,\n" +
                            "      \"entries\": [\n" +
                            "        {\n" +
                            "          \"type\": \"minecraft:alternatives\",\n" +
                            "          \"children\": [\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"conditions\": [\n" +
                            "                {\n" +
                            "                  \"condition\": \"minecraft:match_tool\",\n" +
                            "                  \"predicate\": {\n" +
                            "                    \"enchantments\": [\n" +
                            "                      {\n" +
                            "                        \"enchantment\": \"minecraft:silk_touch\",\n" +
                            "                        \"levels\": {\n" +
                            "                          \"min\": 1\n" +
                            "                        }\n" +
                            "                      }\n" +
                            "                    ]\n" +
                            "                  }\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"" + Unearthed.MOD_ID + ":" + id + "\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"functions\": [\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:apply_bonus\",\n" +
                            "                  \"enchantment\": \"minecraft:fortune\",\n" +
                            "                  \"formula\": \"minecraft:ore_drops\"\n" +
                            "                },\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:explosion_decay\"\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"minecraft:coal\"\n" +
                            "            }\n" +
                            "          ]\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}";
                    String string = prettyPrinting.toJson(new JsonPrimitive(recipe));
                    string = StringEscapeUtils.unescapeJava(string);
                    fileWriter.write(string);
                } else if (id.contains("_emerald_ore")) {
                    String recipe = "{\n" +
                            "  \"type\": \"minecraft:block\",\n" +
                            "  \"pools\": [\n" +
                            "    {\n" +
                            "      \"rolls\": 1,\n" +
                            "      \"entries\": [\n" +
                            "        {\n" +
                            "          \"type\": \"minecraft:alternatives\",\n" +
                            "          \"children\": [\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"conditions\": [\n" +
                            "                {\n" +
                            "                  \"condition\": \"minecraft:match_tool\",\n" +
                            "                  \"predicate\": {\n" +
                            "                    \"enchantments\": [\n" +
                            "                      {\n" +
                            "                        \"enchantment\": \"minecraft:silk_touch\",\n" +
                            "                        \"levels\": {\n" +
                            "                          \"min\": 1\n" +
                            "                        }\n" +
                            "                      }\n" +
                            "                    ]\n" +
                            "                  }\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"" + Unearthed.MOD_ID + ":" + id + "\"\n" +
                            "            },\n" +
                            "            {\n" +
                            "              \"type\": \"minecraft:item\",\n" +
                            "              \"functions\": [\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:apply_bonus\",\n" +
                            "                  \"enchantment\": \"minecraft:fortune\",\n" +
                            "                  \"formula\": \"minecraft:ore_drops\"\n" +
                            "                },\n" +
                            "                {\n" +
                            "                  \"function\": \"minecraft:explosion_decay\"\n" +
                            "                }\n" +
                            "              ],\n" +
                            "              \"name\": \"minecraft:emerald\"\n" +
                            "            }\n" +
                            "          ]\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
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

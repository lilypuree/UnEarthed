package net.oriondevcorgitaco.unearthed;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.ConfigBlockReader;
import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Unearthed implements ModInitializer {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();
    public static UnearthedConfig UE_CONFIG;

    String path = "D:\\Coding\\Recipe Jsons";


    @Override
    public void onInitialize() {
        AutoConfig.register(UnearthedConfig.class, JanksonConfigSerializer::new);
        UE_CONFIG = AutoConfig.getConfigHolder(UnearthedConfig.class).getConfig();
        BlockGeneratorReference.init();
        configReader();
        createTagLists();

//        for (String id : BlockGeneratorHelper.baseBlockIdList)
//            BlockDataHelperCleanedUp.generateAllStoneRecipes(path, MOD_ID, id);
//
//        for (String id : BlockGeneratorHelper.cobbleBlockIdList)
//            BlockDataHelperCleanedUp.generateAllStoneRecipes(path, MOD_ID, id);

//        List<String> idList = new ArrayList<>();
//        for (Block block : Registry.BLOCK) {
//            String blockID = Registry.BLOCK.getId(block).toString();
//
//            if (blockID.contains("unearthed"))
//                if (blockID.contains("diamond"))
//                    idList.add(blockID.replace(MOD_ID + ":", ""));
//
//            BlockDataHelperCleanedUp.createOreRecipe(path, MOD_ID, idList, BlockDataHelperCleanedUp.OreType.DIAMOND, "minecraft:iron_ingot");

//        }
    }

    public static void createTagLists() {
        BlockGeneratorHelper.stairsList.forEach(LOGGER::info);
        BlockGeneratorHelper.slabList.forEach(LOGGER::info);
        BlockGeneratorHelper.buttonList.forEach(LOGGER::info);
        BlockGeneratorHelper.wallIDList.forEach(LOGGER::info);
    }

    public static void configReader() {
        String blockRegistries = UE_CONFIG.generation.naturalgeneratorv1.blocksForGeneration;
        String removeSpaces = blockRegistries.trim().toLowerCase().replace(" ", "");
        String[] blockRegistryList = removeSpaces.split(",");

        for (String s : blockRegistryList) {
            ConfigBlockReader.blocksFromConfig.add(new ConfigBlockReader(s));
        }

        if (ConfigBlockReader.blocksFromConfig.size() == 0)
            ConfigBlockReader.blocksFromConfig.add(new ConfigBlockReader("minecraft:stone"));

        String iceBlockRegistries = UE_CONFIG.generation.naturalgeneratorv1.iceBlocksForGeneration;
        String removeIcySpaces = iceBlockRegistries.trim().toLowerCase().replace(" ", "");
        String[] iceBlockRegistryList = removeIcySpaces.split(",");

        for (String s : iceBlockRegistryList) {
            ConfigBlockReader.iceBlocksFromConfig.add(new ConfigBlockReader(s));
        }

        if (ConfigBlockReader.iceBlocksFromConfig.size() == 0)
            ConfigBlockReader.iceBlocksFromConfig.add(new ConfigBlockReader("minecraft:packed_ice"));

        String desertBlockRegistries = UE_CONFIG.generation.naturalgeneratorv1.desertBlocksForGeneration;
        String removeDesertSpaces = desertBlockRegistries.trim().toLowerCase().replace(" ", "");
        String[] desertBlockRegistryList = removeDesertSpaces.split(",");

        for (String s : desertBlockRegistryList) {
            ConfigBlockReader.desertBlocksFromConfig.add(new ConfigBlockReader(s));
        }

        if (ConfigBlockReader.desertBlocksFromConfig.size() == 0)
            ConfigBlockReader.desertBlocksFromConfig.add(new ConfigBlockReader("minecraft:smooth_sandstone"));

    }
}

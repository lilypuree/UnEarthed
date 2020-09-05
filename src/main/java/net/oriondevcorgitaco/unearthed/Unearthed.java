package net.oriondevcorgitaco.unearthed;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.ConfigBlockReader;
import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
import net.oriondevcorgitaco.unearthed.util.BlockDataHelperCleanedUp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

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

        for (String id : BlockGeneratorHelper.baseBlockIdList)
            BlockDataHelperCleanedUp.generateAllStoneRecipes(path, MOD_ID, id);

        for (String id : BlockGeneratorHelper.cobbleBlockIdList)
            BlockDataHelperCleanedUp.generateAllStoneRecipes(path, MOD_ID, id);

        List<String> idList = new ArrayList<>();
        for (Block block : Registry.BLOCK) {
            String blockID = Registry.BLOCK.getId(block).toString();

            if (blockID.contains("unearthed"))
                if (blockID.contains("diamond"))
                    idList.add(blockID.replace(MOD_ID + ":", ""));

            BlockDataHelperCleanedUp.createOreRecipe(path, MOD_ID, idList, BlockDataHelperCleanedUp.OreType.DIAMOND, "minecraft:iron_ingot");

        }
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
    }
}

package net.oriondevcorgitaco.unearthed;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.ConfigBlockReader;
import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
import net.oriondevcorgitaco.unearthed.util.BlockAssetHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Unearthed implements ModInitializer {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();
    public static UnearthedConfig UE_CONFIG;

    @Override
    public void onInitialize() {
        AutoConfig.register(UnearthedConfig.class, JanksonConfigSerializer::new);
        UE_CONFIG = AutoConfig.getConfigHolder(UnearthedConfig.class).getConfig();

        BlockGeneratorReference.init();
        String blockRegistries = UE_CONFIG.generation.naturalgeneratorv1.blocksForGeneration;
        String removeSpaces = blockRegistries.trim().toLowerCase().replace(" ", "");
        String[] blockRegistryList = removeSpaces.split(",");

        for (String s : blockRegistryList) {
             ConfigBlockReader.blocksFromConfig.add(new ConfigBlockReader(s));
        }

        if (ConfigBlockReader.blocksFromConfig.size() == 0)
            ConfigBlockReader.blocksFromConfig.add(new ConfigBlockReader("minecraft:stone"));

//        FeatureAdder.strataGeneratorForAllBiomes();
        BlockAssetHelper.jsonPrinter();
        BlockAssetHelper.printBlockIDs();
    }
}

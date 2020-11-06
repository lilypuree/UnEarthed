package net.oriondevcorgitaco.unearthed;


import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.ConfigBlockReader;
import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Unearthed.MOD_ID)
public class Unearthed {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();


    public Unearthed() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ueCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnearthedConfig.COMMON_CONFIG);
    }

    public void ueCommonSetup(FMLCommonSetupEvent event) {
        configReader();
    }
    public static void createTagLists() {
        BlockGeneratorHelper.stairsList.forEach(LOGGER::info);
        BlockGeneratorHelper.slabList.forEach(LOGGER::info);
        BlockGeneratorHelper.buttonList.forEach(LOGGER::info);
        BlockGeneratorHelper.wallIDList.forEach(LOGGER::info);
    }

    public static void configReader() {
        String blockRegistries = UnearthedConfig.blocksForGeneration.get();
        String removeSpaces = blockRegistries.trim().toLowerCase().replace(" ", "");
        String[] blockRegistryList = removeSpaces.split(",");

        for (String s : blockRegistryList) {
            ConfigBlockReader.blocksFromConfig.add(new ConfigBlockReader(s));
        }

        if (ConfigBlockReader.blocksFromConfig.size() == 0)
            ConfigBlockReader.blocksFromConfig.add(new ConfigBlockReader("minecraft:stone"));

        String iceBlockRegistries = UnearthedConfig.iceBlocksForGeneration.get();
        String removeIcySpaces = iceBlockRegistries.trim().toLowerCase().replace(" ", "");
        String[] iceBlockRegistryList = removeIcySpaces.split(",");

        for (String s : iceBlockRegistryList) {
            ConfigBlockReader.iceBlocksFromConfig.add(new ConfigBlockReader(s));
        }

        if (ConfigBlockReader.iceBlocksFromConfig.size() == 0)
            ConfigBlockReader.iceBlocksFromConfig.add(new ConfigBlockReader("minecraft:packed_ice"));

        String desertBlockRegistries = UnearthedConfig.desertBlocksForGeneration.get();
        String removeDesertSpaces = desertBlockRegistries.trim().toLowerCase().replace(" ", "");
        String[] desertBlockRegistryList = removeDesertSpaces.split(",");

        for (String s : desertBlockRegistryList) {
            ConfigBlockReader.desertBlocksFromConfig.add(new ConfigBlockReader(s));
        }

        if (ConfigBlockReader.desertBlocksFromConfig.size() == 0)
            ConfigBlockReader.desertBlocksFromConfig.add(new ConfigBlockReader("minecraft:smooth_sandstone"));

    }
}

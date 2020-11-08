package net.oriondevcorgitaco.unearthed;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
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

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class BYGRegistries {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            LOGGER.debug("UE: Registering blocks...");
            BlockGeneratorReference.init();
            BlockGeneratorHelper.blockList.forEach(block -> event.getRegistry().register(block));
            LOGGER.info("UE: Blocks registered!");
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            LOGGER.debug("UE: Registering items...");
            BlockGeneratorHelper.itemList.forEach(item -> event.getRegistry().register(item));
            LOGGER.info("UE: Items registered!");
        }
    }
}

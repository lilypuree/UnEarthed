package net.oriondevcorgitaco.unearthed;


import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.ConfigBlockReader;
import net.oriondevcorgitaco.unearthed.block.LichenBlock;
import net.oriondevcorgitaco.unearthed.block.PuddleBlock;
import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import net.oriondevcorgitaco.unearthed.core.UETags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Unearthed.MOD_ID)
public class Unearthed {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();


    public Unearthed() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ueCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnearthedConfig.COMMON_CONFIG);
    }

    public void ueCommonSetup(FMLCommonSetupEvent event) {
        BlockGeneratorReference.init();
        UETags.init();
//        configReader();
    }
//    public static void createTagLists() {
//        BlockGeneratorHelperOld.stairsList.forEach(LOGGER::info);
//        BlockGeneratorHelperOld.slabList.forEach(LOGGER::info);
//        BlockGeneratorHelperOld.buttonList.forEach(LOGGER::info);
//        BlockGeneratorHelperOld.wallIDList.forEach(LOGGER::info);
//    }

//    public static void configReader() {
//        String blockRegistries = UnearthedConfig.blocksForGeneration.get();
//        String removeSpaces = blockRegistries.trim().toLowerCase().replace(" ", "");
//        String[] blockRegistryList = removeSpaces.split(",");
//
//        for (String s : blockRegistryList) {
//            ConfigBlockReader.blocksFromConfig.add(new ConfigBlockReader(s));
//        }
//
//        if (ConfigBlockReader.blocksFromConfig.size() == 0)
//            ConfigBlockReader.blocksFromConfig.add(new ConfigBlockReader("minecraft:stone"));
//
//        String iceBlockRegistries = UnearthedConfig.iceBlocksForGeneration.get();
//        String removeIcySpaces = iceBlockRegistries.trim().toLowerCase().replace(" ", "");
//        String[] iceBlockRegistryList = removeIcySpaces.split(",");
//
//        for (String s : iceBlockRegistryList) {
//            ConfigBlockReader.iceBlocksFromConfig.add(new ConfigBlockReader(s));
//        }
//
//        if (ConfigBlockReader.iceBlocksFromConfig.size() == 0)
//            ConfigBlockReader.iceBlocksFromConfig.add(new ConfigBlockReader("minecraft:packed_ice"));
//
//        String desertBlockRegistries = UnearthedConfig.desertBlocksForGeneration.get();
//        String removeDesertSpaces = desertBlockRegistries.trim().toLowerCase().replace(" ", "");
//        String[] desertBlockRegistryList = removeDesertSpaces.split(",");
//
//        for (String s : desertBlockRegistryList) {
//            ConfigBlockReader.desertBlocksFromConfig.add(new ConfigBlockReader(s));
//        }
//
//        if (ConfigBlockReader.desertBlocksFromConfig.size() == 0)
//            ConfigBlockReader.desertBlocksFromConfig.add(new ConfigBlockReader("minecraft:smooth_sandstone"));
//
//    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class BYGRegistries {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            LOGGER.debug("UE: Registering blocks...");
            BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry -> event.getRegistry().register(entry.createBlock(type).setRegistryName(entry.getId()))));

            SoundType WATER = new SoundType(1.0F, 1.0F, SoundEvents.BLOCK_WET_GRASS_BREAK, SoundEvents.ENTITY_GENERIC_SPLASH, SoundEvents.BLOCK_WET_GRASS_PLACE, SoundEvents.BLOCK_WET_GRASS_HIT, SoundEvents.ENTITY_GENERIC_SPLASH);

            event.getRegistry().registerAll(
                    UEBlocks.PUDDLE = new PuddleBlock(AbstractBlock.Properties.create(Material.WATER).notSolid().tickRandomly().slipperiness(0.98f).zeroHardnessAndResistance().sound(WATER)).setRegistryName("puddle"),
                    UEBlocks.LICHEN = new LichenBlock(AbstractBlock.Properties.create(Material.PLANTS).notSolid().tickRandomly().hardnessAndResistance(0.2f).sound(SoundType.PLANT)).setRegistryName("lichen"),
                    UEBlocks.LIGNITE_BRIQUETTES = new Block(AbstractBlock.Properties.from(Blocks.COAL_BLOCK)).setRegistryName("lignite_briquettes")
            );
            LOGGER.info("UE: Blocks registered!");
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            LOGGER.debug("UE: Registering items...");
            Item.Properties properties = new Item.Properties().group(UNEARTHED_TAB);
            BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry ->
            {
                if (entry.getId().equals("lignite")) {
                    event.getRegistry().register(new BlockItem(entry.getBlock(), properties) {
                        @Override
                        public int getBurnTime(ItemStack itemStack) {
                            return 200;
                        }
                    }.setRegistryName(entry.getId()));
                } else {
                    event.getRegistry().register(new BlockItem(entry.getBlock(), properties).setRegistryName(entry.getId()));
                }
            }));
            event.getRegistry().registerAll(
                    UEItems.IRON_ORE = new Item(properties).setRegistryName("iron_ore"),
                    UEItems.GOLD_ORE = new Item(properties).setRegistryName("gold_ore"),
                    UEItems.PUDDLE = new BlockItem(UEBlocks.PUDDLE, new Item.Properties().group(UNEARTHED_TAB).maxStackSize(1)).setRegistryName("puddle"),
                    UEItems.LICHEN = new BlockItem(UEBlocks.LICHEN, properties).setRegistryName("lichen"),
                    UEItems.LIGNITE_BRIQUETTES = new BlockItem(UEBlocks.LIGNITE_BRIQUETTES, properties) {
                        @Override
                        public int getBurnTime(ItemStack itemStack) {
                            return 2000;
                        }
                    }.setRegistryName("lignite_briquettes")
            );
            LOGGER.info("UE: Items registered!");
        }
    }

    public static ItemGroup UNEARTHED_TAB = new ItemGroup(Unearthed.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, "kimberlite_diamond_ore")));
        }
    };
}

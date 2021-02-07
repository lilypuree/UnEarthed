package net.lilycorgitaco.unearthed;


import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.block.LichenBlock;
import net.lilycorgitaco.unearthed.block.PuddleBlock;
import net.lilycorgitaco.unearthed.config.UnearthedConfig;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import net.lilycorgitaco.unearthed.core.UEItems;
import net.lilycorgitaco.unearthed.core.UETags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Unearthed {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();


    public Unearthed() {
//        UETextureStitcher.setupFolders();
//        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientConstruction::run);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ueCommonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnearthedConfig.COMMON_CONFIG);
    }

    public void ueCommonSetup(FMLCommonSetupEvent event) {
        BlockGeneratorReference.init();
        UETags.init();
//        configReader();
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class BYGRegistries {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            LOGGER.debug("UE: Registering blocks...");
            BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry -> event.getRegistry().register(entry.createBlock(type).setRegistryName(entry.getId()))));

            BlockSoundGroup WATER = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.BLOCK_WET_GRASS_BREAK, SoundEvents.ENTITY_GENERIC_SPLASH, SoundEvents.BLOCK_WET_GRASS_PLACE, SoundEvents.BLOCK_WET_GRASS_HIT, SoundEvents.ENTITY_GENERIC_SPLASH);

            event.getRegistry().registerAll(
                    UEBlocks.PYROXENE = new GravelBlock(AbstractBlock.Settings.of(Material.AGGREGATE, MaterialColor.BLACK).strength(0.6F).sounds(BlockSoundGroup.GRAVEL)).setRegistryName("pyroxene"),
                    UEBlocks.PUDDLE = new PuddleBlock(AbstractBlock.Settings.of(Material.WATER).nonOpaque().ticksRandomly().slipperiness(0.98f).breakInstantly().sounds(WATER)).setRegistryName("puddle"),
                    UEBlocks.LICHEN = new LichenBlock(AbstractBlock.Settings.of(Material.PLANT).nonOpaque().ticksRandomly().strength(0.2f).sounds(BlockSoundGroup.GRASS)).setRegistryName("lichen"),
                    UEBlocks.LIGNITE_BRIQUETTES = new Block(AbstractBlock.Settings.copy(Blocks.COAL_BLOCK)).setRegistryName("lignite_briquettes")
            );
            LOGGER.info("UE: Blocks registered!");
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            LOGGER.debug("UE: Registering items...");
            Item.Settings properties = new Item.Settings().group(UNEARTHED_TAB);
            BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry ->
            {
//                if (entry.getId().equals("lignite")) {
//                    event.getRegistry().register(new BlockItem(entry.getBlock(), properties) {
//                        @Override
//                        public int getBurnTime(ItemStack itemStack) {
//                            return 200;
//                        }
//                    }.setRegistryName(entry.getId()));
//                } else {
                    event.getRegistry().register(new BlockItem(entry.getBlock(), properties).setRegistryName(entry.getId()));
//                }
            }));
            event.getRegistry().registerAll(
                    UEItems.PYROXENE = new BlockItem(UEBlocks.PYROXENE, properties).setRegistryName("pyroxene"),
                    UEItems.IRON_ORE = new Item(properties).setRegistryName("iron_ore"),
                    UEItems.GOLD_ORE = new Item(properties).setRegistryName("gold_ore"),
                    UEItems.PUDDLE = new BlockItem(UEBlocks.PUDDLE, new Item.Settings().group(UNEARTHED_TAB).maxCount(1)).setRegistryName("puddle"),
                    UEItems.LICHEN = new BlockItem(UEBlocks.LICHEN, properties).setRegistryName("lichen"),
//                    UEItems.LIGNITE_BRIQUETTES = new BlockItem(UEBlocks.LIGNITE_BRIQUETTES, properties) {
//                        @Override
//                        public int getBurnTime(ItemStack itemStack) {
//                            return 2000;
//                        }
//                    }.setRegistryName("lignite_briquettes")
                    UEItems.LIGNITE_BRIQUETTES = new BlockItem(UEBlocks.LIGNITE_BRIQUETTES, properties).setRegistryName("lignite_briquettes")
            );
            LOGGER.info("UE: Items registered!");
        }
    }

    public static ItemGroup UNEARTHED_TAB = new ItemGroup(Unearthed.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, "kimberlite_diamond_ore")));
        }
    };
}

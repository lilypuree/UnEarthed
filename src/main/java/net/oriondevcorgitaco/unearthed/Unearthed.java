package net.oriondevcorgitaco.unearthed;


import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootConditionType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.ConfigBlockReader;
import net.oriondevcorgitaco.unearthed.block.LichenBlock;
import net.oriondevcorgitaco.unearthed.block.PuddleBlock;
import net.oriondevcorgitaco.unearthed.client.ClientConstruction;
import net.oriondevcorgitaco.unearthed.client.UETextureStitcher;
import net.oriondevcorgitaco.unearthed.config.UnearthedConfig;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEEntities;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import net.oriondevcorgitaco.unearthed.core.UETags;
import net.oriondevcorgitaco.unearthed.item.RegolithItem;
import net.oriondevcorgitaco.unearthed.planets.block.*;
import net.oriondevcorgitaco.unearthed.planets.entity.AsteroidEntity;
import net.oriondevcorgitaco.unearthed.planets.entity.CloudEntity;
import net.oriondevcorgitaco.unearthed.planets.planetcore.MantleCoreTile;
import net.oriondevcorgitaco.unearthed.util.BlockStatePropertiesMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Unearthed.MOD_ID)
public class Unearthed {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();


    public Unearthed() {
//        UETextureStitcher.setupFolders();
//        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientConstruction::run);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ueCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnearthedConfig.COMMON_CONFIG);
        BlockStatePropertiesMatch.init();
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
                    UEBlocks.PYROXENE = new GravelBlock(AbstractBlock.Properties.create(Material.SAND, MaterialColor.BLACK).hardnessAndResistance(0.6F).sound(SoundType.GROUND)).setRegistryName("pyroxene"),
                    UEBlocks.PUDDLE = new PuddleBlock(AbstractBlock.Properties.create(Material.WATER).notSolid().tickRandomly().slipperiness(0.98f).zeroHardnessAndResistance().sound(WATER)).setRegistryName("puddle"),
                    UEBlocks.LICHEN = new LichenBlock(AbstractBlock.Properties.create(Material.PLANTS).notSolid().tickRandomly().hardnessAndResistance(0.2f).sound(SoundType.PLANT)).setRegistryName("lichen"),
                    UEBlocks.LIGNITE_BRIQUETTES = new Block(AbstractBlock.Properties.from(Blocks.COAL_BLOCK)).setRegistryName("lignite_briquettes")
//                    UEBlocks.MANTLE_CORE = (MantleCoreBlock) new MantleCoreBlock(AbstractBlock.Properties.create(Material.LAVA).hardnessAndResistance(10f, 10f).sound(SoundType.ANCIENT_DEBRIS).setRequiresTool()).setRegistryName("mantle_core"),
//                    UEBlocks.PLANET_LAVA = (PlanetLavaBlock) new PlanetLavaBlock(AbstractBlock.Properties.create(Material.LAVA).notSolid().noDrops().hardnessAndResistance(-1.0F, 3600000.0F).setLightLevel(b -> 15)).setRegistryName("lava"),
//                    UEBlocks.PLANET_WATER = (PlanetWaterBlock) new PlanetWaterBlock(AbstractBlock.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).notSolid()).setRegistryName("water"),
//                    UEBlocks.VOLCANO = (PlanetVolcanoBlock) new PlanetVolcanoBlock(AbstractBlock.Properties.from(Blocks.MAGMA_BLOCK)).setRegistryName("volcano"),
//                    UEBlocks.FAULT = (PlanetFaultBlock) new PlanetFaultBlock(AbstractBlock.Properties.from(Blocks.STONE)).setRegistryName("fault"),
//                    UEBlocks.SURFACE = (PlanetSurfaceBlock) new PlanetSurfaceBlock(AbstractBlock.Properties.from(Blocks.GRASS_BLOCK)).setRegistryName("surface")
            );
            LOGGER.info("UE: Blocks registered!");
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            LOGGER.debug("UE: Registering items...");
            Item.Properties properties = new Item.Properties().group(UNEARTHED_TAB);
            Item.Properties ungrouped = new Item.Properties();
            BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry ->
            {
                event.getRegistry().register(new BlockItem(entry.getBlock(), properties).setRegistryName(entry.getId()));
            }));
            event.getRegistry().registerAll(
                    UEItems.PYROXENE = new BlockItem(UEBlocks.PYROXENE, new Item.Properties().group(UNEARTHED_TAB).isImmuneToFire()).setRegistryName("pyroxene"),
                    UEItems.IRON_ORE = new Item(properties).setRegistryName("iron_ore"),
                    UEItems.GOLD_ORE = new Item(properties).setRegistryName("gold_ore"),
                    UEItems.REGOLITH = new RegolithItem(properties).setRegistryName("regolith"),
                    UEItems.PUDDLE = new BlockItem(UEBlocks.PUDDLE, new Item.Properties().group(UNEARTHED_TAB).maxStackSize(1)).setRegistryName("puddle"),
                    UEItems.LICHEN = new BlockItem(UEBlocks.LICHEN, properties).setRegistryName("lichen"),
                    UEItems.LIGNITE_BRIQUETTES = new BlockItem(UEBlocks.LIGNITE_BRIQUETTES, properties).setRegistryName("lignite_briquettes")
//                    UEItems.MANTLE_CORE = new BlockItem(UEBlocks.MANTLE_CORE, properties).setRegistryName("mantle_core"),
//                    UEItems.VOLCANO = new BlockItem(UEBlocks.VOLCANO, ungrouped).setRegistryName("volcano"),
//                    UEItems.FAULT = new BlockItem(UEBlocks.FAULT, ungrouped).setRegistryName("fault"),
//                    UEItems.SURFACE = new BlockItem(UEBlocks.SURFACE, ungrouped).setRegistryName("surface"),
//                    UEItems.PLANET_LAVA = new BlockItem(UEBlocks.PLANET_LAVA, ungrouped).setRegistryName("lava"),
//                    UEItems.PLANET_WATER = new BlockItem(UEBlocks.PLANET_WATER, ungrouped).setRegistryName("water")
            );
            LOGGER.info("UE: Items registered!");
        }

//        @SubscribeEvent(priority = EventPriority.LOWEST)
//        public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
////            LOGGER.debug("UE: Registering entities...");
//            UEEntities.CLOUD = EntityType.Builder.<CloudEntity>create(CloudEntity::new, EntityClassification.MISC).size(0.25f, 0.25f).trackingRange(8).func_233608_b_(10).build("cloud");
//            UEEntities.ASTEROID = EntityType.Builder.<AsteroidEntity>create(AsteroidEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).trackingRange(8).func_233608_b_(10).build("asteroid");
//            event.getRegistry().registerAll(
//                    UEEntities.CLOUD.setRegistryName(Unearthed.MOD_ID, "cloud"),
//                    UEEntities.ASTEROID.setRegistryName(Unearthed.MOD_ID, "asteroid")
//            );
//        }
//
//        @SubscribeEvent
//        public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
////            LOGGER.debug("UE: Registering tile entities...");
//
//            UEEntities.MANTLE_CORE = TileEntityType.Builder.create(MantleCoreTile::new, UEBlocks.MANTLE_CORE).build(null);
//            UEEntities.MANTLE_CORE.setRegistryName("mantle_core");
//            UEEntities.PLANET_LAVA = TileEntityType.Builder.create(PlanetLavaTile::new, UEBlocks.PLANET_LAVA).build(null);
//            UEEntities.PLANET_LAVA.setRegistryName("lava");
//            event.getRegistry().registerAll(
//                    UEEntities.PLANET_LAVA,
//                    UEEntities.MANTLE_CORE
//            );
//        }

    }

    public static ItemGroup UNEARTHED_TAB = new ItemGroup(Unearthed.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, "kimberlite_diamond_ore")));
        }
    };
}

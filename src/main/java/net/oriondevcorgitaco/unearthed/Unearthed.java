package net.oriondevcorgitaco.unearthed;


import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.LichenBlock;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import net.oriondevcorgitaco.unearthed.core.UETags;
import net.oriondevcorgitaco.unearthed.item.RegolithItem;
import net.oriondevcorgitaco.unearthed.util.BlockStatePropertiesMatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;

@Mod(Unearthed.MOD_ID)
public class Unearthed {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();


    public Unearthed() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ueCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UnearthedConfig.COMMON_CONFIG);
        BlockStatePropertiesMatch.init();
    }

    public void ueCommonSetup(FMLCommonSetupEvent event) {
        BlockGeneratorReference.init();
        UETags.init();

        if (ModList.get().isLoaded("dynamictrees")){
        	RegistryHandler.setup(MOD_ID);
        }
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class BYGRegistries {
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            LOGGER.debug("UE: Registering blocks...");
            BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry -> event.getRegistry().register(entry.createBlock(type).setRegistryName(entry.getId()))));

            event.getRegistry().registerAll(
                    UEBlocks.PYROXENE = new GravelBlock(AbstractBlock.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.6F).sound(SoundType.GRAVEL)).setRegistryName("pyroxene"),
                    UEBlocks.LICHEN = new LichenBlock(AbstractBlock.Properties.of(Material.PLANT).noOcclusion().randomTicks().strength(0.2f).sound(SoundType.GRASS)).setRegistryName("lichen"),
                    UEBlocks.LIGNITE_BRIQUETTES = new Block(AbstractBlock.Properties.copy(Blocks.COAL_BLOCK)).setRegistryName("lignite_briquettes")
            );
            LOGGER.info("UE: Blocks registered!");
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            LOGGER.debug("UE: Registering items...");
            Item.Properties properties = new Item.Properties().tab(UNEARTHED_TAB);
            Item.Properties ungrouped = new Item.Properties();
            BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry ->
            {
                event.getRegistry().register(new BlockItem(entry.getBlock(), properties).setRegistryName(entry.getId()));
            }));
            event.getRegistry().registerAll(
                    UEItems.PYROXENE = new BlockItem(UEBlocks.PYROXENE, new Item.Properties().tab(UNEARTHED_TAB).fireResistant()).setRegistryName("pyroxene"),
                    UEItems.IRON_ORE = new Item(properties).setRegistryName("iron_ore"),
                    UEItems.GOLD_ORE = new Item(properties).setRegistryName("gold_ore"),
                    UEItems.REGOLITH = new RegolithItem(properties).setRegistryName("regolith"),
                    UEItems.LICHEN = new BlockItem(UEBlocks.LICHEN, properties).setRegistryName("lichen"),
                    UEItems.LIGNITE_BRIQUETTES = new BlockItem(UEBlocks.LIGNITE_BRIQUETTES, properties).setRegistryName("lignite_briquettes")
            );
            LOGGER.info("UE: Items registered!");
        }
    }

    public static ItemGroup UNEARTHED_TAB = new ItemGroup(Unearthed.MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registry.ITEM.get(new ResourceLocation(Unearthed.MOD_ID, "kimberlite_diamond_ore")));
        }
    };

}

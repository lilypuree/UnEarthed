package net.lilycorgitaco.unearthed;


import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.block.LichenBlock;
import net.lilycorgitaco.unearthed.block.PuddleBlock;
import net.lilycorgitaco.unearthed.config.UEConfig;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import net.lilycorgitaco.unearthed.core.UEItems;
import net.lilycorgitaco.unearthed.core.UETags;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Unearthed implements ModInitializer {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();

    public static UEConfig CONFIG;

    @Override
    public void onInitialize() {
        AutoConfig.register(UEConfig.class, JanksonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(UEConfig.class).getConfig();
        BlockGeneratorReference.init();
        registerBlocks();
        registerItems();
        ueCommonSetup();
    }

    public void ueCommonSetup() {
        UETags.init();
//        configReader();
    }

    public static void registerBlocks() {
        LOGGER.debug("UE: Registering blocks...");
        BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry -> {
                createBlock(entry.getBlock(), entry.getId());
        }));

        BlockSoundGroup WATER = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.BLOCK_WET_GRASS_BREAK, SoundEvents.ENTITY_GENERIC_SPLASH, SoundEvents.BLOCK_WET_GRASS_PLACE, SoundEvents.BLOCK_WET_GRASS_HIT, SoundEvents.ENTITY_GENERIC_SPLASH);

        UEBlocks.PYROXENE = createBlock(new GravelBlock(AbstractBlock.Settings.of(Material.AGGREGATE, MaterialColor.BLACK).strength(0.6F).sounds(BlockSoundGroup.GRAVEL)), "pyroxene");
        UEBlocks.PUDDLE = createBlock(new PuddleBlock(AbstractBlock.Settings.of(Material.WATER).nonOpaque().ticksRandomly().slipperiness(0.98f).breakInstantly().sounds(WATER)), "puddle");
        UEBlocks.LICHEN = createBlock(new LichenBlock(AbstractBlock.Settings.of(Material.PLANT).nonOpaque().ticksRandomly().strength(0.2f).sounds(BlockSoundGroup.GRASS)), "lichen");
        UEBlocks.LIGNITE_BRIQUETTES = createBlock(new Block(AbstractBlock.Settings.copy(Blocks.COAL_BLOCK)), "lignite_briquettes");
        LOGGER.info("UE: Blocks registered!");
    }

    public static Block createBlock(Block block, String id) {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, id), block);
        return block;
    }

    public static void registerItems() {
        LOGGER.debug("UE: Registering items...");
        Item.Settings properties = new Item.Settings().group(UNEARTHED_TAB);
        BlockGeneratorReference.ROCK_TYPES.forEach(type -> type.getEntries().forEach(entry -> {
                createItem(new BlockItem(entry.getBlock(), properties), entry.getId());
        }));

        UEItems.PYROXENE = createItem(new BlockItem(UEBlocks.PYROXENE, properties), "pyroxene");
        UEItems.IRON_ORE = createItem(new Item(properties), "iron_ore");
        UEItems.GOLD_ORE = createItem(new Item(properties), "gold_ore");
        UEItems.PUDDLE = createItem(new BlockItem(UEBlocks.PUDDLE, new Item.Settings().group(UNEARTHED_TAB).maxCount(1)), "puddle");
        UEItems.LICHEN = createItem(new BlockItem(UEBlocks.LICHEN, properties), "lichen");
        UEItems.LIGNITE_BRIQUETTES = createItem(new BlockItem(UEBlocks.LIGNITE_BRIQUETTES, properties), "lignite_briquettes");
        LOGGER.info("UE: Items registered!");
    }


    public static Item createItem(Item item, String id) {
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, id), item);
        return item;
    }


    public static final ItemGroup UNEARTHED_TAB = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "tab"), () -> new ItemStack(Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, "kimberlite_diamond_ore"))));
}

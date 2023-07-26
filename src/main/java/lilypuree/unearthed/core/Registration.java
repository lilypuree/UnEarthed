package lilypuree.unearthed.core;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.misc.BlockStatePropertiesMatch;
import lilypuree.unearthed.misc.HoeDig;
import lilypuree.unearthed.misc.RegolithItem;
import lilypuree.unearthed.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static void init (IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        registerBlocks();
        registerItems();
    }

    public static final RegistryObject<Block> LICHEN = BLOCKS.register(UENames.LICHEN, () -> new LichenBlock(BlockBehaviour.Properties.of(Material.PLANT).noOcclusion().randomTicks().strength(0.2f).sound(SoundType.GRASS)));
    public static final RegistryObject<Block> PYROXENE = BLOCKS.register(UENames.PYROXENE, () -> new GravelBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.6F).sound(SoundType.GRAVEL)));
    public static final RegistryObject<Block> LIGNITE_BRIQUETTES = BLOCKS.register(UENames.LIGNITE_BRIQUETTES, () -> new Block(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)));

    public static void registerBlocks() {
        Constants.LOG.debug("UE: Registering blocks...");
        BlockSchemas.ROCK_TYPES.forEach(schema -> schema.entries().forEach(entry -> {
            BLOCKS.register(entry.getId(), () -> entry.createBlock(schema));
        }));
        Constants.LOG.info("UE: Blocks registered!");
    }

    public static final CreativeModeTab tab = Services.PLATFORM.createModTab("general", () -> new ItemStack(Registry.ITEM.get(new ResourceLocation(Constants.MOD_ID, "chiseled_limestone"))));

    public static final RegistryObject<Item> PYROXENE_ITEM = ITEMS.register(UENames.PYROXENE, () -> new BlockItem(PYROXENE.get(), new Item.Properties().tab(tab).fireResistant()));
    public static final RegistryObject<Item> IRON_ORE = ITEMS.register(UENames.IRON_ORE, () -> new Item(new Item.Properties().tab(tab)));
    public static final RegistryObject<Item> GOLD_ORE = ITEMS.register(UENames.GOLD_ORE, () -> new Item(new Item.Properties().tab(tab)));
    public static final RegistryObject<Item>  REGOLITH = ITEMS.register(UENames.REGOLITH, () -> new RegolithItem(new Item.Properties().tab(tab)));
    public static final RegistryObject<Item> LICHEN_ITEM = ITEMS.register(UENames.LICHEN, () -> new BlockItem(LICHEN.get(), new Item.Properties().tab(tab)));
    public static final RegistryObject<Item> LIGNITE_BRIQUETTES_ITEM = ITEMS.register(UENames.LIGNITE_BRIQUETTES, () -> new BlockItem(LIGNITE_BRIQUETTES.get(), new Item.Properties().tab(tab)));

    public static void registerItems() {
        Constants.LOG.debug("UE: Registering items...");
        Item.Properties properties = new Item.Properties().tab(tab);

        BlockSchemas.ROCK_TYPES.forEach(schema -> schema.entries().forEach(entry -> {
            ITEMS.register(entry.getId(), () -> new BlockItem(entry.getBlock(), properties));
        }));
        Constants.LOG.info("UE: Items registered!");
    }

    public static void registerLootConditions() {
        BlockStatePropertiesMatch.init();
        HoeDig.init();
    }

}

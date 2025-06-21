//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package lilypuree.unearthed.core;

import java.util.Objects;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.misc.BlockStatePropertiesMatch;
import lilypuree.unearthed.misc.HoeDig;
import lilypuree.unearthed.misc.RegolithItem;
import lilypuree.unearthed.platform.ForgePlatformHelper;
import lilypuree.unearthed.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {
    public static final DeferredRegister<Block> BLOCKS;
    public static final DeferredRegister<Item> ITEMS;
    public static DeferredRegister<CreativeModeTab> CREATIVE_TABS;
    public static final RegistryObject<Block> LICHEN;
    public static final RegistryObject<Block> PYROXENE;
    public static final RegistryObject<Block> LIGNITE_BRIQUETTES;
    public static final CreativeModeTab tab;
    public static RegistryObject<CreativeModeTab> modeTab;
    public static final RegistryObject<Item> PYROXENE_ITEM;
    public static final RegistryObject<Item> IRON_ORE;
    public static final RegistryObject<Item> GOLD_ORE;
    public static final RegistryObject<Item> REGOLITH;
    public static final RegistryObject<Item> LICHEN_ITEM;
    public static final RegistryObject<Item> LIGNITE_BRIQUETTES_ITEM;

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        registerBlocks();
        registerItems();
        CREATIVE_TABS.register(bus);
    }

    public static void registerBlocks() {
        Constants.LOG.debug("UE: Registering blocks...");
        BlockSchemas.ROCK_TYPES.forEach((schema) -> schema.entries().forEach((entry) -> {
            RegistryObject<Block> block = BLOCKS.register(entry.getId(), () -> entry.createBlock(schema));
        }));
        Constants.LOG.info("UE: Blocks registered!");
    }

    public static void registerItems() {
        Constants.LOG.debug("UE: Registering items...");
        Item.Properties properties = new Item.Properties();
        BlockSchemas.ROCK_TYPES.forEach((schema) -> schema.entries().forEach((entry) -> {
            RegistryObject<Item> item = ITEMS.register(entry.getId(), () -> new BlockItem(entry.getBlock(), properties));
            ForgePlatformHelper.addToMainTabItems(item);
        }));
        ForgePlatformHelper.addToMainTabItems(PYROXENE_ITEM);
        ForgePlatformHelper.addToMainTabItems(IRON_ORE);
        ForgePlatformHelper.addToMainTabItems(GOLD_ORE);
        ForgePlatformHelper.addToMainTabItems(REGOLITH);
        ForgePlatformHelper.addToMainTabItems(LICHEN_ITEM);
        ForgePlatformHelper.addToMainTabItems(LIGNITE_BRIQUETTES_ITEM);
        Constants.LOG.info("UE: Items registered!");
    }

    public static void registerLootConditions(IEventBus bus) {
        HoeDig.init(bus);
        BlockStatePropertiesMatch.init();
    }

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "unearthed");
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "unearthed");
        CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "unearthed");
        LICHEN = BLOCKS.register("lichen", () -> new LichenBlock(Properties.of().noOcclusion().randomTicks().strength(0.2F).sound(SoundType.GRASS)));
        PYROXENE = BLOCKS.register("pyroxene", () -> new GravelBlock(Properties.of().strength(0.6F).sound(SoundType.GRAVEL)));
        LIGNITE_BRIQUETTES = BLOCKS.register("lignite_briquettes", () -> new Block(Properties.copy(Blocks.COAL_BLOCK)));
        tab = Services.PLATFORM.createModTab("general", () -> new ItemStack((ItemLike)Objects.requireNonNull((Item)ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("unearthed", "chiseled_limestone")))));
        modeTab = CREATIVE_TABS.register("general", () -> tab);
        PYROXENE_ITEM = ITEMS.register("pyroxene", () -> new BlockItem((Block)PYROXENE.get(), (new Item.Properties()).fireResistant()));
        IRON_ORE = ITEMS.register("iron_ore", () -> new Item(new Item.Properties()));
        GOLD_ORE = ITEMS.register("gold_ore", () -> new Item(new Item.Properties()));
        REGOLITH = ITEMS.register("regolith", () -> new RegolithItem(new Item.Properties()));
        LICHEN_ITEM = ITEMS.register("lichen", () -> new BlockItem((Block)LICHEN.get(), new Item.Properties()));
        LIGNITE_BRIQUETTES_ITEM = ITEMS.register("lignite_briquettes", () -> new BlockItem((Block)LIGNITE_BRIQUETTES.get(), new Item.Properties()));
    }
}

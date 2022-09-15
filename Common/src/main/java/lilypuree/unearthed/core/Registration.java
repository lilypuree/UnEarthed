package lilypuree.unearthed.core;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.misc.BlockStatePropertiesMatch;
import lilypuree.unearthed.misc.HoeDig;
import lilypuree.unearthed.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class Registration {
    public static void registerBlocks(RegistryHelper<Block> helper) {
        Constants.LOG.debug("UE: Registering blocks...");
        BlockSchemas.ROCK_TYPES.forEach(schema -> schema.entries().forEach(entry -> {
            helper.register(entry.createBlock(schema), entry.getId());
        }));
        UEBlocks.init();
        helper.register(UEBlocks.LICHEN, UENames.LICHEN);
        helper.register(UEBlocks.PYROXENE, UENames.PYROXENE);
        helper.register(UEBlocks.LIGNITE_BRIQUETTES, UENames.LIGNITE_BRIQUETTES);
        Constants.LOG.info("UE: Blocks registered!");
    }

    public static void registerItems(RegistryHelper<Item> helper) {
        Constants.LOG.debug("UE: Registering items...");
        CreativeModeTab tab = Services.PLATFORM.createModTab("general", () -> new ItemStack(Registry.ITEM.get(new ResourceLocation(Constants.MOD_ID, "chiseled_limestone"))));
        Item.Properties properties = new Item.Properties().tab(tab);

        BlockSchemas.ROCK_TYPES.forEach(schema -> schema.entries().forEach(entry -> {
            helper.register(new BlockItem(entry.getBlock(), properties), entry.getId());
        }));
        UEItems.init(tab);
        helper.register(UEItems.LICHEN, UENames.LICHEN);
        helper.register(UEItems.PYROXENE, UENames.PYROXENE);
        helper.register(UEItems.LIGNITE_BRIQUETTES, UENames.LIGNITE_BRIQUETTES);
        helper.register(UEItems.GOLD_ORE, UENames.GOLD_ORE);
        helper.register(UEItems.IRON_ORE, UENames.IRON_ORE);
        helper.register(UEItems.REGOLITH, UENames.REGOLITH);
        Constants.LOG.info("UE: Items registered!");
    }

    public static void registerLootConditions() {
        BlockStatePropertiesMatch.init();
        HoeDig.init();
    }

}

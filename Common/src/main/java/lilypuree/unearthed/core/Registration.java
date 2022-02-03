package lilypuree.unearthed.core;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.BlockSchemas;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;

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
        Item.Properties properties = new Item.Properties().tab(Constants.ITEM_GROUP);

        BlockSchemas.ROCK_TYPES.forEach(schema -> schema.entries().forEach(entry -> {
            helper.register(new BlockItem(entry.getBlock(), properties), entry.getId());
        }));
        UEItems.init();
        helper.register(UEItems.LICHEN, UENames.LICHEN);
        helper.register(UEItems.PYROXENE, UENames.PYROXENE);
        helper.register(UEItems.LIGNITE_BRIQUETTES, UENames.LIGNITE_BRIQUETTES);
        helper.register(UEItems.GOLD_ORE, UENames.GOLD_ORE);
        helper.register(UEItems.IRON_ORE, UENames.IRON_ORE);
        helper.register(UEItems.REGOLITH, UENames.REGOLITH);
        Constants.LOG.info("UE: Items registered!");
    }

    public static void registerFeatures(RegistryHelper<Feature<?>> helper){
        UEFeatures.init();
        helper.register(UEFeatures.STONE_REPLACER, UENames.STONE_REPLACER);
    }
}

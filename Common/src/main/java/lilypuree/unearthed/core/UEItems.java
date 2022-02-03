package lilypuree.unearthed.core;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.misc.RegolithItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class UEItems {
    public static Item REGOLITH;
    public static Item IRON_ORE;
    public static Item GOLD_ORE;
    public static Item LIGNITE_BRIQUETTES;
    public static Item LICHEN;
    public static Item PYROXENE;

    public static void init() {
        Item.Properties properties = new Item.Properties().tab(Constants.ITEM_GROUP);
        UEItems.PYROXENE = new BlockItem(UEBlocks.PYROXENE, new Item.Properties().tab(Constants.ITEM_GROUP).fireResistant());
        UEItems.IRON_ORE = new Item(properties);
        UEItems.GOLD_ORE = new Item(properties);
        UEItems.REGOLITH = new RegolithItem(properties);
        UEItems.LICHEN = new BlockItem(UEBlocks.LICHEN, properties);
        UEItems.LIGNITE_BRIQUETTES = new BlockItem(UEBlocks.LIGNITE_BRIQUETTES, properties);
    }
}

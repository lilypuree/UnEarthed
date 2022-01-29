package lilypuree.unearthed.core;

import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class UETags {

    public static class Blocks {
        public static Tag.Named<Block> REGOLITH_TAG;
        public static Tag.Named<Block> SEDIMENTARY_TAG;
        public static Tag.Named<Block> IGNEOUS_TAG;
        public static Tag.Named<Block> METAMORPHIC_TAG;
        public static Tag.Named<Block> REPLACABLE;
        public static Tag.Named<Block> REPLACE_GRASS;
        public static Tag.Named<Block> REPLACE_DIRT;
    }

    public static class Items {
        public static Tag.Named<Item> REGOLITH_TAG;
        public static Tag.Named<Item> SEDIMENTARY_ITEM;
        public static Tag.Named<Item> IGNEOUS_ITEM;
        public static Tag.Named<Item> METAMORPHIC_ITEM;
        public static Tag.Named<Item> REGOLITH_USABLE;
    }
}

package net.oriondevcorgitaco.unearthed.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.oriondevcorgitaco.unearthed.Unearthed;

public class UETags {
    public static void init() {
        UETags.Blocks.init();
        UETags.Items.init();
    }

    public static class Blocks {
        public static final ITag.INamedTag<Block> IRON_ORE_TAG = tag("iron_ores");
        public static final ITag.INamedTag<Block> COAL_ORE_TAG = tag("coal_ores");
        public static final ITag.INamedTag<Block> GOLD_ORE_TAG = tag("gold_ores");
        public static final ITag.INamedTag<Block> LAPIS_ORE_TAG = tag("lapis_ores");
        public static final ITag.INamedTag<Block> REDSTONE_ORE_TAG =tag("redstone_ores");
        public static final ITag.INamedTag<Block> DIAMOND_ORE_TAG = tag("diamond_ores");
        public static final ITag.INamedTag<Block> EMERALD_ORE_TAG = tag("emerald_ores");
        public static final ITag.INamedTag<Block> REGOLITH_TAG = tag("regolith");
        public static final ITag.INamedTag<Block> SEDIMENTARY_TAG =tag("sedimentary");
        public static final ITag.INamedTag<Block> IGNEOUS_TAG = tag("igneous");
        public static final ITag.INamedTag<Block> METAMORPHIC_TAG = tag("metamorphic");
        public static final ITag.INamedTag<Block> REPLACABLE = tag("replaceable");
        public static final ITag.INamedTag<Block> REPLACE_GRASS = tag("replace_grass");
        public static final ITag.INamedTag<Block> REPLACE_DIRT = tag("replace_dirt");

        private static void init() {
        }

        private static ITag.INamedTag<Block> tag(String name) {
            return BlockTags.makeWrapperTag(Unearthed.MOD_ID + ":" + name);
        }
    }

    public static class Items {
        public static final ITag.INamedTag<Item> SEDIMENTARY_ITEM = tag("sedimentary");
        public static final ITag.INamedTag<Item> IGNEOUS_ITEM = tag("igneous");
        public static final ITag.INamedTag<Item> METAMORPHIC_ITEM = tag("metamorphic");
        public static final ITag.INamedTag<Item> REGOLITH_USABLE = tag("regolith_usable");
        public static final ITag.INamedTag<Item> REGOLITH_TAG = tag("regolith");

        private static void init() {
        }

        private static ITag.INamedTag<Item> tag(String name) {
            return ItemTags.makeWrapperTag(Unearthed.MOD_ID + ":" + name);
        }

    }

}

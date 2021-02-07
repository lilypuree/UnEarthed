package net.lilycorgitaco.unearthed.core;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.lilycorgitaco.unearthed.Unearthed;

public class UETags {
    public static void init() {
        UETags.Blocks.init();
        UETags.Items.init();
    }

    public static class Blocks {
        public static final Tag.Identified<Block> IRON_ORE_TAG = tag("iron_ores");
        public static final Tag.Identified<Block> COAL_ORE_TAG = tag("coal_ores");
        public static final Tag.Identified<Block> GOLD_ORE_TAG = tag("gold_ores");
        public static final Tag.Identified<Block> LAPIS_ORE_TAG = tag("lapis_ores");
        public static final Tag.Identified<Block> REDSTONE_ORE_TAG =tag("redstone_ores");
        public static final Tag.Identified<Block> DIAMOND_ORE_TAG = tag("diamond_ores");
        public static final Tag.Identified<Block> EMERALD_ORE_TAG = tag("emerald_ores");
        public static final Tag.Identified<Block> REGOLITH_TAG = tag("regolith");
        public static final Tag.Identified<Block> SEDIMENTARY_TAG =tag("sedimentary");
        public static final Tag.Identified<Block> IGNEOUS_TAG = tag("igneous");
        public static final Tag.Identified<Block> METAMORPHIC_TAG = tag("metamorphic");
        public static final Tag.Identified<Block> REPLACABLE = tag("replaceable");
        public static final Tag.Identified<Block> REPLACE_GRASS = tag("replace_grass");
        public static final Tag.Identified<Block> REPLACE_DIRT = tag("replace_dirt");

        private static void init() {
        }

        private static Tag.Identified<Block> tag(String name) {
            return BlockTags.register(Unearthed.MOD_ID + ":" + name);
        }
    }

    public static class Items {
        public static final Tag.Identified<Item> SEDIMENTARY_ITEM = tag("sedimentary");
        public static final Tag.Identified<Item> IGNEOUS_ITEM = tag("igneous");
        public static final Tag.Identified<Item> METAMORPHIC_ITEM = tag("metamorphic");
        public static final Tag.Identified<Item> REGOLITH_USABLE = tag("regolith_usable");
        public static final Tag.Identified<Item> REGOLITH_TAG = tag("regolith");

        private static void init() {
        }

        private static Tag.Identified<Item> tag(String name) {
            return ItemTags.register(Unearthed.MOD_ID + ":" + name);
        }

    }

}

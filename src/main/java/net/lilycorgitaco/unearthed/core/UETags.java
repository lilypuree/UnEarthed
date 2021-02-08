package net.lilycorgitaco.unearthed.core;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.lilycorgitaco.unearthed.Unearthed;
import net.minecraft.util.Identifier;

public class UETags {
    public static void init() {
        UETags.Blocks.init();
        UETags.Items.init();
    }

    public static class Blocks {
        public static final Tag<Block> IRON_ORE_TAG = tag("iron_ores");
        public static final Tag<Block> COAL_ORE_TAG = tag("coal_ores");
        public static final Tag<Block> GOLD_ORE_TAG = tag("gold_ores");
        public static final Tag<Block> LAPIS_ORE_TAG = tag("lapis_ores");
        public static final Tag<Block> REDSTONE_ORE_TAG =tag("redstone_ores");
        public static final Tag<Block> DIAMOND_ORE_TAG = tag("diamond_ores");
        public static final Tag<Block> EMERALD_ORE_TAG = tag("emerald_ores");
        public static final Tag<Block> REGOLITH_TAG = tag("regolith");
        public static final Tag<Block> SEDIMENTARY_TAG =tag("sedimentary");
        public static final Tag<Block> IGNEOUS_TAG = tag("igneous");
        public static final Tag<Block> METAMORPHIC_TAG = tag("metamorphic");
        public static final Tag<Block> REPLACABLE = tag("replaceable");
        public static final Tag<Block> REPLACE_GRASS = tag("replace_grass");
        public static final Tag<Block> REPLACE_DIRT = tag("replace_dirt");

        private static void init() {
        }

        private static Tag<Block> tag(String name) {
            return TagRegistry.block(new Identifier(Unearthed.MOD_ID, name));
        }
    }

    public static class Items {
        public static final Tag<Item> SEDIMENTARY_ITEM = tag("sedimentary");
        public static final Tag<Item> IGNEOUS_ITEM = tag("igneous");
        public static final Tag<Item> METAMORPHIC_ITEM = tag("metamorphic");
        public static final Tag<Item> REGOLITH_USABLE = tag("regolith_usable");
        public static final Tag<Item> REGOLITH_TAG = tag("regolith");

        private static void init() {
        }

        private static Tag<Item> tag(String name) {
            return TagRegistry.item(new Identifier(Unearthed.MOD_ID, name));
        }

    }

}

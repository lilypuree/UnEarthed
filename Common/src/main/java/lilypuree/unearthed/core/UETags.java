package lilypuree.unearthed.core;

import lilypuree.unearthed.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class UETags {

    public static void init(){
        Blocks.init();
        Items.init();
    }

    public static class Blocks {
        public static TagKey<Block> REGOLITH_TAG;
        public static TagKey<Block> SEDIMENTARY_TAG;
        public static TagKey<Block> IGNEOUS_TAG;
        public static TagKey<Block> METAMORPHIC_TAG;
        public static TagKey<Block> REPLACABLE;
        public static TagKey<Block> REPLACE_GRASS;
        public static TagKey<Block> REPLACE_DIRT;

        public static void init(){
            UETags.Blocks.IGNEOUS_TAG = create("igneous");
            UETags.Blocks.METAMORPHIC_TAG = create("metamorphic");
            UETags.Blocks.SEDIMENTARY_TAG = create("sedimentary");
            UETags.Blocks.REGOLITH_TAG = create("regolith");

        }
        private static TagKey<Block> create(String name){
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Constants.MOD_ID, name));
        }
    }

    public static class Items {
        public static TagKey<Item> REGOLITH_TAG;
        public static TagKey<Item> SEDIMENTARY_ITEM;
        public static TagKey<Item> IGNEOUS_ITEM;
        public static TagKey<Item> METAMORPHIC_ITEM;
        public static TagKey<Item> REGOLITH_USABLE;


        public static TagKey<Item> UNEARTHED_IRON_ORE;
        public static TagKey<Item> UNEARTHED_GOLD_ORE;
        public static TagKey<Item> UNEARTHED_COPPER_ORE;
        public static TagKey<Item> UNEARTHED_COAL_ORE;
        public static TagKey<Item> UNEARTHED_LAPIS_ORE;
        public static TagKey<Item> UNEARTHED_REDSTONE_ORE;
        public static TagKey<Item> UNEARTHED_EMERALD_ORE;
        public static TagKey<Item> UNEARTHED_DIAMOND_ORE;

        public static void init(){
            UETags.Items.IGNEOUS_ITEM = create("igneous");
            UETags.Items.METAMORPHIC_ITEM = create("metamorphic");
            UETags.Items.SEDIMENTARY_ITEM = create("sedimentary");
            UETags.Items.REGOLITH_USABLE = create("regolith_usable");
            UETags.Items.REGOLITH_TAG = create("regolith");
            UETags.Items.UNEARTHED_IRON_ORE = create("iron_ore");
            UETags.Items.UNEARTHED_COAL_ORE = create("coal_ore");
            UETags.Items.UNEARTHED_GOLD_ORE = create("gold_ore");
            UETags.Items.UNEARTHED_COPPER_ORE = create("copper_ore");
            UETags.Items.UNEARTHED_LAPIS_ORE = create("lapis_ore");
            UETags.Items.UNEARTHED_REDSTONE_ORE = create("redstone_ore");
            UETags.Items.UNEARTHED_DIAMOND_ORE = create("diamond_ore");
            UETags.Items.UNEARTHED_EMERALD_ORE = create("emerald_ore");
        }

        private static TagKey<Item> create(String name){
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Constants.MOD_ID, name));
        }
    }
}

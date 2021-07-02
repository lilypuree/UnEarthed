package net.oriondevcorgitaco.unearthed.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.StoneTiers;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import net.oriondevcorgitaco.unearthed.core.UETags;

import javax.annotation.Nullable;

public class UEItemTagsProvider extends ItemTagsProvider {
    public UEItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, Unearthed.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        ITag.INamedTag<Item> IRON_ORE_ITEM_TAG = ItemTags.bind(new ResourceLocation(Unearthed.MOD_ID, "iron_ores").toString());
        ITag.INamedTag<Item> GOLD_ORE_ITEM_TAG = ItemTags.bind(new ResourceLocation(Unearthed.MOD_ID, "gold_ores").toString());
        copy(UETags.Blocks.IRON_ORE_TAG, IRON_ORE_ITEM_TAG);
        copy(UETags.Blocks.COAL_ORE_TAG, ItemTags.bind(new ResourceLocation(Unearthed.MOD_ID, "coal_ores").toString()));
        copy(UETags.Blocks.GOLD_ORE_TAG, GOLD_ORE_ITEM_TAG);
        copy(UETags.Blocks.REDSTONE_ORE_TAG, ItemTags.bind(new ResourceLocation(Unearthed.MOD_ID, "redstone_ores").toString()));
        copy(UETags.Blocks.LAPIS_ORE_TAG, ItemTags.bind(new ResourceLocation(Unearthed.MOD_ID, "lapis_ores").toString()));
        copy(UETags.Blocks.DIAMOND_ORE_TAG, ItemTags.bind(new ResourceLocation(Unearthed.MOD_ID, "diamond_ores").toString()));
        copy(UETags.Blocks.EMERALD_ORE_TAG, ItemTags.bind(new ResourceLocation(Unearthed.MOD_ID, "emerald_ores").toString()));
        copy(UETags.Blocks.IGNEOUS_TAG, UETags.Items.IGNEOUS_ITEM);
        copy(UETags.Blocks.SEDIMENTARY_TAG, UETags.Items.SEDIMENTARY_ITEM);
        copy(UETags.Blocks.METAMORPHIC_TAG, UETags.Items.METAMORPHIC_ITEM);
        copy(UETags.Blocks.REGOLITH_TAG, UETags.Items.REGOLITH_TAG);
        tag(IRON_ORE_ITEM_TAG).add(UEItems.IRON_ORE);
        tag(GOLD_ORE_ITEM_TAG).add(UEItems.GOLD_ORE);

        copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.STAIRS, ItemTags.STAIRS);

        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            if (type.getTier() == StoneTiers.PRIMARY) {
                if (type.getSchema().getVariants().contains(Variants.COBBLED)) {
                    tag(ItemTags.STONE_CRAFTING_MATERIALS).add(type.getBaseBlock(Variants.COBBLED).asItem());
                    tag(ItemTags.STONE_TOOL_MATERIALS).add(type.getBaseBlock(Variants.COBBLED).asItem());
                } else {
                    tag(ItemTags.STONE_CRAFTING_MATERIALS).add(type.getBaseBlock().asItem());
                    tag(ItemTags.STONE_TOOL_MATERIALS).add(type.getBaseBlock().asItem());
                }
            }
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();
            }
        }
        tag(UETags.Items.REGOLITH_USABLE).add(Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.GOLDEN_HOE, Items.DIAMOND_HOE, Items.NETHERITE_HOE);
    }
}

package lilypuree.unearthed.datagen;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.Variants;
import lilypuree.unearthed.core.UETags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class UEItemTagsProvider extends ItemTagsProvider {
    public UEItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        copy(UETags.Blocks.IGNEOUS_TAG, UETags.Items.IGNEOUS_ITEM);
        copy(UETags.Blocks.SEDIMENTARY_TAG, UETags.Items.SEDIMENTARY_ITEM);
        copy(UETags.Blocks.METAMORPHIC_TAG, UETags.Items.METAMORPHIC_ITEM);
        copy(UETags.Blocks.REGOLITH_TAG, UETags.Items.REGOLITH_TAG);

        copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.STAIRS, ItemTags.STAIRS);

        copy(BlockTags.IRON_ORES, ItemTags.IRON_ORES);
        copy(BlockTags.COAL_ORES, ItemTags.COAL_ORES);
        copy(BlockTags.COPPER_ORES, ItemTags.COPPER_ORES);
        copy(BlockTags.GOLD_ORES, ItemTags.GOLD_ORES);
        copy(BlockTags.REDSTONE_ORES, ItemTags.REDSTONE_ORES);
        copy(BlockTags.LAPIS_ORES, ItemTags.LAPIS_ORES);
        copy(BlockTags.EMERALD_ORES, ItemTags.EMERALD_ORES);
        copy(BlockTags.DIAMOND_ORES, ItemTags.DIAMOND_ORES);

        copy(BlockTags.IRON_ORES, UETags.Items.UNEARTHED_IRON_ORE);
        copy(BlockTags.COAL_ORES, UETags.Items.UNEARTHED_COAL_ORE);
        copy(BlockTags.COPPER_ORES, UETags.Items.UNEARTHED_COPPER_ORE);
        copy(BlockTags.GOLD_ORES, UETags.Items.UNEARTHED_GOLD_ORE);
        copy(BlockTags.REDSTONE_ORES, UETags.Items.UNEARTHED_REDSTONE_ORE);
        copy(BlockTags.LAPIS_ORES, UETags.Items.UNEARTHED_LAPIS_ORE);
        copy(BlockTags.EMERALD_ORES, UETags.Items.UNEARTHED_EMERALD_ORE);
        copy(BlockTags.DIAMOND_ORES, UETags.Items.UNEARTHED_DIAMOND_ORE);

        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {

            if (type.variants().contains(Variants.COBBLED)) {
                Item cobblestone = type.getBaseBlock(Variants.COBBLED).asItem();
                tag(Tags.Items.COBBLESTONE).add(cobblestone);
                tag(ItemTags.STONE_CRAFTING_MATERIALS).add(cobblestone);
                tag(ItemTags.STONE_TOOL_MATERIALS).add(cobblestone);
            } else if (type.variants().contains(Variants.ALL_BLOCKS_PLUS)) {
                tag(ItemTags.STONE_CRAFTING_MATERIALS).add(type.getBaseBlock().asItem());
                tag(ItemTags.STONE_TOOL_MATERIALS).add(type.getBaseBlock().asItem());
            }
        }
    }
}

package lilypuree.unearthed.datagen;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.*;
import lilypuree.unearthed.block.type.IOreType;
import lilypuree.unearthed.block.type.VanillaOreTypes;
import lilypuree.unearthed.core.UETags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class UEBlockTagsProvider extends BlockTagsProvider {
    public UEBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(UETags.Blocks.IGNEOUS_TAG).add(BlockSchemas.GABBRO.getBaseBlock());
        tag(UETags.Blocks.IGNEOUS_TAG).add(BlockSchemas.GRANODIORITE.getBaseBlock());
        tag(UETags.Blocks.IGNEOUS_TAG).add(BlockSchemas.RHYOLITE.getBaseBlock());
        tag(UETags.Blocks.IGNEOUS_TAG).add(BlockSchemas.WHITE_GRANITE.getBaseBlock());
        tag(UETags.Blocks.IGNEOUS_TAG).add(BlockSchemas.DACITE.getBaseBlock());
        tag(UETags.Blocks.IGNEOUS_TAG).add(BlockSchemas.DOLERITE.getBaseBlock());
        tag(UETags.Blocks.IGNEOUS_TAG).add(BlockSchemas.PILLOW_BASALT.getBaseBlock());
        tag(UETags.Blocks.IGNEOUS_TAG).add(BlockSchemas.WEATHERED_RHYOLITE.getBaseBlock());

        tag(UETags.Blocks.METAMORPHIC_TAG).add(BlockSchemas.PHYLLITE.getBaseBlock());
        tag(UETags.Blocks.METAMORPHIC_TAG).add(BlockSchemas.SLATE.getBaseBlock());
        tag(UETags.Blocks.METAMORPHIC_TAG).add(BlockSchemas.QUARTZITE.getBaseBlock());
        tag(UETags.Blocks.METAMORPHIC_TAG).add(BlockSchemas.SCHIST.getBaseBlock());

        tag(UETags.Blocks.SEDIMENTARY_TAG).add(BlockSchemas.SILTSTONE.getBaseBlock());
        tag(UETags.Blocks.SEDIMENTARY_TAG).add(BlockSchemas.MUDSTONE.getBaseBlock());
        tag(UETags.Blocks.SEDIMENTARY_TAG).add(BlockSchemas.LIGNITE.getBaseBlock());
        tag(UETags.Blocks.SEDIMENTARY_TAG).add(BlockSchemas.LIMESTONE.getBaseBlock());
        tag(UETags.Blocks.SEDIMENTARY_TAG).add(BlockSchemas.BEIGE_LIMESTONE.getBaseBlock());
        tag(UETags.Blocks.SEDIMENTARY_TAG).add(BlockSchemas.GREY_LIMESTONE.getBaseBlock());
        tag(UETags.Blocks.SEDIMENTARY_TAG).add(BlockSchemas.CONGLOMERATE.getBaseBlock());

        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            type.entries().forEach(entry -> {
                BlockForm form = entry.getForm();
                Block block = entry.getBlock();

                if (form == Forms.BUTTON) {
                    tag(BlockTags.BUTTONS).add(block);
                } else if (form == Forms.REGOLITH) {
                    tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block);
                } else if (form == Forms.GRASSY_REGOLITH) {
                    tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block);
                    tag(BlockTags.MINEABLE_WITH_HOE).add(block);
                } else {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                }

                if (form == Forms.OVERGROWN_ROCK || form == Forms.GRASSY_REGOLITH){
                    tag(BlockTags.DIRT).add(block);
                    tag(BlockTags.ANIMALS_SPAWNABLE_ON).add(block);
                    tag(BlockTags.VALID_SPAWN).add(block);
                }

                if (form instanceof Forms.OreForm) {
                    IOreType oreType = ((Forms.OreForm) form).getOreType();
                    if (oreType == VanillaOreTypes.COAL) {
                        tag(BlockTags.COAL_ORES).add(block);
                    } else if (oreType == VanillaOreTypes.IRON) {
                        tag(BlockTags.IRON_ORES).add(block);
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (oreType == VanillaOreTypes.LAPIS) {
                        tag(BlockTags.LAPIS_ORES).add(block);
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (oreType == VanillaOreTypes.COPPER) {
                        tag(BlockTags.COPPER_ORES).add(block);
                        tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                    } else if (oreType == VanillaOreTypes.GOLD) {
                        tag(BlockTags.GOLD_ORES).add(block);
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    } else if (oreType == VanillaOreTypes.REDSTONE) {
                        tag(BlockTags.REDSTONE_ORES).add(block);
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    } else if (oreType == VanillaOreTypes.DIAMOND) {
                        tag(BlockTags.DIAMOND_ORES).add(block);
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    } else if (oreType == VanillaOreTypes.EMERALD) {
                        tag(BlockTags.EMERALD_ORES).add(block);
                        tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                    }
                } else if (form == Forms.PRESSURE_PLATE) {
                    tag(BlockTags.STONE_PRESSURE_PLATES).add(block);
                } else if (form == Forms.STAIRS) {
                    tag(BlockTags.STAIRS).add(block);
                } else if (form == Forms.WALLS) {
                    tag(BlockTags.WALLS).add(block);
                } else if (form == Forms.SLAB) {
                    tag(BlockTags.SLABS).add(block);
                } else if (form == Forms.REGOLITH || form == Forms.GRASSY_REGOLITH) {
                    tag(UETags.Blocks.REGOLITH_TAG).add(block);
                    tag(BlockTags.DIRT).add(block);
                } else if (entry.getVariant() == Variants.COBBLED && form.isBaseForm()) {
                    tag(Tags.Blocks.COBBLESTONE).add(block);
                }
            });
        }

        tag(UETags.Blocks.SEDIMENTARY_TAG).add(Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        tag(UETags.Blocks.IGNEOUS_TAG).add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.BASALT);
        tag(UETags.Blocks.METAMORPHIC_TAG).add(Blocks.BLACKSTONE);
    }
}

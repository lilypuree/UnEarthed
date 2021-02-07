package net.lilycorgitaco.unearthed.datagen;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraft.tag.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.lilycorgitaco.unearthed.Unearthed;
import net.lilycorgitaco.unearthed.block.BlockGeneratorHelper;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.block.schema.BlockSchema;
import net.lilycorgitaco.unearthed.block.schema.Forms;
import net.lilycorgitaco.unearthed.block.schema.StoneTiers;
import net.lilycorgitaco.unearthed.core.UETags;
import net.lilycorgitaco.unearthed.datagen.type.IOreType;
import net.lilycorgitaco.unearthed.datagen.type.VanillaOreTypes;

import javax.annotation.Nullable;

public class UEBlockTagsProvider extends BlockTagsProvider {
    public UEBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Unearthed.MOD_ID, existingFileHelper);
    }

    @Override
    protected void configure() {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            if (type.getTier() == StoneTiers.PRIMARY || type.getTier() == StoneTiers.SECONDARY) {
                getOrCreateTagBuilder(BlockTags.BASE_STONE_OVERWORLD).add(type.getBaseBlock());
            }
            switch (type.getClassification()) {
                case IGNEOUS:
                    getOrCreateTagBuilder(UETags.Blocks.IGNEOUS_TAG).add(type.getBaseBlock());
                    break;
                case METAMORPHIC:
                    getOrCreateTagBuilder(UETags.Blocks.METAMORPHIC_TAG).add(type.getBaseBlock());
                    break;
                case SEDIMENTARY:
                    getOrCreateTagBuilder(UETags.Blocks.SEDIMENTARY_TAG).add(type.getBaseBlock());
                    break;
            }
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();

                if (form == Forms.BUTTON) {
                    getOrCreateTagBuilder(BlockTags.BUTTONS).add(block);
                } else if (form == Forms.PRESSURE_PLATE) {
                    getOrCreateTagBuilder(BlockTags.STONE_PRESSURE_PLATES).add(block);
                } else if (form == Forms.REGOLITH || form == Forms.GRASSY_REGOLITH) {
                    getOrCreateTagBuilder(UETags.Blocks.REGOLITH_TAG).add(block);
                    if (form == Forms.GRASSY_REGOLITH) {
                        getOrCreateTagBuilder(BlockTags.VALID_SPAWN).add(block);
                    }
                } else if (form == Forms.STAIRS || form == Forms.SIDETOP_STAIRS) {
                    getOrCreateTagBuilder(BlockTags.STAIRS).add(block);
                } else if (form == Forms.WALLS) {
                    getOrCreateTagBuilder(BlockTags.WALLS).add(block);
                }
                if (form instanceof Forms.OreForm) {
                    IOreType oreType = ((Forms.OreForm) form).getOreType();
                    if (oreType == VanillaOreTypes.IRON) {
                        getOrCreateTagBuilder(UETags.Blocks.IRON_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.COAL) {
                        getOrCreateTagBuilder(UETags.Blocks.COAL_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.GOLD) {
                        getOrCreateTagBuilder(UETags.Blocks.GOLD_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.LAPIS) {
                        getOrCreateTagBuilder(UETags.Blocks.LAPIS_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.REDSTONE) {
                        getOrCreateTagBuilder(UETags.Blocks.REDSTONE_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.DIAMOND) {
                        getOrCreateTagBuilder(UETags.Blocks.DIAMOND_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.EMERALD) {
                        getOrCreateTagBuilder(UETags.Blocks.EMERALD_ORE_TAG).add(block);
                    }
                }
            }
        }
        getOrCreateTagBuilder(UETags.Blocks.SEDIMENTARY_TAG).add(Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        getOrCreateTagBuilder(UETags.Blocks.IGNEOUS_TAG).add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.BASALT);
        getOrCreateTagBuilder(UETags.Blocks.METAMORPHIC_TAG).add(Blocks.BLACKSTONE);
    }
}

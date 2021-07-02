package net.oriondevcorgitaco.unearthed.datagen;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.StoneTiers;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;
import net.oriondevcorgitaco.unearthed.core.UETags;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;

import javax.annotation.Nullable;

public class UEBlockTagsProvider extends BlockTagsProvider {
    public UEBlockTagsProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Unearthed.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            if (type.getTier() == StoneTiers.PRIMARY || type.getTier() == StoneTiers.SECONDARY) {
                tag(BlockTags.BASE_STONE_OVERWORLD).add(type.getBaseBlock());
            }
            switch (type.getClassification()) {
                case IGNEOUS:
                    tag(UETags.Blocks.IGNEOUS_TAG).add(type.getBaseBlock());
                    break;
                case METAMORPHIC:
                    tag(UETags.Blocks.METAMORPHIC_TAG).add(type.getBaseBlock());
                    break;
                case SEDIMENTARY:
                    tag(UETags.Blocks.SEDIMENTARY_TAG).add(type.getBaseBlock());
                    break;
            }
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();
                if (entry.getVariant() == Variants.COBBLED && entry.isBaseEntry()) {
                    tag(Tags.Blocks.COBBLESTONE).add(block);
                }
                if (form == Forms.BUTTON) {
                    tag(BlockTags.BUTTONS).add(block);
                } else if (form == Forms.PRESSURE_PLATE) {
                    tag(BlockTags.STONE_PRESSURE_PLATES).add(block);
                } else if (form == Forms.REGOLITH || form == Forms.GRASSY_REGOLITH) {
                    tag(UETags.Blocks.REGOLITH_TAG).add(block);
                    if (form == Forms.GRASSY_REGOLITH) {
                        tag(BlockTags.VALID_SPAWN).add(block);
                    }
                } else if (form == Forms.STAIRS || form == Forms.SIDETOP_STAIRS) {
                    tag(BlockTags.STAIRS).add(block);
                } else if (form == Forms.WALLS) {
                    tag(BlockTags.WALLS).add(block);
                }
                if (form instanceof Forms.OreForm) {
                    IOreType oreType = ((Forms.OreForm) form).getOreType();
                    if (oreType == VanillaOreTypes.IRON) {
                        tag(UETags.Blocks.IRON_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.COAL) {
                        tag(UETags.Blocks.COAL_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.GOLD) {
                        tag(UETags.Blocks.GOLD_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.LAPIS) {
                        tag(UETags.Blocks.LAPIS_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.REDSTONE) {
                        tag(UETags.Blocks.REDSTONE_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.DIAMOND) {
                        tag(UETags.Blocks.DIAMOND_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.EMERALD) {
                        tag(UETags.Blocks.EMERALD_ORE_TAG).add(block);
                    }
                }
            }
        }

        tag(UETags.Blocks.SEDIMENTARY_TAG).add(Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        tag(UETags.Blocks.IGNEOUS_TAG).add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.BASALT);
        tag(UETags.Blocks.METAMORPHIC_TAG).add(Blocks.BLACKSTONE);
    }
}

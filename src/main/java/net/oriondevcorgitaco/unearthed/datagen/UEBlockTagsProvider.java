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
    protected void registerTags() {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            if (type.getTier() == StoneTiers.PRIMARY || type.getTier() == StoneTiers.SECONDARY) {
                getOrCreateBuilder(BlockTags.BASE_STONE_OVERWORLD).add(type.getBaseBlock());
            }
            switch (type.getClassification()) {
                case IGNEOUS:
                    getOrCreateBuilder(UETags.Blocks.IGNEOUS_TAG).add(type.getBaseBlock());
                    break;
                case METAMORPHIC:
                    getOrCreateBuilder(UETags.Blocks.METAMORPHIC_TAG).add(type.getBaseBlock());
                    break;
                case SEDIMENTARY:
                    getOrCreateBuilder(UETags.Blocks.SEDIMENTARY_TAG).add(type.getBaseBlock());
                    break;
            }
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();
                if (entry.getVariant() == Variants.COBBLED && entry.isBaseEntry()) {
                    getOrCreateBuilder(Tags.Blocks.COBBLESTONE).add(block);
                }
                if (form == Forms.BUTTON) {
                    getOrCreateBuilder(BlockTags.BUTTONS).add(block);
                } else if (form == Forms.PRESSURE_PLATE) {
                    getOrCreateBuilder(BlockTags.STONE_PRESSURE_PLATES).add(block);
                } else if (form == Forms.REGOLITH || form == Forms.GRASSY_REGOLITH) {
                    getOrCreateBuilder(UETags.Blocks.REGOLITH_TAG).add(block);
                    if (form == Forms.GRASSY_REGOLITH) {
                        getOrCreateBuilder(BlockTags.VALID_SPAWN).add(block);
                    }
                } else if (form == Forms.STAIRS || form == Forms.SIDETOP_STAIRS) {
                    getOrCreateBuilder(BlockTags.STAIRS).add(block);
                } else if (form == Forms.WALLS) {
                    getOrCreateBuilder(BlockTags.WALLS).add(block);
                }
                if (form instanceof Forms.OreForm) {
                    IOreType oreType = ((Forms.OreForm) form).getOreType();
                    if (oreType == VanillaOreTypes.IRON) {
                        getOrCreateBuilder(UETags.Blocks.IRON_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.COAL) {
                        getOrCreateBuilder(UETags.Blocks.COAL_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.GOLD) {
                        getOrCreateBuilder(UETags.Blocks.GOLD_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.LAPIS) {
                        getOrCreateBuilder(UETags.Blocks.LAPIS_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.REDSTONE) {
                        getOrCreateBuilder(UETags.Blocks.REDSTONE_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.DIAMOND) {
                        getOrCreateBuilder(UETags.Blocks.DIAMOND_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.EMERALD) {
                        getOrCreateBuilder(UETags.Blocks.EMERALD_ORE_TAG).add(block);
                    }
                }
            }
        }

        getOrCreateBuilder(UETags.Blocks.SEDIMENTARY_TAG).add(Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        getOrCreateBuilder(UETags.Blocks.IGNEOUS_TAG).add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.BASALT);
        getOrCreateBuilder(UETags.Blocks.METAMORPHIC_TAG).add(Blocks.BLACKSTONE);
    }
}

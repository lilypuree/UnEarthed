package net.oriondevcorgitaco.unearthed.datagen;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.StoneTiers;
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
                    getOrCreateBuilder(BlockGeneratorReference.IGNEOUS_TAG).add(type.getBaseBlock());
                    break;
                case METAMORPHIC:
                    getOrCreateBuilder(BlockGeneratorReference.METAMORPHIC_TAG).add(type.getBaseBlock());
                    break;
                case SEDIMENTARY:
                    getOrCreateBuilder(BlockGeneratorReference.SEDIMENTARY_TAG).add(type.getBaseBlock());
                    break;
            }
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();

                if (form == Forms.BUTTON) {
                    getOrCreateBuilder(BlockTags.BUTTONS).add(block);
                } else if (form == Forms.PRESSURE_PLATE) {
                    getOrCreateBuilder(BlockTags.STONE_PRESSURE_PLATES).add(block);
                } else if (form == Forms.REGOLITH || form == Forms.GRASSY_REGOLITH) {
                    getOrCreateBuilder(BlockGeneratorReference.REGOLITH_TAG).add(block);
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
                        getOrCreateBuilder(BlockGeneratorReference.IRON_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.COAL) {
                        getOrCreateBuilder(BlockGeneratorReference.COAL_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.GOLD) {
                        getOrCreateBuilder(BlockGeneratorReference.GOLD_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.LAPIS) {
                        getOrCreateBuilder(BlockGeneratorReference.LAPIS_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.REDSTONE) {
                        getOrCreateBuilder(BlockGeneratorReference.REDSTONE_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.DIAMOND) {
                        getOrCreateBuilder(BlockGeneratorReference.DIAMOND_ORE_TAG).add(block);
                    } else if (oreType == VanillaOreTypes.EMERALD) {
                        getOrCreateBuilder(BlockGeneratorReference.EMERALD_ORE_TAG).add(block);
                    }
                }
            }
        }
        getOrCreateBuilder(BlockGeneratorReference.SEDIMENTARY_TAG).add(Blocks.SANDSTONE, Blocks.RED_SANDSTONE);
        getOrCreateBuilder(BlockGeneratorReference.IGNEOUS_TAG).add(Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.BASALT);
        getOrCreateBuilder(BlockGeneratorReference.METAMORPHIC_TAG).add(Blocks.BLACKSTONE);
    }
}

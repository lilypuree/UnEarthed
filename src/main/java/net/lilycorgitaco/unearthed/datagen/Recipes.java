package net.lilycorgitaco.unearthed.datagen;

import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import net.lilycorgitaco.unearthed.block.BlockGeneratorHelper;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.block.schema.BlockSchema;
import net.lilycorgitaco.unearthed.block.schema.Forms;
import net.lilycorgitaco.unearthed.block.schema.Variants;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import net.lilycorgitaco.unearthed.core.UETags;
import net.lilycorgitaco.unearthed.datagen.type.IOreType;
import net.lilycorgitaco.unearthed.datagen.type.VanillaOreTypes;

import java.util.function.Consumer;

public class Recipes extends RecipesProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    private Consumer<RecipeJsonProvider> consumer;

    @Override
    protected void generate(Consumer<RecipeJsonProvider> consumerIn) {
        consumer = consumerIn;
        stoneRecipe();
        for (IOreType oreType : VanillaOreTypes.values()) {
            oreType.addCookingRecipes(consumer);
        }

        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                BlockSchema.Variant variant = entry.getVariant();
                Block block = entry.getBlock();
                Block baseBlock = type.getBaseBlock(entry.getVariant());
                Block typeBaseBlock = type.getBaseBlock();

                if (form instanceof Forms.OreForm) {
//                    ((Forms.OreForm) form).getOreType().addCookingRecipes(block, consumer);
                } else if (form == Forms.SLAB || form == Forms.SIDETOP_SLAB || form == Forms.SIXWAY_SLAB) {
                    if (variant.isBaseVariant() && type.getSchema().getVariants().contains(Variants.CHISELED)) {
                        slabRecipeOf(block, baseBlock, type.getBaseBlock(Variants.CHISELED));
                    } else {
                        slabRecipeOf(block, baseBlock);
                    }
                    stoneCuttingRecipes(type, variant, form, 2);
                } else if (form == Forms.STAIRS || form == Forms.SIDETOP_STAIRS) {
                    stairRecipeOf(block, baseBlock);
                    stoneCuttingRecipes(type, variant, form, 1);
                } else if (form == Forms.WALLS) {
                    wallRecipeOf(block, baseBlock);
                    stoneCuttingRecipes(type, variant, form, 1);
                } else if (form == Forms.PRESSURE_PLATE) {
                    pressurePlateRecipeOf(type, variant);
                } else if (form == Forms.BUTTON) {
                    buttonRecipeOf(type, variant);
                } else if (form == Forms.REGOLITH) {
                    regolithConversionRecipe(block, typeBaseBlock);
                } else if (entry.isBaseEntry()) {
                    if (type.getSchema().getVariants().contains(Variants.COBBLED)) {
                        stoneSmeltingRecipeOf(block, type.getBaseBlock(Variants.COBBLED));
                    }
                } else if (form.isBaseForm()) {
                    stoneCuttingRecipes(type, variant, form, 1);
                    if (variant == Variants.BRICK || variant == Variants.BRICKS || variant == Variants.POLISHED || variant == Variants.CUT || variant == Variants.POLISHED_PILLAR || variant == Variants.POLISHED_NOWALL) {
                        brickRecipeOf(block, typeBaseBlock);
                    } else if (variant == Variants.POLISHED_BRICKS){
                        brickRecipeOf(block, type.getBaseBlock(Variants.POLISHED));
                    } else if (variant == Variants.SMOOTH) {
                        stoneSmeltingRecipeOf(block, typeBaseBlock);
                    } else if (variant == Variants.CHISELED || variant == Variants.CHISELED_FULL) {
                        BlockGeneratorHelper.Entry form1 = type.getEntry(type.getBaseEntry().getVariant(), Forms.SLAB);
                        BlockGeneratorHelper.Entry form2 = type.getEntry(type.getBaseEntry().getVariant(), Forms.SIDETOP_SLAB);
                        chiseledRecipeOf(block, (form1 == null) ? form2.getBlock() : form1.getBlock(), typeBaseBlock);
                    } else if (variant == Variants.CHISELED_BRICKS) {
                        chiseledRecipeOf(block, type.getEntry(Variants.BRICKS, Forms.SLAB).getBlock(), type.getBaseBlock(Variants.BRICKS));
                    } else if (variant == Variants.CHISELED_POLISHED) {
                        chiseledRecipeOf(block, type.getEntry(Variants.POLISHED, Forms.SLAB).getBlock(), type.getBaseBlock(Variants.POLISHED));
                    } else if (variant.getForms().contains(Forms.AXISBLOCK) || variant == Variants.PILLAR_BLOCK) {
                        pillarRecipeOf(block, typeBaseBlock, typeBaseBlock);
                    } else if (variant == Variants.MOSSY_COBBLED) {
                        mossyRecipe(type.getBaseBlock(Variants.COBBLED), block);
                    } else if (variant == Variants.MOSSY_BRICKS) {
                        mossyRecipe(type.getBaseBlock(Variants.BRICKS), block);
                    } else if (variant == Variants.CRACKED_BRICKS) {
                        crackedRecipe(type.getBaseBlock(Variants.BRICKS), block);
                    } else if (variant == Variants.CRACKED_POLISHED_BRICKS) {
                        crackedRecipe(type.getBaseBlock(Variants.POLISHED_BRICKS), block);
                    }
                }
            }
        }
        ShapelessRecipeJsonFactory.create(Blocks.GRAVEL).input(UEBlocks.PYROXENE).criterion("has_pyroxene", conditionsFromItem(UEBlocks.PYROXENE)).offerTo(consumer);
        ShapedRecipeJsonFactory.create(UEBlocks.LIGNITE_BRIQUETTES).input('#', BlockGeneratorReference.LIGNITE.getBaseBlock())
                .input('*', Blocks.CLAY)
                .pattern("###").pattern("#*#").pattern("###").criterion("has_lignite", conditionsFromItem(BlockGeneratorReference.LIGNITE.getBaseBlock())).offerTo(consumer);
    }

    private void regolithConversionRecipe(ItemConvertible result, ItemConvertible baseBlock) {
        ShapelessRecipeJsonFactory.create(result, 8)
                .input(Ingredient.fromTag(UETags.Items.REGOLITH_TAG), 8)
                .input(baseBlock)
                .criterion("has_" + getPath(baseBlock), conditionsFromItem(baseBlock)).offerTo(consumer);
    }

    private void mossyRecipe(Block baseBlock, Block mossyBlock) {
        ShapelessRecipeJsonFactory.create(mossyBlock).input(baseBlock).input(Items.VINE)
                .criterion("has_" + getPath(mossyBlock), conditionsFromItem(mossyBlock)).offerTo(consumer);
    }

    private void crackedRecipe(Block baseBlock, Block crackedBlock) {
        CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(baseBlock), crackedBlock, 0.1f, 200)
                .criterion("has_" + getPath(baseBlock), conditionsFromItem(baseBlock)).offerTo(consumer);
    }

    private void stoneRecipe() {
        ShapelessRecipeJsonFactory.create(Items.STONE).input(UETags.Items.IGNEOUS_ITEM).input(UETags.Items.METAMORPHIC_ITEM).input(UETags.Items.SEDIMENTARY_ITEM).criterion("has_stone", conditionsFromItem(Items.STONE)).offerTo(consumer);
    }

    private void brickRecipeOf(ItemConvertible result, ItemConvertible ingredient) {
        ShapedRecipeJsonFactory.create(result, 4).input('#', ingredient).pattern("##").pattern("##")
                .criterion("has_" + getPath(ingredient), conditionsFromItem(ingredient)).offerTo(consumer);
    }

    private void chiseledRecipeOf(ItemConvertible result, ItemConvertible ingredient, ItemConvertible trigger) {
        ShapedRecipeJsonFactory.create(result, 1).input('#', ingredient).pattern("#").pattern("#")
                .criterion("has_" + getPath(ingredient), conditionsFromItem(trigger)).offerTo(consumer);
    }

    private void pillarRecipeOf(ItemConvertible result, ItemConvertible ingredient, ItemConvertible trigger) {
        ShapedRecipeJsonFactory.create(result, 2).input('#', ingredient).pattern("#").pattern("#")
                .criterion("has_" + getPath(ingredient), conditionsFromItem(trigger)).offerTo(consumer);
    }

    private void wallRecipeOf(ItemConvertible result, ItemConvertible ingredient) {
        ShapedRecipeJsonFactory.create(result, 6).input('#', ingredient).pattern("###").pattern("###")
                .criterion("has_" + getPath(ingredient), conditionsFromItem(ingredient)).offerTo(consumer);
    }

    private void stairRecipeOf(ItemConvertible result, ItemConvertible ingredient) {
        ShapedRecipeJsonFactory.create(result, 4).input('#', ingredient).pattern("#  ").pattern("## ").pattern("###")
                .criterion("has_" + getPath(ingredient), conditionsFromItem(ingredient)).offerTo(consumer);
    }

    private void slabRecipeOf(ItemConvertible result, ItemConvertible ingredient) {
        ShapedRecipeJsonFactory.create(result, 6).input('#', ingredient).pattern("###")
                .criterion("has_" + getPath(ingredient), conditionsFromItem(ingredient)).offerTo(consumer);
    }

    private void slabRecipeOf(ItemConvertible result, ItemConvertible... ingredients) {
        ShapedRecipeJsonFactory builder = ShapedRecipeJsonFactory.create(result, 6).input('#', Ingredient.ofItems(ingredients)).pattern("###");
        for (ItemConvertible provider : ingredients) {
            builder.criterion("has_" + getPath(provider), conditionsFromItem(provider));
        }
        builder.offerTo(consumer);
    }


    private void buttonRecipeOf(BlockGeneratorHelper type, BlockSchema.Variant variant) {
        BlockGeneratorHelper.Entry button = type.getEntry(variant, Forms.BUTTON);
        if (button == null) return;
        Block baseBlock = type.getBaseBlock(variant);
        ShapelessRecipeJsonFactory.create(button.getBlock()).input(baseBlock)
                .criterion("has_" + getPath(baseBlock), conditionsFromItem(baseBlock)).offerTo(consumer);
    }

    private void pressurePlateRecipeOf(BlockGeneratorHelper type, BlockSchema.Variant variant) {
        BlockGeneratorHelper.Entry pressurePlate = type.getEntry(variant, Forms.PRESSURE_PLATE);
        if (pressurePlate == null) return;
        Block baseBlock = type.getBaseBlock(variant);
        ShapedRecipeJsonFactory.create(pressurePlate.getBlock())
                .pattern("##")
                .input('#', baseBlock)
                .criterion("has_" + getPath(baseBlock), conditionsFromItem(baseBlock)).offerTo(consumer);
    }

    private void stoneSmeltingRecipeOf(ItemConvertible result, ItemConvertible ingredient) {
        CookingRecipeJsonFactory.createSmelting(Ingredient.ofItems(ingredient), result, 0.1F, 200)
                .criterion("has_" + getPath(ingredient), conditionsFromItem(ingredient)).offerTo(consumer);
    }

//    private List<BlockSchema.Variant> derivativeStones = Lists.newArrayList(Variants.BRICK, Variants. Variants.BRICKS, Variants.CHISELED_BRICKS, Variants.CHISELED, Variants.CUT, Variants.POLISHED, Variants.POLISHED_NOWALL, Variants.POLISHED_BRICKS, Variants.CHISELED_POLISHED);

//    private List<BlockSchema.Variant> nonDerivativeStones = Lists.newArrayList(Variants.SMOOTH, Variants.COBBLED, Variants.MOSSY_COBBLED, Variants.MOSSY_BRICKS, Variants.SEDIMENTARY, Variants.BASIC, Variants.STONE_LIKE, Variants.SECONDARY_STONE, Variants.PILLAR, Variants.);
//    private List<BlockSchema.Variant> derivativeStones = Lists.newArrayList(Variants.SMOOTH, Variants.COBBLED, Variants.MOSSY_COBBLED, Variants.MOSSY_BRICKS, Variants.SEDIMENTARY, Variants.BASIC, Variants.STONE_LIKE, Variants.SECONDARY_STONE, Variants.PILLAR, Variants.);

    private void stoneCuttingRecipes(BlockGeneratorHelper type, BlockSchema.Variant variant, BlockSchema.Form form, int count) {
        ItemConvertible result = type.getEntry(variant, form).getBlock();
        if (!form.isBaseForm()) {
            singleStoneCuttingRecipe(result, count, type.getBaseBlock(variant));
        }
        if (variant.isDerivative()) {
            singleStoneCuttingRecipe(result, count, type.getBaseBlock());
        }
        if (variant == Variants.POLISHED_BRICKS || variant == Variants.CHISELED_POLISHED) {
            singleStoneCuttingRecipe(result, count, type.getBaseBlock(Variants.POLISHED));
        }
        if (variant == Variants.CHISELED_BRICKS) {
            singleStoneCuttingRecipe(result, count, type.getBaseBlock(Variants.BRICKS));
        }
    }

    private void singleStoneCuttingRecipe(ItemConvertible result, int count, ItemConvertible ingredient) {
        SingleItemRecipeJsonFactory.create(Ingredient.ofItems(ingredient), result, count)
                .create("has_" + getPath(ingredient), conditionsFromItem(ingredient)).offerTo(consumer, getPath(result) + "_from_" + getPath(ingredient) + "_stonecutting");
    }

    private String getPath(ItemConvertible ingredient) {
        return ingredient.asItem().getRegistryName().getPath();
    }

    /**
     * Creates a new {@link InventoryChangeTrigger} that checks for a player having a certain item.
     */
    public static InventoryChangedCriterion.Conditions conditionsFromItem(ItemConvertible itemProvider) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.create().item(itemProvider).build());
    }

    public static InventoryChangedCriterion.Conditions hasTaggedItem(Tag<Item> tag) {
        return conditionsFromItemPredicates(ItemPredicate.Builder.create().tag(tag).build());
    }
}
package net.oriondevcorgitaco.unearthed.datagen;

import com.google.common.collect.Lists;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;

import java.util.List;
import java.util.function.Consumer;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    private Consumer<IFinishedRecipe> consumer;

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumerIn) {
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
                } else if (form == Forms.SLAB || form == Forms.SIDETOP_SLAB) {
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
                } else if (entry.isBaseEntry()) {
                    if (type.getSchema().getVariants().contains(Variants.COBBLED)) {
                        stoneSmeltingRecipeOf(block, type.getBaseBlock(Variants.COBBLED));
                    }
                } else if (form.isBaseForm()) {
                    stoneCuttingRecipes(type, variant, form, 1);
                    if (variant == Variants.BRICK || variant == Variants.BRICKS || variant == Variants.POLISHED || variant == Variants.CUT || variant == Variants.POLISHED_PILLAR || variant == Variants.POLISHED_NOWALL) {
                        brickRecipeOf(block, typeBaseBlock);
                    } else if (variant == Variants.SMOOTH) {
                        stoneSmeltingRecipeOf(block, typeBaseBlock);
                    } else if (variant == Variants.CHISELED) {
                        chiseledRecipeOf(block, type.getEntry(type.getBaseEntry().getVariant(), Forms.SLAB).getBlock(), typeBaseBlock);
                    } else if (variant == Variants.CHISELED_BRICKS) {
                        chiseledRecipeOf(block, type.getEntry(Variants.BRICKS, Forms.SLAB).getBlock(), type.getBaseBlock(Variants.BRICKS));
                    } else if (variant == Variants.CHISELED_POLISHED) {
                        chiseledRecipeOf(block, type.getEntry(Variants.POLISHED, Forms.SLAB).getBlock(), type.getBaseBlock(Variants.POLISHED));
                    } else if (variant == Variants.PILLAR) {
                        pillarRecipeOf(block, typeBaseBlock, typeBaseBlock);
                    }
                }
            }
        }
    }

    private void stoneRecipe() {
        ShapelessRecipeBuilder.shapelessRecipe(Items.STONE).addIngredient(BlockGeneratorReference.IGNEOUS_ITEM).addIngredient(BlockGeneratorReference.METAMORPHIC_ITEM).addIngredient(BlockGeneratorReference.SEDIMENTARY_ITEM).addCriterion("has_stone", hasItem(Items.STONE)).build(consumer);
    }

    private void brickRecipeOf(IItemProvider result, IItemProvider ingredient) {
        ShapedRecipeBuilder.shapedRecipe(result, 4).key('#', ingredient).patternLine("##").patternLine("##")
                .addCriterion("has_" + getPath(ingredient), hasItem(ingredient)).build(consumer);
    }

    private void chiseledRecipeOf(IItemProvider result, IItemProvider ingredient, IItemProvider trigger) {
        ShapedRecipeBuilder.shapedRecipe(result, 1).key('#', ingredient).patternLine("#").patternLine("#")
                .addCriterion("has_" + getPath(ingredient), hasItem(trigger)).build(consumer);
    }

    private void pillarRecipeOf(IItemProvider result, IItemProvider ingredient, IItemProvider trigger) {
        ShapedRecipeBuilder.shapedRecipe(result, 2).key('#', ingredient).patternLine("#").patternLine("#")
                .addCriterion("has_" + getPath(ingredient), hasItem(trigger)).build(consumer);
    }

    private void wallRecipeOf(IItemProvider result, IItemProvider ingredient) {
        ShapedRecipeBuilder.shapedRecipe(result, 6).key('#', ingredient).patternLine("###").patternLine("###")
                .addCriterion("has_" + getPath(ingredient), hasItem(ingredient)).build(consumer);
    }

    private void stairRecipeOf(IItemProvider result, IItemProvider ingredient) {
        ShapedRecipeBuilder.shapedRecipe(result, 4).key('#', ingredient).patternLine("#  ").patternLine("## ").patternLine("###")
                .addCriterion("has_" + getPath(ingredient), hasItem(ingredient)).build(consumer);
    }

    private void slabRecipeOf(IItemProvider result, IItemProvider ingredient) {
        ShapedRecipeBuilder.shapedRecipe(result, 6).key('#', ingredient).patternLine("###")
                .addCriterion("has_" + getPath(ingredient), hasItem(ingredient)).build(consumer);
    }

    private void slabRecipeOf(IItemProvider result, IItemProvider... ingredients) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shapedRecipe(result, 6).key('#', Ingredient.fromItems(ingredients)).patternLine("###");
        for (IItemProvider provider : ingredients) {
            builder.addCriterion("has_" + getPath(provider), hasItem(provider));
        }
        builder.build(consumer);
    }


    private void buttonRecipeOf(BlockGeneratorHelper type, BlockSchema.Variant variant) {
        BlockGeneratorHelper.Entry button = type.getEntry(variant, Forms.BUTTON);
        if (button == null) return;
        Block baseBlock = type.getBaseBlock(variant);
        ShapelessRecipeBuilder.shapelessRecipe(button.getBlock()).addIngredient(baseBlock)
                .addCriterion("has_" + getPath(baseBlock), hasItem(baseBlock)).build(consumer);
    }

    private void pressurePlateRecipeOf(BlockGeneratorHelper type, BlockSchema.Variant variant) {
        BlockGeneratorHelper.Entry pressurePlate = type.getEntry(variant, Forms.PRESSURE_PLATE);
        if (pressurePlate == null) return;
        Block baseBlock = type.getBaseBlock(variant);
        ShapedRecipeBuilder.shapedRecipe(pressurePlate.getBlock())
                .patternLine("##")
                .key('#', baseBlock)
                .addCriterion("has_" + getPath(baseBlock), hasItem(baseBlock)).build(consumer);
    }

    private void stoneSmeltingRecipeOf(IItemProvider result, IItemProvider ingredient) {
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromItems(ingredient), result, 0.1F, 200)
                .addCriterion("has_" + getPath(ingredient), hasItem(ingredient)).build(consumer);
    }

//    private List<BlockSchema.Variant> derivativeStones = Lists.newArrayList(Variants.BRICK, Variants. Variants.BRICKS, Variants.CHISELED_BRICKS, Variants.CHISELED, Variants.CUT, Variants.POLISHED, Variants.POLISHED_NOWALL, Variants.POLISHED_BRICKS, Variants.CHISELED_POLISHED);

    private List<BlockSchema.Variant> nonDerivativeStones = Lists.newArrayList(Variants.SMOOTH, Variants.COBBLED, Variants.MOSSY_COBBLED, Variants.MOSSY_BRICKS, Variants.SEDIMENTARY, Variants.BASIC, Variants.STONE_LIKE, Variants.SECONDARY_STONE, Variants.PILLAR);

    private void stoneCuttingRecipes(BlockGeneratorHelper type, BlockSchema.Variant variant, BlockSchema.Form form, int count) {
        IItemProvider result = type.getEntry(variant, form).getBlock();
        if (!form.isBaseForm()) {
            singleStoneCuttingRecipe(result, count, type.getBaseBlock(variant));
        }
        if (!nonDerivativeStones.contains(variant)) {
            singleStoneCuttingRecipe(result, count, type.getBaseBlock());
        }
        if (variant == Variants.POLISHED_BRICKS || variant == Variants.CHISELED_POLISHED) {
            singleStoneCuttingRecipe(result, count, type.getBaseBlock(Variants.POLISHED));
        }
        if (variant == Variants.CHISELED_BRICKS) {
            singleStoneCuttingRecipe(result, count, type.getBaseBlock(Variants.BRICKS));
        }
    }

    private void singleStoneCuttingRecipe(IItemProvider result, int count, IItemProvider ingredient) {
        SingleItemRecipeBuilder.stonecuttingRecipe(Ingredient.fromItems(ingredient), result, count)
                .addCriterion("has_" + getPath(ingredient), hasItem(ingredient)).build(consumer, getPath(result) + "_from_" + getPath(ingredient) + "_stonecutting");
    }

    private String getPath(IItemProvider ingredient) {
        return ingredient.asItem().getRegistryName().getPath();
    }

    /**
     * Creates a new {@link InventoryChangeTrigger} that checks for a player having a certain item.
     */
    public static InventoryChangeTrigger.Instance hasItem(IItemProvider itemProvider) {
        return hasItem(ItemPredicate.Builder.create().item(itemProvider).build());
    }

    public static InventoryChangeTrigger.Instance hasTaggedItem(ITag<Item> tag) {
        return hasItem(ItemPredicate.Builder.create().tag(tag).build());
    }
}
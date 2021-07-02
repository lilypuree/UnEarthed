package net.oriondevcorgitaco.unearthed.datagen;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import net.oriondevcorgitaco.unearthed.core.UETags;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;

import java.util.function.Consumer;

import static net.oriondevcorgitaco.unearthed.block.schema.Variants.STONE_LIKE;

public class Recipes extends RecipeProvider {
    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    private Consumer<IFinishedRecipe> consumer;

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumerIn) {
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
                    } else if (variant == Variants.POLISHED_BRICKS) {
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
        ShapelessRecipeBuilder.shapeless(Blocks.GRAVEL).requires(UEBlocks.PYROXENE).unlockedBy("has_pyroxene", hasItem(UEBlocks.PYROXENE)).save(consumer, "unearthed:gravel_from_pyroxene");
        ShapedRecipeBuilder.shaped(UEBlocks.LIGNITE_BRIQUETTES).define('#', BlockGeneratorReference.LIGNITE.getBaseBlock())
                .define('*', Blocks.CLAY)
                .pattern("###").pattern("#*#").pattern("###").unlockedBy("has_lignite", hasItem(BlockGeneratorReference.LIGNITE.getBaseBlock())).save(consumer);

        mixingRecipe(BlockGeneratorReference.SILTSTONE.getBaseBlock(), Blocks.SAND, Blocks.COARSE_DIRT);
        mixingRecipe(BlockGeneratorReference.MUDSTONE.getBaseBlock(), Blocks.RED_SAND, Blocks.TERRACOTTA);
        mixingRecipe(BlockGeneratorReference.CONGLOMERATE.getBaseBlock(), Blocks.GRAVEL, Blocks.COARSE_DIRT);
        addingRecipe(BlockGeneratorReference.GRANODIORITE.getBaseBlock(), Blocks.GRANITE, Blocks.DIORITE);
        addingRecipe(BlockGeneratorReference.RHYOLITE.getBaseBlock(), Blocks.ANDESITE, Items.QUARTZ);
        addingRecipe(BlockGeneratorReference.GABBRO.getBaseBlock(), Blocks.DIORITE, UEItems.PYROXENE);
        mixingRecipe(BlockGeneratorReference.WEATHERED_RHYOLITE.getBaseBlock(), BlockGeneratorReference.RHYOLITE.getBaseBlock(), BlockGeneratorReference.RHYOLITE.getEntry(STONE_LIKE, Forms.REGOLITH).getBlock());
        stoneSmeltingRecipeOf(BlockGeneratorReference.SLATE.getBaseBlock(), Blocks.SMOOTH_STONE);
        stoneSmeltingRecipeOf(BlockGeneratorReference.PHYLLITE.getBaseBlock(), BlockGeneratorReference.SLATE.getBaseBlock());
        stoneSmeltingRecipeOf(BlockGeneratorReference.SCHIST.getBaseBlock(), BlockGeneratorReference.PHYLLITE.getBaseBlock());
        stoneSmeltingRecipeOf(BlockGeneratorReference.QUARTZITE.getBaseBlock(),Blocks.SMOOTH_SANDSTONE);
        stoneSmeltingRecipeOf(BlockGeneratorReference.QUARTZITE.getBaseBlock(),Blocks.SMOOTH_RED_SANDSTONE);
        stoneSmeltingRecipeOf(BlockGeneratorReference.MARBLE.getBaseBlock(),BlockGeneratorReference.LIMESTONE.getBaseBlock());
        stoneSmeltingRecipeOf(BlockGeneratorReference.MARBLE.getBaseBlock(),BlockGeneratorReference.GREY_LIMESTONE.getBaseBlock());
        stoneSmeltingRecipeOf(BlockGeneratorReference.MARBLE.getBaseBlock(),BlockGeneratorReference.BEIGE_LIMESTONE.getBaseBlock());
        addingRecipe(BlockGeneratorReference.BEIGE_LIMESTONE.getBaseBlock(), BlockGeneratorReference.LIMESTONE.getBaseBlock(), Blocks.SAND);
        addingRecipe(BlockGeneratorReference.GREY_LIMESTONE.getBaseBlock(), BlockGeneratorReference.LIMESTONE.getBaseBlock(), Blocks.SOUL_SAND);
        stoneSmeltingRecipeOf(BlockGeneratorReference.BEIGE_LIMESTONE.getBaseBlock(), Blocks.DEAD_HORN_CORAL_BLOCK);
        stoneSmeltingRecipeOf(BlockGeneratorReference.GREY_LIMESTONE.getBaseBlock(), Blocks.DEAD_TUBE_CORAL);
        stoneSmeltingRecipeOf(BlockGeneratorReference.LIMESTONE.getBaseBlock(), Blocks.DEAD_BUBBLE_CORAL);
        stoneSmeltingRecipeOf(BlockGeneratorReference.LIMESTONE.getBaseBlock(), Blocks.DEAD_FIRE_CORAL);
        stoneSmeltingRecipeOf(BlockGeneratorReference.DOLOMITE.getBaseBlock(), Blocks.DEAD_BRAIN_CORAL);
    }

    private void regolithConversionRecipe(IItemProvider result, IItemProvider baseBlock) {
        ShapelessRecipeBuilder.shapeless(result, 8)
                .requires(Ingredient.of(UETags.Items.REGOLITH_TAG), 8)
                .requires(baseBlock)
                .unlockedBy("has_" + getPath(baseBlock), hasItem(baseBlock)).save(consumer);
    }

    private void mixingRecipe(IItemProvider result, IItemProvider ingredient1, IItemProvider ingredient2) {
        ShapedRecipeBuilder.shaped(result, 4)
                .pattern("ox").pattern("xo").define('x', ingredient1).define('o', ingredient2)
                .unlockedBy("has_" + getPath(result), hasItem(result)).save(consumer);
    }

    private void addingRecipe(IItemProvider result, IItemProvider ingredient1, IItemProvider ingredient2) {
        ShapedRecipeBuilder.shaped(result, 4)
                .pattern("ox").define('x', ingredient1).define('o', ingredient2)
                .unlockedBy("has_" + getPath(result), hasItem(result)).save(consumer);
    }

    private void mossyRecipe(Block baseBlock, Block mossyBlock) {
        ShapelessRecipeBuilder.shapeless(mossyBlock).requires(baseBlock).requires(Items.VINE)
                .unlockedBy("has_" + getPath(mossyBlock), hasItem(mossyBlock)).save(consumer);
    }

    private void crackedRecipe(Block baseBlock, Block crackedBlock) {
        CookingRecipeBuilder.smelting(Ingredient.of(baseBlock), crackedBlock, 0.1f, 200)
                .unlockedBy("has_" + getPath(baseBlock), hasItem(baseBlock)).save(consumer);
    }

    private void stoneRecipe() {
        ShapelessRecipeBuilder.shapeless(Items.STONE).requires(UETags.Items.IGNEOUS_ITEM).requires(UETags.Items.METAMORPHIC_ITEM).requires(UETags.Items.SEDIMENTARY_ITEM).unlockedBy("has_stone", hasItem(Items.STONE)).save(consumer, "unearthed:stone_from_stones");
    }

    private void brickRecipeOf(IItemProvider result, IItemProvider ingredient) {
        ShapedRecipeBuilder.shaped(result, 4).define('#', ingredient).pattern("##").pattern("##")
                .unlockedBy("has_" + getPath(ingredient), hasItem(ingredient)).save(consumer);
    }

    private void chiseledRecipeOf(IItemProvider result, IItemProvider ingredient, IItemProvider trigger) {
        ShapedRecipeBuilder.shaped(result, 1).define('#', ingredient).pattern("#").pattern("#")
                .unlockedBy("has_" + getPath(ingredient), hasItem(trigger)).save(consumer);
    }

    private void pillarRecipeOf(IItemProvider result, IItemProvider ingredient, IItemProvider trigger) {
        ShapedRecipeBuilder.shaped(result, 2).define('#', ingredient).pattern("#").pattern("#")
                .unlockedBy("has_" + getPath(ingredient), hasItem(trigger)).save(consumer);
    }

    private void wallRecipeOf(IItemProvider result, IItemProvider ingredient) {
        ShapedRecipeBuilder.shaped(result, 6).define('#', ingredient).pattern("###").pattern("###")
                .unlockedBy("has_" + getPath(ingredient), hasItem(ingredient)).save(consumer);
    }

    private void stairRecipeOf(IItemProvider result, IItemProvider ingredient) {
        ShapedRecipeBuilder.shaped(result, 4).define('#', ingredient).pattern("#  ").pattern("## ").pattern("###")
                .unlockedBy("has_" + getPath(ingredient), hasItem(ingredient)).save(consumer);
    }

    private void slabRecipeOf(IItemProvider result, IItemProvider ingredient) {
        ShapedRecipeBuilder.shaped(result, 6).define('#', ingredient).pattern("###")
                .unlockedBy("has_" + getPath(ingredient), hasItem(ingredient)).save(consumer);
    }

    private void slabRecipeOf(IItemProvider result, IItemProvider... ingredients) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(result, 6).define('#', Ingredient.of(ingredients)).pattern("###");
        for (IItemProvider provider : ingredients) {
            builder.unlockedBy("has_" + getPath(provider), hasItem(provider));
        }
        builder.save(consumer);
    }


    private void buttonRecipeOf(BlockGeneratorHelper type, BlockSchema.Variant variant) {
        BlockGeneratorHelper.Entry button = type.getEntry(variant, Forms.BUTTON);
        if (button == null) return;
        Block baseBlock = type.getBaseBlock(variant);
        ShapelessRecipeBuilder.shapeless(button.getBlock()).requires(baseBlock)
                .unlockedBy("has_" + getPath(baseBlock), hasItem(baseBlock)).save(consumer);
    }

    private void pressurePlateRecipeOf(BlockGeneratorHelper type, BlockSchema.Variant variant) {
        BlockGeneratorHelper.Entry pressurePlate = type.getEntry(variant, Forms.PRESSURE_PLATE);
        if (pressurePlate == null) return;
        Block baseBlock = type.getBaseBlock(variant);
        ShapedRecipeBuilder.shaped(pressurePlate.getBlock())
                .pattern("##")
                .define('#', baseBlock)
                .unlockedBy("has_" + getPath(baseBlock), hasItem(baseBlock)).save(consumer);
    }

    private void stoneSmeltingRecipeOf(IItemProvider result, IItemProvider ingredient) {
        String name = Unearthed.MOD_ID + ":" + result.asItem().getRegistryName().getPath() + "_from_" + ingredient.asItem().getRegistryName().getPath();
        CookingRecipeBuilder.smelting(Ingredient.of(ingredient), result, 0.1F, 200)
                .unlockedBy("has_" + getPath(ingredient), hasItem(ingredient)).save(consumer, name);
    }


//    private List<BlockSchema.Variant> derivativeStones = Lists.newArrayList(Variants.BRICK, Variants. Variants.BRICKS, Variants.CHISELED_BRICKS, Variants.CHISELED, Variants.CUT, Variants.POLISHED, Variants.POLISHED_NOWALL, Variants.POLISHED_BRICKS, Variants.CHISELED_POLISHED);

//    private List<BlockSchema.Variant> nonDerivativeStones = Lists.newArrayList(Variants.SMOOTH, Variants.COBBLED, Variants.MOSSY_COBBLED, Variants.MOSSY_BRICKS, Variants.SEDIMENTARY, Variants.BASIC, Variants.STONE_LIKE, Variants.SECONDARY_STONE, Variants.PILLAR, Variants.);
//    private List<BlockSchema.Variant> derivativeStones = Lists.newArrayList(Variants.SMOOTH, Variants.COBBLED, Variants.MOSSY_COBBLED, Variants.MOSSY_BRICKS, Variants.SEDIMENTARY, Variants.BASIC, Variants.STONE_LIKE, Variants.SECONDARY_STONE, Variants.PILLAR, Variants.);

    private void stoneCuttingRecipes(BlockGeneratorHelper type, BlockSchema.Variant variant, BlockSchema.Form form, int count) {
        IItemProvider result = type.getEntry(variant, form).getBlock();
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

    private void singleStoneCuttingRecipe(IItemProvider result, int count, IItemProvider ingredient) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ingredient), result, count)
                .unlocks("has_" + getPath(ingredient), hasItem(ingredient)).save(consumer, getPath(result) + "_from_" + getPath(ingredient) + "_stonecutting");
    }

    private String getPath(IItemProvider ingredient) {
        return ingredient.asItem().getRegistryName().getPath();
    }

    /**
     * Creates a new {@link InventoryChangeTrigger} that checks for a player having a certain item.
     */
    public static InventoryChangeTrigger.Instance hasItem(IItemProvider itemProvider) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemProvider).build());
    }

    public static InventoryChangeTrigger.Instance hasTaggedItem(ITag<Item> tag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(tag).build());
    }
}
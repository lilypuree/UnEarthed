package lilypuree.unearthed_forge.datagen;

import com.google.common.collect.ImmutableMap;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.*;
import lilypuree.unearthed.block.type.IOreType;
import lilypuree.unearthed.core.UEBlocks;
import lilypuree.unearthed.core.UEItems;
import lilypuree.unearthed.core.UETags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class UERecipes extends RecipeProviderAccessor {
    public UERecipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    private Consumer<FinishedRecipe> consumer;

    private static final Map<BlockForm, Integer> stoneCutterCount = ImmutableMap.<BlockForm, Integer>builder()
            .put(Forms.SLAB, 2)
            .put(Forms.STAIRS, 1)
            .put(Forms.WALLS, 1).build();
    private static final Map<BlockForm, BiFunction<ItemLike, ItemLike, RecipeBuilder>> shapeBuilders = ImmutableMap.<BlockForm, BiFunction<ItemLike, ItemLike, RecipeBuilder>>builder()
            .put(Forms.SLAB, (result, material) -> slabBuilder(result, Ingredient.of(material)))
            .put(Forms.SIXWAY_SLAB, (result, material) -> slabBuilder(result, Ingredient.of(material)))
            .put(Forms.BUTTON, (result, material) -> buttonBuilder(result, Ingredient.of(material)))
            .put(Forms.WALLS, (result, material) -> wallBuilder(result, Ingredient.of(material)))
            .put(Forms.STAIRS, (result, material) -> stairBuilder(result, Ingredient.of(material)))
            .put(Forms.PRESSURE_PLATE, (result, material) -> pressurePlateBuilder(result, Ingredient.of(material)))
            .put(Forms.REGOLITH, UERecipes::regolithConversionRecipe)
            .build();
    private Map<BlockVariant, BiConsumer<ItemLike, ItemLike>> baseShapeBuilders = ImmutableMap.<BlockVariant, BiConsumer<ItemLike, ItemLike>>builder()
            .put(Variants.BRICK, this::brickRecipeOf)
            .put(Variants.BRICKS, this::brickRecipeOf)
            .put(Variants.POLISHED, this::brickRecipeOf)
            .put(Variants.CUT, this::brickRecipeOf)
            .put(Variants.SMOOTH, this::stoneSmeltingRecipeOf)
            .put(Variants.POLISHED_PILLAR, this::brickRecipeOf)
            .put(Variants.POLISHED_NOWALL, this::brickRecipeOf)
            .put(Variants.POLISHED_BRICKS, this::brickRecipeOf)
            .put(Variants.CHISELED, this::chiseledRecipeOf)
            .put(Variants.CHISELED_FULL, this::chiseledRecipeOf)
            .put(Variants.CHISELED_BRICKS, this::chiseledRecipeOf)
            .put(Variants.CHISELED_POLISHED, this::chiseledRecipeOf)
            .put(Variants.PILLAR_BLOCK, this::pillarRecipeOf)
            .put(Variants.MOSSY_COBBLED, this::mossyRecipe)
            .put(Variants.MOSSY_BRICKS, this::mossyRecipe)
            .put(Variants.CRACKED_BRICKS, this::crackedRecipe)
            .put(Variants.CRACKED_POLISHED_BRICKS, this::crackedRecipe)
            .build();


    private static Block getBaseBlock(BlockSchema schema, SchemaEntry entry) {
        BlockVariant variant = entry.getVariant();
        if (!entry.getForm().isBaseForm()) {
            return schema.getBaseBlock(variant);
        } else if (variant == Variants.POLISHED_BRICKS) {
            return schema.getBaseBlock(Variants.POLISHED);
        } else if (variant == Variants.MOSSY_COBBLED) {
            return schema.getBaseBlock(Variants.COBBLED);
        } else if (variant == Variants.MOSSY_BRICKS || variant == Variants.CRACKED_BRICKS) {
            return schema.getBaseBlock(Variants.BRICKS);
        } else if (variant == Variants.CRACKED_POLISHED_BRICKS) {
            return schema.getBaseBlock(Variants.POLISHED_BRICKS);
        } else if (variant == Variants.CHISELED || variant == Variants.CHISELED_FULL) {
            return schema.getEntry(schema.getBaseEntry().getVariant(), Forms.SLAB).getBlock();
        } else if (variant == Variants.CHISELED_BRICKS) {
            return schema.getEntry(Variants.BRICKS, Forms.SLAB).getBlock();
        } else if (variant == Variants.CHISELED_POLISHED) {
            return schema.getEntry(Variants.POLISHED, Forms.SLAB).getBlock();
        } else {
            return schema.getBaseBlock();
        }
    }

    private void generateRecipes(Consumer<FinishedRecipe> consumer, BlockSchema schema, SchemaEntry entry) {
        Block block = entry.getBlock();
        BlockForm form = entry.getForm();
        BlockVariant variant = entry.getVariant();

        if (entry.isBaseEntry() && schema.variants().contains(Variants.COBBLED)) {

            stoneSmeltingRecipeOf(block, schema.getBaseBlock(Variants.COBBLED));
        } else if (form instanceof Forms.OreForm oreForm) {

            addOreRecipes(consumer, block, oreForm.getOreType());
        } else if (form.isBaseForm()) {

            stoneCuttingRecipes(consumer, schema, variant, form, 1);
            ItemLike ingredient = getBaseBlock(schema, entry);

            BiConsumer<ItemLike, ItemLike> baseShapeBuilder = baseShapeBuilders.get(variant);
            if (baseShapeBuilder != null) {
                baseShapeBuilder.accept(block, ingredient);
            }
        } else {
            BiFunction<ItemLike, ItemLike, RecipeBuilder> shapeBuilder = shapeBuilders.get(form);
            ItemLike ingredient = getBaseBlock(schema, entry);

            if (shapeBuilder != null) {
                RecipeBuilder builder = shapeBuilder.apply(block, ingredient);
                //                family.getRecipeGroupPrefix().ifPresent((prefix) -> builder.group(prefix + (variant == BlockFamily.Variant.CUT ? "" : "_" + variant.getName())));
                builder.unlockedBy(getHasName(ingredient), has(ingredient));
                builder.save(consumer);
            }
            if (stoneCutterCount.containsKey(form)) {
                stoneCuttingRecipes(consumer, schema, variant, form, stoneCutterCount.get(form));
            }
        }
    }


    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumerIn) {
        consumer = consumerIn;
        stoneRecipe();

        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                generateRecipes(consumerIn, type, entry);
            }
        }
        ShapelessRecipeBuilder.shapeless(Blocks.GRAVEL).requires(UEBlocks.PYROXENE).unlockedBy("has_pyroxene", has(UEBlocks.PYROXENE)).save(consumer, "unearthed:gravel_from_pyroxene");
        ShapedRecipeBuilder.shaped(UEBlocks.LIGNITE_BRIQUETTES).define('#', BlockSchemas.LIGNITE.getBaseBlock())
                .define('*', Blocks.CLAY)
                .pattern("###").pattern("#*#").pattern("###").unlockedBy("has_lignite", has(BlockSchemas.LIGNITE.getBaseBlock())).save(consumer);

        mixingRecipe(BlockSchemas.SILTSTONE.getBaseBlock(), Blocks.SAND, Blocks.COARSE_DIRT);
        mixingRecipe(BlockSchemas.MUDSTONE.getBaseBlock(), Blocks.RED_SAND, Blocks.TERRACOTTA);
        mixingRecipe(BlockSchemas.CONGLOMERATE.getBaseBlock(), Blocks.GRAVEL, Blocks.COARSE_DIRT);
        addingRecipe(BlockSchemas.GRANODIORITE.getBaseBlock(), Blocks.GRANITE, Blocks.DIORITE);
        addingRecipe(BlockSchemas.RHYOLITE.getBaseBlock(), Blocks.ANDESITE, Items.QUARTZ);
        addingRecipe(BlockSchemas.GABBRO.getBaseBlock(), Blocks.DIORITE, UEItems.PYROXENE);
        mixingRecipe(BlockSchemas.WEATHERED_RHYOLITE.getBaseBlock(), BlockSchemas.RHYOLITE.getBaseBlock(), BlockSchemas.RHYOLITE.getEntry(Variants.ALL_BLOCKS_PLUS, Forms.REGOLITH).getBlock());
        stoneSmeltingRecipeOf(BlockSchemas.SLATE.getBaseBlock(), Blocks.SMOOTH_STONE);
        stoneSmeltingRecipeOf(BlockSchemas.PHYLLITE.getBaseBlock(), BlockSchemas.SLATE.getBaseBlock());
        stoneSmeltingRecipeOf(BlockSchemas.SCHIST.getBaseBlock(), BlockSchemas.PHYLLITE.getBaseBlock());
        stoneSmeltingRecipeOf(BlockSchemas.QUARTZITE.getBaseBlock(), Blocks.SMOOTH_SANDSTONE);
        stoneSmeltingRecipeOf(BlockSchemas.QUARTZITE.getBaseBlock(), Blocks.SMOOTH_RED_SANDSTONE);
//        stoneSmeltingRecipeOf(BlockSchemas.MARBLE.getBaseBlock(), BlockSchemas.LIMESTONE.getBaseBlock());
//        stoneSmeltingRecipeOf(BlockSchemas.MARBLE.getBaseBlock(), BlockSchemas.GREY_LIMESTONE.getBaseBlock());
//        stoneSmeltingRecipeOf(BlockSchemas.MARBLE.getBaseBlock(), BlockSchemas.BEIGE_LIMESTONE.getBaseBlock());
        addingRecipe(BlockSchemas.BEIGE_LIMESTONE.getBaseBlock(), BlockSchemas.LIMESTONE.getBaseBlock(), Blocks.SAND);
        addingRecipe(BlockSchemas.GREY_LIMESTONE.getBaseBlock(), BlockSchemas.LIMESTONE.getBaseBlock(), Blocks.SOUL_SAND);
        stoneSmeltingRecipeOf(BlockSchemas.BEIGE_LIMESTONE.getBaseBlock(), Blocks.DEAD_HORN_CORAL_BLOCK);
        stoneSmeltingRecipeOf(BlockSchemas.GREY_LIMESTONE.getBaseBlock(), Blocks.DEAD_TUBE_CORAL);
        stoneSmeltingRecipeOf(BlockSchemas.LIMESTONE.getBaseBlock(), Blocks.DEAD_BUBBLE_CORAL);
        stoneSmeltingRecipeOf(BlockSchemas.LIMESTONE.getBaseBlock(), Blocks.DEAD_FIRE_CORAL);
        stoneSmeltingRecipeOf(BlockSchemas.LIMESTONE.getBaseBlock(), Blocks.DEAD_BRAIN_CORAL);
    }

    private static void addOreRecipes(Consumer consumer, Block oreBlock, IOreType oreType) {
        oreSmelting(consumer, List.of(oreBlock), oreType.getSmeltResult(), oreType.getExperience(), oreType.getSmeltTime(), getItemName(oreType.getSmeltResult()));
        oreBlasting(consumer, List.of(oreBlock), oreType.getSmeltResult(), oreType.getExperience(), oreType.getBlastTime(), getItemName(oreType.getSmeltResult()));
    }

    private static RecipeBuilder regolithConversionRecipe(ItemLike result, ItemLike baseBlock) {
        return ShapelessRecipeBuilder.shapeless(result, 8)
                .requires(Ingredient.of(UETags.Items.REGOLITH_TAG), 8)
                .requires(baseBlock);
    }

    private void mixingRecipe(ItemLike result, ItemLike ingredient1, ItemLike ingredient2) {
        ShapedRecipeBuilder.shaped(result, 4)
                .pattern("ox").pattern("xo").define('x', ingredient1).define('o', ingredient2)
                .unlockedBy(getHasName(result), has(result)).save(consumer);
    }

    private void addingRecipe(ItemLike result, ItemLike ingredient1, ItemLike ingredient2) {
        ShapedRecipeBuilder.shaped(result, 4)
                .pattern("ox").define('x', ingredient1).define('o', ingredient2)
                .unlockedBy(getHasName(result), has(result)).save(consumer);
    }

    private void mossyRecipe(ItemLike mossyBlock, ItemLike baseBlock) {
        ShapelessRecipeBuilder.shapeless(mossyBlock).requires(baseBlock).requires(Items.VINE)
                .unlockedBy(getHasName(mossyBlock), has(mossyBlock)).save(consumer);
    }

    private void crackedRecipe(ItemLike crackedBlock, ItemLike baseBlock) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(baseBlock), crackedBlock, 0.1f, 200)
                .unlockedBy(getHasName(baseBlock), has(baseBlock)).save(consumer);
    }

    private void stoneRecipe() {
        ShapelessRecipeBuilder.shapeless(Items.STONE, 3).requires(UETags.Items.IGNEOUS_ITEM).requires(UETags.Items.METAMORPHIC_ITEM).requires(UETags.Items.SEDIMENTARY_ITEM).unlockedBy("has_stone", has(Items.STONE)).save(consumer, "unearthed:stone_from_stones");
    }

    private void brickRecipeOf(ItemLike result, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(result, 4).define('#', ingredient).pattern("##").pattern("##")
                .unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    private void chiseledRecipeOf(ItemLike result, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(result, 1).define('#', ingredient).pattern("#").pattern("#")
                .unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    private void pillarRecipeOf(ItemLike result, ItemLike ingredient) {
        ShapedRecipeBuilder.shaped(result, 2).define('#', ingredient).pattern("#").pattern("#")
                .unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    private void stoneSmeltingRecipeOf(ItemLike result, ItemLike ingredient) {
//        smeltingResultFromBase(consumer, result, ingredient);
//        String name = Unearthed.MOD_ID + ":" + result.asItem().getRegistryName().getPath() + "_from_" + ingredient.asItem().getRegistryName().getPath();
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ingredient), result, 0.1F, 200)
                .unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getSmeltingRecipeName(result) + "_" + getItemName(ingredient));
    }

    public static void stoneCuttingRecipes(Consumer<FinishedRecipe> consumer, BlockSchema type, BlockVariant variant, BlockForm form, int count) {
        ItemLike result = type.getEntry(variant, form).getBlock();


        if (!form.isBaseForm()) {
            stonecutterResultFromBase(consumer, result, type.getBaseBlock(variant), count);
        }
        if (variant.isDerivative()) {
            stonecutterResultFromBase(consumer, result, type.getBaseBlock(), count);
        }
        if (variant == Variants.POLISHED_BRICKS || variant == Variants.CHISELED_POLISHED) {
            stonecutterResultFromBase(consumer, result, type.getBaseBlock(Variants.POLISHED), count);
        }
        if (variant == Variants.CHISELED_BRICKS) {
            stonecutterResultFromBase(consumer, result, type.getBaseBlock(Variants.BRICKS), count);
        }
    }
}
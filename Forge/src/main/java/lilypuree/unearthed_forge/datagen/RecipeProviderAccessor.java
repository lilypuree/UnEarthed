package lilypuree.unearthed_forge.datagen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lilypuree.unearthed.Constants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.Registry;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class RecipeProviderAccessor implements DataProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    private static final ImmutableList<ItemLike> COAL_SMELTABLES = ImmutableList.of(net.minecraft.world.item.Items.COAL_ORE, net.minecraft.world.item.Items.DEEPSLATE_COAL_ORE);
    private static final ImmutableList<ItemLike> IRON_SMELTABLES = ImmutableList.of(net.minecraft.world.item.Items.IRON_ORE, net.minecraft.world.item.Items.DEEPSLATE_IRON_ORE, net.minecraft.world.item.Items.RAW_IRON);
    private static final ImmutableList<ItemLike> COPPER_SMELTABLES = ImmutableList.of(net.minecraft.world.item.Items.COPPER_ORE, net.minecraft.world.item.Items.DEEPSLATE_COPPER_ORE, net.minecraft.world.item.Items.RAW_COPPER);
    private static final ImmutableList<ItemLike> GOLD_SMELTABLES = ImmutableList.of(net.minecraft.world.item.Items.GOLD_ORE, net.minecraft.world.item.Items.DEEPSLATE_GOLD_ORE, net.minecraft.world.item.Items.NETHER_GOLD_ORE, net.minecraft.world.item.Items.RAW_GOLD);
    private static final ImmutableList<ItemLike> DIAMOND_SMELTABLES = ImmutableList.of(net.minecraft.world.item.Items.DIAMOND_ORE, net.minecraft.world.item.Items.DEEPSLATE_DIAMOND_ORE);
    private static final ImmutableList<ItemLike> LAPIS_SMELTABLES = ImmutableList.of(net.minecraft.world.item.Items.LAPIS_ORE, net.minecraft.world.item.Items.DEEPSLATE_LAPIS_ORE);
    private static final ImmutableList<ItemLike> REDSTONE_SMELTABLES = ImmutableList.of(net.minecraft.world.item.Items.REDSTONE_ORE, net.minecraft.world.item.Items.DEEPSLATE_REDSTONE_ORE);
    private static final ImmutableList<ItemLike> EMERALD_SMELTABLES = ImmutableList.of(net.minecraft.world.item.Items.EMERALD_ORE, net.minecraft.world.item.Items.DEEPSLATE_EMERALD_ORE);
    private final DataGenerator generator;
    private static final Map<BlockFamily.Variant, BiFunction<ItemLike, ItemLike, RecipeBuilder>> shapeBuilders = ImmutableMap.<BlockFamily.Variant, BiFunction<ItemLike, ItemLike, RecipeBuilder>>builder()
            .put(BlockFamily.Variant.BUTTON, ($$0, $$1) -> buttonBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.CHISELED, ($$0, $$1) -> chiseledBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.CUT, ($$0, $$1) -> cutBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.DOOR, ($$0, $$1) -> doorBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.FENCE, ($$0, $$1) -> fenceBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.FENCE_GATE, ($$0, $$1) -> fenceGateBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.SIGN, ($$0, $$1) -> signBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.SLAB, ($$0, $$1) -> slabBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.STAIRS, ($$0, $$1) -> stairBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.PRESSURE_PLATE, ($$0, $$1) -> pressurePlateBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.POLISHED, ($$0, $$1) -> polishedBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.TRAPDOOR, ($$0, $$1) -> trapdoorBuilder($$0, Ingredient.of($$1)))
            .put(BlockFamily.Variant.WALL, ($$0, $$1) -> wallBuilder($$0, Ingredient.of($$1))).build();

    public RecipeProviderAccessor(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void run(HashCache cache) {
        Path path = this.generator.getOutputFolder();
        Set<ResourceLocation> recipeIds = Sets.newHashSet();
        buildCraftingRecipes((finishedRecipe) -> {
            if (!recipeIds.add(finishedRecipe.getId())) {
                throw new IllegalStateException("Duplicate recipe " + finishedRecipe.getId());
            } else {
                saveRecipe(cache, finishedRecipe.serializeRecipe(), path.resolve("data/" + finishedRecipe.getId().getNamespace() + "/recipes/" + finishedRecipe.getId().getPath() + ".json"));
                JsonObject advancementJson = finishedRecipe.serializeAdvancement();
                if (advancementJson != null) {
                    saveAdvancement(cache, advancementJson, path.resolve("data/" + finishedRecipe.getId().getNamespace() + "/advancements/" + finishedRecipe.getAdvancementId().getPath() + ".json"));
                }
            }
        });
        saveAdvancement(cache, Advancement.Builder.advancement().addCriterion("impossible", new ImpossibleTrigger.TriggerInstance()).serializeToJson(), path.resolve("data/minecraft/advancements/recipes/root.json"));
    }

    private static void saveRecipe(HashCache cache, JsonObject serializedRecipe, Path path) {
        try {
            String serialized = GSON.toJson((JsonElement) serializedRecipe);
            String encoded = SHA1.hashUnencodedChars(serialized).toString();
            if (!Objects.equals(cache.getHash(path), encoded) || !Files.exists(path)) {
                Files.createDirectories(path.getParent());
                BufferedWriter $$5 = Files.newBufferedWriter(path);

                try {
                    $$5.write(serialized);
                } catch (Throwable var9) {
                    if ($$5 != null) {
                        try {
                            $$5.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }

                    throw var9;
                }

                if ($$5 != null) {
                    $$5.close();
                }
            }

            cache.putNew(path, encoded);
        } catch (IOException var10) {
            LOGGER.error("Couldn't save recipe {}", path, var10);
        }
    }

    private static void saveAdvancement(HashCache $$0, JsonObject $$1, Path $$2) {
        try {
            String $$3 = GSON.toJson((JsonElement) $$1);
            String $$4 = SHA1.hashUnencodedChars($$3).toString();
            if (!Objects.equals($$0.getHash($$2), $$4) || !Files.exists($$2)) {
                Files.createDirectories($$2.getParent());
                BufferedWriter $$5 = Files.newBufferedWriter($$2);

                try {
                    $$5.write($$3);
                } catch (Throwable var9) {
                    if ($$5 != null) {
                        try {
                            $$5.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }

                    throw var9;
                }

                if ($$5 != null) {
                    $$5.close();
                }
            }

            $$0.putNew($$2, $$4);
        } catch (IOException var10) {
            LOGGER.error("Couldn't save recipe advancement {}", $$2, var10);
        }
    }

    protected abstract void buildCraftingRecipes(Consumer<FinishedRecipe> consumer);

    public static void oneToOneConversionRecipe(Consumer<FinishedRecipe> consumer, ItemLike source, ItemLike result, @Nullable String group) {
        oneToOneConversionRecipe(consumer, source, result, group, 1);
    }

    public static void oneToOneConversionRecipe(Consumer<FinishedRecipe> consumer, ItemLike source, ItemLike result, @Nullable String group, int count) {
        ShapelessRecipeBuilder.shapeless(source, count).requires(result).group(group).unlockedBy(getHasName(result), has(result)).save(consumer, getConversionRecipeName(source, result));
    }

    public static void oreSmelting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(consumer, RecipeSerializer.SMELTING_RECIPE, ingredients, result, experience, cookingTime, group, "_from_smelting");
    }

    public static void oreBlasting(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(consumer, RecipeSerializer.BLASTING_RECIPE, ingredients, result, experience, cookingTime, group, "_from_blasting");
    }

    public static void oreCooking(Consumer<FinishedRecipe> consumer, SimpleCookingSerializer<?> serializer, List<ItemLike> ingredients, ItemLike result, float exp, int cookingTime, String group, String suffix) {
        for (ItemLike ingredient : ingredients) {
            SimpleCookingRecipeBuilder.cooking(Ingredient.of(ingredient), result, exp, cookingTime, serializer).group(group).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getItemName(result) + suffix + "_" + getItemName(ingredient));
        }
    }

    public static RecipeBuilder buttonBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapelessRecipeBuilder.shapeless($$0).requires($$1);
    }

    public static void button(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike material) {
        buttonBuilder(result, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer);
    }

    public static void pressurePlate(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        pressurePlateBuilder($$1, Ingredient.of($$2)).unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    public static RecipeBuilder pressurePlateBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0).define('#', $$1).pattern("##");
    }

    public static void slab(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike material) {
        slabBuilder(result, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer);
    }

    public static RecipeBuilder slabBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0, 6).define('#', $$1).pattern("###");
    }

    public static RecipeBuilder stairBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0, 4).define('#', $$1).pattern("#  ").pattern("## ").pattern("###");
    }

    public static void stair(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike material) {
        stairBuilder(result, Ingredient.of(material)).unlockedBy(getHasName(material), has(material)).save(consumer);
    }

    public static void wall(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        wallBuilder($$1, Ingredient.of($$2)).unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    public static RecipeBuilder wallBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0, 6).define('#', $$1).pattern("###").pattern("###");
    }

    public static void polished(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        polishedBuilder($$1, Ingredient.of($$2)).unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    public static RecipeBuilder polishedBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0, 4).define('S', $$1).pattern("SS").pattern("SS");
    }

    public static void cut(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        cutBuilder($$1, Ingredient.of($$2)).unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    public static ShapedRecipeBuilder cutBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0, 4).define('#', $$1).pattern("##").pattern("##");
    }

    public static void chiseled(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        chiseledBuilder($$1, Ingredient.of($$2)).unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    public static ShapedRecipeBuilder chiseledBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0).define('#', $$1).pattern("#").pattern("#");
    }

    public static void stonecutterResultFromBase(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        stonecutterResultFromBase($$0, $$1, $$2, 1);
    }

    public static void stonecutterResultFromBase(Consumer<FinishedRecipe> $$0, ItemLike result, ItemLike material, int count) {

        SingleItemRecipeBuilder.stonecutting(Ingredient.of(material), result, count).unlockedBy(getHasName(material), has(material)).save($$0, getConversionRecipeName(result, material) + "_stonecutting");
    }

    public static void smeltingResultFromBase(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of($$2), $$1, 0.1F, 200).unlockedBy(getHasName($$2), has($$2)).save($$0);
    }


    ///Uused

    private static void netheriteSmithing(Consumer<FinishedRecipe> $$0, Item $$1, Item $$2) {
        UpgradeRecipeBuilder.smithing(Ingredient.of($$1), Ingredient.of(net.minecraft.world.item.Items.NETHERITE_INGOT), $$2).unlocks("has_netherite_ingot", has(net.minecraft.world.item.Items.NETHERITE_INGOT)).save($$0, getItemName($$2) + "_smithing");
    }

    private static void planksFromLog(Consumer<FinishedRecipe> $$0, ItemLike $$1, Tag<Item> $$2) {
        ShapelessRecipeBuilder.shapeless($$1, 4).requires($$2).group("planks").unlockedBy("has_log", has($$2)).save($$0);
    }

    private static void planksFromLogs(Consumer<FinishedRecipe> $$0, ItemLike $$1, Tag<Item> $$2) {
        ShapelessRecipeBuilder.shapeless($$1, 4).requires($$2).group("planks").unlockedBy("has_logs", has($$2)).save($$0);
    }

    private static void woodFromLogs(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1, 3).define('#', $$2).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has($$2)).save($$0);
    }

    private static void woodenBoat(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1).define('#', $$2).pattern("# #").pattern("###").group("boat").unlockedBy("in_water", insideOf(Blocks.WATER)).save($$0);
    }

    public static RecipeBuilder doorBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0, 3).define('#', $$1).pattern("##").pattern("##").pattern("##");
    }

    public static RecipeBuilder fenceBuilder(ItemLike $$0, Ingredient $$1) {
        int $$2 = $$0 == Blocks.NETHER_BRICK_FENCE ? 6 : 3;
        Item $$3 = $$0 == Blocks.NETHER_BRICK_FENCE ? net.minecraft.world.item.Items.NETHER_BRICK : net.minecraft.world.item.Items.STICK;
        return ShapedRecipeBuilder.shaped($$0, $$2).define('W', $$1).define('#', $$3).pattern("W#W").pattern("W#W");
    }

    public static RecipeBuilder fenceGateBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0).define('#', net.minecraft.world.item.Items.STICK).define('W', $$1).pattern("#W#").pattern("#W#");
    }

    public static RecipeBuilder trapdoorBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0, 2).define('#', $$1).pattern("###").pattern("###");
    }

    public static RecipeBuilder signBuilder(ItemLike $$0, Ingredient $$1) {
        return ShapedRecipeBuilder.shaped($$0, 3).group("sign").define('#', $$1).define('X', net.minecraft.world.item.Items.STICK).pattern("###").pattern("###").pattern(" X ");
    }

    private static void coloredWoolFromWhiteWoolAndDye(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapelessRecipeBuilder.shapeless($$1).requires($$2).requires(Blocks.WHITE_WOOL).group("wool").unlockedBy("has_white_wool", has(Blocks.WHITE_WOOL)).save($$0);
    }

    private static void carpet(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1, 3).define('#', $$2).pattern("##").group("carpet").unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    private static void coloredCarpetFromWhiteCarpetAndDye(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1, 8).define('#', Blocks.WHITE_CARPET).define('$', $$2).pattern("###").pattern("#$#").pattern("###").group("carpet").unlockedBy("has_white_carpet", has(Blocks.WHITE_CARPET)).unlockedBy(getHasName($$2), has($$2)).save($$0, getConversionRecipeName($$1, Blocks.WHITE_CARPET));
    }

    private static void bedFromPlanksAndWool(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1).define('#', $$2).define('X', ItemTags.PLANKS).pattern("###").pattern("XXX").group("bed").unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    private static void bedFromWhiteBedAndDye(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapelessRecipeBuilder.shapeless($$1).requires(net.minecraft.world.item.Items.WHITE_BED).requires($$2).group("dyed_bed").unlockedBy("has_bed", has(net.minecraft.world.item.Items.WHITE_BED)).save($$0, getConversionRecipeName($$1, net.minecraft.world.item.Items.WHITE_BED));
    }

    private static void banner(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1).define('#', $$2).define('|', net.minecraft.world.item.Items.STICK).pattern("###").pattern("###").pattern(" | ").group("banner").unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    private static void stainedGlassFromGlassAndDye(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1, 8).define('#', Blocks.GLASS).define('X', $$2).pattern("###").pattern("#X#").pattern("###").group("stained_glass").unlockedBy("has_glass", has(Blocks.GLASS)).save($$0);
    }

    private static void stainedGlassPaneFromStainedGlass(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1, 16).define('#', $$2).pattern("###").pattern("###").group("stained_glass_pane").unlockedBy("has_glass", has($$2)).save($$0);
    }

    private static void stainedGlassPaneFromGlassPaneAndDye(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1, 8).define('#', Blocks.GLASS_PANE).define('$', $$2).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE)).unlockedBy(getHasName($$2), has($$2)).save($$0, getConversionRecipeName($$1, Blocks.GLASS_PANE));
    }

    private static void coloredTerracottaFromTerracottaAndDye(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapedRecipeBuilder.shaped($$1, 8).define('#', Blocks.TERRACOTTA).define('X', $$2).pattern("###").pattern("#X#").pattern("###").group("stained_terracotta").unlockedBy("has_terracotta", has(Blocks.TERRACOTTA)).save($$0);
    }

    private static void concretePowder(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapelessRecipeBuilder.shapeless($$1, 8).requires($$2).requires(Blocks.SAND, 4).requires(Blocks.GRAVEL, 4).group("concrete_powder").unlockedBy("has_sand", has(Blocks.SAND)).unlockedBy("has_gravel", has(Blocks.GRAVEL)).save($$0);
    }

    public static void candle(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        ShapelessRecipeBuilder.shapeless($$1).requires(Blocks.CANDLE).requires($$2).group("dyed_candle").unlockedBy(getHasName($$2), has($$2)).save($$0);
    }

    private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2) {
        nineBlockStorageRecipes($$0, $$1, $$2, getSimpleRecipeName($$2), (String) null, getSimpleRecipeName($$1), (String) null);
    }

    private static void nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2, String $$3, String $$4) {
        nineBlockStorageRecipes($$0, $$1, $$2, $$3, $$4, getSimpleRecipeName($$1), (String) null);
    }

    private static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2, String $$3, String $$4) {
        nineBlockStorageRecipes($$0, $$1, $$2, getSimpleRecipeName($$2), (String) null, $$3, $$4);
    }

    private static void nineBlockStorageRecipes(Consumer<FinishedRecipe> $$0, ItemLike $$1, ItemLike $$2, String $$3, @Nullable String $$4, String $$5, @Nullable String $$6) {
        ShapelessRecipeBuilder.shapeless($$1, 9).requires($$2).group($$6).unlockedBy(getHasName($$2), has($$2)).save($$0, new ResourceLocation($$5));
        ShapedRecipeBuilder.shaped($$2).define('#', $$1).pattern("###").pattern("###").pattern("###").group($$4).unlockedBy(getHasName($$1), has($$1)).save($$0, new ResourceLocation($$3));
    }

    private static void simpleCookingRecipe(Consumer<FinishedRecipe> $$0, String $$1, SimpleCookingSerializer<?> $$2, int $$3, ItemLike $$4, ItemLike $$5, float $$6) {
        SimpleCookingRecipeBuilder.cooking(Ingredient.of($$4), $$5, $$6, $$3, $$2).unlockedBy(getHasName($$4), has($$4)).save($$0, getItemName($$5) + "_from_" + $$1);
    }

    private static EnterBlockTrigger.TriggerInstance insideOf(Block $$0) {
        return new EnterBlockTrigger.TriggerInstance(EntityPredicate.Composite.ANY, $$0, StatePropertiesPredicate.ANY);
    }

    public static InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints count, ItemLike item) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(item).withCount(count).build());
    }

    public static InventoryChangeTrigger.TriggerInstance has(ItemLike item) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(item).build());
    }

    public static InventoryChangeTrigger.TriggerInstance has(Tag<Item> itemTag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemTag).build());
    }

    private static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... predicates) {
        return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, predicates);
    }

    protected static String getHasName(ItemLike item) {
        return "has_" + getItemName(item);
    }

    protected static String getItemName(ItemLike item) {
        return Registry.ITEM.getKey(item.asItem()).getPath();
    }

    protected static String getSimpleRecipeName(ItemLike item) {
        return getItemName(item);
    }

    protected static String getConversionRecipeName(ItemLike recipe, ItemLike result) {
        return getItemName(recipe) + "_from_" + getItemName(result);
    }

    protected static String getSmeltingRecipeName(ItemLike item) {
        return getItemName(item) + "_from_smelting";
    }

    protected static String getBlastingRecipeName(ItemLike item) {
        return getItemName(item) + "_from_blasting";
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}

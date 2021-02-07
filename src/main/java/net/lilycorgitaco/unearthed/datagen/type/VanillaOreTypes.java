package net.lilycorgitaco.unearthed.datagen.type;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CookingRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ApplyBonusLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.lilycorgitaco.unearthed.Unearthed;
import net.lilycorgitaco.unearthed.core.UEItems;
import net.lilycorgitaco.unearthed.datagen.BlockLootTableAccessor;

import java.util.function.Consumer;
import java.util.function.Function;

import static net.lilycorgitaco.unearthed.datagen.Recipes.hasTaggedItem;

public enum VanillaOreTypes implements IOreType {
    COAL(Blocks.COAL_ORE, Items.COAL, 0.1f, 200, 0) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return block -> BlockLootTableAccessor.oreDrops(block, Items.COAL);
        }
    }, LAPIS(Blocks.LAPIS_ORE, Items.LAPIS_LAZULI, 0.2f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return block -> BlockLootTableAccessor.dropsWithSilkTouch(block, BlockLootTableAccessor.withExplosionDecayWithoutImmuneCheck(block, ItemEntry.builder(Items.LAPIS_LAZULI).apply(SetCountLootFunction.builder(UniformLootTableRange.between(4.0F, 9.0F))).apply(ApplyBonusLootFunction.oreDrops(Enchantments.FORTUNE))));
        }
    }, REDSTONE(Blocks.REDSTONE_ORE, Items.REDSTONE, 0.7f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return redstoneOre -> BlockLootTableAccessor.dropsWithSilkTouch(redstoneOre, BlockLootTableAccessor.withExplosionDecayWithoutImmuneCheck(redstoneOre, ItemEntry.builder(Items.REDSTONE).apply(SetCountLootFunction.builder(UniformLootTableRange.between(4.0F, 5.0F))).apply(ApplyBonusLootFunction.uniformBonusCount(Enchantments.FORTUNE))));
        }
    }, DIAMOND(Blocks.DIAMOND_ORE, Items.DIAMOND, 1.0f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return diamondOre -> BlockLootTableAccessor.oreDrops(diamondOre, Items.DIAMOND);
        }
    }, EMERALD(Blocks.EMERALD_ORE, Items.EMERALD, 1.0f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return emeraldOre -> BlockLootTableAccessor.oreDrops(emeraldOre, Items.EMERALD);
        }
    }, IRON(Blocks.IRON_ORE, Items.IRON_INGOT, 0.7f, 200, 1) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return ironOre -> BlockLootTableAccessor.drops(ironOre, UEItems.IRON_ORE);
        }
    },
    GOLD(Blocks.GOLD_ORE, Items.GOLD_INGOT, 1.0f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return goldOre -> BlockLootTableAccessor.drops(goldOre, UEItems.GOLD_ORE);
        }
    };

    private final Block block;
    private final Item smeltResult;
    private final float experience;
    private final int smeltTime;
    private final int blastTime;
    private final int harvestLevel;

    VanillaOreTypes(Block block, Item smeltResult, float experience, int smeltTime, int harvestLevel) {
        this.block = block;
        this.smeltResult = smeltResult;
        this.experience = experience;
        this.smeltTime = smeltTime;
        this.blastTime = smeltTime / 2;
        this.harvestLevel = harvestLevel;
    }


    @Override
    public void addCookingRecipes(Consumer<RecipeJsonProvider> consumer) {
        Tag<Item> oreTag = TagRegistry.item(new Identifier(Unearthed.MOD_ID, getName() + "_ores"));
        CookingRecipeJsonFactory.createSmelting(Ingredient.fromTag(oreTag), smeltResult, experience, smeltTime)
                .criterion("has_" + getName() + "_ore", hasTaggedItem(oreTag)).offerTo(consumer, Registry.ITEM.getId(smeltResult).getPath() + "_from_smelting_" + getName() + "_ore");
        CookingRecipeJsonFactory.createBlasting(Ingredient.fromTag(oreTag), smeltResult, experience, blastTime)
                .criterion("has_" + getName() + "_ore", hasTaggedItem(oreTag)).offerTo(consumer, Registry.ITEM.getId(smeltResult).getPath() + "_from_blasting_" + getName() + "_ore");
    }

    @Override
    public String getName() {
        return toString().toLowerCase();
    }

    @Override
    public Function<Block, LootTable.Builder> createLootFactory() {
        return BlockLootTableAccessor::drops;
    }

    @Override
    public int getHarvestLevel() {
        return harvestLevel;
    }

    @Override
    public Block getBlock() {
        return block;
    }
}
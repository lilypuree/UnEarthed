package net.oriondevcorgitaco.unearthed.datagen.type;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import net.oriondevcorgitaco.unearthed.datagen.BlockLootTableAccessor;

import java.util.function.Consumer;
import java.util.function.Function;

import static net.oriondevcorgitaco.unearthed.datagen.Recipes.hasTaggedItem;

public enum VanillaOreTypes implements IOreType {
    COAL(Blocks.COAL_ORE, Items.COAL, 0.1f, 200, 0) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return block -> BlockLootTableAccessor.droppingItemWithFortune(block, Items.COAL);
        }
    }, LAPIS(Blocks.LAPIS_ORE, Items.LAPIS_LAZULI, 0.2f, 200, 1) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return block -> BlockLootTableAccessor.droppingWithSilkTouch(block, BlockLootTableAccessor.withExplosionDecayWithoutImmuneCheck(block, ItemLootEntry.builder(Items.LAPIS_LAZULI).acceptFunction(SetCount.builder(RandomValueRange.of(4.0F, 9.0F))).acceptFunction(ApplyBonus.oreDrops(Enchantments.FORTUNE))));
        }
    }, REDSTONE(Blocks.REDSTONE_ORE, Items.REDSTONE, 0.7f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return redstoneOre -> BlockLootTableAccessor.droppingWithSilkTouch(redstoneOre, BlockLootTableAccessor.withExplosionDecayWithoutImmuneCheck(redstoneOre, ItemLootEntry.builder(Items.REDSTONE).acceptFunction(SetCount.builder(RandomValueRange.of(4.0F, 5.0F))).acceptFunction(ApplyBonus.uniformBonusCount(Enchantments.FORTUNE))));
        }
    }, DIAMOND(Blocks.DIAMOND_ORE, Items.DIAMOND, 1.0f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return diamondOre -> BlockLootTableAccessor.droppingItemWithFortune(diamondOre, Items.DIAMOND);
        }
    }, EMERALD(Blocks.EMERALD_ORE, Items.EMERALD, 1.0f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return emeraldOre -> BlockLootTableAccessor.droppingItemWithFortune(emeraldOre, Items.EMERALD);
        }
    }, IRON(Blocks.IRON_ORE, Items.IRON_INGOT, 0.7f, 200, 1) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return ironOre -> BlockLootTableAccessor.droppingWithSilkTouch(ironOre, UEItems.IRON_ORE);
        }
    },
    GOLD(Blocks.GOLD_ORE, Items.GOLD_INGOT, 1.0f, 200, 2) {
        @Override
        public Function<Block, LootTable.Builder> createLootFactory() {
            return goldOre -> BlockLootTableAccessor.droppingWithSilkTouch(goldOre, UEItems.GOLD_ORE);
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
    public void addCookingRecipes(Consumer<IFinishedRecipe> consumer) {
        ITag.INamedTag<Item> oreTag = ItemTags.createOptional(new ResourceLocation(Unearthed.MOD_ID, getName() + "_ores"));
        CookingRecipeBuilder.smeltingRecipe(Ingredient.fromTag(oreTag), smeltResult, experience, smeltTime)
                .addCriterion("has_" + getName() + "_ore", hasTaggedItem(oreTag)).build(consumer, smeltResult.getRegistryName().getPath() + "_from_smelting_" + getName() + "_ore");
        CookingRecipeBuilder.blastingRecipe(Ingredient.fromTag(oreTag), smeltResult, experience, blastTime)
                .addCriterion("has_" + getName() + "_ore", hasTaggedItem(oreTag)).build(consumer, smeltResult.getRegistryName().getPath() + "_from_blasting_" + getName() + "_ore");
    }

    @Override
    public String getName() {
        return toString().toLowerCase();
    }

    @Override
    public Function<Block, LootTable.Builder> createLootFactory() {
        return BlockLootTableAccessor::dropping;
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
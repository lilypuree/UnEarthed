package net.lilycorgitaco.unearthed.datagen.type;

import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.loot.LootTable;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IOreType {
    Block getBlock();

    String getName();

    Function<Block, LootTable.Builder> createLootFactory();

    int getHarvestLevel();

    void addCookingRecipes(Consumer<RecipeJsonProvider> consumer);
}
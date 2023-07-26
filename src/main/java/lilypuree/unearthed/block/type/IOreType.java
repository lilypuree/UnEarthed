package lilypuree.unearthed.block.type;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public interface IOreType {
    Block getBlock();

    Item getOreDrop();

    Item getSmeltResult();

    String getName();

    int getSmeltTime();

    default int getBlastTime() {
        return getSmeltTime() / 2;
    }

    float getExperience();
}
package lilypuree.unearthed;


import lilypuree.unearthed.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface CommonHelper {
    CreativeModeTab creativeModeTab(ResourceLocation name, Supplier<ItemStack> stack);

    Tag.Named<Block> createBlock(ResourceLocation name);

    Tag.Named<Item> createItem(ResourceLocation name);

    default Tag.Named<Block> createBlock(String name) {
        return createBlock(new ResourceLocation(Constants.MOD_ID, name));
    }

    default Tag.Named<Item> createItem(String name) {
        return createItem(new ResourceLocation(Constants.MOD_ID, name));
    }
}

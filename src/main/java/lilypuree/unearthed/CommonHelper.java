package lilypuree.unearthed;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface CommonHelper {
    CreativeModeTab creativeModeTab(ResourceLocation name, Supplier<ItemStack> stack);
}

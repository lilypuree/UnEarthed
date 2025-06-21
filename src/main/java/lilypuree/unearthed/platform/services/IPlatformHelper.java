package lilypuree.unearthed.platform.services;

import java.util.function.Supplier;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlatformHelper {
    CreativeModeTab createModTab(String var1, Supplier<ItemStack> var2);

    SoundType createSoundType(float var1, float var2, Supplier<SoundEvent> var3, Supplier<SoundEvent> var4, Supplier<SoundEvent> var5, Supplier<SoundEvent> var6, Supplier<SoundEvent> var7);

    void setRenderLayer(Block var1, RenderType var2);

    void setBlockColor(Block var1, BlockColor var2);

    int getBlockColor(BlockState var1, int var2);

    void setItemColor(ItemLike var1, ItemColor var2);

    boolean isDiggingHoe(ItemStack var1);
}

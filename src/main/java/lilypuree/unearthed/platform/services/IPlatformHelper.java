package lilypuree.unearthed.platform.services;

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

import java.util.function.Supplier;

public interface IPlatformHelper {

    CreativeModeTab createModTab(String name, Supplier<ItemStack> icon);

    SoundType createSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn);

    void setRenderLayer(Block block, RenderType renderType);

    void setBlockColor(Block block, BlockColor blockColor);

    int getBlockColor(BlockState block, int color);

    void setItemColor(ItemLike item, ItemColor itemColor);

    boolean isDiggingHoe(ItemStack item);

}

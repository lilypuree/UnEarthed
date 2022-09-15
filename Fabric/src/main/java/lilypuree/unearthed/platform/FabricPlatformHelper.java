package lilypuree.unearthed.platform;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public CreativeModeTab createModTab(String name, Supplier<ItemStack> icon) {
        return FabricItemGroupBuilder.build(new ResourceLocation(Constants.MOD_ID, name), icon);
    }

    @Override
    public SoundType createSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        return new SoundType(volumeIn, pitchIn, breakSoundIn.get(), stepSoundIn.get(), placeSoundIn.get(), hitSoundIn.get(), fallSoundIn.get());
    }

    @Override
    public void setRenderLayer(Block block, RenderType renderType) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderType);
    }

    @Override
    public void setBlockColor(Block block, BlockColor blockColor) {
        ColorProviderRegistry.BLOCK.register(blockColor, block);
    }

    @Override
    public int getBlockColor(BlockState block, int color) {
        return ColorProviderRegistry.BLOCK.get(block.getBlock()).getColor(block, null, null, color);
    }

    @Override
    public void setItemColor(ItemLike item, ItemColor itemColor) {
        ColorProviderRegistry.ITEM.register(itemColor, item);
    }

    @Override
    public boolean isDiggingHoe(ItemStack item) {
        return item.is(ConventionalItemTags.HOES);
    }
}

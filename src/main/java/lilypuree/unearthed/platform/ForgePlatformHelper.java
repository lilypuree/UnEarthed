//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package lilypuree.unearthed.platform;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lilypuree.unearthed.platform.services.IPlatformHelper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.registries.RegistryObject;

public class ForgePlatformHelper implements IPlatformHelper {
    public static BlockColors blockColors;
    public static ItemColors itemColors;
    public static final List<Supplier<? extends ItemLike>> MAIN_BLOCKS = new ArrayList();
    public static final List<Supplier<? extends ItemLike>> MAIN_ITEMS = new ArrayList();

    public CreativeModeTab createModTab(String name, Supplier<ItemStack> icon) {
        return CreativeModeTab.builder().title(Component.translatable("itemGroup.unearthed." + name)).icon(icon).displayItems((pParameters, pOutput) -> {
            MAIN_BLOCKS.forEach((itemLike) -> pOutput.accept(itemLike.get()));
            MAIN_ITEMS.forEach((itemLike) -> pOutput.accept(itemLike.get()));
        }).build();
    }

    public SoundType createSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        return new ForgeSoundType(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
    }

    public void setRenderLayer(Block block, RenderType renderType) {
        ItemBlockRenderTypes.setRenderLayer(block, renderType);
    }

    public void setBlockColor(Block block, BlockColor blockColor) {
        blockColors.register(blockColor, block);
    }

    public int getBlockColor(BlockState block, int color) {
        return blockColors.getColor(block, (BlockAndTintGetter)null, (BlockPos)null, color);
    }

    public void setItemColor(ItemLike item, ItemColor itemColor) {
        itemColors.register(itemColor, item);
    }

    public boolean isDiggingHoe(ItemStack item) {
        return item.getItem().canPerformAction(item, ToolActions.HOE_DIG);
    }

    public static <T extends Item> RegistryObject<T> addToMainTab(RegistryObject<T> itemLike) {
        MAIN_BLOCKS.add(itemLike);
        return itemLike;
    }

    public static <T extends Item> RegistryObject<T> addToMainTabItems(RegistryObject<T> itemLike) {
        MAIN_ITEMS.add(itemLike);
        return itemLike;
    }
}

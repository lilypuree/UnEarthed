package lilypuree.unearthed.platform;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.platform.services.IPlatformHelper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.util.ForgeSoundType;

import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    public static BlockColors blockColors;
    public static ItemColors itemColors;

    @Override
    public CreativeModeTab createModTab(String name, Supplier<ItemStack> icon) {
        return new CreativeModeTab(-1, Constants.MOD_ID + "." + name) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }

    @Override
    public SoundType createSoundType(float volumeIn, float pitchIn, Supplier<SoundEvent> breakSoundIn, Supplier<SoundEvent> stepSoundIn, Supplier<SoundEvent> placeSoundIn, Supplier<SoundEvent> hitSoundIn, Supplier<SoundEvent> fallSoundIn) {
        return new ForgeSoundType(volumeIn, pitchIn, breakSoundIn, stepSoundIn, placeSoundIn, hitSoundIn, fallSoundIn);
    }

    @Override
    public void setRenderLayer(Block block, RenderType renderType) {
        ItemBlockRenderTypes.setRenderLayer(block, renderType);
    }

    @Override
    public void setBlockColor(Block block, BlockColor blockColor) {
        blockColors.register(blockColor, block);
    }

    @Override
    public int getBlockColor(BlockState block, int color) {
        return blockColors.getColor(block, null, null, color);
    }

    @Override
    public void setItemColor(ItemLike item, ItemColor itemColor) {
        itemColors.register(itemColor, item);
    }

    @Override
    public boolean isDiggingHoe(ItemStack item) {
        return item.getItem().canPerformAction(item, ToolActions.HOE_DIG);
    }
}

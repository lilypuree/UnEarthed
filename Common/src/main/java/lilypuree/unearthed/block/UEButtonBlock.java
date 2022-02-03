package lilypuree.unearthed.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class UEButtonBlock extends ButtonBlock {
    public UEButtonBlock(BlockBehaviour.Properties properties){
        super(false, properties);
    }

    @Override
    protected SoundEvent getSound(boolean var1) {
        return var1 ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}

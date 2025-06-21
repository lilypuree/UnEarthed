package lilypuree.unearthed.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class UEButtonBlock extends ButtonBlock {
    public UEButtonBlock(BlockBehaviour.Properties properties){
        super(properties, BlockSetType.STONE, 40, false);
    }

    @Override
    protected SoundEvent getSound(boolean var1) {
        return var1 ? SoundEvents.STONE_BUTTON_CLICK_ON : SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}

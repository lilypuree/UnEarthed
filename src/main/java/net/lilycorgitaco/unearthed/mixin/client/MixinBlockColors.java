package net.lilycorgitaco.unearthed.mixin.client;

import net.minecraft.client.color.block.BlockColors;
import net.lilycorgitaco.unearthed.ClientSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class MixinBlockColors {

    @Inject(method = "create", at = @At("RETURN"))
    private static void addColors(CallbackInfoReturnable<BlockColors> cir) {
        ClientSetup.onBlockColourHandleEvent(cir.getReturnValue());
    }
}

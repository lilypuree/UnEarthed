package net.lilycorgitaco.unearthed.mixin.client;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.lilycorgitaco.unearthed.ClientSetup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemColors.class)
public class MixinItemsColors {

    @Inject(method = "create", at = @At("RETURN"))
    private static void addColors(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
        ClientSetup.onItemColourHandlerEvent(cir.getReturnValue(), blockColors);
    }
}

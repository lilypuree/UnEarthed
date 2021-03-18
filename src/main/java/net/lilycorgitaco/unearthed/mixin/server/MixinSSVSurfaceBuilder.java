package net.lilycorgitaco.unearthed.mixin.server;

import net.lilycorgitaco.unearthed.Unearthed;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.surfacebuilder.SoulSandValleySurfaceBuilder;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoulSandValleySurfaceBuilder.class)
public class MixinSSVSurfaceBuilder {

    @Inject(method = "getLavaShoreState", at = @At("HEAD"),
            cancellable = true)
    private void getGravel(CallbackInfoReturnable<BlockState> cir) {
        if (!Unearthed.CONFIG.disableNetherGeneration){
            cir.setReturnValue(UEBlocks.PYROXENE.getDefaultState());
        }
    }
}

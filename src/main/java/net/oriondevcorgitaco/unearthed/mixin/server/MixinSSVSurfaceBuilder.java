package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.surfacebuilders.SoulSandValleySurfaceBuilder;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoulSandValleySurfaceBuilder.class)
public class MixinSSVSurfaceBuilder {

    @Inject(method = "Lnet/minecraft/world/gen/surfacebuilders/SoulSandValleySurfaceBuilder;func_230389_c_()Lnet/minecraft/block/BlockState;", at = @At("HEAD"),
            cancellable = true)
    private void getGravel(CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(UEBlocks.PYROXENE.getDefaultState());
    }
}

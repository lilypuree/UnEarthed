package lilypuree.unearthed.mixin.server;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.material.MaterialRuleList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaterialRuleList.class)
public class MaterialRuleListMixin {

    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    private void onDoFill(NoiseChunk chunk, int posX, int posY, int posZ, CallbackInfoReturnable<BlockState> cir) {
        if (posX < 0) cir.setReturnValue(Blocks.AIR.defaultBlockState());
        if (posX == -1) cir.setReturnValue(Blocks.GLASS.defaultBlockState());
    }
}

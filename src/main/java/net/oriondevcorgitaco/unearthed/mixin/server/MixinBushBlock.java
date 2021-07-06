package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.oriondevcorgitaco.unearthed.block.RegolithBlock;
import net.oriondevcorgitaco.unearthed.block.RegolithGrassBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BushBlock.class)
public class MixinBushBlock {

    @Inject(method = "mayPlaceOn", cancellable = true, at = @At("HEAD"))
    private void onMayPlaceOn(BlockState block, IBlockReader p_200014_2_, BlockPos p_200014_3_, CallbackInfoReturnable<Boolean> cir){
        if (block.getBlock() instanceof RegolithBlock || block.getBlock() instanceof RegolithGrassBlock){
            cir.setReturnValue(true);
        }
    }
}

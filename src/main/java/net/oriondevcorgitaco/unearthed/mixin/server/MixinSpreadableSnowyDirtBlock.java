package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableSnowyDirtBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.block.RegolithGrassBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(SpreadableSnowyDirtBlock.class)
public class MixinSpreadableSnowyDirtBlock {

    @Inject(method = "randomTick", at = @At(target = "Lnet/minecraft/util/math/BlockPos;offset(III)Lnet/minecraft/util/math/BlockPos;", value = "INVOKE_ASSIGN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRandomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random, CallbackInfo cir, BlockState blockstate, int i, BlockPos blockpos) {
        BlockState newBlock = worldIn.getBlockState(blockpos);
        if (RegolithGrassBlock.regolithToGrassMap.containsKey(newBlock.getBlock()) && RegolithGrassBlock.canPropagate(newBlock, worldIn, blockpos)) {
            if (!worldIn.getFluidState(blockpos.above()).is(FluidTags.WATER)) {
                worldIn.setBlockAndUpdate(blockpos, RegolithGrassBlock.regolithToGrassMap.get(newBlock.getBlock()).defaultBlockState());
                worldIn.setBlockAndUpdate(blockpos.above(), Blocks.AIR.defaultBlockState());
            }
        }
    }
}

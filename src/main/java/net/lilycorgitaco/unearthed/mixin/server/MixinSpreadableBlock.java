package net.lilycorgitaco.unearthed.mixin.server;

import net.lilycorgitaco.unearthed.block.RegolithGrassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(SpreadableBlock.class)
public class MixinSpreadableBlock {

    @Inject(method = "randomTick", at = @At(target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;", value = "INVOKE_ASSIGN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRandomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random, CallbackInfo cir, BlockState blockstate, int i, BlockPos blockpos) {
        BlockState newBlock = worldIn.getBlockState(blockpos);
        if (RegolithGrassBlock.regolithToGrassMap.containsKey(newBlock.getBlock()) && RegolithGrassBlock.canPropagate(newBlock, worldIn, blockpos)) {
            if (!worldIn.getFluidState(blockpos.up()).isIn(FluidTags.WATER)) {
                worldIn.setBlockState(blockpos, RegolithGrassBlock.regolithToGrassMap.get(newBlock.getBlock()).getDefaultState());
                worldIn.setBlockState(blockpos.up(), Blocks.AIR.getDefaultState());
            }
        }
    }
}

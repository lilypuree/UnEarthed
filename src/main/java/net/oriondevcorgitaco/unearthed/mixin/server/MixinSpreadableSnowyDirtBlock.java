package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableSnowyDirtBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.block.RegolithGrassBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

import static net.minecraft.block.SpreadableSnowyDirtBlock.isSnowyAndNotUnderwater;
import static net.minecraft.state.properties.BlockStateProperties.SNOWY;

@Mixin(SpreadableSnowyDirtBlock.class)
public class MixinSpreadableSnowyDirtBlock {

    @Inject(method = "randomTick", at = @At(target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;", value = "INVOKE_ASSIGN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRandomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random, CallbackInfo cir, BlockState blockstate, int i, BlockPos blockpos) {
        if (RegolithGrassBlock.regolithToGrassMap.containsKey(blockstate.getBlock()) && RegolithGrassBlock.canPropagate(blockstate, worldIn, blockpos)) {
            worldIn.setBlockState(blockpos, RegolithGrassBlock.regolithToGrassMap.get(blockstate.getBlock()).getDefaultState());
            worldIn.setBlockState(blockpos.up(), Blocks.AIR.getDefaultState());
        }
    }
}

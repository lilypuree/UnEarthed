package lilypuree.unearthed.mixin.server;

import lilypuree.unearthed.block.RegolithGrassBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(SpreadingSnowyDirtBlock.class)
public class MixinSpreadableSnowyDirtBlock {

    @Inject(method = "randomTick", at = @At(target = "Lnet/minecraft/core/BlockPos;offset(III)Lnet/minecraft/core/BlockPos;", value = "INVOKE_ASSIGN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRandomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random, CallbackInfo cir, BlockState blockstate, int i, BlockPos blockpos) {
        BlockState newBlock = worldIn.getBlockState(blockpos);
        if (RegolithGrassBlock.regolithToGrassMap.containsKey(newBlock.getBlock()) && RegolithGrassBlock.canPropagate(newBlock, worldIn, blockpos)) {
            if (!worldIn.getFluidState(blockpos.above()).is(FluidTags.WATER)) {
                worldIn.setBlockAndUpdate(blockpos, RegolithGrassBlock.regolithToGrassMap.get(newBlock.getBlock()).defaultBlockState());
                worldIn.setBlockAndUpdate(blockpos.above(), Blocks.AIR.defaultBlockState());
            }
        }
    }
}

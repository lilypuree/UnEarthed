package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FlowingFluidBlock.class)
public abstract class MixinFlowingFluidBlock {


    @Shadow protected abstract void fizz(IWorld p_180688_1_, BlockPos p_180688_2_);

    @Inject(method = "shouldSpreadLiquid", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/BlockPos;relative(Lnet/minecraft/util/Direction;)Lnet/minecraft/util/math/BlockPos;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onReactWithNeighbors(World worldIn, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir, boolean flag, Direction[] direction, int i, int j, Direction direction2, BlockPos blockPos) {


        boolean isClay = worldIn.getBlockState(pos.below()).is(Blocks.CLAY);
        if (isClay && worldIn.getBlockState(blockPos).is(Blocks.BLUE_ICE)) {
            Block block = BlockGeneratorReference.DACITE.getBaseBlock();
            worldIn.setBlockAndUpdate(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(worldIn, pos, pos, block.defaultBlockState()));
            this.fizz(worldIn, pos);
            cir.setReturnValue(false);
        }

        boolean isSnow = worldIn.getBlockState(pos.below()).is(Blocks.SNOW_BLOCK);
        if (isSnow && worldIn.getFluidState(blockPos).is(FluidTags.WATER)) {
            Block block = worldIn.getFluidState(pos).isSource() ? Blocks.OBSIDIAN : BlockGeneratorReference.WHITE_GRANITE.getBaseBlock();
            worldIn.setBlockAndUpdate(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(worldIn, pos, pos, block.defaultBlockState()));
            this.fizz(worldIn, pos);
            cir.setReturnValue(false);
        }
    }

}

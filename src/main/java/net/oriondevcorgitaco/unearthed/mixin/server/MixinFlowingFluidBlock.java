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


    @Shadow
    protected abstract void triggerMixEffects(IWorld worldIn, BlockPos pos);

    @Inject(method = "reactWithNeighbors", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/BlockPos;offset(Lnet/minecraft/util/Direction;)Lnet/minecraft/util/math/BlockPos;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onReactWithNeighbors(World worldIn, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir, boolean flag, Direction[] direction, int i, int j, Direction direction2, BlockPos blockPos) {


        boolean isClay = worldIn.getBlockState(pos.down()).isIn(Blocks.CLAY);
        if (isClay && worldIn.getBlockState(blockPos).isIn(Blocks.BLUE_ICE)) {
            Block block = BlockGeneratorReference.DACITE.getBaseBlock();
            worldIn.setBlockState(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(worldIn, pos, pos, block.getDefaultState()));
            this.triggerMixEffects(worldIn, pos);
            cir.setReturnValue(false);
        }

        boolean isSnow = worldIn.getBlockState(pos.down()).isIn(Blocks.SNOW_BLOCK);
        if (isSnow && worldIn.getFluidState(blockPos).isTagged(FluidTags.WATER)) {
            Block block = worldIn.getFluidState(pos).isSource() ? Blocks.OBSIDIAN : BlockGeneratorReference.WHITE_GRANITE.getBaseBlock();
            worldIn.setBlockState(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(worldIn, pos, pos, block.getDefaultState()));
            this.triggerMixEffects(worldIn, pos);
            cir.setReturnValue(false);
        }
    }

}

package net.lilycorgitaco.unearthed.mixin.server;


import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FluidBlock.class)
public abstract class MixinFluidBlock {


    @Shadow protected abstract void playExtinguishSound(WorldAccess world, BlockPos pos);

    @Inject(method = "receiveNeighborFluids", at = @At(value = "INVOKE_ASSIGN", target ="Lnet/minecraft/util/math/BlockPos;offset(Lnet/minecraft/util/math/Direction;)Lnet/minecraft/util/math/BlockPos;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onReactWithNeighbors(World worldIn, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir, boolean flag, Direction[] direction, int i, int j, Direction direction2, BlockPos blockPos) {


        boolean isClay = worldIn.getBlockState(pos.down()).isOf(Blocks.CLAY);
        if (isClay && worldIn.getBlockState(blockPos).isOf(Blocks.BLUE_ICE)) {
            Block block = BlockGeneratorReference.DACITE.getBaseBlock();
            worldIn.setBlockState(pos, block.getDefaultState());
            this.playExtinguishSound(worldIn, pos);
            cir.setReturnValue(false);
        }

        boolean isSnow = worldIn.getBlockState(pos.down()).isOf(Blocks.SNOW_BLOCK);
        if (isSnow && worldIn.getFluidState(blockPos).isIn(FluidTags.WATER)) {
            Block block = worldIn.getFluidState(pos).isStill() ? Blocks.OBSIDIAN : BlockGeneratorReference.WHITE_GRANITE.getBaseBlock();
            worldIn.setBlockState(pos, block.getDefaultState());
            this.playExtinguishSound(worldIn, pos);
            cir.setReturnValue(false);
        }
    }
}

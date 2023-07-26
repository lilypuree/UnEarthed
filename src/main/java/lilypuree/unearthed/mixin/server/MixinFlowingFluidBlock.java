package lilypuree.unearthed.mixin.server;

import com.google.common.collect.UnmodifiableIterator;
import lilypuree.unearthed.block.schema.BlockSchemas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LiquidBlock.class)
public abstract class MixinFlowingFluidBlock {


    @Shadow
    protected abstract void fizz(LevelAccessor p_180688_1_, BlockPos p_180688_2_);

    @Inject(method = "shouldSpreadLiquid", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/core/BlockPos;relative(Lnet/minecraft/core/Direction;)Lnet/minecraft/core/BlockPos;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void onReactWithNeighbors(Level level, BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir, boolean flag, UnmodifiableIterator iterator, Direction direction) {

        BlockPos blockPos = pos.relative(direction.getOpposite());

        boolean isClay = level.getBlockState(pos.below()).is(Blocks.CLAY);
        if (isClay && level.getBlockState(blockPos).is(Blocks.BLUE_ICE)) {
            Block block = BlockSchemas.DACITE.getBaseBlock();
            level.setBlockAndUpdate(pos, block.defaultBlockState());
            this.fizz(level, pos);
            cir.setReturnValue(false);
        }

        boolean isSnow = level.getBlockState(pos.below()).is(Blocks.SNOW_BLOCK);
        if (isSnow && level.getFluidState(blockPos).is(FluidTags.WATER)) {
            Block block = level.getFluidState(pos).isSource() ? Blocks.OBSIDIAN : BlockSchemas.WHITE_GRANITE.getBaseBlock();
            level.setBlockAndUpdate(pos, block.defaultBlockState());
            this.fizz(level, pos);
            cir.setReturnValue(false);
        }
    }
}

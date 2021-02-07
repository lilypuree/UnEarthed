package net.lilycorgitaco.unearthed.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.surfacebuilder.NetherSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NetherSurfaceBuilder.class)
public class MixinNetherSurfaceBuilder {

    @Mutable
    @Shadow
    @Final
    private static BlockState GRAVEL;

    @Inject(method = "generate", at = @At(value = "HEAD"))
    private void onInit(Random random, Chunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, TernarySurfaceConfig config, CallbackInfo ci) {
        GRAVEL = UEBlocks.PYROXENE.getDefaultState();
    }
}

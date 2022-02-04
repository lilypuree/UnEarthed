package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.NetherSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.oriondevcorgitaco.unearthed.UnearthedConfig;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NetherSurfaceBuilder.class)
public class MixinNetherSurfaceBuilder {

    @Shadow
    public static BlockState GRAVEL;

    @Inject(method = "apply(Ljava/util/Random;Lnet/minecraft/world/chunk/IChunk;Lnet/minecraft/world/biome/Biome;IIIDLnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;IJLnet/minecraft/world/gen/surfacebuilders/SurfaceBuilderConfig;)V", at = @At(value = "HEAD"))
    private void onInit(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config, CallbackInfo ci) {
        if (!UnearthedConfig.disableNetherGeneration.get()){
            GRAVEL = UEBlocks.PYROXENE.defaultBlockState();
        }
    }
}

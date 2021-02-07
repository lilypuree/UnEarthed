package net.lilycorgitaco.unearthed.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.lilycorgitaco.unearthed.UEFeatures.NEW_GENERATOR;

@Mixin(FlatChunkGeneratorConfig.class)
public class MixinFlatGenerationSettings {

    @Inject(method = "createBiome", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/GenerationSettings$Builder;build()Lnet/minecraft/world/biome/GenerationSettings;"), locals = LocalCapture.PRINT)
    private void addUnearthedFeature(CallbackInfoReturnable<Biome> ci, Biome biome, GenerationSettings biomeGenerationSettings, GenerationSettings.Builder builder, boolean flag, BlockState[] blockStates) {
        builder.feature(GenerationStep.Feature.TOP_LAYER_MODIFICATION, NEW_GENERATOR);
    }
}

package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.oriondevcorgitaco.unearthed.UEFeatures;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static net.oriondevcorgitaco.unearthed.UEFeatures.NEW_GENERATOR;

@Mixin(FlatGenerationSettings.class)
public class MixinFlatGenerationSettings {

    @Inject(method = "Lnet/minecraft/world/gen/FlatGenerationSettings;func_236942_c_()Lnet/minecraft/world/biome/Biome;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/BiomeGenerationSettings$Builder;build()Lnet/minecraft/world/biome/BiomeGenerationSettings;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void addUnearthedFeature(CallbackInfoReturnable<Biome> ci, Biome biome, BiomeGenerationSettings biomeGenerationSettings, BiomeGenerationSettings.Builder builder, boolean flag, BlockState[] blockStates) {
        builder.withFeature(GenerationStage.Decoration.TOP_LAYER_MODIFICATION, NEW_GENERATOR);
    }
}

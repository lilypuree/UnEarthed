package net.oriondevcorgitaco.unearthed.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.color.ColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.level.ColorResolver;
import net.oriondevcorgitaco.unearthed.world.LichenColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public class MixinClientWorld {


    @Shadow public Object2ObjectArrayMap<ColorResolver, ColorCache> tintCaches;

    @Inject(at = @At("RETURN"), method = "Lnet/minecraft/client/world/ClientWorld;<init>(Lnet/minecraft/client/network/play/ClientPlayNetHandler;Lnet/minecraft/client/world/ClientWorld$ClientWorldInfo;Lnet/minecraft/util/RegistryKey;Lnet/minecraft/world/DimensionType;ILjava/util/function/Supplier;Lnet/minecraft/client/renderer/WorldRenderer;ZJ)V")
    private void addLichenColorCaches(ClientPlayNetHandler clientPlayNetHandler, ClientWorld.ClientWorldInfo clientWorldInfo, RegistryKey<World> key, DimensionType type, int viewDistance, Supplier<IProfiler> profiler, WorldRenderer renderer, boolean isDebug, long seed, CallbackInfo ci) {
        tintCaches = Util.make(new Object2ObjectArrayMap<>(tintCaches.size() + 1), map -> {
            map.putAll(tintCaches);
            map.put(LichenColors.LICHEN_COLOR, new ColorCache());
        });
    }

    @Inject(at = @At(value = "NEW", target = "net/minecraft/util/math/CubeCoordinateIterator"), method = "calculateBlockTint(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/level/ColorResolver;)I", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void setLichenBlendRadius(BlockPos blockPosIn, ColorResolver colorResolverIn, CallbackInfoReturnable<Integer> cir, int i, int j, int k, int l, int i1) {
        if (colorResolverIn == LichenColors.LICHEN_COLOR) {
            i = 1;
            j = (i * 2 + 1) * (i * 2 + 1);
        }
    }
}

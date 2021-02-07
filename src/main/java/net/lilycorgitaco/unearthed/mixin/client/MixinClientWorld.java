package net.lilycorgitaco.unearthed.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.BiomeColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.ColorResolver;
import net.lilycorgitaco.unearthed.world.LichenColors;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public class MixinClientWorld {

    @Mutable
    @Shadow
    @Final
    private Object2ObjectArrayMap<ColorResolver, BiomeColorCache> colorCache;

    @Inject(at = @At("RETURN"), method = "<init>")
    private void addLichenColorCaches(ClientPlayNetworkHandler clientPlayNetHandler, ClientWorld.Properties clientWorldInfo, RegistryKey<World> key, DimensionType type, int viewDistance, Supplier<Profiler> profiler, WorldRenderer renderer, boolean isDebug, long seed, CallbackInfo ci) {
        colorCache = Util.make(new Object2ObjectArrayMap<>(colorCache.size() + 1), map -> {
            map.putAll(colorCache);
            map.put(LichenColors.LICHEN_COLOR, new BiomeColorCache());
        });
    }

    @Inject(at = @At(value = "NEW", target = "Lnet/minecraft/util/CuboidBlockIterator;<init>"), method = "calculateColor", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void setLichenBlendRadius(BlockPos blockPosIn, ColorResolver colorResolverIn, CallbackInfoReturnable<Integer> cir, int i, int j, int k, int l, int i1) {
        if (colorResolverIn == LichenColors.LICHEN_COLOR) {
            i = 1;
            j = (i * 2 + 1) * (i * 2 + 1);
        }
    }
}

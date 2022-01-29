package lilypuree.unearthed.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lilypuree.unearthed.world.LichenColors;
import net.minecraft.Util;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
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

@Mixin(ClientLevel.class)
public abstract class MixinClientWorld {


    @Mutable
    @Final
    @Shadow
    private Object2ObjectArrayMap<ColorResolver, BlockTintCache> tintCaches;

    @Shadow public abstract int calculateBlockTint(BlockPos $$0, ColorResolver $$1);

    @Inject(at = @At("RETURN"), method = "<init>")
    private void addLichenColorCaches(ClientPacketListener clientPlayNetHandler, ClientLevel.ClientLevelData clientWorldInfo, ResourceKey<Level> key, DimensionType type, int viewDistance, int serverSimulationDistance, Supplier<ProfilerFiller> profiler, LevelRenderer renderer, boolean isDebug, long seed, CallbackInfo ci) {
        tintCaches = Util.make(new Object2ObjectArrayMap<>(tintCaches.size() + 1), map -> {
            map.putAll(tintCaches);
            map.put(LichenColors.LICHEN_COLOR, new BlockTintCache(i->this.calculateBlockTint(i, LichenColors.LICHEN_COLOR)));
        });
    }

    @Inject(at = @At(value = "NEW", target = "net/minecraft/core/Cursor3D"), method = "calculateBlockTint", locals = LocalCapture.CAPTURE_FAILSOFT)
    private void setLichenBlendRadius(BlockPos blockPosIn, ColorResolver colorResolverIn, CallbackInfoReturnable<Integer> cir, int i, int j, int k, int l, int i1) {
        if (colorResolverIn == LichenColors.LICHEN_COLOR) {
            i = 1;
            j = (i * 2 + 1) * (i * 2 + 1);
        }
    }
}

package lilypuree.unearthed.mixin;

import lilypuree.unearthed.BiomeInjector;
import lilypuree.unearthed.Constants;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(MinecraftServer sever, Executor executor, LevelStorageSource.LevelStorageAccess storageAccess, ServerLevelData levelData, ResourceKey<Level> key, DimensionType dimensionType, ChunkProgressListener chunkProgressListener, ChunkGenerator generator, boolean p_8579_, long p_8580_, List<CustomSpawner> p_8581_, boolean p_8582_, CallbackInfo ci) {
        if (key.equals(Level.OVERWORLD) && !Constants.CONFIG.disableReplacement()) {
            Constants.LOG.log(org.apache.logging.log4j.Level.INFO, "Unearthed biomesource cache modification started");
            long start = System.currentTimeMillis();

            if (generator.biomeSource instanceof FixedBiomeSource) {
                BiomeInjector.apply(generator.biomeSource.possibleBiomes(), sever.registryAccess());
            }

            BiomeInjector.apply(generator.getBiomeSource());
            if (generator.biomeSource != generator.getBiomeSource()) {
                BiomeInjector.apply(generator.biomeSource);
            }
            long timeTook = System.currentTimeMillis() - start;
            Constants.LOG.log(org.apache.logging.log4j.Level.INFO, "Unearthed biomesource cache modification took {} ms to complete.", timeTook);
        }
    }
}

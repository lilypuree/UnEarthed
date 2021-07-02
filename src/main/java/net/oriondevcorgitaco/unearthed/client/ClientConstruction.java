package net.oriondevcorgitaco.unearthed.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;

import java.util.concurrent.CompletableFuture;

public class ClientConstruction {
    @SuppressWarnings("ConstantConditions")
    public static void run() {
        if (Minecraft.getInstance() == null) return;
        Minecraft.getInstance().getResourcePackRepository().addPackFinder(new UEPackFinder(UETextureStitcher.RESOURCE_PACK_FOLDER));
        if (Minecraft.getInstance().getResourceManager() instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(
                    (stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
                        CompletableFuture<Void> branchStitcher = CompletableFuture.supplyAsync(UETextureStitcher::new)
                                .thenApplyAsync(UETextureStitcher::prepare)
                                .thenAcceptAsync(UETextureStitcher::generate, backgroundExecutor);
                        return branchStitcher.thenCompose(stage::wait);
                    });
        }
    }
}

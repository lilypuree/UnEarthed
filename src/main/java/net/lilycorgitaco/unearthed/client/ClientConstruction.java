//package net.lilycorgitaco.unearthed.client;
//
//import java.util.concurrent.CompletableFuture;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.resource.ReloadableResourceManager;
//
//public class ClientConstruction {
//    @SuppressWarnings("ConstantConditions")
//    public static void run() {
//        if (MinecraftClient.getInstance() == null) return;
//        MinecraftClient.getInstance().getResourcePackManager().addPackFinder(new UEPackFinder(UETextureStitcher.RESOURCE_PACK_FOLDER));
//        if (MinecraftClient.getInstance().getResourceManager() instanceof ReloadableResourceManager) {
//            ((ReloadableResourceManager) MinecraftClient.getInstance().getResourceManager()).registerListener(
//                    (stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> {
//                        CompletableFuture<Void> branchStitcher = CompletableFuture.supplyAsync(UETextureStitcher::new)
//                                .thenApplyAsync(UETextureStitcher::prepare)
//                                .thenAcceptAsync(UETextureStitcher::generate, backgroundExecutor);
//                        return branchStitcher.thenCompose(stage::whenPrepared);
//                    });
//        }
//    }
//}

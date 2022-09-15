package lilypuree.unearthed;

import lilypuree.unearthed.client.ClientCommon;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommon.initRenderLayers();
        ClientCommon.blockColors();
        ClientCommon.itemColors();
    }
}

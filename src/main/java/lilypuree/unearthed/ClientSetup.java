package lilypuree.unearthed;

import lilypuree.unearthed.client.ClientCommon;
import lilypuree.unearthed.platform.ForgePlatformHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ClientCommon.initRenderLayers();
    }

    @SubscribeEvent
    public static void onBlockColourHandleEvent(final RegisterColorHandlersEvent.Block event) {
        ForgePlatformHelper.blockColors = event.getBlockColors();
        ClientCommon.blockColors();
    }

    @SubscribeEvent
    public static void onItemColourHandlerEvent(final RegisterColorHandlersEvent.Item event) {
        ForgePlatformHelper.blockColors = event.getBlockColors();
        ForgePlatformHelper.itemColors = event.getItemColors();
        ClientCommon.itemColors();
    }
}
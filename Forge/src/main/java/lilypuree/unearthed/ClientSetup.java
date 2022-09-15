package lilypuree.unearthed;

import lilypuree.unearthed.client.ClientCommon;
import lilypuree.unearthed.platform.ForgePlatformHelper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        ClientCommon.initRenderLayers();
    }

    @SubscribeEvent
    public static void onBlockColourHandleEvent(final ColorHandlerEvent.Block event) {
        ForgePlatformHelper.blockColors = event.getBlockColors();
        ClientCommon.blockColors();
    }

    @SubscribeEvent
    public static void onItemColourHandlerEvent(final ColorHandlerEvent.Item event) {
        ForgePlatformHelper.blockColors = event.getBlockColors();
        ForgePlatformHelper.itemColors = event.getItemColors();
        ClientCommon.itemColors();
    }
}
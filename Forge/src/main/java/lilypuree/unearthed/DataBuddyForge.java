package lilypuree.unearthed;

import commoble.databuddy.data.CodecJsonDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Map;
import java.util.function.Function;

public class DataBuddyForge {

    /**
     * This should be called at most once, during construction of your mod (static init of your main mod class is fine)
     * (FMLCommonSetupEvent *may* work as well)
     * Calling this method automatically subscribes a packet-sender to {@link OnDatapackSyncEvent}.
     * @param <PACKET> the packet type that will be sent on the given channel
     * @param channel The networking channel of your mod
     * @param packetFactory  A packet constructor or factory method that converts the given map to a packet object to send on the given channel
     */
    public static <PACKET, T> void subscribe(CodecJsonDataManager<T> dataManager, final SimpleChannel channel, final Function<Map<ResourceLocation, T>, PACKET> packetFactory) {
        /** Generate an event listener function for the on-datapack-sync event **/
        MinecraftForge.EVENT_BUS.addListener((OnDatapackSyncEvent e) -> {
            ServerPlayer player = e.getPlayer();
            PACKET packet = packetFactory.apply(dataManager.getData());
            PacketDistributor.PacketTarget target = player == null
                    ? PacketDistributor.ALL.noArg()
                    : PacketDistributor.PLAYER.with(() -> player);
            channel.send(target, packet);
        });
    }
}

package net.oriondevcorgitaco.unearthed.planets.mixin.client;

import net.minecraft.client.audio.MinecartTickableSound;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.oriondevcorgitaco.unearthed.core.UEEntities;
import net.oriondevcorgitaco.unearthed.planets.entity.AsteroidEntity;
import net.oriondevcorgitaco.unearthed.planets.entity.CloudEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//@Mixin(ClientPlayNetHandler.class)
public class MixinClientPlayNetHandler {
//    @Shadow
    private ClientWorld world;

//    @Inject(
//            method = "handleSpawnObject(Lnet/minecraft/network/play/server/SSpawnObjectPacket;)V",
//            at = @At("HEAD"),
//            cancellable = true
//    )
    private void onEntitySpawn(SSpawnObjectPacket packetIn, CallbackInfo ci) {
        EntityType<?> type = packetIn.getType();
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        Entity entity = null;
        if (type == UEEntities.ASTEROID) {
            entity = new AsteroidEntity(world, Float.intBitsToFloat(packetIn.getData()));
        }else if (type == UEEntities.CLOUD){
            entity = new CloudEntity(world);
        }
        if (entity != null) {
            int i = packetIn.getEntityID();
            entity.setPacketCoordinates(d0, d1, d2);
            entity.moveForced(d0, d1, d2);
            entity.rotationPitch = (float) (packetIn.getPitch() * 360) / 256.0F;
            entity.rotationYaw = (float) (packetIn.getYaw() * 360) / 256.0F;
            entity.setEntityId(i);
            entity.setUniqueId(packetIn.getUniqueId());
            this.world.addEntity(i, entity);
            ci.cancel();
        }
    }
}

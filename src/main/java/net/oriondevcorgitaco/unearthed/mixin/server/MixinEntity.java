package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.properties.ModBlockProperties;
import net.oriondevcorgitaco.unearthed.block.PuddleBlock;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "doWaterSplashEffect()V", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void makePuddlesAfterSplash(CallbackInfo ci, Entity entity, float f, Vector3d vector3d, float f1, float f2) {
        if (f1 > 0.20f) {
            World world = entity.getCommandSenderWorld();
            BlockPos pos = entity.blockPosition();
            if (entity instanceof PlayerEntity && !world.isClientSide() && world.getGameRules().getBoolean(Unearthed.DO_PUDDLE_CREATION)) {
                int radius = MathHelper.clamp(((int) (f1 * 40 - 8)), 1, 3);
                BlockPos.randomBetweenClosed(world.random, ((int) (5 + radius * radius)), pos.getX() - radius, pos.getY() - 1, pos.getZ() - radius, pos.getX() + radius, pos.getY() + 1, pos.getZ() + radius).forEach(blockPos -> {
                    BlockState block = world.getBlockState(blockPos);
                    BlockPos up = blockPos.above();
                    if (world.getBlockState(up).isAir() && Block.isFaceFull(block.getCollisionShape(world, blockPos), Direction.UP)) {
                        if (entity.getType() == EntityType.PLAYER) {
                            world.setBlockAndUpdate(up, UEBlocks.PUDDLE.defaultBlockState());
                        } else {
                            world.setBlockAndUpdate(up, UEBlocks.PUDDLE.defaultBlockState().setValue(ModBlockProperties.TRANSIENT, true));
                            world.getBlockTicks().scheduleTick(up, UEBlocks.PUDDLE, PuddleBlock.getEvaporationDelay(world, pos));
                        }
                    }
                });
            }
        }
    }
}

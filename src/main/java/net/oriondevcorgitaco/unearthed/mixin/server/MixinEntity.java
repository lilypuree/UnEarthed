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
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getPosition();
            if (entity instanceof PlayerEntity && !world.isRemote() && world.getGameRules().getBoolean(Unearthed.DO_PUDDLE_CREATION)) {
                int radius = MathHelper.clamp(((int) (f1 * 40 - 8)), 1, 3);
                BlockPos.getRandomPositions(world.rand, ((int) (5 + radius * radius)), pos.getX() - radius, pos.getY() - 1, pos.getZ() - radius, pos.getX() + radius, pos.getY() + 1, pos.getZ() + radius).forEach(blockPos -> {
                    BlockState block = world.getBlockState(blockPos);
                    BlockPos up = blockPos.up();
                    if (world.getBlockState(up).isAir() && Block.doesSideFillSquare(block.getCollisionShape(world, blockPos), Direction.UP)) {
                        if (entity.getType() == EntityType.PLAYER) {
                            world.setBlockState(up, UEBlocks.PUDDLE.getDefaultState());
                        } else {
                            world.setBlockState(up, UEBlocks.PUDDLE.getDefaultState().with(ModBlockProperties.TRANSIENT, true));
                            world.getPendingBlockTicks().scheduleTick(up, UEBlocks.PUDDLE, PuddleBlock.getEvaporationDelay(world, pos));
                        }
                    }
                });
            }
        }
    }
}

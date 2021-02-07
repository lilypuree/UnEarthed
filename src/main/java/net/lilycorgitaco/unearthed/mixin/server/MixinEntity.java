package net.lilycorgitaco.unearthed.mixin.server;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.lilycorgitaco.unearthed.block.ModBlockProperties;
import net.lilycorgitaco.unearthed.block.PuddleBlock;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "onSwimmingStart()V", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void makePuddlesAfterSplash(CallbackInfo ci, Entity entity, float f, Vec3d vector3d, float f1, float f2) {
        if (f1 > 0.20f) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getBlockPos();
            if (!world.isClient()) {
                int radius = MathHelper.clamp(((int) (f1 * 40 - 8)), 1, 3);
                BlockPos.iterateRandomly(world.random, ((int) (5 + radius * radius)), pos.getX() - radius, pos.getY() - 1, pos.getZ() - radius, pos.getX() + radius, pos.getY() + 1, pos.getZ() + radius).forEach(blockPos -> {
                    BlockState block = world.getBlockState(blockPos);
                    BlockPos up = blockPos.up();
                    if (world.getBlockState(up).isAir() && Block.isFaceFullSquare(block.getCollisionShape(world, blockPos), Direction.UP)) {
                        if (entity.getType() == EntityType.PLAYER) {
                            world.setBlockState(up, UEBlocks.PUDDLE.getDefaultState());
                        } else {
                            world.setBlockState(up, UEBlocks.PUDDLE.getDefaultState().with(ModBlockProperties.TRANSIENT, true));
                            world.getBlockTickScheduler().schedule(up, UEBlocks.PUDDLE, PuddleBlock.getEvaporationDelay(world, pos));
                        }
                    }
                });
            }
        }
    }
}

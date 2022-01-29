package lilypuree.unearthed.mixin.server;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "doWaterSplashEffect()V", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void makePuddlesAfterSplash(CallbackInfo ci, Entity entity, float f, Vec3 vector3d, float f1, float f2) {
//        if (f1 > 0.20f) {
//            Level world = entity.getCommandSenderWorld();
//            BlockPos pos = entity.blockPosition();
//            if (entity instanceof Player && !world.isClientSide() && world.getGameRules().getBoolean(Unearthed.DO_PUDDLE_CREATION)) {
//                int radius = Mth.clamp(((int) (f1 * 40 - 8)), 1, 3);
//                BlockPos.randomBetweenClosed(world.random, ((int) (5 + radius * radius)), pos.getX() - radius, pos.getY() - 1, pos.getZ() - radius, pos.getX() + radius, pos.getY() + 1, pos.getZ() + radius).forEach(blockPos -> {
//                    BlockState block = world.getBlockState(blockPos);
//                    BlockPos up = blockPos.above();
//                    if (world.getBlockState(up).isAir() && Block.isFaceFull(block.getCollisionShape(world, blockPos), Direction.UP)) {
//                        if (entity.getType() == EntityType.PLAYER) {
//                            world.setBlockAndUpdate(up, UEBlocks.PUDDLE.defaultBlockState());
//                        } else {
//                            world.setBlockAndUpdate(up, UEBlocks.PUDDLE.defaultBlockState().setValue(ModBlockProperties.TRANSIENT, true));
//                            world.getBlockTicks().scheduleTick(up, UEBlocks.PUDDLE, PuddleBlock.getEvaporationDelay(world, pos));
//                        }
//                    }
//                });
//            }
//        }
    }
}

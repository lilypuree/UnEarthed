package net.oriondevcorgitaco.unearthed.mixin.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.oriondevcorgitaco.unearthed.block.RegolithGrassBlock;
import net.oriondevcorgitaco.unearthed.core.UETags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(AnimalEntity.class)
public class MixinAnimalEntity {

    @Inject(at = @At("HEAD"), method = "checkAnimalSpawnRules", cancellable = true)
    private static void onCheckAnimalSpawnRules(EntityType<? extends AnimalEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random rand, CallbackInfoReturnable<Boolean> cir){
        if (world.getBlockState(pos.below()).getBlock() instanceof RegolithGrassBlock){
            cir.setReturnValue(world.getRawBrightness(pos, 0) > 8);
        }
    }
}

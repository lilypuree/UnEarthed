package net.oriondevcorgitaco.unearthed.mixin.server;


import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IItemProvider;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(AbstractFurnaceTileEntity.class)
public abstract class MixinAbstractFurnaceTileEntity {

    @Shadow
    protected static void add(Map<Item, Integer> map, IItemProvider item, int burnTime) {
    }

    @Inject(method = "getFuel()Ljava/util/Map;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void onGetBurnTime(CallbackInfoReturnable<Map<Item, Integer>> cir, Map<Item, Integer> map){
        add(map, UEItems.LIGNITE_BRIQUETTES, 2000);
        add(map, BlockGeneratorReference.LIGNITE.getBaseBlock(), 200);
    }
}

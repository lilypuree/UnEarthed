package net.lilycorgitaco.unearthed.mixin.server;


import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.tag.Tag;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.core.UEItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public class MixinAbstractFurnaceTileEntity {

    @Shadow
    protected static void addFuel(Map<Item, Integer> map, Tag<Item> itemTag, int burnTimeIn) {
    }

    @Shadow
    protected static void addFuel(Map<Item, Integer> map, ItemConvertible itemProvider, int burnTimeIn) {
    }

    @Inject(method = "Lnet/minecraft/tileentity/AbstractFurnaceTileEntity;getBurnTimes()Ljava/util/Map;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void onGetBurnTime(CallbackInfoReturnable<Map<Item, Integer>> cir, Map<Item, Integer> map){
        addFuel(map, UEItems.LIGNITE_BRIQUETTES, 2000);
        addFuel(map, BlockGeneratorReference.LIGNITE.getBaseBlock(), 200);
    }
}

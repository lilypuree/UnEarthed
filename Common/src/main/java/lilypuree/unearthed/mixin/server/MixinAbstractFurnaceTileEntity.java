package lilypuree.unearthed.mixin.server;


import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.core.UEItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceTileEntity {

    @Shadow
    protected static void add(Map<Item, Integer> map, ItemLike item, int burnTime) {
    }

    @Inject(method = "getFuel()Ljava/util/Map;", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void onGetBurnTime(CallbackInfoReturnable<Map<Item, Integer>> cir, Map<Item, Integer> map) {
        add(map, UEItems.LIGNITE_BRIQUETTES, 2000);
        add(map, BlockSchemas.LIGNITE.getBaseBlock(), 200);
    }
}

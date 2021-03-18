package net.lilycorgitaco.unearthed.misc;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.lilycorgitaco.unearthed.block.BlockGeneratorHelper;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.block.schema.BlockSchema;
import net.lilycorgitaco.unearthed.block.schema.Forms;
import net.lilycorgitaco.unearthed.block.schema.Variants;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public class VillagerTrades {

    public static void addMapTrades(){
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                if (entry.getForm() == Forms.BLOCK) {
                    BlockSchema.Variant variant = entry.getVariant();
                    if (variant == Variants.POLISHED) {
                        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 3, factories -> {
                            factories.add(new TradeOffers.SellItemFactory(entry.getBlock(), 1, 4, 16, 10));
                        });
                    } else if (variant == Variants.CHISELED_BRICKS) {
                        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 3, factories -> {
                            factories.add(new TradeOffers.SellItemFactory(entry.getBlock(), 1, 4, 16, 5));
                        });
                    } else if (variant == Variants.CHISELED_FULL || variant == Variants.CHISELED) {
                        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 4, factories -> {
                            factories.add(new TradeOffers.SellItemFactory(entry.getBlock(), 1, 1, 12, 15));
                        });
                    } else if (variant == Variants.CHISELED_POLISHED) {
                        TradeOfferHelper.registerVillagerOffers(VillagerProfession.MASON, 5, factories -> {
                            factories.add(new TradeOffers.SellItemFactory(entry.getBlock(), 1, 1, 12, 30));
                        });
                    }
                }
            }
        }
    }

}

package net.oriondevcorgitaco.unearthed;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Items;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.StoneTiers;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;

@Mod.EventBusSubscriber(modid = Unearthed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.MASON) {
            for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
                for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                    if (entry.getForm() == Forms.BLOCK) {
                        BlockSchema.Variant variant = entry.getVariant();
                        if (variant == Variants.POLISHED) {
                            event.getTrades().get(3).add(new VillagerTrades.ItemsForEmeraldsTrade(entry.getBlock(), 1, 4, 16, 10));
                        } else if (variant == Variants.CHISELED_BRICKS) {
                            event.getTrades().get(3).add(new VillagerTrades.ItemsForEmeraldsTrade(entry.getBlock(), 1, 4, 16, 5));
                        } else if (variant == Variants.CHISELED_FULL || variant == Variants.CHISELED) {
                            event.getTrades().get(4).add(new VillagerTrades.ItemsForEmeraldsTrade(entry.getBlock(), 1, 1, 12, 15));
                        } else if (variant == Variants.CHISELED_POLISHED) {
                            event.getTrades().get(5).add(new VillagerTrades.ItemsForEmeraldsTrade(entry.getBlock(), 1, 1, 12, 30));
                        }
                    }
                }
            }
        }
    }


}

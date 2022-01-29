package lilypuree.unearthed_forge;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.*;
import lilypuree.unearthed.world.feature.UEConfiguredFeatures;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {


    @SubscribeEvent
    public static void onVillagerTradesEvent(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.MASON) {
            for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
                for (SchemaEntry entry : type.entries()) {
                    if (entry.getForm() == Forms.BLOCK) {
                        BlockVariant variant = entry.getVariant();
                        ItemStack stack = new ItemStack(entry.getBlock().asItem(), 4);
                        if (variant == Variants.POLISHED) {
                            event.getTrades().get(3).add(new BasicItemListing(1, stack, 16, 10));
                        } else if (variant == Variants.CHISELED_BRICKS) {
                            event.getTrades().get(3).add(new BasicItemListing(1, stack, 16, 5));
                        } else if (variant == Variants.CHISELED_FULL || variant == Variants.CHISELED) {
                            stack.setCount(1);
                            event.getTrades().get(4).add(new BasicItemListing(1, stack, 12, 15));
                        } else if (variant == Variants.CHISELED_POLISHED) {
                            stack.setCount(1);
                            event.getTrades().get(5).add(new BasicItemListing(1, stack, 12, 30));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getCategory() != Biome.BiomeCategory.NETHER && event.getCategory() != Biome.BiomeCategory.THEEND) {
            event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, UEConfiguredFeatures.STONE_REPLACER_PLACED);
        }
    }
}

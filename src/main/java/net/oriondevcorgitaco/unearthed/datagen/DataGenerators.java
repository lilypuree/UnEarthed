package net.oriondevcorgitaco.unearthed.datagen;

import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilProperties;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.oriondevcorgitaco.unearthed.Unearthed;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.addProvider(new Recipes(generator));
            generator.addProvider(new LootTables(generator));
            BlockTagsProvider blockTagsProvider = new UEBlockTagsProvider(generator, event.getExistingFileHelper());
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new UEItemTagsProvider(generator, blockTagsProvider, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
            generator.addProvider(new BlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new Items(generator, event.getExistingFileHelper()));
            generator.addProvider(new Languages(generator, "en_us"));
            generator.addProvider(new Languages(generator, "en_gb"));
        }
        if (ModList.get().isLoaded("dynamictrees")) {
            GatherDataHelper.gatherBlockStateAndModelData(Unearthed.MOD_ID, event, SoilProperties.REGISTRY);
        }
    }
}
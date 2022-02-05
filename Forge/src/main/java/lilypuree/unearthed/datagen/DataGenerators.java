package lilypuree.unearthed.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
//            generator.addProvider(new UERecipes(generator));
            generator.addProvider(new UELootTables(generator));
//            BlockTagsProvider blockTagsProvider = new UEBlockTagsProvider(generator, event.getExistingFileHelper());
//            generator.addProvider(blockTagsProvider);
//            generator.addProvider(new UEItemTagsProvider(generator, blockTagsProvider, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
//            generator.addProvider(new UEBlockStates(generator, event.getExistingFileHelper()));
//            generator.addProvider(new UEItemModels(generator, event.getExistingFileHelper()));
//            generator.addProvider(new UELanguages(generator, "en_us"));
//            generator.addProvider(new UELanguages(generator, "en_gb"));
        }
    }
}
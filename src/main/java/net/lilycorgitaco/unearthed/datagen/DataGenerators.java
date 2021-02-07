package net.lilycorgitaco.unearthed.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            generator.install(new Recipes(generator));
            generator.install(new LootTables(generator));
            BlockTagsProvider blockTagsProvider = new UEBlockTagsProvider(generator, event.getExistingFileHelper());
            generator.install(blockTagsProvider);
            generator.install(new UEItemTagsProvider(generator, blockTagsProvider, event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
            generator.install(new BlockStates(generator, event.getExistingFileHelper()));
            generator.install(new Items(generator, event.getExistingFileHelper()));
            generator.install(new Languages(generator, "en_us"));
        }
    }
}
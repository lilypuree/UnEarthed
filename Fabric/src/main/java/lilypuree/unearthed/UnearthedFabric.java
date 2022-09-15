package lilypuree.unearthed;

import lilypuree.unearthed.core.Registration;
import lilypuree.unearthed.core.RegistryHelper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class UnearthedFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        AutoConfig.register(UEFabricConfigs.class, JanksonConfigSerializer::new);
        Constants.CONFIG = AutoConfig.getConfigHolder(UEFabricConfigs.class).getConfig();

        CommonSetup.init();
        Registration.registerBlocks(new RegistryHelperFabric<>(Registry.BLOCK));
        Registration.registerItems(new RegistryHelperFabric<>(Registry.ITEM));
        Registration.registerLootConditions();
        CommonSetup.commonSetup();
    }

    public static class RegistryHelperFabric<T> implements RegistryHelper<T> {
        Registry<T> registry;

        public RegistryHelperFabric(Registry<T> registry) {
            this.registry = registry;
        }

        @Override
        public void register(T entry, ResourceLocation name) {
            Registry.register(registry, name, entry);
        }
    }
}

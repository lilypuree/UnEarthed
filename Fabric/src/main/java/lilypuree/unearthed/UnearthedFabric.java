package lilypuree.unearthed;

import lilypuree.unearthed.core.Registration;
import lilypuree.unearthed.core.RegistryHelper;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class UnearthedFabric implements ModInitializer, CommonHelper {

    @Override
    public void onInitialize() {

        AutoConfig.register(UEFabricConfigs.class, JanksonConfigSerializer::new);
        Constants.CONFIG = AutoConfig.getConfigHolder(UEFabricConfigs.class).getConfig();

        CommonMod.init(this);
        Registration.registerBlocks(new RegistryHelperFabric<>(Registry.BLOCK));
        Registration.registerItems(new RegistryHelperFabric<>(Registry.ITEM));

        CommonMod.commonSetup();
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

    @Override
    public CreativeModeTab creativeModeTab(ResourceLocation name, Supplier<ItemStack> stack) {
        return FabricItemGroupBuilder.build(name, stack);
    }
}

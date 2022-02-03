package lilypuree.unearthed;


import lilypuree.unearthed.misc.UEDataLoaders;
import lilypuree.unearthed.core.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.Supplier;

@Mod(Constants.MOD_ID)
public class UnearthedForge implements CommonHelper {
    public UnearthedForge() {
        CommonMod.init(this);
        Constants.CONFIG = new UEForgeConfigs();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UEForgeConfigs.COMMON_CONFIG);

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(this::commonSetup);
        modbus.addGenericListener(Block.class, (RegistryEvent.Register<Block> e) -> Registration.registerBlocks(new RegistryHelperForge<>(e.getRegistry())));
        modbus.addGenericListener(Item.class, (RegistryEvent.Register<Item> e) -> Registration.registerItems(new RegistryHelperForge<>(e.getRegistry())));
        modbus.addGenericListener(Feature.class, (RegistryEvent.Register<Feature<?>> e) -> Registration.registerFeatures(new RegistryHelperForge<>(e.getRegistry())));

        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent e) -> {
            e.addListener(UEDataLoaders.STONE_TYPE_LOADER);
            e.addListener(UEDataLoaders.REGION_LOADER);
        });
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CommonMod.commonSetup();
       }

    public static class RegistryHelperForge<T extends IForgeRegistryEntry<T>> implements RegistryHelper<T> {
        IForgeRegistry<T> registry;

        public RegistryHelperForge(IForgeRegistry<T> registry) {
            this.registry = registry;
        }

        @Override
        public void register(T entry, ResourceLocation name) {
            registry.register(entry.setRegistryName(name));
        }
    }

    @Override
    public Tag.Named<Block> createBlock(ResourceLocation name) {
        return BlockTags.bind(name.toString());
    }

    @Override
    public Tag.Named<Item> createItem(ResourceLocation name) {
        return ItemTags.bind(name.toString());
    }

    @Override
    public CreativeModeTab creativeModeTab(ResourceLocation name, Supplier<ItemStack> stack) {
        return new CreativeModeTab(-1, name.getNamespace() + "." + name.getPath()) {
            @Override
            public ItemStack makeIcon() {
                return stack.get();
            }
        };
    }
}

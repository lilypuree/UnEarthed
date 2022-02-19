package lilypuree.unearthed;

import lilypuree.unearthed.compat.StoneTypeCallback;
import lilypuree.unearthed.core.Registration;
import lilypuree.unearthed.core.RegistryHelper;
import lilypuree.unearthed.core.UENames;
import lilypuree.unearthed.misc.StoneTypeCodecJsonDataManager;
import lilypuree.unearthed.world.feature.gen.StoneType;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.function.Supplier;

public class UnearthedFabric implements ModInitializer, CommonHelper {

    @Override
    public void onInitialize() {

        AutoConfig.register(UEFabricConfigs.class, JanksonConfigSerializer::new);
        Constants.CONFIG = AutoConfig.getConfigHolder(UEFabricConfigs.class).getConfig();

        CommonMod.init(this);
        Registration.registerBlocks(new RegistryHelperFabric<>(Registry.BLOCK));
        Registration.registerItems(new RegistryHelperFabric<>(Registry.ITEM));
        Registration.registerFeatures(new RegistryHelperFabric<>(Registry.FEATURE));

        CommonMod.commonSetup();

        if (Constants.CONFIG.disableReplacement()) return;
        BiomeModifications.addFeature(ctx -> {
                    Biome.BiomeCategory category = ctx.getBiome().getBiomeCategory();
                    return category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND;
                }, GenerationStep.Decoration.TOP_LAYER_MODIFICATION,
                ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, UENames.STONE_REPLACER));
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
    public StoneTypeCodecJsonDataManager getStoneTypeManager() {
        return new StoneTypeCodecJsonDataManager() {
            @Override
            public boolean fireEvent(StoneType stoneType) {
                return StoneTypeCallback.EVENT.invoker().apply(stoneType);
            }
        };
    }

    @Override
    public Tag.Named<Block> createBlock(ResourceLocation name) {
        return TagFactory.BLOCK.create(name);
    }

    @Override
    public Tag.Named<Item> createItem(ResourceLocation name) {
        return TagFactory.ITEM.create(name);
    }

    @Override
    public CreativeModeTab creativeModeTab(ResourceLocation name, Supplier<ItemStack> stack) {
        return FabricItemGroupBuilder.build(name, stack);
    }
}

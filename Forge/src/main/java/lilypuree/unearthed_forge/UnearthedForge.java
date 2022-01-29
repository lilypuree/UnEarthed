package lilypuree.unearthed_forge;


import lilypuree.unearthed.CommonMod;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.Forms;
import lilypuree.unearthed.block.schema.SchemaEntry;
import lilypuree.unearthed.core.UEBlocks;
import lilypuree.unearthed.core.UEItems;
import lilypuree.unearthed.core.UENames;
import lilypuree.unearthed.core.UETags;
import lilypuree.unearthed.world.feature.BlockStates;
import lilypuree.unearthed.world.feature.UEConfiguredFeatures;
import lilypuree.unearthed.world.feature.UEFeatures;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class UnearthedForge {
    public UnearthedForge() {
        CommonMod.init();
        tagInit();
//        Constants.config = new UEForgeConfigs();
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UEForgeConfigs.COMMON_CONFIG);
        Constants.ITEM_GROUP = new CreativeModeTab(-1, Constants.MOD_ID + ".general") {
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(Registry.ITEM.get(new ResourceLocation(getName("chiseled_limestone"))));
            }
        };

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(this::commonSetup);
        modbus.addGenericListener(Block.class, this::registerBlocks);
        modbus.addGenericListener(Item.class, this::registerItems);
        modbus.addGenericListener(Feature.class, this::registerFeatures);
    }

    public void tagInit() {
        UETags.Blocks.IGNEOUS_TAG = BlockTags.bind(getName("igneous"));
        UETags.Blocks.METAMORPHIC_TAG = BlockTags.bind(getName("metamorphic"));
        UETags.Blocks.SEDIMENTARY_TAG = BlockTags.bind(getName("sedimentary"));
        UETags.Blocks.REGOLITH_TAG = BlockTags.bind(getName("regolith"));
        UETags.Blocks.REPLACABLE = BlockTags.bind(getName("replaceable"));
        UETags.Blocks.REPLACE_DIRT = BlockTags.bind(getName("replace_dirt"));
        UETags.Blocks.REPLACE_GRASS = BlockTags.bind(getName("replace_grass"));

        UETags.Items.IGNEOUS_ITEM = ItemTags.bind(getName("igneous"));
        UETags.Items.METAMORPHIC_ITEM = ItemTags.bind(getName("metamorphic"));
        UETags.Items.SEDIMENTARY_ITEM = ItemTags.bind(getName("sedimentary"));
        UETags.Items.REGOLITH_USABLE = ItemTags.bind(getName("regolith_usable"));
        UETags.Items.REGOLITH_TAG = ItemTags.bind(getName("regolith"));
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : schema.entries()) {
                if (entry.getForm() == Forms.REGOLITH) {
                    LichenBlock.addErosionMap(schema.getBaseBlock(), entry.getBlock());
                }
            }
        }
        BlockStates.init();

        event.enqueueWork(() -> {
            UEConfiguredFeatures.register();
        });
    }

    private String getName(String name) {
        return Constants.MOD_ID + ":" + name;
    }

    public void registerBlocks(RegistryEvent.Register<Block> event) {
        Constants.LOG.debug("UE: Registering blocks...");
        BlockSchemas.ROCK_TYPES.forEach(schema -> schema.entries().forEach(entry -> {
            event.getRegistry().register(entry.createBlock(schema).setRegistryName(entry.getId()));
        }));
        UEBlocks.init();
        event.getRegistry().registerAll(
                UEBlocks.LICHEN.setRegistryName(UENames.LICHEN),
                UEBlocks.PYROXENE.setRegistryName(UENames.PYROXENE),
                UEBlocks.PUDDLE.setRegistryName(UENames.PUDDLE),
                UEBlocks.LIGNITE_BRIQUETTES.setRegistryName(UENames.LIGNITE_BRIQUETTES)
        );
        Constants.LOG.info("UE: Blocks registered!");
    }

    public void registerItems(RegistryEvent.Register<Item> event) {
        Constants.LOG.debug("UE: Registering items...");
        Item.Properties properties = new Item.Properties().tab(Constants.ITEM_GROUP);

        BlockSchemas.ROCK_TYPES.forEach(schema -> schema.entries().forEach(entry -> {
            event.getRegistry().register(new BlockItem(entry.getBlock(), properties).setRegistryName(entry.getId()));
        }));
        UEItems.init();
        event.getRegistry().registerAll(
                UEItems.LICHEN.setRegistryName(UENames.LICHEN),
                UEItems.PYROXENE.setRegistryName(UENames.PYROXENE),
                UEItems.PUDDLE.setRegistryName(UENames.PUDDLE),
                UEItems.LIGNITE_BRIQUETTES.setRegistryName(UENames.LIGNITE_BRIQUETTES),
                UEItems.GOLD_ORE.setRegistryName(UENames.GOLD_ORE),
                UEItems.IRON_ORE.setRegistryName(UENames.IRON_ORE),
                UEItems.REGOLITH.setRegistryName(UENames.REGOLITH)
        );

        Constants.LOG.info("UE: Items registered!");
    }

    public void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        UEFeatures.init();
        event.getRegistry().registerAll(
                UEFeatures.STONE_REPLACER.setRegistryName(UENames.STONE_REPLACER)
        );
    }

//    public static final GameRules.Key<GameRules.BooleanValue> DO_PUDDLE_CREATION = GameRules.register("doPuddleCreation", GameRules.Category.MISC, GameRules.BooleanValue.create(true));
}

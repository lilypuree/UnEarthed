package lilypuree.unearthed;

import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.Forms;
import lilypuree.unearthed.block.schema.SchemaEntry;
import lilypuree.unearthed.core.UETags;
import lilypuree.unearthed.misc.BlockStatePropertiesMatch;
import lilypuree.unearthed.misc.UEDataLoaders;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.openhft.hashing.LongHashFunction;

public class CommonMod {

    public static void init(CommonHelper helper) {
        UEDataLoaders.init(helper.getStoneTypeManager());
        BlockStatePropertiesMatch.init();

        Constants.ITEM_GROUP = helper.creativeModeTab(new ResourceLocation(Constants.MOD_ID, "general"), () -> new ItemStack(Registry.ITEM.get(new ResourceLocation(Constants.MOD_ID, "chiseled_limestone"))));

        UETags.Blocks.IGNEOUS_TAG = helper.createBlock("igneous");
        UETags.Blocks.METAMORPHIC_TAG = helper.createBlock("metamorphic");
        UETags.Blocks.SEDIMENTARY_TAG = helper.createBlock("sedimentary");
        UETags.Blocks.REGOLITH_TAG = helper.createBlock("regolith");
        UETags.Blocks.REPLACABLE = helper.createBlock("replaceable");
        UETags.Blocks.REPLACE_DIRT = helper.createBlock("replace_dirt");
        UETags.Blocks.REPLACE_GRASS = helper.createBlock("replace_grass");

        UETags.Items.IGNEOUS_ITEM = helper.createItem("igneous");
        UETags.Items.METAMORPHIC_ITEM = helper.createItem("metamorphic");
        UETags.Items.SEDIMENTARY_ITEM = helper.createItem("sedimentary");
        UETags.Items.REGOLITH_USABLE = helper.createItem("regolith_usable");
        UETags.Items.REGOLITH_TAG = helper.createItem("regolith");
        UETags.Items.UNEARTHED_IRON_ORE = helper.createItem("iron_ore");
        UETags.Items.UNEARTHED_COAL_ORE = helper.createItem("coal_ore");
        UETags.Items.UNEARTHED_GOLD_ORE = helper.createItem("gold_ore");
        UETags.Items.UNEARTHED_COPPER_ORE = helper.createItem("copper_ore");
        UETags.Items.UNEARTHED_LAPIS_ORE = helper.createItem("lapis_ore");
        UETags.Items.UNEARTHED_REDSTONE_ORE = helper.createItem("redstone_ore");
        UETags.Items.UNEARTHED_DIAMOND_ORE = helper.createItem("diamond_ore");
        UETags.Items.UNEARTHED_EMERALD_ORE = helper.createItem("emerald_ore");
    }

    public static void commonSetup() {
        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : schema.entries()) {
                if (entry.getForm() == Forms.REGOLITH) {
                    LichenBlock.addErosionMap(schema.getBaseBlock(), entry.getBlock());
                }
            }
        }
    }
}

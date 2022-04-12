package lilypuree.unearthed;

import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.Forms;
import lilypuree.unearthed.block.schema.SchemaEntry;
import lilypuree.unearthed.core.UETags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CommonMod {

    public static void init(CommonHelper helper) {
        Constants.ITEM_GROUP = helper.creativeModeTab(new ResourceLocation(Constants.MOD_ID, "general"), () -> new ItemStack(Registry.ITEM.get(new ResourceLocation(Constants.MOD_ID, "chiseled_limestone"))));

        UETags.init();
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

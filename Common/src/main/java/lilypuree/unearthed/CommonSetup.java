package lilypuree.unearthed;

import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.Forms;
import lilypuree.unearthed.block.schema.SchemaEntry;
import lilypuree.unearthed.core.UETags;
import lilypuree.unearthed.misc.BlockStatePropertiesMatch;
import lilypuree.unearthed.misc.HoeDig;

public class CommonSetup {

    public static void init() {
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

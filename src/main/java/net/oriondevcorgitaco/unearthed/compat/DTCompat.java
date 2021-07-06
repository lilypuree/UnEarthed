package net.oriondevcorgitaco.unearthed.compat;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.init.DTRegistries;
import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import com.ferreusveritas.dynamictrees.systems.RootyBlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;

public class DTCompat {

    public static void addDirtTag(){
        BlockGeneratorReference.ROCK_TYPES.forEach(helper ->{
            helper.getEntries().forEach(entry -> {
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();

                if (form == Forms.REGOLITH || form == Forms.GRASSY_REGOLITH){
                    DirtHelper.registerSoil(block, DirtHelper.DIRT_LIKE, RootyBlockHelper.getRootyBlock(Blocks.DIRT));
                    DirtHelper.registerSoil(block, DirtHelper.GRAVEL_LIKE, RootyBlockHelper.getRootyBlock(Blocks.DIRT));
                    DirtHelper.registerSoil(block, DirtHelper.SAND_LIKE, RootyBlockHelper.getRootyBlock(Blocks.DIRT));
                }
            });
        });

    }
}

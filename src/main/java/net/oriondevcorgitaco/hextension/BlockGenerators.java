package net.oriondevcorgitaco.hextension;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class BlockGenerators {

    public static List<String> SEDIMENTARY_ROCKS = new ArrayList<>();


    public static void addToSedimentaryList() {
        SEDIMENTARY_ROCKS.add("limestone");
    }



    public static void generateStoneBlocks() {
        SEDIMENTARY_ROCKS.forEach(id -> {
            String slabID = id + "_slab";
            String stairID = id + "_stair";

            String cobbleID = id + "_cobble";
            String cobbleSlabID = cobbleID + "_slab";
            String cobbleStairID = cobbleID + "_stairs";

            String polishedID = "polished_" + id;
            String polishedSlabID = polishedID + "_slab";
            String polishedStairID = polishedID + "_stairs";

            //Base
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, id), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, slabID), new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, stairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Hextension.MOD_ID, id)).getDefaultState(), FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));

            //Cobble
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, cobbleID), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, cobbleSlabID), new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, cobbleStairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Hextension.MOD_ID, cobbleID)).getDefaultState(), FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));

            //Polished
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, polishedID), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, polishedSlabID), new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Hextension.MOD_ID, polishedStairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Hextension.MOD_ID, polishedID)).getDefaultState(), FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
        });
    }

    public static class StairsBlockAccess extends StairsBlock {
        public StairsBlockAccess(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }
}

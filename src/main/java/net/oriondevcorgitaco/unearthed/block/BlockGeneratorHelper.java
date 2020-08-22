package net.oriondevcorgitaco.unearthed.block;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.oriondevcorgitaco.unearthed.Unearthed;

import java.util.ArrayList;
import java.util.List;

public class BlockGeneratorHelper {
    public static final ItemGroup HEXTENSION_TAB = FabricItemGroupBuilder.build(new Identifier(Unearthed.MOD_ID, "hextension"), () -> new ItemStack(BlockGeneratorReference.LIMESTONE.getItem()));

    private final String id;
    private final boolean hasPolished;
    private final boolean hasCobble;
    private final boolean hasMossy;
    private final boolean hasBricks;
    private final boolean hasMossyBricks;

    public static List<String> blockIdList = new ArrayList<>();


    public BlockGeneratorHelper(String id, boolean hasPolishedVariant, boolean hasCobbleVariant, boolean hasMossyVariant, boolean hasBricksVariant, boolean hasMossyBricksVariant) {
        this.id = id;
        this.hasPolished = hasPolishedVariant;
        this.hasCobble = hasCobbleVariant;
        this.hasMossy = hasMossyVariant;
        this.hasBricks = hasBricksVariant;
        this.hasMossyBricks = hasMossyBricksVariant;
        BlockGeneratorReference.ROCK_TYPES.add(this);

        generateBaseVariant();
//        generatePolishedVariant(hasPolishedVariant);
//        generateCobbleVariant(hasCobbleVariant);

        blockIdList.add(id);
    }

    private void generateBaseVariant() {
        String slabID = id + "_slab";
        String stairID = id + "_stairs";
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, id), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, slabID), new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, stairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, id)).getDefaultState(), FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
        Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, id), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, id)), new Item.Settings().group(HEXTENSION_TAB)));
        Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, slabID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, slabID)), new Item.Settings().group(HEXTENSION_TAB)));
        Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, stairID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, stairID)), new Item.Settings().group(HEXTENSION_TAB)));

    }

    private void generatePolishedVariant(boolean generate) {
        if (generate) {
            String polishedID = "polished_" + this.id;
            String polishedSlabID = polishedID + "_slab";
            String polishedStairID = polishedID + "_stairs";
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, polishedID), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, polishedSlabID), new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, polishedStairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedID)).getDefaultState(), FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, polishedID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedID)), new Item.Settings().group(HEXTENSION_TAB)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, polishedSlabID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedSlabID)), new Item.Settings().group(HEXTENSION_TAB)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, polishedStairID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedStairID)), new Item.Settings().group(HEXTENSION_TAB)));

        }
    }

    private void generateCobbleVariant(boolean generate) {
        if (generate) {
            String cobbleID = id + "_cobble";
            String cobbleSlabID = cobbleID + "_slab";
            String cobbleStairID = cobbleID + "_stairs";
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, cobbleID), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, cobbleSlabID), new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, cobbleStairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, cobbleID)).getDefaultState(), FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, cobbleID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, cobbleID)), new Item.Settings().group(HEXTENSION_TAB)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, cobbleSlabID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, cobbleSlabID)), new Item.Settings().group(HEXTENSION_TAB)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, cobbleStairID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, cobbleStairID)), new Item.Settings().group(HEXTENSION_TAB)));
        }
    }

    public Block getBlock() {
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, this.id));
    }

    public Block getSlabBlock() {
        String slabID = id + "_slab";
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, slabID));
    }

    public Block getStairBlock() {
        String stairID = id + "_stair";
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, stairID));
    }

    public Block getPolishedBlock() {
        String polishedID = "polished_" + this.id;
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedID));
    }

    public Block getPolishedSlabBlock() {
        String polishedID = "polished_" + this.id;
        String polishedSlabID = polishedID + "_slab";
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedSlabID));
    }

    public Block getPolishedStairBlock() {
        String polishedID = "polished_" + id;
        String polishedStairID = polishedID + "_stairs";
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedStairID));
    }

    public Block getCobbleBlock() {
        String cobbleID = id + "_cobble";
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, cobbleID));
    }

    public Block getCobbleSlabBlock() {
        String cobbleID = id + "_cobble";
        String cobbleSlabID = cobbleID + "_slab";
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, cobbleSlabID));
    }

    public Block getCobbleStairBlock() {
        String cobbleID = id + "_cobble";
        String cobbleStairID = cobbleID + "_stairs";
        return Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, cobbleStairID));
    }

    public Item getItem() {
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, this.id));
    }

    public Item getSlabItem() {
        String slabID = id + "_slab";
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, slabID));
    }

    public Item getStairItem() {
        String stairID = id + "_stair";
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, stairID));
    }

    public Item getPolishedItem() {
        String polishedID = "polished_" + this.id;
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, polishedID));
    }

    public Item getPolishedSlabItem() {
        String polishedID = "polished_" + this.id;
        String polishedSlabID = polishedID + "_slab";
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, polishedSlabID));
    }

    public Item getPolishedStairItem() {
        String polishedID = "polished_" + id;
        String polishedStairID = polishedID + "_stairs";
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, polishedStairID));
    }

    public Item getCobbleItem() {
        String cobbleID = id + "_cobble";
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, cobbleID));
    }

    public Item getCobbleSlabItem() {
        String cobbleID = id + "_cobble";
        String cobbleSlabID = cobbleID + "_slab";
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, cobbleSlabID));
    }

    public Item getCobbleStairItem() {
        String cobbleID = id + "_cobble";
        String cobbleStairID = cobbleID + "_stairs";
        return Registry.ITEM.get(new Identifier(Unearthed.MOD_ID, cobbleStairID));
    }

    public static class StairsBlockAccess extends StairsBlock {
        public StairsBlockAccess(BlockState baseBlockState, AbstractBlock.Settings settings) {
            super(baseBlockState, settings);
        }
    }
}

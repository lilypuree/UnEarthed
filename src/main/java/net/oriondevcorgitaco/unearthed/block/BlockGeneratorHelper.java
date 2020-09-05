package net.oriondevcorgitaco.unearthed.block;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.util.BlockAssetHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockGeneratorHelper {
    public static final ItemGroup UNEARTHED_TAB = FabricItemGroupBuilder.build(new Identifier(Unearthed.MOD_ID, "hextension"), () -> new ItemStack(BlockGeneratorReference.LIMESTONE.getItem()));

    private final String id;
    private final boolean hasPolished;
    private final boolean hasCobble;
    private final boolean hasMossy;
    private final boolean hasBricks;
    private final boolean hasMossyBricks;

    public static List<String> baseBlockIdList = new ArrayList<>();
    public static List<String> cobbleBlockIdList = new ArrayList<>();
    public static List<String> oreBlockIdList = new ArrayList<>();
    public static List<Block> baseStoneBlockArray = new ArrayList<>();
    public static List<Block> cobbleStoneBlockArray = new ArrayList<>();
    public static List<Block> oreBlockArray = new ArrayList<>();

    public static List<String> getCobbleBlockIdList = new ArrayList<>();


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
        generateCobbleVariant(hasCobbleVariant);
        generateVanillaOreVariants();

//        Mod Support
        if (FabricLoader.getInstance().isModLoaded("byg"))
            generateBYGOreVariants();

        baseBlockIdList.add(id);
    }

    private void generateBaseVariant() {
        String slabID = id + "_slab";
        String stairID = id + "_stairs";
        String buttonID = id + "_button";
        String plateID = id + "_pressure_plate";
        String wallID = id + "_wall";

        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, id), new Block(FabricBlockSettings.copyOf(Blocks.STONE)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, slabID), new SlabBlock(FabricBlockSettings.copyOf(Blocks.STONE_SLAB)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, stairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, id)).getDefaultState(), FabricBlockSettings.copyOf(Blocks.STONE_STAIRS)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, buttonID), new ButtonBlockAccess(FabricBlockSettings.copyOf(Blocks.STONE_BUTTON)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, plateID), new PressurePlateBlockAccess(FabricBlockSettings.copyOf(Blocks.STONE_PRESSURE_PLATE)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, wallID), new WallBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICK_WALL)));
        Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, id), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, id)), new Item.Settings().group(UNEARTHED_TAB)));

        for (String type : BlockAssetHelper.BASE_TYPES) {
            String modifiedID = id + type;
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, modifiedID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, modifiedID)), new Item.Settings().group(UNEARTHED_TAB)));
        }

        baseStoneBlockArray.add(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, id)));
    }

    private void generatePolishedVariant(boolean generate) {
        if (generate) {
            String polishedID = "polished_" + this.id;
            String polishedSlabID = polishedID + "_slab";
            String polishedStairID = polishedID + "_stairs";
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, polishedID), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, polishedSlabID), new SlabBlock(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, polishedStairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedID)).getDefaultState(), FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, polishedID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedID)), new Item.Settings().group(UNEARTHED_TAB)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, polishedSlabID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedSlabID)), new Item.Settings().group(UNEARTHED_TAB)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, polishedStairID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, polishedStairID)), new Item.Settings().group(UNEARTHED_TAB)));

        }
    }

    private void generateVanillaOreVariants() {
        String coalOreID = this.id + "_coal_ore";
        String ironOreID = this.id + "_iron_ore";
        String goldOreID = this.id + "_gold_ore";
        String lapisOreID = this.id + "_lapis_ore";
        String redStoneOreID = this.id + "_redstone_ore";
        String diamondOreID = this.id + "_diamond_ore";
        String emeraldOreID = this.id + "_emerald_ore";
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, coalOreID), new UEOreBlock(FabricBlockSettings.copyOf(Blocks.COAL_ORE).breakByTool(FabricToolTags.PICKAXES)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, ironOreID), new UEOreBlock(FabricBlockSettings.copyOf(Blocks.IRON_ORE).breakByTool(FabricToolTags.PICKAXES)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, goldOreID), new UEOreBlock(FabricBlockSettings.copyOf(Blocks.GOLD_ORE).breakByTool(FabricToolTags.PICKAXES)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, lapisOreID), new UEOreBlock(FabricBlockSettings.copyOf(Blocks.LAPIS_ORE).breakByTool(FabricToolTags.PICKAXES)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, redStoneOreID), new RedstoneOreBlock(FabricBlockSettings.copyOf(Blocks.REDSTONE_ORE).breakByTool(FabricToolTags.PICKAXES)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, diamondOreID), new UEOreBlock(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE).breakByTool(FabricToolTags.PICKAXES)));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, emeraldOreID), new UEOreBlock(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE).breakByTool(FabricToolTags.PICKAXES)));
        for (String type : BlockAssetHelper.VANILLA_ORE_TYPES) {
            String modifiedID = this.id + type;
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, modifiedID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, modifiedID)), new Item.Settings().group(UNEARTHED_TAB)));
            oreBlockArray.add(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, modifiedID)));
            oreBlockIdList.add(modifiedID);
        }
    }

    private void generateBYGOreVariants() {
        String bygID = "byg";
        String ametrineOreID = this.id + "_ametrine_ore";
        String pendoriteOreID = this.id + "_pendorite_ore";
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, ametrineOreID), new UEOreBlock(FabricBlockSettings.copyOf(Registry.BLOCK.get(new Identifier(bygID, "ametrine_ore")))));
        Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, pendoriteOreID), new UEOreBlock(FabricBlockSettings.copyOf(Registry.BLOCK.get(new Identifier(bygID, "pendorite_ore")))));
        for (String type : BlockAssetHelper.BYG_ORE_TYPES) {
            String modifiedID = this.id + type;
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, modifiedID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, modifiedID)), new Item.Settings().group(UNEARTHED_TAB)));
//            oreBlockArray.add(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, modifiedID)));
//            oreBlockIdList.add(modifiedID);
        }
    }



    private void generateCobbleVariant(boolean generate) {
        if (generate) {
            String cobbleID = id + "_cobble";
            String slabID = cobbleID + "_slab";
            String stairID = cobbleID + "_stairs";
            String buttonID = cobbleID + "_button";
            String plateID = cobbleID + "_pressure_plate";
            String wallID = cobbleID + "_wall";
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, cobbleID), new Block(FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, slabID), new SlabBlock(FabricBlockSettings.copyOf(Blocks.STONE_SLAB)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, stairID), new StairsBlockAccess(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, id)).getDefaultState(), FabricBlockSettings.copyOf(Blocks.STONE_STAIRS)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, buttonID), new ButtonBlockAccess(FabricBlockSettings.copyOf(Blocks.STONE_BUTTON)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, plateID), new PressurePlateBlockAccess(FabricBlockSettings.copyOf(Blocks.STONE_PRESSURE_PLATE)));
            Registry.register(Registry.BLOCK, new Identifier(Unearthed.MOD_ID, wallID), new WallBlock(FabricBlockSettings.copyOf(Blocks.STONE_BRICK_WALL)));
            Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, cobbleID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, cobbleID)), new Item.Settings().group(UNEARTHED_TAB)));
            cobbleBlockIdList.add(cobbleID);

            for (String type : BlockAssetHelper.BASE_TYPES) {
                String modifiedID = cobbleID + type;
                cobbleBlockIdList.add(modifiedID);
                Registry.register(Registry.ITEM, new Identifier(Unearthed.MOD_ID, modifiedID), new BlockItem(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, modifiedID)), new Item.Settings().group(UNEARTHED_TAB)));
            }
            cobbleStoneBlockArray.add(Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, id)));
            cobbleBlockIdList.add(cobbleID);
        }
    }

    public Block getBlock() {
        Block block = Registry.BLOCK.get(new Identifier(Unearthed.MOD_ID, this.id));
        if (block == null) {
            Unearthed.LOGGER.error("Block does not exist");
            block = Blocks.STONE;
        }
        return block;
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

    public static class PressurePlateBlockAccess extends PressurePlateBlock {
        public PressurePlateBlockAccess(AbstractBlock.Settings settings) {
            super(ActivationRule.MOBS, settings);
        }
    }

    public static class ButtonBlockAccess extends StoneButtonBlock {
        public ButtonBlockAccess(AbstractBlock.Settings settings) {
            super(settings);
        }
    }

    public static class UEOreBlock extends OreBlock {
        public UEOreBlock(AbstractBlock.Settings settings) {
            super(settings);
        }


        @Override
        protected int getExperienceWhenMined(Random random) {
            if (this.isIn(BlockGeneratorReference.COAL_ORE_TAG)) {
                return MathHelper.nextInt(random, 0, 2);
            } else if (this.isIn(BlockGeneratorReference.DIAMOND_ORE_TAG)) {
                return MathHelper.nextInt(random, 3, 7);
            } else if (this.isIn(BlockGeneratorReference.EMERALD_ORE_TAG)) {
                return MathHelper.nextInt(random, 3, 7);
            } else if (this.isIn(BlockGeneratorReference.LAPIS_ORE_TAG)) {
                return MathHelper.nextInt(random, 2, 5);
            }
            else
                return 0;
        }
    }
}

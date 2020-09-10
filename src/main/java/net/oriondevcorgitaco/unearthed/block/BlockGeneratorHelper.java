package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.util.BlockDataHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockGeneratorHelper {
    public static ItemGroup UNEARTHED_TAB = new ItemGroup(Unearthed.MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, "kimberlite_diamond_ore")));
        }

        @Override
        public boolean hasSearchBar() {
            return true;
        }

        @Override
        public boolean hasScrollbar() {
            return true;
        }
    };
    
    
    private final String id;
    private final boolean hasPolished;
    private final boolean hasCobble;
    private final boolean hasMossy;
    private final boolean hasBricks;
    private final boolean hasMossyBricks;

    public static List<String> baseBlockIdList = new ArrayList<>();
    public static List<String> cobbleBlockIdList = new ArrayList<>();
    public static List<String> oreBlockIdList = new ArrayList<>();

    public static List<String> wallIDList = new ArrayList<>();
    public static List<String> slabList = new ArrayList<>();
    public static List<String> buttonList = new ArrayList<>();
    public static List<String> stairsList = new ArrayList<>();


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
//        if (FabricLoader.getInstance().isModLoaded("byg"))
//            generateBYGOreVariants();

        baseBlockIdList.add(id);
    }

    private void generateBaseVariant() {
        String slabID = id + "_slab";
        String stairID = id + "_stairs";
        String buttonID = id + "_button";
        String plateID = id + "_pressure_plate";
        String wallID = id + "_wall";

        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, id), new Block(AbstractBlock.Properties.from(Blocks.STONE).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, slabID), new SlabBlock(AbstractBlock.Properties.from(Blocks.STONE_SLAB).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, stairID), new StairsBlockAccess(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, id)).getDefaultState(), AbstractBlock.Properties.from(Blocks.STONE_STAIRS).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, buttonID), new ButtonBlockAccess(AbstractBlock.Properties.from(Blocks.STONE_BUTTON).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, plateID), new PressurePlateBlockAccess(AbstractBlock.Properties.from(Blocks.STONE_PRESSURE_PLATE).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, wallID), new WallBlock(AbstractBlock.Properties.from(Blocks.STONE_BRICK_WALL).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, id), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, id)), new Item.Properties().group(UNEARTHED_TAB)));

        slabList.add("\"unearthed:" + slabID + "\",");
        wallIDList.add("\"unearthed:" + wallID + "\",");
        buttonList.add("\"unearthed:" + buttonID + "\",");
        stairsList.add("\"unearthed:" + stairID + "\",");


        for (String type : BlockDataHelper.BASE_TYPES) {
            String modifiedID = id + type;
            Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, modifiedID), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, modifiedID)), new Item.Properties().group(UNEARTHED_TAB)));
        }

        baseStoneBlockArray.add(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, id)));
    }

    private void generatePolishedVariant(boolean generate) {
        if (generate) {
            String polishedID = "polished_" + this.id;
            String polishedSlabID = polishedID + "_slab";
            String polishedStairID = polishedID + "_stairs";
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, polishedID), new Block(AbstractBlock.Properties.from(Blocks.POLISHED_ANDESITE)));
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, polishedSlabID), new SlabBlock(AbstractBlock.Properties.from(Blocks.POLISHED_ANDESITE_STAIRS)));
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, polishedStairID), new StairsBlockAccess(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedID)).getDefaultState(), AbstractBlock.Properties.from(Blocks.COBBLESTONE)));
            Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, polishedID), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedID)), new Item.Properties().group(UNEARTHED_TAB)));
            Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, polishedSlabID), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedSlabID)), new Item.Properties().group(UNEARTHED_TAB)));
            Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, polishedStairID), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedStairID)), new Item.Properties().group(UNEARTHED_TAB)));

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
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, coalOreID), new UEOreBlock(AbstractBlock.Properties.from(Blocks.COAL_ORE).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, ironOreID), new UEOreBlock(AbstractBlock.Properties.from(Blocks.IRON_ORE).harvestLevel(1).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, goldOreID), new UEOreBlock(AbstractBlock.Properties.from(Blocks.GOLD_ORE).harvestLevel(2).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, lapisOreID), new UEOreBlock(AbstractBlock.Properties.from(Blocks.LAPIS_ORE).harvestLevel(2).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, redStoneOreID), new RedstoneOreBlock(AbstractBlock.Properties.from(Blocks.REDSTONE_ORE).harvestLevel(2).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, diamondOreID), new UEOreBlock(AbstractBlock.Properties.from(Blocks.DIAMOND_ORE).harvestLevel(2).harvestTool(ToolType.PICKAXE)));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, emeraldOreID), new UEOreBlock(AbstractBlock.Properties.from(Blocks.EMERALD_ORE).harvestLevel(2).harvestTool(ToolType.PICKAXE)));
        for (String type : BlockDataHelper.VANILLA_ORE_TYPES) {
            String modifiedID = this.id + type;
            Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, modifiedID), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, modifiedID)), new Item.Properties().group(UNEARTHED_TAB)));
            oreBlockArray.add(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, modifiedID)));
            oreBlockIdList.add(modifiedID);
        }
    }

    private void generateBYGOreVariants() {
        String bygID = "byg";
        String ametrineOreID = this.id + "_ametrine_ore";
        String pendoriteOreID = this.id + "_pendorite_ore";
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, ametrineOreID), new UEOreBlock(AbstractBlock.Properties.from(Registry.BLOCK.getOrDefault(new ResourceLocation(bygID, "ametrine_ore")))));
        Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, pendoriteOreID), new UEOreBlock(AbstractBlock.Properties.from(Registry.BLOCK.getOrDefault(new ResourceLocation(bygID, "pendorite_ore")))));
        for (String type : BlockDataHelper.BYG_ORE_TYPES) {
            String modifiedID = this.id + type;
            Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, modifiedID), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, modifiedID)), new Item.Properties().group(UNEARTHED_TAB)));
//            oreBlockArray.add(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, modifiedID)));
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
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, cobbleID), new Block(AbstractBlock.Properties.from(Blocks.COBBLESTONE).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, slabID), new SlabBlock(AbstractBlock.Properties.from(Blocks.STONE_SLAB).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, stairID), new StairsBlockAccess(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, id)).getDefaultState(), AbstractBlock.Properties.from(Blocks.STONE_STAIRS).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, buttonID), new ButtonBlockAccess(AbstractBlock.Properties.from(Blocks.STONE_BUTTON).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, plateID), new PressurePlateBlockAccess(AbstractBlock.Properties.from(Blocks.STONE_PRESSURE_PLATE).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
            Registry.register(Registry.BLOCK, new ResourceLocation(Unearthed.MOD_ID, wallID), new WallBlock(AbstractBlock.Properties.from(Blocks.STONE_BRICK_WALL).harvestLevel(0).harvestTool(ToolType.PICKAXE)));
            Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, cobbleID), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, cobbleID)), new Item.Properties().group(UNEARTHED_TAB)));
            cobbleBlockIdList.add(cobbleID);
            slabList.add("\"unearthed:" + slabID + "\",");
            wallIDList.add("\"unearthed:" + wallID + "\",");
            buttonList.add("\"unearthed:" + buttonID + "\",");
            stairsList.add("\"unearthed:" + stairID + "\",");
            for (String type : BlockDataHelper.BASE_TYPES) {
                String modifiedID = cobbleID + type;
                Registry.register(Registry.ITEM, new ResourceLocation(Unearthed.MOD_ID, modifiedID), new BlockItem(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, modifiedID)), new Item.Properties().group(UNEARTHED_TAB)));
            }
            cobbleStoneBlockArray.add(Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, id)));
            cobbleBlockIdList.add(cobbleID);
        }
    }

    public Block getBlock() {
        Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, this.id));
        if (block == null) {
            Unearthed.LOGGER.error("Block does not exist");
            block = Blocks.STONE;
        }
        return block;
    }

    public Block getSlabBlock() {
        String slabID = id + "_slab";
        return Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, slabID));
    }

    public Block getStairBlock() {
        String stairID = id + "_stair";
        return Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, stairID));
    }

    public Block getPolishedBlock() {
        String polishedID = "polished_" + this.id;
        return Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedID));
    }

    public Block getPolishedSlabBlock() {
        String polishedID = "polished_" + this.id;
        String polishedSlabID = polishedID + "_slab";
        return Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedSlabID));
    }

    public Block getPolishedStairBlock() {
        String polishedID = "polished_" + id;
        String polishedStairID = polishedID + "_stairs";
        return Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedStairID));
    }

    public Block getCobbleBlock() {
        String cobbleID = id + "_cobble";
        return Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, cobbleID));
    }

    public Block getCobbleSlabBlock() {
        String cobbleID = id + "_cobble";
        String cobbleSlabID = cobbleID + "_slab";
        return Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, cobbleSlabID));
    }

    public Block getCobbleStairBlock() {
        String cobbleID = id + "_cobble";
        String cobbleStairID = cobbleID + "_stairs";
        return Registry.BLOCK.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, cobbleStairID));
    }

    public Item getItem() {
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, this.id));
    }

    public Item getSlabItem() {
        String slabID = id + "_slab";
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, slabID));
    }

    public Item getStairItem() {
        String stairID = id + "_stair";
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, stairID));
    }

    public Item getPolishedItem() {
        String polishedID = "polished_" + this.id;
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedID));
    }

    public Item getPolishedSlabItem() {
        String polishedID = "polished_" + this.id;
        String polishedSlabID = polishedID + "_slab";
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedSlabID));
    }

    public Item getPolishedStairItem() {
        String polishedID = "polished_" + id;
        String polishedStairID = polishedID + "_stairs";
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, polishedStairID));
    }

    public Item getCobbleItem() {
        String cobbleID = id + "_cobble";
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, cobbleID));
    }

    public Item getCobbleSlabItem() {
        String cobbleID = id + "_cobble";
        String cobbleSlabID = cobbleID + "_slab";
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, cobbleSlabID));
    }

    public Item getCobbleStairItem() {
        String cobbleID = id + "_cobble";
        String cobbleStairID = cobbleID + "_stairs";
        return Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, cobbleStairID));
    }

    public static class StairsBlockAccess extends StairsBlock {
        public StairsBlockAccess(BlockState baseBlockState, AbstractBlock.Properties Properties) {
            super(baseBlockState, Properties);
        }
    }

    public static class PressurePlateBlockAccess extends PressurePlateBlock {
        public PressurePlateBlockAccess(AbstractBlock.Properties Properties) {
            super(Sensitivity.MOBS, Properties);
        }
    }

    public static class ButtonBlockAccess extends StoneButtonBlock {
        public ButtonBlockAccess(AbstractBlock.Properties Properties) {
            super(Properties);
        }
    }

    public static class UEOreBlock extends OreBlock {
        public UEOreBlock(AbstractBlock.Properties Properties) {
            super(Properties);
        }


        @Override
        protected int getExperience(Random random) {
            if (this.isIn(Tags.Blocks.ORES_COAL)) {
                return MathHelper.nextInt(random, 0, 2);
            } else if (this.isIn(Tags.Blocks.ORES_DIAMOND)) {
                return MathHelper.nextInt(random, 3, 7);
            } else if (this.isIn(Tags.Blocks.ORES_EMERALD)) {
                return MathHelper.nextInt(random, 3, 7);
            } else if (this.isIn(Tags.Blocks.ORES_LAPIS)) {
                return MathHelper.nextInt(random, 2, 5);
            }
            else
                return 0;
        }
    }

    public static class UECreativeTab {
        public static ItemGroup ueCreativeTab = new ItemGroup(Unearthed.MOD_ID) {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(Registry.ITEM.getOrDefault(new ResourceLocation(Unearthed.MOD_ID, "kimberlite_diamond_ore")));
            }

            @Override
            public boolean hasSearchBar() {
                return true;
            }

            @Override
            public boolean hasScrollbar() {
                return true;
            }
        };

        public static void init() {
            Unearthed.LOGGER.debug("Unearthed: Item Group Created!");
        }
    }

}

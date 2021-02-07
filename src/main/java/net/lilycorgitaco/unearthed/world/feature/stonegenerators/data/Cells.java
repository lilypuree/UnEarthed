package net.lilycorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.lilycorgitaco.unearthed.block.BlockGeneratorHelper;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.block.schema.BlockSchema;
import net.lilycorgitaco.unearthed.block.schema.Forms;
import net.lilycorgitaco.unearthed.block.schema.Variants;
import net.lilycorgitaco.unearthed.datagen.type.IOreType;

import java.util.HashMap;
import java.util.Map;

public class Cells {
    public static final Cell GLASS = new Cell(Blocks.GLASS.getDefaultState(), true, true);
    public static final Cell EMERALD = new Cell(Blocks.EMERALD_BLOCK.getDefaultState(), true, true);
    public static final Cell DIAMOND = new Cell(Blocks.DIAMOND_BLOCK.getDefaultState(), true, true);
    public static final Cell GOLD = new Cell(Blocks.GOLD_BLOCK.getDefaultState(), true, true);
    public static final Cell IRON = new Cell(Blocks.IRON_BLOCK.getDefaultState(), true, true);
    public static final Cell NETHERITE = new Cell(Blocks.NETHERITE_BLOCK.getDefaultState(), true, true);
    public static final Cell LAPIS = new Cell(Blocks.LAPIS_BLOCK.getDefaultState(), true, true);
    public static final Cell REDSTONE = new Cell(Blocks.REDSTONE_BLOCK.getDefaultState(), true, true);
    public static final Cell COAL = new Cell(Blocks.COAL_BLOCK.getDefaultState(), true, true);

    public static final Cell STONE = fromBlockHelper(BlockGeneratorReference.STONE);
    public static final Cell PHYLLITE = fromBlockHelper(BlockGeneratorReference.PHYLLITE);
    public static final Cell SLATE = fromBlockHelper(BlockGeneratorReference.SLATE);
    public static final Cell GABBRO = fromBlockHelper(BlockGeneratorReference.GABBRO);
    public static final Cell GRANODIORITE = fromBlockHelper(BlockGeneratorReference.GRANODIORITE);
    public static final Cell WHITE_GRANITE = fromBlockHelper(BlockGeneratorReference.WHITE_GRANITE);
    public static final Cell RHYOLITE = fromBlockHelper(BlockGeneratorReference.RHYOLITE);
    public static final Cell LIMESTONE = fromBlockHelper(BlockGeneratorReference.LIMESTONE);
    public static final Cell BEIGE_LIMESTONE = fromBlockHelper(BlockGeneratorReference.BEIGE_LIMESTONE);
    public static final Cell GREY_LIMESTONE = fromBlockHelper(BlockGeneratorReference.GREY_LIMESTONE);
    public static final Cell DOLOMITE = fromBlockHelper(BlockGeneratorReference.DOLOMITE);
    public static final Cell QUARTZITE = fromBlockHelper(BlockGeneratorReference.QUARTZITE);
    public static final Cell SILTSTONE = fromBlockHelper(BlockGeneratorReference.SILTSTONE);
    public static final Cell MUDSTONE = fromBlockHelper(BlockGeneratorReference.MUDSTONE);
    public static final Cell CONGLOMERATE = fromBlockHelper(BlockGeneratorReference.CONGLOMERATE);
    public static final Cell GRANITE = fromBlockHelper(BlockGeneratorReference.GRANITE);
    public static final Cell ANDESITE = fromBlockHelper(BlockGeneratorReference.ANDESITE);
    public static final Cell DIORITE = fromBlockHelper(BlockGeneratorReference.DIORITE);
    public static final Cell SANDSTONE = fromBlockHelper(BlockGeneratorReference.SANDSTONE);

    public static final Cell SMOOTH_SANDSTONE = new Cell(BlockStates.SMOOTH_SANDSTONE, BlockStates.SANDSTONE, BlockStates.SANDSTONE_REGOLITH, BlockStates.SANDSTONE_GRASSY_REGOLITH, SANDSTONE.getOreMap());
    public static final Cell WEATHERED_GRANITE = new Cell(BlockStates.WHITE_GRANITE, BlockStates.COBBLESTONE, BlockStates.WEATHERED_RHYOLITE, BlockStates.WEATHERED_RHYOLITE, WHITE_GRANITE.getOreMap());
    public static final Cell WEATHERED_RHYOLITE = new Cell(BlockStates.RHYOLITE, BlockStates.COBBLESTONE, BlockStates.WEATHERED_RHYOLITE, BlockStates.WEATHERED_RHYOLITE, RHYOLITE.getOreMap());
    public static final Cell TERRACOTTA = new Cell(BlockStates.TERRACOTTA, BlockStates.MUDSTONE, BlockStates.MUDSTONE_REGOLITH, BlockStates.MUDSTONE_GRASSY_REGOLITH, MUDSTONE.getOreMap());
    public static final Cell DACITE = new Cell(BlockStates.DACITE, true, true);
    public static final Cell MARBLE = new Cell(BlockStates.MARBLE, BlockStates.DOLOMITE_REGOLITH, BlockStates.DOLOMITE_GRASSY_REGOLITH, true);
    public static final Cell LIGNITE = new Cell(BlockStates.LIGNITE, BlockStates.STONE_REGOLITH, BlockStates.STONE_GRASSY_REGOLITH, true);
    public static final Cell DOLERITE = new Cell(BlockStates.DOLERITE, true, true);
    public static final Cell PUMICE = new Cell(BlockStates.PUMICE, true, true);
    public static final Cell PILLOW_BASALT = new Cell(BlockStates.PILLOW_BASALT, true, true);
    public static final Cell SCHIST_X = new Cell(BlockStates.SCHIST_X, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH, true);
    public static final Cell SCHIST_Y = new Cell(BlockStates.SCHIST_Y, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH, true);
    public static final Cell SCHIST_Z = new Cell(BlockStates.SCHIST_Z, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH, true);


    private static Cell fromBlockHelper(BlockGeneratorHelper helper) {
        Identifier id = helper.getBaseBlock().getRegistryName();
        BlockState baseBlock = helper.getBaseBlock().getDefaultState();
        BlockGeneratorHelper.Entry entry = helper.getEntry(Variants.COBBLED, Forms.BLOCK);
        BlockState cobbleBlock = entry != null ? entry.getBlock().getDefaultState() : Blocks.COBBLESTONE.getDefaultState();
        BlockState dirtReplacement = Blocks.DIRT.getDefaultState();
        BlockState grassReplacement = Blocks.GRASS_BLOCK.getDefaultState();
        Map<IOreType, BlockState> oreMap = new HashMap<>();
        for (BlockGeneratorHelper.Entry e : helper.getEntries()) {
            BlockSchema.Form form = e.getForm();
            if (form == Forms.OVERGROWN_ROCK) {
                dirtReplacement = baseBlock;
                grassReplacement = e.getBlock().getDefaultState();
            } else if (form == Forms.REGOLITH) {
                dirtReplacement = e.getBlock().getDefaultState();
            } else if (form == Forms.GRASSY_REGOLITH) {
                grassReplacement = e.getBlock().getDefaultState();
            } else if (form instanceof Forms.OreForm) {
                oreMap.put(((Forms.OreForm) form).getOreType(), e.getBlock().getDefaultState());
            }
        }
        return new Cell(baseBlock, cobbleBlock, dirtReplacement, grassReplacement, oreMap);
    }


}

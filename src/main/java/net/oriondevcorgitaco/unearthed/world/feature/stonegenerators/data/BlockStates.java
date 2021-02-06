package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;

public class BlockStates {
    protected static final BlockState GABBRO = getBaseBlock(BlockGeneratorReference.GABBRO);
    protected static final BlockState GABBRO_REGOLITH = getRegolithBlock(BlockGeneratorReference.GABBRO);
    protected static final BlockState GABBRO_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.GABBRO);
    protected static final BlockState GRANODIORITE = getBaseBlock(BlockGeneratorReference.GRANODIORITE);
    protected static final BlockState GRANODIORITE_REGOLITH = getRegolithBlock(BlockGeneratorReference.GRANODIORITE);
    protected static final BlockState GRANODIORITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.GRANODIORITE);
    protected static final BlockState WHITE_GRANITE = getBaseBlock(BlockGeneratorReference.WHITE_GRANITE);
    protected static final BlockState WHITE_GRANITE_REGOLITH = getRegolithBlock(BlockGeneratorReference.WHITE_GRANITE);
    protected static final BlockState WHITE_GRANITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.WHITE_GRANITE);
    protected static final BlockState RHYOLITE = getBaseBlock(BlockGeneratorReference.RHYOLITE);
    protected static final BlockState RHYOLITE_REGOLITH = getRegolithBlock(BlockGeneratorReference.RHYOLITE);
    protected static final BlockState RHYOLITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.RHYOLITE);
    protected static final BlockState WEATHERED_RHYOLITE = getBaseBlock(BlockGeneratorReference.WEATHERED_RHYOLITE);
    protected static final BlockState DACITE = getBaseBlock(BlockGeneratorReference.DACITE);
    protected static final BlockState PUMICE = getBaseBlock(BlockGeneratorReference.PUMICE);
    protected static final BlockState DOLERITE = getBaseBlock(BlockGeneratorReference.DOLERITE);
    protected static final BlockState PILLOW_BASALT = getBaseBlock(BlockGeneratorReference.PILLOW_BASALT);
    protected static final BlockState PHYLLITE = getBaseBlock(BlockGeneratorReference.PHYLLITE);
    protected static final BlockState PHYLLITE_REGOLITH = getRegolithBlock(BlockGeneratorReference.PHYLLITE);
    protected static final BlockState PHYLLITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.PHYLLITE);
    protected static final BlockState SLATE = getBaseBlock(BlockGeneratorReference.SLATE);
    protected static final BlockState SLATE_REGOLITH = getRegolithBlock(BlockGeneratorReference.SLATE);
    protected static final BlockState SLATE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.SLATE);
    protected static final BlockState QUARTZITE = getBaseBlock(BlockGeneratorReference.QUARTZITE);
    protected static final BlockState QUARTZITE_REGOLITH = getRegolithBlock(BlockGeneratorReference.QUARTZITE);
    protected static final BlockState QUARTZITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.QUARTZITE);
    protected static final BlockState DOLOMITE = getBaseBlock(BlockGeneratorReference.DOLOMITE);
    protected static final BlockState DOLOMITE_REGOLITH = getRegolithBlock(BlockGeneratorReference.DOLOMITE);
    protected static final BlockState DOLOMITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.DOLOMITE);
    protected static final BlockState SCHIST_X = getBaseBlock(BlockGeneratorReference.SCHIST).with(BlockStateProperties.AXIS, Direction.Axis.X);
    protected static final BlockState SCHIST_Y = getBaseBlock(BlockGeneratorReference.SCHIST).with(BlockStateProperties.AXIS, Direction.Axis.Y);
    protected static final BlockState SCHIST_Z = getBaseBlock(BlockGeneratorReference.SCHIST).with(BlockStateProperties.AXIS, Direction.Axis.Z);
    protected static final BlockState MARBLE = getBaseBlock(BlockGeneratorReference.MARBLE);
    protected static final BlockState BEIGE_LIMESTONE = getBaseBlock(BlockGeneratorReference.BEIGE_LIMESTONE);
    protected static final BlockState BEIGE_LIMESTONE_REGOLITH = getRegolithBlock(BlockGeneratorReference.BEIGE_LIMESTONE);
    protected static final BlockState BEIGE_LIMESTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.BEIGE_LIMESTONE);
    protected static final BlockState LIMESTONE = getBaseBlock(BlockGeneratorReference.LIMESTONE);
    protected static final BlockState LIMESTONE_REGOLITH = getRegolithBlock(BlockGeneratorReference.LIMESTONE);
    protected static final BlockState LIMESTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.LIMESTONE);
    protected static final BlockState GREY_LIMESTONE = getBaseBlock(BlockGeneratorReference.GREY_LIMESTONE);
    protected static final BlockState GREY_LIMESTONE_REGOLITH = getRegolithBlock(BlockGeneratorReference.GREY_LIMESTONE);
    protected static final BlockState GREY_LIMESTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.GREY_LIMESTONE);
    protected static final BlockState CONGLOMERATE = getBaseBlock(BlockGeneratorReference.CONGLOMERATE);
    protected static final BlockState CONGLOMERATE_REGOLITH = getRegolithBlock(BlockGeneratorReference.CONGLOMERATE);
    protected static final BlockState CONGLOMERATE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.CONGLOMERATE);
    protected static final BlockState SILTSTONE = getBaseBlock(BlockGeneratorReference.SILTSTONE);
    protected static final BlockState SILTSTONE_REGOLITH = getRegolithBlock(BlockGeneratorReference.SILTSTONE);
    protected static final BlockState SILTSTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.SILTSTONE);
    protected static final BlockState MUDSTONE = getBaseBlock(BlockGeneratorReference.MUDSTONE);
    protected static final BlockState MUDSTONE_REGOLITH = getRegolithBlock(BlockGeneratorReference.MUDSTONE);
    protected static final BlockState MUDSTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockGeneratorReference.MUDSTONE);
    protected static final BlockState LIGNITE = getBaseBlock(BlockGeneratorReference.LIGNITE);
    protected static final BlockState STONE = Blocks.STONE.getDefaultState();
    protected static final BlockState COBBLESTONE = Blocks.COBBLESTONE.getDefaultState();
    protected static final BlockState STONE_REGOLITH = BlockGeneratorReference.STONE.getEntry(Variants.REGOLITHS, Forms.REGOLITH).getBlock().getDefaultState();
    protected static final BlockState STONE_GRASSY_REGOLITH = BlockGeneratorReference.STONE.getEntry(Variants.REGOLITHS, Forms.GRASSY_REGOLITH).getBlock().getDefaultState();
    protected static final BlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
    protected static final BlockState SMOOTH_SANDSTONE = Blocks.SMOOTH_SANDSTONE.getDefaultState();
    protected static final BlockState SANDSTONE_REGOLITH = BlockGeneratorReference.SANDSTONE.getEntry(Variants.SANDSTONE, Forms.REGOLITH).getBlock().getDefaultState();
    protected static final BlockState SANDSTONE_GRASSY_REGOLITH = BlockGeneratorReference.SANDSTONE.getEntry(Variants.SANDSTONE, Forms.GRASSY_REGOLITH).getBlock().getDefaultState();
    protected static final BlockState TERRACOTTA = Blocks.TERRACOTTA.getDefaultState();


    private static BlockState getBaseBlock(BlockGeneratorHelper helper) {
        return helper.getBaseBlock().getDefaultState();
    }

    private static BlockState getRegolithBlock(BlockGeneratorHelper helper) {
        return helper.getEntry(helper.getBaseEntry().getVariant(), Forms.REGOLITH).getBlock().getDefaultState();
    }

    private static BlockState getGrassyRegolithBlock(BlockGeneratorHelper helper) {
        return helper.getEntry(helper.getBaseEntry().getVariant(), Forms.GRASSY_REGOLITH).getBlock().getDefaultState();
    }
}

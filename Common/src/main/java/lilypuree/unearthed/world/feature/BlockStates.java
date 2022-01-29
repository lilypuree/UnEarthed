package lilypuree.unearthed.world.feature;

import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.Forms;
import lilypuree.unearthed.block.schema.Variants;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BlockStates {
    protected static final BlockState GABBRO = getBaseBlock(BlockSchemas.GABBRO);
    protected static final BlockState GABBRO_REGOLITH = getRegolithBlock(BlockSchemas.GABBRO);
    protected static final BlockState GABBRO_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.GABBRO);
    protected static final BlockState GRANODIORITE = getBaseBlock(BlockSchemas.GRANODIORITE);
    protected static final BlockState GRANODIORITE_REGOLITH = getRegolithBlock(BlockSchemas.GRANODIORITE);
    protected static final BlockState GRANODIORITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.GRANODIORITE);
    protected static final BlockState WHITE_GRANITE = getBaseBlock(BlockSchemas.WHITE_GRANITE);
    protected static final BlockState WHITE_GRANITE_REGOLITH = getRegolithBlock(BlockSchemas.WHITE_GRANITE);
    protected static final BlockState WHITE_GRANITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.WHITE_GRANITE);
    protected static final BlockState RHYOLITE = getBaseBlock(BlockSchemas.RHYOLITE);
    protected static final BlockState RHYOLITE_REGOLITH = getRegolithBlock(BlockSchemas.RHYOLITE);
    protected static final BlockState RHYOLITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.RHYOLITE);
    protected static final BlockState WEATHERED_RHYOLITE = getBaseBlock(BlockSchemas.WEATHERED_RHYOLITE);
    protected static final BlockState DACITE = getBaseBlock(BlockSchemas.DACITE);
    protected static final BlockState PUMICE = getBaseBlock(BlockSchemas.PUMICE);
    protected static final BlockState DOLERITE = getBaseBlock(BlockSchemas.DOLERITE);
    protected static final BlockState PILLOW_BASALT = getBaseBlock(BlockSchemas.PILLOW_BASALT);
    protected static final BlockState PHYLLITE = getBaseBlock(BlockSchemas.PHYLLITE);
    protected static final BlockState PHYLLITE_REGOLITH = getRegolithBlock(BlockSchemas.PHYLLITE);
    protected static final BlockState PHYLLITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.PHYLLITE);
    protected static final BlockState SLATE = getBaseBlock(BlockSchemas.SLATE);
    protected static final BlockState SLATE_REGOLITH = getRegolithBlock(BlockSchemas.SLATE);
    protected static final BlockState SLATE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.SLATE);
    protected static final BlockState QUARTZITE = getBaseBlock(BlockSchemas.QUARTZITE);
//    protected static final BlockState QUARTZITE_REGOLITH = getRegolithBlock(BlockSchemas.QUARTZITE);
//    protected static final BlockState QUARTZITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.QUARTZITE);
//    protected static final BlockState DOLOMITE = getBaseBlock(BlockSchemas.DOLOMITE);
//    protected static final BlockState DOLOMITE_REGOLITH = getRegolithBlock(BlockSchemas.DOLOMITE);
//    protected static final BlockState DOLOMITE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.DOLOMITE);
    protected static final BlockState SCHIST_X = getBaseBlock(BlockSchemas.SCHIST).setValue(BlockStateProperties.AXIS, Direction.Axis.X);
    protected static final BlockState SCHIST_Y = getBaseBlock(BlockSchemas.SCHIST).setValue(BlockStateProperties.AXIS, Direction.Axis.Y);
    protected static final BlockState SCHIST_Z = getBaseBlock(BlockSchemas.SCHIST).setValue(BlockStateProperties.AXIS, Direction.Axis.Z);
//    protected static final BlockState MARBLE = getBaseBlock(BlockSchemas.MARBLE);
    protected static final BlockState BEIGE_LIMESTONE = getBaseBlock(BlockSchemas.BEIGE_LIMESTONE);
    protected static final BlockState BEIGE_LIMESTONE_REGOLITH = getRegolithBlock(BlockSchemas.BEIGE_LIMESTONE);
    protected static final BlockState BEIGE_LIMESTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.BEIGE_LIMESTONE);
    protected static final BlockState LIMESTONE = getBaseBlock(BlockSchemas.LIMESTONE);
    protected static final BlockState LIMESTONE_REGOLITH = getRegolithBlock(BlockSchemas.LIMESTONE);
    protected static final BlockState LIMESTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.LIMESTONE);
    protected static final BlockState GREY_LIMESTONE = getBaseBlock(BlockSchemas.GREY_LIMESTONE);
    protected static final BlockState GREY_LIMESTONE_REGOLITH = getRegolithBlock(BlockSchemas.GREY_LIMESTONE);
    protected static final BlockState GREY_LIMESTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.GREY_LIMESTONE);
    protected static final BlockState CONGLOMERATE = getBaseBlock(BlockSchemas.CONGLOMERATE);
    protected static final BlockState CONGLOMERATE_REGOLITH = getRegolithBlock(BlockSchemas.CONGLOMERATE);
    protected static final BlockState CONGLOMERATE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.CONGLOMERATE);
    protected static final BlockState SILTSTONE = getBaseBlock(BlockSchemas.SILTSTONE);
    protected static final BlockState SILTSTONE_REGOLITH = getRegolithBlock(BlockSchemas.SILTSTONE);
    protected static final BlockState SILTSTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.SILTSTONE);
    protected static final BlockState MUDSTONE = getBaseBlock(BlockSchemas.MUDSTONE);
    protected static final BlockState MUDSTONE_REGOLITH = getRegolithBlock(BlockSchemas.MUDSTONE);
    protected static final BlockState MUDSTONE_GRASSY_REGOLITH = getGrassyRegolithBlock(BlockSchemas.MUDSTONE);
    protected static final BlockState LIGNITE = getBaseBlock(BlockSchemas.LIGNITE);
    protected static final BlockState STONE = Blocks.STONE.defaultBlockState();
    protected static final BlockState COBBLESTONE = Blocks.COBBLESTONE.defaultBlockState();
//    protected static final BlockState STONE_REGOLITH = BlockSchemas.STONE.getEntry(Variants.REGOLITHS, Forms.REGOLITH).getBlock().defaultBlockState();
//    protected static final BlockState STONE_GRASSY_REGOLITH = BlockSchemas.STONE.getEntry(Variants.REGOLITHS, Forms.GRASSY_REGOLITH).getBlock().defaultBlockState();
    protected static final BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
    protected static final BlockState SMOOTH_SANDSTONE = Blocks.SMOOTH_SANDSTONE.defaultBlockState();
//    protected static final BlockState SANDSTONE_REGOLITH = BlockSchemas.SANDSTONE.getEntry(Variants.SANDSTONE, Forms.REGOLITH).getBlock().defaultBlockState();
//    protected static final BlockState SANDSTONE_GRASSY_REGOLITH = BlockSchemas.SANDSTONE.getEntry(Variants.SANDSTONE, Forms.GRASSY_REGOLITH).getBlock().defaultBlockState();
    protected static final BlockState TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();


    private static BlockState getBaseBlock(BlockSchema helper) {
        return helper.getBaseBlock().defaultBlockState();
    }

    private static BlockState getRegolithBlock(BlockSchema helper) {
        return helper.getEntry(helper.getBaseEntry().getVariant(), Forms.REGOLITH).getBlock().defaultBlockState();
    }

    private static BlockState getGrassyRegolithBlock(BlockSchema helper) {
        return helper.getEntry(helper.getBaseEntry().getVariant(), Forms.GRASSY_REGOLITH).getBlock().defaultBlockState();
    }

    public static void init(){

    }
}

package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.regions;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.StoneWrapper;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.*;

public class Regions {
    public static Region DEFAULT;
    public static Region LIMESTONE;
    public static Region SEDIMENTARY;

    static {
        State stone = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.STONE));
        State phyllite = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.PHYLLITE));
        State slate = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.SLATE));
        State gabbro = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.GABBRO));
        State granodiorite = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.GRANODIORITE));
        State limestone = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.LIMESTONE));
        State grey_limestone = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.GREY_LIMESTONE));
        State beige_limestone = new StoneState(Type.SECONDARY, getWrapper(BlockGeneratorReference.BEIGE_LIMESTONE));
        State rhyolite = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.RHYOLITE));
        State marble = new StoneState(Type.TERTIARY, getWrapper(BlockGeneratorReference.MARBLE));
        State white_granite = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.WHITE_GRANITE));

        State dolomite = new StoneState(Type.SECONDARY, getWrapper(BlockGeneratorReference.DOLOMITE));
        State andesite = new StoneState(Type.SECONDARY, getWrapper(BlockGeneratorReference.ANDESITE));
        State siltstone = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.SILTSTONE));
        State mudstone = new StoneState(Type.SECONDARY, getWrapper(BlockGeneratorReference.MUDSTONE));
        State sandstone = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.SANDSTONE));
        State smooth_sandstone = new SimpleState(Type.PRIMARY, Blocks.SMOOTH_SANDSTONE.getDefaultState(), BlockStates.SANDSTONE_REGOLITH, BlockStates.SANDSTONE_GRASSY_REGOLITH);
        State terracotta = new SimpleState(Type.PRIMARY, Blocks.TERRACOTTA.getDefaultState(), BlockStates.MUDSTONE_REGOLITH, BlockStates.MUDSTONE_GRASSY_REGOLITH);

        State dacite_strata = new SimpleState(Type.STRATA, BlockStates.DACITE, BlockStates.DACITE, BlockStates.DACITE);
        State marble_strata = new SimpleState(Type.STRATA, BlockStates.MARBLE, BlockStates.DOLOMITE_REGOLITH, BlockStates.DOLOMITE_GRASSY_REGOLITH);
        State lignite_strata = new SimpleState(Type.STRATA, BlockStates.LIGNITE, BlockStates.STONE_REGOLITH, Blocks.GRASS_BLOCK.getDefaultState());
        State dolerite_strata = new SimpleState(Type.STRATA, BlockStates.DOLERITE, BlockStates.DOLERITE, BlockStates.DOLERITE);

        DEFAULT = new RegionBuilder()
                .addStates(stone, gabbro, slate, limestone) // primary
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.GABBRO))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.SLATE))
                .addSimpleStoneType(Type.OROGEN, BlockStates.SCHIST_X, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH)
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.GABBRO))
                .addSimpleStoneType(Type.OROGEN, BlockStates.RHYOLITE, BlockStates.WEATHERED_RHYOLITE, BlockStates.WEATHERED_RHYOLITE)
                .addSimpleStoneType(Type.OROGEN, BlockStates.SCHIST_Y, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH)
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.SLATE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.GABBRO))
                .addSimpleStoneType(Type.OROGEN, BlockStates.SCHIST_Z, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH)
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.SLATE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.PUMICE))
                .setSecondaryStoneType(getWrapper(BlockGeneratorReference.DOLOMITE))
                .addStates(dacite_strata, lignite_strata, dolerite_strata)
                .setStrataParams(0.2f, 5, 2)
                .setOreProbability(0.001f, 0.002f, 0)
                .addRegolith(2, 1)
                .addOre(1, new BasicReplacer(phyllite, stone), 1)
                .addOre(1, new BasicReplacer(granodiorite, dolomite), 1)
                .addOre(1, new BasicReplacer(dolomite, limestone), 1)
                .build();

        LIMESTONE = new RegionBuilder()
                .addStates(stone, limestone, granodiorite, grey_limestone, phyllite)
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.SLATE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.QUARTZITE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.GRANODIORITE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.GREY_LIMESTONE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.DOLOMITE))
                .addSimpleStoneType(Type.OROGEN, BlockStates.WHITE_GRANITE, BlockStates.WEATHERED_RHYOLITE, BlockStates.WEATHERED_RHYOLITE)
                .addStates(dacite_strata, marble_strata, lignite_strata)
                .addStrataStoneType(getWrapper(BlockGeneratorReference.SILTSTONE))
                .setStrataParams(0.15f, 5, 2)
                .setSecondaryStoneType(getWrapper(BlockGeneratorReference.BEIGE_LIMESTONE))
                .setOreProbability(0.001f, 0.002f, 0.0001f)
                .addRegolith(2, 1)
                .addOre(1, new BasicReplacer(andesite, stone), 5)
                .addOre(1, new BasicReplacer(marble, granodiorite), 1)
                .addOre(1, new BasicReplacer(beige_limestone, limestone), 5)
                .addOre(2, new BasicReplacer(beige_limestone, limestone), 1)
                .addOre(3, new BasicReplacer(beige_limestone, limestone), 1)
                .addOre(1, new BasicReplacer(limestone, grey_limestone), 5)
                .addOre(1, new BasicReplacer(white_granite, beige_limestone), 5)
                .addOre(2, new BasicReplacer(white_granite, beige_limestone), 1)
                .addOre(3, new BasicReplacer(white_granite, beige_limestone), 1)
                .build();

        SEDIMENTARY = new RegionBuilder()
                .addStates(siltstone, rhyolite, sandstone, white_granite)
                .addPrimaryStoneType(getWrapper(BlockGeneratorReference.BEIGE_LIMESTONE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.QUARTZITE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.DACITE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.QUARTZITE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.DACITE))
                .addSimpleStoneType(Type.OROGEN, BlockStates.SCHIST_X, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH)
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.GREY_LIMESTONE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.STONE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.GREY_LIMESTONE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.STONE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.CONGLOMERATE))
                .addOrogenyStoneType(getWrapper(BlockGeneratorReference.CONGLOMERATE))
                .addSimpleStoneType(Type.OROGEN, BlockStates.SCHIST_Z, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH)
                .setSecondaryStoneType(getWrapper(BlockGeneratorReference.MUDSTONE))
                .addSimpleStoneType(Type.STRATA, BlockStates.SCHIST_Y, BlockStates.PHYLLITE_REGOLITH, BlockStates.PHYLLITE_GRASSY_REGOLITH)
                .addStates(lignite_strata, dacite_strata)
                .addStrataStoneType(getWrapper(BlockGeneratorReference.CONGLOMERATE))
                .addStrataStoneType(getWrapper(BlockGeneratorReference.BEIGE_LIMESTONE))
                .setStrataParams(0.1f, 5, 1)
                .setOreProbability(0.004f, 0.002f, 0)
                .addRegolith(2, 3)
                .addOre(1, new BasicReplacer(smooth_sandstone, sandstone), 10)
                .addOre(1, new BasicReplacer(terracotta, mudstone), 1)
                .addOre(1, new BasicReplacer(beige_limestone, siltstone), 1)
                .build();
    }

    private static StoneWrapper getWrapper(BlockGeneratorHelper dolomite) {
        return StoneWrapper.fromGenerationHelper(dolomite);
    }

    private static ResourceLocation getLocation(BlockGeneratorHelper helper) {
        return helper.getBaseBlock().getRegistryName();
    }


    public static class BlockStates {
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
        protected static final BlockState STONE_REGOLITH = BlockGeneratorReference.STONE.getEntry(Variants.REGOLITHS, Forms.REGOLITH).getBlock().getDefaultState();
        protected static final BlockState STONE_GRASSY_REGOLITH = BlockGeneratorReference.STONE.getEntry(Variants.REGOLITHS, Forms.GRASSY_REGOLITH).getBlock().getDefaultState();
        protected static final BlockState SANDSTONE = Blocks.SANDSTONE.getDefaultState();
        protected static final BlockState SMOOTH_SANDSTONE = Blocks.SMOOTH_SANDSTONE.getDefaultState();
        protected static final BlockState SANDSTONE_REGOLITH = BlockGeneratorReference.SANDSTONE.getEntry(Variants.SANDSTONE, Forms.REGOLITH).getBlock().getDefaultState();
        protected static final BlockState SANDSTONE_GRASSY_REGOLITH = BlockGeneratorReference.SANDSTONE.getEntry(Variants.SANDSTONE, Forms.GRASSY_REGOLITH).getBlock().getDefaultState();

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
}

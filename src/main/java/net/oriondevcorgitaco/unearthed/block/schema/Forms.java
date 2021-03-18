package net.oriondevcorgitaco.unearthed.block.schema;

import net.minecraft.block.*;
import net.oriondevcorgitaco.unearthed.block.*;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;

import java.util.function.Function;

public class Forms {
    private static class SimpleForm extends BlockSchema.Form {
        Function<AbstractBlock.Properties, Block> blockCreator;

        public SimpleForm(String name, Function<AbstractBlock.Properties, Block> blockCreator) {
            super(name);
            this.blockCreator = blockCreator;
        }

        @Override
        public Function<AbstractBlock.Properties, Block> getBlockCreator(BlockGeneratorHelper schema, BlockSchema.Variant variant) {
            return blockCreator;
        }
    }

    private static class StairForm extends BlockSchema.Form {
        public StairForm(String name) {
            super(name);
        }

        @Override
        public Function<AbstractBlock.Properties, Block> getBlockCreator(BlockGeneratorHelper schema, BlockSchema.Variant variant) {
            return properties -> new StairsBlock(() -> schema.getBaseBlock(variant).getDefaultState(), properties);
        }
    }

    public static class OreForm extends BlockSchema.Form {
        private IOreType oreType;

        public OreForm(String name, IOreType oreType) {
            super(name);
            this.oreType = oreType;
        }

        public IOreType getOreType() {
            return oreType;
        }

        @Override
        public Function<AbstractBlock.Properties, Block> getBlockCreator(BlockGeneratorHelper schema, BlockSchema.Variant variant) {
            return UEOreBlock::new;
        }
    }

    public static final BlockSchema.Form BLOCK = new SimpleForm("", Block::new);
    public static final BlockSchema.Form AXISBLOCK = new SimpleForm("", RotatedPillarBlock::new);
    public static final BlockSchema.Form BEAM = new SimpleForm("", RotatedPillarBlock::new);
    public static final BlockSchema.Form SIDETOP_BLOCK = new SimpleForm("", Block::new).sideTopBlock();
    public static final BlockSchema.Form REGOLITH = new SimpleForm("regolith", RegolithBlock::new);
    public static final BlockSchema.Form GRASSY_REGOLITH = new BlockSchema.Form("grassy_regolith") {
        @Override
        public Function<AbstractBlock.Properties, Block> getBlockCreator(BlockGeneratorHelper schema, BlockSchema.Variant variant) {
            return properties -> new RegolithGrassBlock(schema.getEntry(variant, Forms.REGOLITH).getBlock(), properties);
        }
    };
    public static final BlockSchema.Form OVERGROWN_ROCK = new BlockSchema.Form("") {
        @Override
        public Function<AbstractBlock.Properties, Block> getBlockCreator(BlockGeneratorHelper schema, BlockSchema.Variant variant) {
            return properties -> new RegolithGrassBlock(schema.getBaseBlock().getBlock(), properties);
        }
    };
    public static final BlockSchema.Form SLAB = new SimpleForm("slab", SlabBlock::new);
    public static final BlockSchema.Form SIDETOP_SLAB = new SimpleForm("slab", SlabBlock::new).sideTopBlock();
    public static final BlockSchema.Form SIXWAY_SLAB = new SimpleForm("slab", SixwaySlabBlock::new).sideTopBlock();
    public static final BlockSchema.Form STAIRS = new StairForm("stairs");
    public static final BlockSchema.Form SIDETOP_STAIRS = new StairForm("stairs").sideTopBlock();
    public static final BlockSchema.Form WALLS = new SimpleForm("wall", WallBlock::new);
    public static final BlockSchema.Form PRESSURE_PLATE = new BlockSchema.Form("pressure_plate") {
        @Override
        public Function<AbstractBlock.Properties, Block> getBlockCreator(BlockGeneratorHelper schema, BlockSchema.Variant variant) {
            return properties -> new PressurePlateBlock(schema.getPressurePlateSensitivity(), properties);
        }
    };
    public static final BlockSchema.Form BUTTON = new SimpleForm("button", StoneButtonBlock::new);

    public static final BlockSchema.Form IRON_ORE = new OreForm("iron_ore", VanillaOreTypes.IRON);
    public static final BlockSchema.Form COAL_ORE = new OreForm("coal_ore", VanillaOreTypes.COAL);
    public static final BlockSchema.Form GOLD_ORE = new OreForm("gold_ore", VanillaOreTypes.GOLD);
    public static final BlockSchema.Form REDSTONE_ORE = new OreForm("redstone_ore", VanillaOreTypes.REDSTONE);
    public static final BlockSchema.Form LAPIS_ORE =    new OreForm("lapis_ore", VanillaOreTypes.LAPIS);
    public static final BlockSchema.Form DIAMOND_ORE = new OreForm("diamond_ore", VanillaOreTypes.DIAMOND);
    public static final BlockSchema.Form EMERALD_ORE = new OreForm("emerald_ore", VanillaOreTypes.EMERALD);

    public static final BlockSchema.Form SIDETOP_IRON_ORE = new OreForm("iron_ore", VanillaOreTypes.IRON).sideTopBlock();
    public static final BlockSchema.Form SIDETOP_COAL_ORE = new OreForm("coal_ore", VanillaOreTypes.COAL).sideTopBlock();
    public static final BlockSchema.Form SIDETOP_GOLD_ORE = new OreForm("gold_ore", VanillaOreTypes.GOLD).sideTopBlock();
    public static final BlockSchema.Form SIDETOP_REDSTONE_ORE = new OreForm("redstone_ore", VanillaOreTypes.REDSTONE).sideTopBlock();
    public static final BlockSchema.Form SIDETOP_LAPIS_ORE = new OreForm("lapis_ore", VanillaOreTypes.LAPIS).sideTopBlock();
    public static final BlockSchema.Form SIDETOP_DIAMOND_ORE = new OreForm("diamond_ore", VanillaOreTypes.DIAMOND).sideTopBlock();
    public static final BlockSchema.Form SIDETOP_EMERALD_ORE = new OreForm("emerald_ore", VanillaOreTypes.EMERALD).sideTopBlock();

    public static final BlockSchema.Form KIMBERLITE_DIAMOND_ORE = new OreForm("diamond_ore", VanillaOreTypes.DIAMOND);


}

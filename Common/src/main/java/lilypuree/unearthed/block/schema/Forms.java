package lilypuree.unearthed.block.schema;

import lilypuree.unearthed.block.*;
import lilypuree.unearthed.block.type.IOreType;
import lilypuree.unearthed.block.type.VanillaOreTypes;
import lilypuree.unearthed.mixin.server.StairBlockInvoker;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class Forms {
    private static class SimpleForm extends BlockForm {
        Function<BlockBehaviour.Properties, Block> blockCreator;

        public SimpleForm(String name, Function<BlockBehaviour.Properties, Block> blockCreator) {
            super(name);
            this.blockCreator = blockCreator;
        }

        @Override
        public Function<BlockBehaviour.Properties, Block> getBlockCreator(BlockSchema schema, BlockVariant variant) {
            return blockCreator;
        }
    }

    private static class StairForm extends BlockForm {
        public StairForm(String name) {
            super(name);
        }

        @Override
        public Function<BlockBehaviour.Properties, Block> getBlockCreator(BlockSchema schema, BlockVariant variant) {
            return properties -> StairBlockInvoker.onConstruction(schema.getBaseBlock(variant).defaultBlockState(), properties);
        }
    }

    public static class OreForm extends BlockForm {
        private IOreType oreType;

        public OreForm(String name, IOreType oreType) {
            super(name);
            this.oreType = oreType;
        }

        public IOreType getOreType() {
            return oreType;
        }

        @Override
        public Function<BlockBehaviour.Properties, Block> getBlockCreator(BlockSchema schema, BlockVariant variant) {
            if (oreType == VanillaOreTypes.REDSTONE) {
                return RedStoneOreBlock::new;
            } else {
                return UEOreBlock::new;
            }
        }
    }

    public static final BlockForm BLOCK = new SimpleForm("", Block::new);
    public static final BlockForm AXISBLOCK = new SimpleForm("", RotatedPillarBlock::new);
    public static final BlockForm BEAM = new SimpleForm("", RotatedPillarBlock::new);
    public static final BlockForm REGOLITH = new SimpleForm("regolith", RegolithBlock::new);
    public static final BlockForm GRASSY_REGOLITH = new BlockForm("grassy_regolith") {
        @Override
        public Function<BlockBehaviour.Properties, Block> getBlockCreator(BlockSchema schema, BlockVariant variant) {
            return properties -> new RegolithGrassBlock(schema.getEntry(variant, Forms.REGOLITH).getBlock(), properties);
        }
    };
    public static final BlockForm OVERGROWN_ROCK = new BlockForm("") {
        @Override
        public Function<BlockBehaviour.Properties, Block> getBlockCreator(BlockSchema schema, BlockVariant variant) {
            return properties -> new RegolithGrassBlock(schema.getBaseBlock(), properties);
        }
    };
    public static final BlockForm SLAB = new SimpleForm("slab", SlabBlock::new);
    public static final BlockForm SIXWAY_SLAB = new SimpleForm("slab", SixwaySlabBlock::new);
    public static final BlockForm STAIRS = new StairForm("stairs");
    public static final BlockForm WALLS = new SimpleForm("wall", WallBlock::new);
    public static final BlockForm PRESSURE_PLATE = new SimpleForm("pressure_plate", UEPressurePlateBlock::new);
    public static final BlockForm BUTTON = new SimpleForm("button", UEButtonBlock::new);

    public static final BlockForm IRON_ORE = new OreForm("iron_ore", VanillaOreTypes.IRON);
    public static final BlockForm COAL_ORE = new OreForm("coal_ore", VanillaOreTypes.COAL);
    public static final BlockForm COPPER_ORE = new OreForm("copper_ore", VanillaOreTypes.COPPER);
    public static final BlockForm GOLD_ORE = new OreForm("gold_ore", VanillaOreTypes.GOLD);
    public static final BlockForm REDSTONE_ORE = new OreForm("redstone_ore", VanillaOreTypes.REDSTONE);
    public static final BlockForm LAPIS_ORE = new OreForm("lapis_ore", VanillaOreTypes.LAPIS);
    public static final BlockForm DIAMOND_ORE = new OreForm("diamond_ore", VanillaOreTypes.DIAMOND);
    public static final BlockForm EMERALD_ORE = new OreForm("emerald_ore", VanillaOreTypes.EMERALD);


    public static final BlockForm KIMBERLITE_DIAMOND_ORE = new OreForm("diamond_ore", VanillaOreTypes.DIAMOND);
}

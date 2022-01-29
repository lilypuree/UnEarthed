package lilypuree.unearthed.block.schema;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Variants {

    public static BlockVariant ALL_BLOCKS_PLUS;
    public static BlockVariant ALL_BASE_BLOCKS;
    public static BlockVariant SEDIMENTARY;
    public static BlockVariant COBBLED;
    public static BlockVariant MOSSY_COBBLED;
    public static BlockVariant POLISHED;
    public static BlockVariant SMOOTH;
    public static BlockVariant CUT;
    public static BlockVariant CHISELED;
    public static BlockVariant CHISELED_FULL;
    public static BlockVariant POLISHED_NOWALL;
    public static BlockVariant BRICK;
    public static BlockVariant BRICKS;
    public static BlockVariant MOSSY_BRICKS;
    public static BlockVariant CRACKED_BRICKS;
    public static BlockVariant CHISELED_BRICKS;
    public static BlockVariant POLISHED_BRICKS;
    public static BlockVariant CHISELED_POLISHED;
    public static BlockVariant CRACKED_POLISHED_BRICKS;
    public static BlockVariant PILLAR;
    public static BlockVariant POLISHED_PILLAR;

    public static BlockVariant BASIC;
    public static BlockVariant PILLAR_BLOCK;

    public static BlockVariant VANILLA;
    public static BlockVariant REGOLITHS;
    public static BlockVariant SANDSTONE;
    public static BlockVariant SCHIST;
    public static BlockVariant OVERGROWN;

    static {
        List<BlockForm> ores = Lists.newArrayList(Forms.IRON_ORE, Forms.COAL_ORE, Forms.COPPER_ORE, Forms.GOLD_ORE, Forms.LAPIS_ORE, Forms.REDSTONE_ORE, Forms.DIAMOND_ORE, Forms.EMERALD_ORE);
        List<BlockForm> regoliths = Lists.newArrayList(Forms.REGOLITH, Forms.GRASSY_REGOLITH);

        List<BlockForm> fullBlocks = Lists.newArrayList(Forms.BLOCK, Forms.SLAB, Forms.STAIRS, Forms.WALLS, Forms.PRESSURE_PLATE, Forms.BUTTON);
        List<BlockForm> baseBlocks = Lists.newArrayList(Forms.BLOCK, Forms.SLAB, Forms.STAIRS, Forms.WALLS);
        List<BlockForm> noWall = Lists.newArrayList(Forms.BLOCK, Forms.SLAB, Forms.STAIRS);
        List<BlockForm> singleBlock = Lists.newArrayList(Forms.BLOCK);

        List<BlockForm> stoneLike = new ArrayList<>();
        stoneLike.addAll(fullBlocks);
        stoneLike.addAll(ores);
        stoneLike.addAll(regoliths);

        List<BlockForm> secondary = new ArrayList<>();
        secondary.addAll(baseBlocks);
        secondary.addAll(ores);
        secondary.addAll(regoliths);


        ALL_BLOCKS_PLUS = new BlockVariant("", stoneLike);
        ALL_BASE_BLOCKS = new BlockVariant("", secondary);


        BASIC = new BlockVariant("", baseBlocks);
        SEDIMENTARY = new BlockVariant("", secondary).sideTop();

        COBBLED = new BlockVariant("cobbled", baseBlocks);
        MOSSY_COBBLED = new BlockVariant("mossy_cobbled", baseBlocks);

        POLISHED = new BlockVariant("polished", baseBlocks).setDerivative();
        POLISHED_NOWALL = new BlockVariant("polished", noWall).setDerivative();
        CHISELED_POLISHED = new BlockVariant("chiseled_polished", singleBlock).setDerivative();
        POLISHED_BRICKS = new ComplexVariant("polished", "brick", true, baseBlocks).setDerivative();
        CRACKED_POLISHED_BRICKS = new ComplexVariant("cracked_polished", "brick", true, singleBlock);

        BRICKS = new ComplexVariant("", "brick", true, baseBlocks).setDerivative();
        MOSSY_BRICKS = new ComplexVariant("mossy", "brick", true, baseBlocks);
        CRACKED_BRICKS = new ComplexVariant("cracked", "brick", true, singleBlock);
        CHISELED_BRICKS = new ComplexVariant("chiseled", "brick", true, singleBlock).setDerivative();

        CUT = new BlockVariant("cut", baseBlocks).setDerivative().sideTop();
        SMOOTH = new BlockVariant("smooth", noWall);
        CHISELED = new BlockVariant("chiseled", singleBlock).setDerivative().sideTop();
        CHISELED_FULL = new BlockVariant("chiseled", singleBlock).setDerivative();

        PILLAR = new BlockVariant("", Forms.AXISBLOCK);
        POLISHED_PILLAR = new BlockVariant("polished", Forms.AXISBLOCK).setDerivative();


        BRICK = new ComplexVariant("", "bricks", false, Forms.BLOCK).setDerivative();
        PILLAR_BLOCK = new ComplexVariant("", "pillar", false, Forms.BEAM).setDerivative();

        VANILLA = new BlockVariant("", ores);
        List<BlockForm> sandstone = new ArrayList<>();
        sandstone.addAll(ores);
        sandstone.addAll(regoliths);
        SANDSTONE = new BlockVariant("", sandstone).sideTop();
        OVERGROWN = new BlockVariant("overgrown", Forms.OVERGROWN_ROCK);
        REGOLITHS = new BlockVariant("", regoliths);

        SCHIST = new BlockVariant("", Forms.AXISBLOCK, Forms.SIXWAY_SLAB).sideTop();
    }

    public static class ComplexVariant extends BlockVariant {
        String prefix;
        String suffix;
        boolean plural;

        public ComplexVariant(String prefix, String suffix, boolean plural, List<BlockForm> forms) {
            super(prefix + "_" + suffix + (plural ? "s" : ""), forms);
            this.prefix = prefix;
            this.suffix = suffix;
            this.plural = plural;
        }

        public ComplexVariant(String prefix, String suffix, boolean plural, BlockForm... forms) {
            this(prefix, suffix, plural, Lists.newArrayList(forms));
        }

        @Override
        public String getBlockId(String baseName, String suffix2) {
            String fullId = baseName;
            if (!prefix.equals("")) {
                fullId = prefix + "_" + baseName;
            }
            if (!suffix.equals("")) {
                fullId = fullId + "_" + this.suffix;
            }
            if (suffix2.equals("")) {
                if (plural) {
                    fullId = fullId + "s";
                }
            } else {
                fullId = fullId + "_" + suffix2;
            }
            return fullId;
        }
    }
}

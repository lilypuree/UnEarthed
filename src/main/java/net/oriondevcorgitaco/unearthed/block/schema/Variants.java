package net.oriondevcorgitaco.unearthed.block.schema;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Variants {

    public static BlockSchema.Variant STONE_LIKE;
    public static BlockSchema.Variant SECONDARY_STONE;
    public static BlockSchema.Variant SEDIMENTARY;
    public static BlockSchema.Variant IGNEOUS;
    public static BlockSchema.Variant COBBLED;
    public static BlockSchema.Variant MOSSY_COBBLED;
    public static BlockSchema.Variant POLISHED;
    public static BlockSchema.Variant SMOOTH;
    public static BlockSchema.Variant CUT;
    public static BlockSchema.Variant CHISELED;
    public static BlockSchema.Variant CHISELED_FULL;
    public static BlockSchema.Variant POLISHED_NOWALL;
    public static BlockSchema.Variant BRICK;
    public static BlockSchema.Variant BRICKS;
    public static BlockSchema.Variant MOSSY_BRICKS;
    public static BlockSchema.Variant CRACKED_BRICKS;
    public static BlockSchema.Variant CHISELED_BRICKS;
    public static BlockSchema.Variant POLISHED_BRICKS;
    public static BlockSchema.Variant CHISELED_POLISHED;
    public static BlockSchema.Variant CRACKED_POLISHED_BRICKS;
    public static BlockSchema.Variant PILLAR;
    public static BlockSchema.Variant POLISHED_PILLAR;

    public static BlockSchema.Variant BASIC;
    public static BlockSchema.Variant PILLAR_BLOCK;

    public static BlockSchema.Variant VANILLA;
    public static BlockSchema.Variant REGOLITHS;
    public static BlockSchema.Variant SANDSTONE;
    public static BlockSchema.Variant OVERGROWN;

    static {
        List<BlockSchema.Form> all_ores = Lists.newArrayList(Forms.IRON_ORE, Forms.COAL_ORE, Forms.GOLD_ORE, Forms.LAPIS_ORE, Forms.REDSTONE_ORE, Forms.DIAMOND_ORE, Forms.EMERALD_ORE);
        List<BlockSchema.Form> ores = Lists.newArrayList(Forms.IRON_ORE, Forms.COAL_ORE, Forms.GOLD_ORE, Forms.LAPIS_ORE, Forms.REDSTONE_ORE, Forms.EMERALD_ORE);
        List<BlockSchema.Form> igneous_ores = Lists.newArrayList(Forms.IRON_ORE, Forms.GOLD_ORE, Forms.LAPIS_ORE, Forms.REDSTONE_ORE, Forms.EMERALD_ORE);
        List<BlockSchema.Form> fullBlocks = Lists.newArrayList(Forms.BLOCK, Forms.SLAB, Forms.STAIRS, Forms.WALLS, Forms.PRESSURE_PLATE, Forms.BUTTON);
        List<BlockSchema.Form> baseBlocks = Lists.newArrayList(Forms.BLOCK, Forms.SLAB, Forms.STAIRS, Forms.WALLS);
        List<BlockSchema.Form> noWall = Lists.newArrayList(Forms.BLOCK, Forms.SLAB, Forms.STAIRS);
        List<BlockSchema.Form> singleBlock = Lists.newArrayList(Forms.BLOCK);
        List<BlockSchema.Form> sideTopBlock = Lists.newArrayList(Forms.SIDETOP_BLOCK, Forms.SIDETOP_SLAB, Forms.SIDETOP_STAIRS, Forms.WALLS);
        List<BlockSchema.Form> sideTopOres = Lists.newArrayList(Forms.SIDETOP_IRON_ORE, Forms.SIDETOP_COAL_ORE, Forms.SIDETOP_GOLD_ORE, Forms.SIDETOP_LAPIS_ORE, Forms.SIDETOP_REDSTONE_ORE, Forms.SIDETOP_EMERALD_ORE, Forms.SIDETOP_DIAMOND_ORE);
        List<BlockSchema.Form> regoliths = Lists.newArrayList(Forms.REGOLITH, Forms.GRASSY_REGOLITH);


        List<BlockSchema.Form> stoneLike = new ArrayList<>();
        stoneLike.addAll(fullBlocks);
        stoneLike.addAll(all_ores);
        stoneLike.addAll(regoliths);
        STONE_LIKE = new BlockSchema.Variant("", stoneLike);
        COBBLED = new BlockSchema.Variant("cobbled", baseBlocks);
        MOSSY_COBBLED = new BlockSchema.Variant("mossy_cobbled", baseBlocks);
        POLISHED = new BlockSchema.Variant("polished", baseBlocks).setDerivative();
        POLISHED_NOWALL = new BlockSchema.Variant("polished", noWall).setDerivative();
        CHISELED_POLISHED = new BlockSchema.Variant("chiseled_polished", singleBlock).setDerivative();
        POLISHED_BRICKS = new ComplexVariant("polished", "brick", true, baseBlocks).setDerivative();
        BRICKS = new ComplexVariant("", "brick", true, baseBlocks).setDerivative();
        MOSSY_BRICKS = new ComplexVariant("mossy", "brick", true, baseBlocks);
        CRACKED_BRICKS = new ComplexVariant("cracked", "brick", true, singleBlock);
        CHISELED_BRICKS = new ComplexVariant("chiseled", "brick", true, singleBlock).setDerivative();
        CRACKED_POLISHED_BRICKS = new ComplexVariant("cracked_polished", "brick", true, singleBlock);

        List<BlockSchema.Form> secondary = new ArrayList<>();
        secondary.addAll(baseBlocks);
        secondary.addAll(all_ores);
        secondary.addAll(regoliths);
        SECONDARY_STONE = new BlockSchema.Variant("", secondary);

        List<BlockSchema.Form> igneous = new ArrayList<>();
        igneous.addAll(baseBlocks);
        igneous.addAll(all_ores);
        igneous.addAll(regoliths);
        IGNEOUS = new BlockSchema.Variant("", igneous);

        List<BlockSchema.Form> sedimentary = new ArrayList<>();
        sedimentary.addAll(sideTopBlock);
        sedimentary.addAll(sideTopOres);
        sedimentary.addAll(regoliths);
        SEDIMENTARY = new BlockSchema.Variant("", sedimentary);
        CUT = new BlockSchema.Variant("cut", Lists.newArrayList(Forms.SIDETOP_BLOCK, Forms.SIDETOP_SLAB, Forms.SIDETOP_STAIRS, Forms.WALLS)).setDerivative();
        CHISELED = new BlockSchema.Variant("chiseled", Lists.newArrayList(Forms.SIDETOP_BLOCK)).setDerivative();
        CHISELED_FULL = new BlockSchema.Variant("chiseled", Lists.newArrayList(Forms.BLOCK)).setDerivative();
        SMOOTH = new BlockSchema.Variant("smooth", noWall);

        PILLAR = new BlockSchema.Variant("", Forms.AXISBLOCK);
        POLISHED_PILLAR = new BlockSchema.Variant("polished", Forms.AXISBLOCK).setDerivative();

        BASIC = new BlockSchema.Variant("", baseBlocks);
        BRICK = new ComplexVariant("", "bricks", false, Lists.newArrayList(Forms.BLOCK)).setDerivative();
        PILLAR_BLOCK = new ComplexVariant("", "pillar", false, Lists.newArrayList(Forms.BEAM)).setDerivative();

        VANILLA = new BlockSchema.Variant("", all_ores);
        List<BlockSchema.Form> sandstone = new ArrayList<>();
        sandstone.addAll(sideTopOres);
        sandstone.addAll(regoliths);
        SANDSTONE = new BlockSchema.Variant("", sandstone);
        OVERGROWN = new BlockSchema.Variant("overgrown", Forms.OVERGROWN_ROCK);
        REGOLITHS = new BlockSchema.Variant("", regoliths);
    }

    public static class ComplexVariant extends BlockSchema.Variant {
        String prefix;
        String suffix;
        boolean plural;

        public ComplexVariant(String prefix, String suffix, boolean plural, List<BlockSchema.Form> forms) {
            super(prefix + "_" + suffix + (plural ? "s" : ""), forms);
            this.prefix = prefix;
            this.suffix = suffix;
            this.plural = plural;
        }

        @Override
        public String concatID(String baseName, String suffix2) {
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

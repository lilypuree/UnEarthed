package net.oriondevcorgitaco.unearthed.block.schema;

import com.google.common.collect.Lists;

import static net.oriondevcorgitaco.unearthed.block.schema.Variants.*;

public class BlockSchemas {

    public static BlockSchema BASIC = new BlockSchema(Lists.newArrayList(Variants.BASIC));
    public static BlockSchema STONE_LIKE = new BlockSchema(Variants.STONE_LIKE, COBBLED, MOSSY_COBBLED, BRICKS, MOSSY_BRICKS, CRACKED_BRICKS, CHISELED_BRICKS);
    public static BlockSchema BLACKSTONE_LIKE_FULL = new BlockSchema(Lists.newArrayList(Variants.STONE_LIKE, POLISHED, POLISHED_BRICKS, CHISELED_POLISHED, CRACKED_POLISHED_BRICKS));
    public static BlockSchema BLACKSTONE_LIKE = new BlockSchema(Lists.newArrayList(Variants.STONE_LIKE));
    //    public static BlockSchema BLACKSTONE_LIKE_IGNEOUS = new BlockSchema(Variants.IGNEOUS);
    //    public static BlockSchema
    public static BlockSchema BASALT_LIKE = new BlockSchema(Variants.PILLAR, Variants.POLISHED_PILLAR);
    public static BlockSchema SANDSTONE_LIKE_FULL = new BlockSchema(Lists.newArrayList(Variants.SEDIMENTARY, SMOOTH, CUT, CHISELED));

        public static BlockSchema SECONDARY = new BlockSchema(Lists.newArrayList(Variants.SECONDARY_STONE, Variants.POLISHED));
    //    public static BlockSchema DECORATIVE = new BlockSchema(Lists.newArrayList(Variants.BASIC, Variants.POLISHED_NOWALL));
    public static BlockSchema DECORATIVE = new BlockSchema(Lists.newArrayList(Variants.BASIC));

    //    public static BlockSchema SCHIST = new BlockSchema(Lists.newArrayList(Variants.PILLAR, Variants.POLISHED, Variants.POLISHED_BRICKS, Variants.MOSSY_BRICKS));
    public static BlockSchema SCHIST = new BlockSchema(new BlockSchema.Variant("", Forms.AXISBLOCK, Forms.SIXWAY_SLAB));

    //    public static BlockSchema LIMESTONE = new BlockSchema(Lists.newArrayList(Variants.SECONDARY_STONE, Variants.COBBLED, Variants.BRICK, Variants.CHISELED, Variants.PILLAR_BLOCK));
    public static BlockSchema LIMESTONE = new BlockSchema(Variants.SECONDARY_STONE, COBBLED);
    public static BlockSchema LIMESTONE_FULL = new BlockSchema(Variants.SECONDARY_STONE, COBBLED, BRICKS, PILLAR_BLOCK, CHISELED_FULL);
    public static BlockSchema BEIGE_LIMESTONE = new BlockSchema(SEDIMENTARY, COBBLED, BRICKS, CHISELED, PILLAR_BLOCK);

    public static BlockSchema KIMBERLITE = new BlockSchema(new BlockSchema.Variant("", Forms.BLOCK, Forms.SLAB, Forms.STAIRS, Forms.WALLS, Forms.KIMBERLITE_DIAMOND_ORE, Forms.REGOLITH, Forms.GRASSY_REGOLITH));

    public static BlockSchema INTRUSIVE = new BlockSchema(Variants.VANILLA, Variants.OVERGROWN);

}

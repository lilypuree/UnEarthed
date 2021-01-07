package net.oriondevcorgitaco.unearthed.block.schema;

import com.google.common.collect.Lists;

public class BlockSchemas {

    public static BlockSchema BASIC = new BlockSchema(Lists.newArrayList(Variants.BASIC));
    //    public static BlockSchema STONE_LIKE = new BlockSchema(Lists.newArrayList(Variants.STONE_LIKE, Variants.COBBLED, Variants.MOSSY_COBBLED, Variants.COBBLED, Variants.BRICKS, Variants.MOSSY_BRICKS, Variants.CHISELED_BRICKS));
    public static BlockSchema STONE_LIKE = new BlockSchema(Lists.newArrayList(Variants.STONE_LIKE));
    //    public static BlockSchema BLACKSTONE_LIKE = new BlockSchema(Lists.newArrayList(Variants.STONE_LIKE, Variants.POLISHED, Variants.POLISHED_BRICKS, Variants.CHISELED_POLISHED));
    public static BlockSchema BLACKSTONE_LIKE = new BlockSchema(Lists.newArrayList(Variants.STONE_LIKE));
    //    public static BlockSchema
    public static BlockSchema BASALT_LIKE = new BlockSchema(Lists.newArrayList(Variants.PILLAR, Variants.POLISHED_PILLAR));
    //    public static BlockSchema SANDSTONE_LIKE = new BlockSchema(Lists.newArrayList(Variants.SEDIMENTARY, Variants.SMOOTH, Variants.CUT, Variants.CHISELED));
    public static BlockSchema SANDSTONE_LIKE = new BlockSchema(Lists.newArrayList(Variants.SEDIMENTARY));
    //    public static BlockSchema SECONDARY = new BlockSchema(Lists.newArrayList(Variants.SECONDARY_STONE, Variants.POLISHED));
    public static BlockSchema SECONDARY = new BlockSchema(Lists.newArrayList(Variants.SECONDARY_STONE));
    //    public static BlockSchema DECORATIVE = new BlockSchema(Lists.newArrayList(Variants.BASIC, Variants.POLISHED_NOWALL));
    public static BlockSchema DECORATIVE = new BlockSchema(Lists.newArrayList(Variants.BASIC));

    //    public static BlockSchema SCHIST = new BlockSchema(Lists.newArrayList(Variants.PILLAR, Variants.POLISHED, Variants.POLISHED_BRICKS, Variants.MOSSY_BRICKS));
    public static BlockSchema SCHIST = new BlockSchema(Lists.newArrayList(Variants.PILLAR));

    //    public static BlockSchema LIMESTONE = new BlockSchema(Lists.newArrayList(Variants.SECONDARY_STONE, Variants.COBBLED, Variants.BRICK, Variants.CHISELED, Variants.PILLAR_BLOCK));
    public static BlockSchema LIMESTONE = new BlockSchema(Lists.newArrayList(Variants.SECONDARY_STONE));
    //    public static BlockSchema BEIGE_LIMESTONE = new BlockSchema(Lists.newArrayList(Variants.SEDIMENTARY, Variants.COBBLED, Variants.BRICK, Variants.CHISELED, Variants.PILLAR_BLOCK));
    public static BlockSchema BEIGE_LIMESTONE = new BlockSchema(Lists.newArrayList(Variants.SEDIMENTARY));

}

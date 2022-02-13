package lilypuree.unearthed.block.schema;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.ArrayList;
import java.util.List;

import static lilypuree.unearthed.block.schema.Variants.*;

public class BlockSchemas {
    private static float stoneHardness = 1.5f;
    private static float cobbleHardness = 2.0f;
    private static float stoneResistance = 6.0f;
    private static float miscResistance = 0.5f;
    private static BlockBehaviour.Properties stoneProperty = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(stoneHardness, stoneResistance);
    private static BlockBehaviour dummyBehavior = new Block(stoneProperty);

    public static List<BlockSchema> ROCK_TYPES = new ArrayList<>();

    public static final BlockSchema PHYLLITE;
    public static final BlockSchema SLATE;
    public static final BlockSchema GABBRO;
    public static final BlockSchema GRANODIORITE;
    public static final BlockSchema RHYOLITE;
    public static final BlockSchema WHITE_GRANITE;
    public static final BlockSchema LIMESTONE;
    public static final BlockSchema BEIGE_LIMESTONE;
    public static final BlockSchema GREY_LIMESTONE;
    public static final BlockSchema SILTSTONE;
    public static final BlockSchema MUDSTONE;
    public static final BlockSchema CONGLOMERATE;
    public static final BlockSchema DACITE;
    public static final BlockSchema SCHIST;


    public static final BlockSchema WEATHERED_RHYOLITE;
    public static final BlockSchema PILLOW_BASALT;
//    public static final BlockSchema PUMICE;
    public static final BlockSchema DOLERITE;
//    public static final BlockSchema MARBLE;
    public static final BlockSchema LIGNITE;
    public static final BlockSchema QUARTZITE;


    public static final BlockSchema GRANITE;
    public static final BlockSchema DIORITE;
    public static final BlockSchema ANDESITE;
    public static final BlockSchema SANDSTONE;
    public static final BlockSchema STONE;

    static {
        List<BlockVariant> basic = create(BASIC);
        List<BlockVariant> stone_like = create(ALL_BLOCKS_PLUS, COBBLED, MOSSY_COBBLED, BRICKS, MOSSY_BRICKS, CRACKED_BRICKS, CHISELED_BRICKS);
        List<BlockVariant> blackstone_like = create(ALL_BLOCKS_PLUS, POLISHED, POLISHED_BRICKS, CHISELED_POLISHED, CRACKED_POLISHED_BRICKS);

        List<BlockVariant> basalt_like = create(PILLAR, POLISHED_PILLAR);
        List<BlockVariant> sandstone_like = create(SEDIMENTARY, SMOOTH, CUT, CHISELED);
        List<BlockVariant> mudstone = create(SEDIMENTARY, CUT, CHISELED_FULL);

        List<BlockVariant> secondary = create(ALL_BASE_BLOCKS, POLISHED);
        List<BlockVariant> decorative = create(BASIC);

        List<BlockVariant> schist = create(Variants.SCHIST);
        List<BlockVariant> limestone = create(ALL_BASE_BLOCKS, COBBLED, BRICKS, PILLAR_BLOCK, CHISELED_FULL);
        List<BlockVariant> beige_limestone = create(SEDIMENTARY, COBBLED, BRICKS, PILLAR_BLOCK, CHISELED);

        List<BlockVariant> intrusive = create(OVERGROWN);

        PHYLLITE = register("phyllite", stone_like, MaterialColor.COLOR_LIGHT_GRAY);
        SLATE = register("slate", stone_like, MaterialColor.COLOR_GRAY);

        GABBRO = register("gabbro", blackstone_like);
        GRANODIORITE = register("granodiorite", blackstone_like, MaterialColor.COLOR_LIGHT_GRAY);
        RHYOLITE = register("rhyolite", blackstone_like, MaterialColor.COLOR_ORANGE);
        WHITE_GRANITE = register("white_granite", blackstone_like, MaterialColor.QUARTZ);
        LIMESTONE = register("limestone", limestone);
        BEIGE_LIMESTONE = register("beige_limestone", beige_limestone, MaterialColor.TERRACOTTA_WHITE);
        GREY_LIMESTONE = register("grey_limestone", limestone, MaterialColor.CLAY);
        SILTSTONE = registerSand("siltstone", sandstone_like, MaterialColor.TERRACOTTA_ORANGE);
        MUDSTONE = registerSand("mudstone", mudstone, MaterialColor.TERRACOTTA_RED);
        CONGLOMERATE = registerSand("conglomerate", secondary, MaterialColor.DIRT);

        QUARTZITE = register("quartzite", basic, MaterialColor.QUARTZ);
        WEATHERED_RHYOLITE = register("weathered_rhyolite", basic);
        DOLERITE = register("dolerite", basic, MaterialColor.COLOR_BROWN);
//        MARBLE = register("marble", decorative, MaterialColor.SNOW);
        SCHIST = register("schist", schist, MaterialColor.COLOR_CYAN);
        LIGNITE = registerSand("lignite", decorative, MaterialColor.COLOR_BLACK);
        PILLOW_BASALT = register("pillow_basalt", basic, stone().strength(3.0f, 6.0f));
//        PUMICE = register("pumice", basic, stone().color(MaterialColor.COLOR_BLACK).sound(SoundType.BASALT));
        DACITE = register("dacite", basalt_like, stone().color(MaterialColor.COLOR_LIGHT_GRAY).strength(1.25f, 4.2F).sound(SoundType.BASALT));

        //vanilla block schemas
        GRANITE = registerDefault("granite", intrusive, Blocks.GRANITE);
        DIORITE = registerDefault("diorite", intrusive, Blocks.DIORITE);
        ANDESITE = registerDefault("andesite", intrusive, Blocks.ANDESITE);
        SANDSTONE = registerDefault("sandstone", List.of(Variants.SANDSTONE), Blocks.SANDSTONE);
        STONE = registerDefault("stone", List.of(Variants.REGOLITHS), Blocks.STONE);
    }

    private static BlockSchema register(String name, List<BlockVariant> variants) {
        return register(name, variants, stoneProperty);
    }

    private static BlockSchema register(String name, List<BlockVariant> variants, MaterialColor color) {
        return register(name, variants, stone().color(color));
    }

    private static BlockSchema registerSand(String name, List<BlockVariant> variants, MaterialColor color) {
        return register(name, variants, sandStone().color(color));
    }

    private static BlockSchema registerDefault(String name, List<BlockVariant> variants, Block defaultBlock) {
        BlockSchema schema = register(name, variants, BlockBehaviour.Properties.copy(defaultBlock));
        schema.setDefaultBlock(defaultBlock);
        return schema;
    }

    private static BlockSchema register(String name, List<BlockVariant> variants, BlockBehaviour.Properties prop) {
        BlockSchema schema = BlockSchema.builder(name, variants).defaultProperty(prop).build();
        ROCK_TYPES.add(schema);
        return schema;
    }

    private static List<BlockVariant> create(BlockVariant... variants) {
        return List.of(variants);
    }

    private static BlockBehaviour.Properties stone() {
        return BlockBehaviour.Properties.copy(dummyBehavior);
    }

    private static BlockBehaviour.Properties sandStone() {
        return BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(0.8f);
    }
}

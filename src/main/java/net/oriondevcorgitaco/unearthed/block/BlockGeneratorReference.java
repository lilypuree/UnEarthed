package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchemas;
import net.oriondevcorgitaco.unearthed.block.schema.StoneClassification;
import net.oriondevcorgitaco.unearthed.block.schema.StoneTiers;

import java.util.ArrayList;
import java.util.List;

import static net.oriondevcorgitaco.unearthed.block.schema.StoneClassification.IGNEOUS;
import static net.oriondevcorgitaco.unearthed.block.schema.StoneClassification.METAMORPHIC;
import static net.oriondevcorgitaco.unearthed.block.schema.StoneClassification.SEDIMENTARY;
import static net.oriondevcorgitaco.unearthed.block.schema.StoneTiers.PRIMARY;
import static net.oriondevcorgitaco.unearthed.block.schema.StoneTiers.SECONDARY;
import static net.oriondevcorgitaco.unearthed.block.schema.StoneTiers.TERTIARY;

public class BlockGeneratorReference {

    public static List<BlockGeneratorHelper> ROCK_TYPES = new ArrayList<>();

    private static float stoneHardness = 1.5f;
    private static float cobbleHardness = 2.0f;
    private static float stoneResistance = 6.0f;
    private static float miscResistance = 0.5f;
    private static AbstractBlock.Properties stoneProperty = AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE).setRequiresTool().hardnessAndResistance(1.5F, 6.0F);

    //igneous
    public static final BlockGeneratorHelper GABBRO;
    public static final BlockGeneratorHelper GRANODIORITE;
    public static final BlockGeneratorHelper WHITE_GRANITE;
    public static final BlockGeneratorHelper RHYOLITE;
    public static final BlockGeneratorHelper WEATHERED_RHYOLITE;
    public static final BlockGeneratorHelper KIMBERLITE;
    public static final BlockGeneratorHelper DACITE;
    public static final BlockGeneratorHelper PUMICE;
    public static final BlockGeneratorHelper DOLERITE;
    public static final BlockGeneratorHelper PILLOW_BASALT;
    //metamorphic
    public static final BlockGeneratorHelper PHYLLITE;
    public static final BlockGeneratorHelper SLATE;
    public static final BlockGeneratorHelper QUARTZITE;
    public static final BlockGeneratorHelper DOLOMITE;
    public static final BlockGeneratorHelper SCHIST;
    public static final BlockGeneratorHelper MARBLE;
    //sedimentary
    public static final BlockGeneratorHelper BEIGE_LIMESTONE;
    public static final BlockGeneratorHelper LIMESTONE;
    public static final BlockGeneratorHelper GREY_LIMESTONE;
    public static final BlockGeneratorHelper CONGLOMERATE;
    public static final BlockGeneratorHelper SILTSTONE;
    public static final BlockGeneratorHelper MUDSTONE;
    public static final BlockGeneratorHelper LIGNITE;

    public static void init() {
    }

    static {
        PHYLLITE = new BlockGeneratorHelper.Builder("phyllite", BlockSchemas.STONE_LIKE).setTier(PRIMARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance))
                .createCobbleProperties().createMiscProperties().createOreProperties().build();
        SLATE = new BlockGeneratorHelper.Builder("slate", BlockSchemas.STONE_LIKE).setTier(PRIMARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance))
                .createCobbleProperties().createMiscProperties().createOreProperties().build();

        QUARTZITE = new BlockGeneratorHelper.Builder("quartzite", BlockSchemas.BLACKSTONE_LIKE).setTier(PRIMARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.QUARTZ).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance))
                .createMiscProperties().createOreProperties().build();
        GABBRO = new BlockGeneratorHelper.Builder("gabbro", BlockSchemas.BLACKSTONE_LIKE).setTier(PRIMARY).setClassification(IGNEOUS)
                .defaultProperty(stoneProperty).createMiscProperties().createOreProperties().build();
        GRANODIORITE = new BlockGeneratorHelper.Builder("granodiorite", BlockSchemas.BLACKSTONE_LIKE).setTier(PRIMARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance))
                .createMiscProperties().createOreProperties().build();
        WHITE_GRANITE = new BlockGeneratorHelper.Builder("white_granite", BlockSchemas.BLACKSTONE_LIKE).setTier(PRIMARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.from(Blocks.DIORITE))
                .createMiscProperties().createOreProperties().build();
        RHYOLITE = new BlockGeneratorHelper.Builder("rhyolite", BlockSchemas.BLACKSTONE_LIKE).setTier(PRIMARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.ADOBE).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance))
                .createMiscProperties().createOreProperties().build();

        DOLOMITE = new BlockGeneratorHelper.Builder("dolomite", BlockSchemas.SECONDARY).setTier(SECONDARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.CLAY).hardnessAndResistance(stoneHardness, stoneResistance))
                .createOreProperties().build();
        CONGLOMERATE = new BlockGeneratorHelper.Builder("conglomerate", BlockSchemas.SECONDARY).setTier(SECONDARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.DIRT).setRequiresTool().hardnessAndResistance(0.8f))
                .createOreProperties().build();

        BEIGE_LIMESTONE = new BlockGeneratorHelper.Builder("beige_limestone", BlockSchemas.BEIGE_LIMESTONE).setTier(SECONDARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance))
                .createCobbleProperties().createOreProperties().build();
        LIMESTONE = new BlockGeneratorHelper.Builder("limestone", BlockSchemas.LIMESTONE).setTier(PRIMARY).setClassification(SEDIMENTARY)
                .defaultProperty(stoneProperty).createCobbleProperties().createOreProperties().build();
        GREY_LIMESTONE = new BlockGeneratorHelper.Builder("grey_limestone", BlockSchemas.LIMESTONE).setTier(PRIMARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.CLAY).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance))
                .createCobbleProperties().createOreProperties().build();

        SILTSTONE = new BlockGeneratorHelper.Builder("siltstone", BlockSchemas.SANDSTONE_LIKE).setTier(SECONDARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA).setRequiresTool().hardnessAndResistance(0.8f))
                .createOreProperties().build();
        MUDSTONE = new BlockGeneratorHelper.Builder("mudstone", BlockSchemas.SANDSTONE_LIKE).setTier(SECONDARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).setRequiresTool().hardnessAndResistance(0.8f))
                .createOreProperties().build();

        KIMBERLITE = new BlockGeneratorHelper.Builder("kimberlite", BlockSchemas.KIMBERLITE).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GREEN).setRequiresTool().hardnessAndResistance(4.0f, 8.0f)).build();
        LIGNITE = new BlockGeneratorHelper.Builder("lignite", BlockSchemas.DECORATIVE).setTier(TERTIARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BLACK).setRequiresTool().hardnessAndResistance(0.8f)).build();

        DACITE = new BlockGeneratorHelper.Builder("dacite", BlockSchemas.BASALT_LIKE).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY_TERRACOTTA).setRequiresTool().hardnessAndResistance(1.25F, 4.2F).sound(SoundType.BASALT)).build();
        SCHIST = new BlockGeneratorHelper.Builder("schist", BlockSchemas.SCHIST).setTier(TERTIARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(stoneHardness, stoneResistance)).build();

        PUMICE = new BlockGeneratorHelper.Builder("pumice", BlockSchemas.BASIC).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BLACK).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance).sound(SoundType.BASALT)).build();
        DOLERITE = new BlockGeneratorHelper.Builder("dolerite", BlockSchemas.BASIC).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BROWN).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).build();
        MARBLE = new BlockGeneratorHelper.Builder("marble", BlockSchemas.DECORATIVE).setTier(TERTIARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.SNOW).setRequiresTool().hardnessAndResistance(2.0f, 6.0f)).build();

        WEATHERED_RHYOLITE = new BlockGeneratorHelper.Builder("weathered_rhyolite", BlockSchemas.DECORATIVE).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(stoneProperty).build();
        PILLOW_BASALT = new BlockGeneratorHelper.Builder("pillow_basalt", BlockSchemas.BASIC).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE).setRequiresTool().hardnessAndResistance(3.0f, 6.0f)).build();
    }

    public static final ITag.INamedTag<Block> IRON_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "iron_ores").toString());
    public static final ITag.INamedTag<Block> COAL_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "coal_ores").toString());
    public static final ITag.INamedTag<Block> GOLD_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "gold_ores").toString());
    public static final ITag.INamedTag<Block> LAPIS_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "lapis_ores").toString());
    public static final ITag.INamedTag<Block> REDSTONE_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "redstone_ores").toString());
    public static final ITag.INamedTag<Block> DIAMOND_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "diamond_ores").toString());
    public static final ITag.INamedTag<Block> EMERALD_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "emerald_ores").toString());
    public static final ITag.INamedTag<Block> REGOLITH_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "regolith").toString());

    public static final ITag.INamedTag<Block> SEDIMENTARY_TAG = BlockTags.makeWrapperTag(Unearthed.MOD_ID + ":sedimentary");
    public static final ITag.INamedTag<Block> IGNEOUS_TAG = BlockTags.makeWrapperTag(Unearthed.MOD_ID + ":igneous");
    public static final ITag.INamedTag<Block> METAMORPHIC_TAG = BlockTags.makeWrapperTag(Unearthed.MOD_ID + ":metamorphic");

    public static final ITag.INamedTag<Item> SEDIMENTARY_ITEM = ItemTags.makeWrapperTag(Unearthed.MOD_ID + ":sedimentary");
    public static final ITag.INamedTag<Item> IGNEOUS_ITEM = ItemTags.makeWrapperTag(Unearthed.MOD_ID + ":igneous");
    public static final ITag.INamedTag<Item> METAMORPHIC_ITEM = ItemTags.makeWrapperTag(Unearthed.MOD_ID + ":metamorphic");

}

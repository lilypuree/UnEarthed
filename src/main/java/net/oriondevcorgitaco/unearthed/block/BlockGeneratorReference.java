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

public class BlockGeneratorReference {

    public static List<BlockGeneratorHelper> ROCK_TYPES = new ArrayList<>();

    private static float stoneHardness = 1.5f;
    private static float cobbleHardness = 2.0f;
    private static float stoneResistance = 6.0f;
    private static float miscResistance = 0.5f;
    private static AbstractBlock.Properties stoneProperty = AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE).setRequiresTool().hardnessAndResistance(1.5F, 6.0F);
    //igneous


    public static final BlockGeneratorHelper PILLOW_BASALT = new BlockGeneratorHelper.Builder("pillow_basalt", BlockSchemas.BASIC).setTier(StoneTiers.TERTIARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.STONE).setRequiresTool().hardnessAndResistance(3.0f, 6.0f)).build();
    public static final BlockGeneratorHelper PUMICE = new BlockGeneratorHelper.Builder("pumice", BlockSchemas.BASIC).setTier(StoneTiers.TERTIARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BLACK).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance).sound(SoundType.BASALT)).build();
    public static final BlockGeneratorHelper DOLERITE = new BlockGeneratorHelper.Builder("dolerite", BlockSchemas.BASIC).setTier(StoneTiers.TERTIARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BROWN).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).build();
    public static final BlockGeneratorHelper KIMBERLITE = new BlockGeneratorHelper.Builder("kimberlite", BlockSchemas.BASIC).setTier(StoneTiers.TERTIARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GREEN).setRequiresTool().hardnessAndResistance(4.0f, 8.0f)).build();
    public static final BlockGeneratorHelper GABBRO = new BlockGeneratorHelper.Builder("gabbro", BlockSchemas.BLACKSTONE_LIKE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(stoneProperty).createMiscProperties().createOreProperties().build();
    public static final BlockGeneratorHelper GRANODIORITE = new BlockGeneratorHelper.Builder("granodiorite", BlockSchemas.BLACKSTONE_LIKE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).createMiscProperties().createOreProperties().build();
    public static final BlockGeneratorHelper WHITE_GRANITE = new BlockGeneratorHelper.Builder("white_granite", BlockSchemas.BLACKSTONE_LIKE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(AbstractBlock.Properties.from(Blocks.DIORITE)).createMiscProperties().createOreProperties().build();
    public static final BlockGeneratorHelper RHYOLITE = new BlockGeneratorHelper.Builder("rhyolite", BlockSchemas.BLACKSTONE_LIKE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.ADOBE).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).createOreProperties().build();
    public static final BlockGeneratorHelper WEATHERED_RHYOLITE = new BlockGeneratorHelper.Builder("weathered_rhyolite", BlockSchemas.DECORATIVE).setTier(StoneTiers.TERTIARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(stoneProperty).build();
    public static final BlockGeneratorHelper DACITE = new BlockGeneratorHelper.Builder("dacite", BlockSchemas.BASALT_LIKE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.IGNEOUS).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY_TERRACOTTA).setRequiresTool().hardnessAndResistance(1.25F, 4.2F).sound(SoundType.BASALT)).build();

    //metamorphic

    public static final BlockGeneratorHelper SLATE = new BlockGeneratorHelper.Builder("slate", BlockSchemas.STONE_LIKE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.METAMORPHIC).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.GRAY).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).createCobbleProperties().createMiscProperties().createOreProperties().build();
    public static final BlockGeneratorHelper PHYLLITE = new BlockGeneratorHelper.Builder("phyllite", BlockSchemas.STONE_LIKE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.METAMORPHIC).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.LIGHT_GRAY).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).createCobbleProperties().createMiscProperties().createOreProperties().build();
    public static final BlockGeneratorHelper QUARTZITE = new BlockGeneratorHelper.Builder("quartzite", BlockSchemas.BLACKSTONE_LIKE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.METAMORPHIC).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.QUARTZ).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).createMiscProperties().createOreProperties().build();
    public static final BlockGeneratorHelper MARBLE = new BlockGeneratorHelper.Builder("marble", BlockSchemas.DECORATIVE).setTier(StoneTiers.TERTIARY).setClassification(StoneClassification.METAMORPHIC).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.SNOW).setRequiresTool().hardnessAndResistance(2.0f, 6.0f)).build();
    public static final BlockGeneratorHelper DOLOMITE = new BlockGeneratorHelper.Builder("dolomite", BlockSchemas.SECONDARY).setTier(StoneTiers.SECONDARY).setClassification(StoneClassification.METAMORPHIC).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.CLAY).hardnessAndResistance(stoneHardness, stoneResistance)).createOreProperties().build();
    public static final BlockGeneratorHelper SCHIST = new BlockGeneratorHelper.Builder("schist", BlockSchemas.SCHIST).setTier(StoneTiers.TERTIARY).setClassification(StoneClassification.METAMORPHIC).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(stoneHardness, stoneResistance)).build();

    //sedimentary

    public static final BlockGeneratorHelper BEIGE_LIMESTONE = new BlockGeneratorHelper.Builder("beige_limestone", BlockSchemas.BEIGE_LIMESTONE).setTier(StoneTiers.SECONDARY).setClassification(StoneClassification.SEDIMENTARY).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).createCobbleProperties().createOreProperties().build();
    public static final BlockGeneratorHelper LIMESTONE = new BlockGeneratorHelper.Builder("limestone", BlockSchemas.LIMESTONE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.SEDIMENTARY).defaultProperty(stoneProperty).createCobbleProperties().createOreProperties().build();
    public static final BlockGeneratorHelper GREY_LIMESTONE = new BlockGeneratorHelper.Builder("grey_limestone", BlockSchemas.LIMESTONE).setTier(StoneTiers.PRIMARY).setClassification(StoneClassification.SEDIMENTARY).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.CLAY).setRequiresTool().hardnessAndResistance(stoneHardness, stoneResistance)).createCobbleProperties().createOreProperties().build();
    public static final BlockGeneratorHelper LIGNITE = new BlockGeneratorHelper.Builder("lignite", BlockSchemas.DECORATIVE).setTier(StoneTiers.TERTIARY).setClassification(StoneClassification.SEDIMENTARY).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.BLACK).setRequiresTool().hardnessAndResistance(0.8f)).build();
    public static final BlockGeneratorHelper CONGLOMERATE = new BlockGeneratorHelper.Builder("conglomerate", BlockSchemas.SECONDARY).setTier(StoneTiers.SECONDARY).setClassification(StoneClassification.SEDIMENTARY).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.DIRT).setRequiresTool().hardnessAndResistance(0.8f)).createOreProperties().build();
    public static final BlockGeneratorHelper SILTSTONE = new BlockGeneratorHelper.Builder("siltstone", BlockSchemas.SANDSTONE_LIKE).setTier(StoneTiers.SECONDARY).setClassification(StoneClassification.SEDIMENTARY).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.ORANGE_TERRACOTTA).setRequiresTool().hardnessAndResistance(0.8f)).createOreProperties().build();
    public static final BlockGeneratorHelper MUDSTONE = new BlockGeneratorHelper.Builder("mudstone", BlockSchemas.SANDSTONE_LIKE).setTier(StoneTiers.SECONDARY).setClassification(StoneClassification.SEDIMENTARY).defaultProperty(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.RED_TERRACOTTA).setRequiresTool().hardnessAndResistance(0.8f)).createOreProperties().build();

    public static void init() {
    }

    public static final ITag.INamedTag<Block> IRON_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "iron_ores").toString());
    public static final ITag.INamedTag<Block> COAL_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "coal_ores").toString());
    public static final ITag.INamedTag<Block> GOLD_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "gold_ores").toString());
    public static final ITag.INamedTag<Block> LAPIS_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "lapis_ores").toString());
    public static final ITag.INamedTag<Block> REDSTONE_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "redstone_ores").toString());
    public static final ITag.INamedTag<Block> DIAMOND_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "diamond_ores").toString());
    public static final ITag.INamedTag<Block> EMERALD_ORE_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "emerald_ores").toString());
    public static final ITag.INamedTag<Block> REGOLITH_TAG = BlockTags.makeWrapperTag(new ResourceLocation(Unearthed.MOD_ID, "regolith").toString());

    public static final ITag.INamedTag<Block> SEDIMENTARY = BlockTags.makeWrapperTag(Unearthed.MOD_ID + ":sedimentary");
    public static final ITag.INamedTag<Block> IGNEOUS = BlockTags.makeWrapperTag(Unearthed.MOD_ID + ":igneous");
    public static final ITag.INamedTag<Block> METAMORPHIC = BlockTags.makeWrapperTag(Unearthed.MOD_ID + ":metamorphic");

    public static final ITag.INamedTag<Item> SEDIMENTARY_ITEM = ItemTags.makeWrapperTag(Unearthed.MOD_ID + ":sedimentary");
    public static final ITag.INamedTag<Item> IGNEOUS_ITEM = ItemTags.makeWrapperTag(Unearthed.MOD_ID + ":igneous");
    public static final ITag.INamedTag<Item> METAMORPHIC_ITEM = ItemTags.makeWrapperTag(Unearthed.MOD_ID + ":metamorphic");

}

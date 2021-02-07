package net.lilycorgitaco.unearthed.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.sound.BlockSoundGroup;
import net.lilycorgitaco.unearthed.block.schema.BlockSchema;
import net.lilycorgitaco.unearthed.block.schema.BlockSchemas;
import net.lilycorgitaco.unearthed.block.schema.Forms;
import net.lilycorgitaco.unearthed.block.schema.Variants;

import java.util.ArrayList;
import java.util.List;

import static net.lilycorgitaco.unearthed.block.schema.StoneClassification.*;
import static net.lilycorgitaco.unearthed.block.schema.StoneTiers.*;

public class BlockGeneratorReference {

    public static List<BlockGeneratorHelper> ROCK_TYPES = new ArrayList<>();

    private static float stoneHardness = 1.5f;
    private static float cobbleHardness = 2.0f;
    private static float stoneResistance = 6.0f;
    private static float miscResistance = 0.5f;
    private static AbstractBlock.Settings stoneProperty = AbstractBlock.Settings.of(Material.STONE, MaterialColor.STONE).requiresTool().strength(1.5F, 6.0F);

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

    //vanilla
    public static final BlockGeneratorHelper GRANITE;
    public static final BlockGeneratorHelper DIORITE;
    public static final BlockGeneratorHelper ANDESITE;
    public static final BlockGeneratorHelper SANDSTONE;
    public static final BlockGeneratorHelper STONE;

    public static void init() {
        for (BlockGeneratorHelper schema : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : schema.getEntries()) {
                if (entry.getForm() == Forms.REGOLITH) {
                    LichenBlock.addErosionMap(schema.getBaseBlock(), entry.getBlock());
                }
            }
        }
    }

    static {
        PHYLLITE = new BlockGeneratorHelper.Builder("phyllite", BlockSchemas.STONE_LIKE).setTier(PRIMARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.LIGHT_GRAY).requiresTool().strength(stoneHardness, stoneResistance))
                .createCobbleProperties().createMiscProperties().createOreProperties().build();
        SLATE = new BlockGeneratorHelper.Builder("slate", BlockSchemas.STONE_LIKE).setTier(PRIMARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.GRAY).requiresTool().strength(stoneHardness, stoneResistance))
                .createCobbleProperties().createMiscProperties().createOreProperties().build();

        QUARTZITE = new BlockGeneratorHelper.Builder("quartzite", BlockSchemas.BLACKSTONE_LIKE).setTier(PRIMARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.QUARTZ).requiresTool().strength(stoneHardness, stoneResistance))
                .createMiscProperties().createOreProperties().build();
        GABBRO = new BlockGeneratorHelper.Builder("gabbro", BlockSchemas.BLACKSTONE_LIKE_FULL).setTier(PRIMARY).setClassification(IGNEOUS)
                .defaultProperty(stoneProperty).createMiscProperties().createOreProperties().build();
        GRANODIORITE = new BlockGeneratorHelper.Builder("granodiorite", BlockSchemas.BLACKSTONE_LIKE_FULL).setTier(PRIMARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.LIGHT_GRAY).requiresTool().strength(stoneHardness, stoneResistance))
                .createMiscProperties().createOreProperties().build();
        WHITE_GRANITE = new BlockGeneratorHelper.Builder("white_granite", BlockSchemas.BLACKSTONE_LIKE_FULL).setTier(PRIMARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.copy(Blocks.DIORITE))
                .createMiscProperties().createOreProperties().build();
        RHYOLITE = new BlockGeneratorHelper.Builder("rhyolite", BlockSchemas.BLACKSTONE_LIKE_FULL).setTier(PRIMARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.ORANGE).requiresTool().strength(stoneHardness, stoneResistance))
                .createMiscProperties().createOreProperties().build();

        DOLOMITE = new BlockGeneratorHelper.Builder("dolomite", BlockSchemas.SECONDARY).setTier(SECONDARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.CLAY).strength(stoneHardness, stoneResistance))
                .createOreProperties().build();
        CONGLOMERATE = new BlockGeneratorHelper.Builder("conglomerate", BlockSchemas.SECONDARY).setTier(SECONDARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.DIRT).requiresTool().strength(0.8f))
                .createOreProperties().build();

        BEIGE_LIMESTONE = new BlockGeneratorHelper.Builder("beige_limestone", BlockSchemas.BEIGE_LIMESTONE).setTier(SECONDARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.WHITE_TERRACOTTA).requiresTool().strength(stoneHardness, stoneResistance))
                .createCobbleProperties().createOreProperties().build();
        LIMESTONE = new BlockGeneratorHelper.Builder("limestone", BlockSchemas.LIMESTONE_FULL).setTier(PRIMARY).setClassification(SEDIMENTARY)
                .defaultProperty(stoneProperty).createCobbleProperties().createOreProperties().build();
        GREY_LIMESTONE = new BlockGeneratorHelper.Builder("grey_limestone", BlockSchemas.LIMESTONE_FULL).setTier(PRIMARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.CLAY).requiresTool().strength(stoneHardness, stoneResistance))
                .createCobbleProperties().createOreProperties().build();

        SILTSTONE = new BlockGeneratorHelper.Builder("siltstone", BlockSchemas.SANDSTONE_LIKE_FULL).setTier(SECONDARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.ORANGE_TERRACOTTA).requiresTool().strength(0.8f))
                .createOreProperties().build();
        MUDSTONE = new BlockGeneratorHelper.Builder("mudstone", new BlockSchema(Variants.SEDIMENTARY, Variants.CUT, Variants.CHISELED_FULL)).setTier(SECONDARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.RED_TERRACOTTA).requiresTool().strength(0.8f))
                .createOreProperties().build();

        KIMBERLITE = new BlockGeneratorHelper.Builder("kimberlite", BlockSchemas.KIMBERLITE).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.GREEN).requiresTool().strength(4.0f, 8.0f)).build();
        LIGNITE = new BlockGeneratorHelper.Builder("lignite", BlockSchemas.DECORATIVE).setTier(TERTIARY).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.BLACK).requiresTool().strength(0.8f)).build();

        DACITE = new BlockGeneratorHelper.Builder("dacite", BlockSchemas.BASALT_LIKE).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.LIGHT_GRAY_TERRACOTTA).requiresTool().strength(1.25F, 4.2F).sounds(BlockSoundGroup.BASALT)).build();
        SCHIST = new BlockGeneratorHelper.Builder("schist", BlockSchemas.SCHIST).setTier(TERTIARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.CYAN).strength(stoneHardness, stoneResistance)).build();

        PUMICE = new BlockGeneratorHelper.Builder("pumice", BlockSchemas.BASIC).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.BLACK).requiresTool().strength(stoneHardness, stoneResistance).sounds(BlockSoundGroup.BASALT)).build();
        DOLERITE = new BlockGeneratorHelper.Builder("dolerite", BlockSchemas.BASIC).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.BROWN).requiresTool().strength(stoneHardness, stoneResistance)).build();
        MARBLE = new BlockGeneratorHelper.Builder("marble", BlockSchemas.DECORATIVE).setTier(TERTIARY).setClassification(METAMORPHIC)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.WHITE).requiresTool().strength(2.0f, 6.0f)).build();

        WEATHERED_RHYOLITE = new BlockGeneratorHelper.Builder("weathered_rhyolite", BlockSchemas.BASIC).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(stoneProperty).build();
        PILLOW_BASALT = new BlockGeneratorHelper.Builder("pillow_basalt", BlockSchemas.BASIC).setTier(TERTIARY).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.of(Material.STONE, MaterialColor.STONE).requiresTool().strength(3.0f, 6.0f)).build();

        GRANITE = new BlockGeneratorHelper.Builder("granite", BlockSchemas.INTRUSIVE).setTier(VANILLA).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.copy(Blocks.GRANITE)).createOreProperties().build();
        GRANITE.setDefaultBlock(Blocks.GRANITE);
        DIORITE = new BlockGeneratorHelper.Builder("diorite", BlockSchemas.INTRUSIVE).setTier(VANILLA).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.copy(Blocks.DIORITE)).createOreProperties().build();
        DIORITE.setDefaultBlock(Blocks.DIORITE);
        ANDESITE = new BlockGeneratorHelper.Builder("andesite", BlockSchemas.INTRUSIVE).setTier(VANILLA).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Settings.copy(Blocks.ANDESITE)).createOreProperties().build();
        ANDESITE.setDefaultBlock(Blocks.ANDESITE);
        SANDSTONE = new BlockGeneratorHelper.Builder("sandstone", new BlockSchema(Variants.SANDSTONE)).setTier(VANILLA).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Settings.copy(Blocks.SANDSTONE)).createOreProperties().createRegolithProperties().build();
        SANDSTONE.setDefaultBlock(Blocks.SANDSTONE);
        STONE = new BlockGeneratorHelper.Builder("stone", new BlockSchema(Variants.REGOLITHS)).setTier(VANILLA).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Settings.copy(Blocks.STONE)).createRegolithProperties().build();
        STONE.setDefaultBlock(Blocks.STONE);
    }
}

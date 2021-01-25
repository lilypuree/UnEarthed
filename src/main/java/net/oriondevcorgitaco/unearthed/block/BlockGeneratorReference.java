package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchemas;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;

import java.util.ArrayList;
import java.util.List;

import static net.oriondevcorgitaco.unearthed.block.schema.StoneClassification.*;
import static net.oriondevcorgitaco.unearthed.block.schema.StoneTiers.*;

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

        GRANITE = new BlockGeneratorHelper.Builder("granite", BlockSchemas.INTRUSIVE).setTier(VANILLA).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.from(Blocks.GRANITE)).createOreProperties().build();
        GRANITE.setDefaultBlock(Blocks.GRANITE);
        DIORITE = new BlockGeneratorHelper.Builder("diorite", BlockSchemas.INTRUSIVE).setTier(VANILLA).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.from(Blocks.DIORITE)).createOreProperties().build();
        DIORITE.setDefaultBlock(Blocks.DIORITE);
        ANDESITE = new BlockGeneratorHelper.Builder("andesite", BlockSchemas.INTRUSIVE).setTier(VANILLA).setClassification(IGNEOUS)
                .defaultProperty(AbstractBlock.Properties.from(Blocks.ANDESITE)).createOreProperties().build();
        ANDESITE.setDefaultBlock(Blocks.ANDESITE);
        SANDSTONE = new BlockGeneratorHelper.Builder("sandstone", new BlockSchema(Variants.SANDSTONE)).setTier(VANILLA).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Properties.from(Blocks.SANDSTONE)).createOreProperties().createRegolithProperties().build();
        SANDSTONE.setDefaultBlock(Blocks.SANDSTONE);
        STONE = new BlockGeneratorHelper.Builder("stone", new BlockSchema(Variants.REGOLITHS)).setTier(VANILLA).setClassification(SEDIMENTARY)
                .defaultProperty(AbstractBlock.Properties.from(Blocks.STONE)).createRegolithProperties().build();
        STONE.setDefaultBlock(Blocks.STONE);
    }
}

package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.regions;

import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.*;

import static net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.Cells.*;

public class Regions {
    public static Region DEFAULT;
    public static Region LIMESTONE_REGION;
    public static Region SEDIMENTARY;

    static {
        DEFAULT = new RegionBuilder()
                .addPrimary(STONE, GABBRO, SLATE, LIMESTONE)
                .addOrogen(GABBRO, SLATE, SCHIST_X, GABBRO, WEATHERED_RHYOLITE, SCHIST_Y, SLATE, GABBRO, SCHIST_Z, PUMICE, DOLOMITE)
                .setSecondary(DOLOMITE)
                .addStrata(DACITE, LIGNITE, DOLERITE)
                .setStrataParams(0.2f, 5, 2)
                .setOreProbability(0.001f, 0.002f, 0)
                .addRegolith(2, 1)
                .addOre(1, new BasicReplacer(PHYLLITE, STONE), 1)
                .addOre(1, new BasicReplacer(GRANODIORITE, DOLOMITE), 1)
                .addOre(1, new BasicReplacer(DOLOMITE, LIMESTONE), 1)
                .build();

        LIMESTONE_REGION = new RegionBuilder()
                .addPrimary(STONE, LIMESTONE, GRANODIORITE, GREY_LIMESTONE, PHYLLITE)
                .addOrogen(SLATE, QUARTZITE, GRANODIORITE, GREY_LIMESTONE, DOLOMITE, WEATHERED_GRANITE)
                .setSecondary(BEIGE_LIMESTONE)
                .addStrata(DACITE, MARBLE, LIGNITE)
                .setStrataParams(0.15f, 5, 2)
                .setOreProbability(0.001f, 0.002f, 0.0001f)
                .addRegolith(2, 1)
                .addOre(1, new BasicReplacer(ANDESITE, STONE), 5)
                .addOre(1, new BasicReplacer(MARBLE, GRANODIORITE), 1)
                .addOre(1, new BasicReplacer(BEIGE_LIMESTONE, LIMESTONE), 5)
                .addOre(2, new BasicReplacer(BEIGE_LIMESTONE, LIMESTONE), 1)
                .addOre(3, new BasicReplacer(BEIGE_LIMESTONE, LIMESTONE), 1)
                .addOre(1, new BasicReplacer(LIMESTONE, GREY_LIMESTONE), 5)
                .addOre(1, new BasicReplacer(WHITE_GRANITE, BEIGE_LIMESTONE), 5)
                .addOre(2, new BasicReplacer(WHITE_GRANITE, BEIGE_LIMESTONE), 1)
                .addOre(3, new BasicReplacer(WHITE_GRANITE, BEIGE_LIMESTONE), 1)
                .build();

        SEDIMENTARY = new RegionBuilder()
                .addPrimary(BEIGE_LIMESTONE, SILTSTONE, RHYOLITE, SANDSTONE, WHITE_GRANITE)
                .addOrogen(QUARTZITE, DACITE, QUARTZITE, SCHIST_X, DACITE, GREY_LIMESTONE, STONE, GREY_LIMESTONE, CONGLOMERATE, CONGLOMERATE, SCHIST_Z, SCHIST_Y)
                .setSecondary(MUDSTONE)
                .addStrata(SCHIST_Y, LIGNITE, DACITE, SILTSTONE, CONGLOMERATE, LIGNITE, BEIGE_LIMESTONE)
                .setStrataParams(0.1f, 5, 1)
                .setOreProbability(0.004f, 0.002f, 0)
                .addRegolith(2, 3)
                .addOre(1, new BasicReplacer(SMOOTH_SANDSTONE, SANDSTONE), 10)
                .addOre(1, new BasicReplacer(TERRACOTTA, MUDSTONE), 1)
                .addOre(1, new BasicReplacer(BEIGE_LIMESTONE, SILTSTONE), 1)
                .build();
    }
}

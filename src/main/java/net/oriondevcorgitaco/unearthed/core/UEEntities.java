package net.oriondevcorgitaco.unearthed.core;

import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.oriondevcorgitaco.unearthed.planets.block.PlanetLavaTile;
import net.oriondevcorgitaco.unearthed.planets.entity.AsteroidEntity;
import net.oriondevcorgitaco.unearthed.planets.entity.CloudEntity;
import net.oriondevcorgitaco.unearthed.planets.planetcore.MantleCoreTile;

public class UEEntities {
    public static EntityType<CloudEntity> CLOUD;
    public static EntityType<AsteroidEntity> ASTEROID;

    public static TileEntityType<MantleCoreTile> MANTLE_CORE;
    public static TileEntityType<PlanetLavaTile> PLANET_LAVA;
}

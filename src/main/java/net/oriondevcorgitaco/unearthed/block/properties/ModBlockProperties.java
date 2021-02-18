package net.oriondevcorgitaco.unearthed.block.properties;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.oriondevcorgitaco.unearthed.planets.block.SurfaceTypes;

public class ModBlockProperties {
    public static DirectionProperty SECONDARY_FACING = DirectionProperty.create("secondary_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static BooleanProperty WET = BooleanProperty.create("wet");
    public static BooleanProperty TRANSIENT = BooleanProperty.create("transient");
    public static EnumProperty<SurfaceTypes> SURFACE_TYPE = EnumProperty.create("surface_type", SurfaceTypes.class);
}

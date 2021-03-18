package net.lilycorgitaco.unearthed.block.properties;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;

public class ModBlockProperties {
    public static DirectionProperty SECONDARY_FACING = DirectionProperty.of("secondary_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static BooleanProperty WET = BooleanProperty.of("wet");
    public static BooleanProperty TRANSIENT = BooleanProperty.of("transient");
}

package net.oriondevcorgitaco.unearthed.block.properties;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.util.Direction;

public class ModBlockProperties {
    public static DirectionProperty SECONDARY_FACING = DirectionProperty.create("secondary_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
    public static BooleanProperty WET = BooleanProperty.create("wet");
    public static BooleanProperty TRANSIENT = BooleanProperty.create("transient");
}

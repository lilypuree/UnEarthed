package net.oriondevcorgitaco.unearthed.planets.block;

import net.minecraft.util.IStringSerializable;

public enum SurfaceTypes implements IStringSerializable {
    DESERT("desert"), PLAIN("plain"), FOREST("forest"), POLAR("polar");

    private String name;

    private SurfaceTypes(String name){
        this.name = name;
    }

    @Override
    public String getString() {
        return name;
    }
}

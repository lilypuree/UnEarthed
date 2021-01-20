package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.regions;

import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.States;

public class Regions {
    private static Region REGION = new Region();
    public static Region getRegion(float value) {
        return REGION;
    }

    public static State getBatolithState(float qf, float ap){
        return States.GOLD;
    }

    public static State getTertiaryState(float level){
        return States.COAL;
    }
}

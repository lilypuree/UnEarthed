package net.lilycorgitaco.unearthed.world.feature.stonegenerators.data;

import static net.lilycorgitaco.unearthed.world.feature.stonegenerators.data.Cells.*;

public class States {
    public static CellularOre LARGE = new BasicReplacer(NETHERITE, null) {
        @Override
        public boolean canReplace(State existing) {
            return true;
        }
    };
    public static CellularOre MID = new BasicReplacer(LAPIS, null) {
        @Override
        public boolean canReplace(State existing) {
            return true;
        }
    };
    public static CellularOre SMALL = new BasicReplacer(REDSTONE, null) {
        @Override
        public boolean canReplace(State existing) {
            return true;
        }
    };
}

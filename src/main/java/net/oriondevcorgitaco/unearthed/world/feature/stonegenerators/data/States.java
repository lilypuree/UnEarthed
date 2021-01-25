package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.Blocks;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;

import static net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.Cells.*;

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

package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.Blocks;
import net.minecraftforge.common.Tags;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.StoneWrapper;

public class States {
    public static final State GLASS = new BaseState(Type.OROGEN, Blocks.GLASS.getDefaultState());
    public static final State EMERALD = new BaseState(Type.OROGEN, Blocks.EMERALD_BLOCK.getDefaultState());
    public static final State DIAMOND = new BaseState(Type.STRATA, Blocks.DIAMOND_BLOCK.getDefaultState());
    public static final State GOLD = new BaseState(Type.BATOLITH, Blocks.GOLD_BLOCK.getDefaultState());
    public static final State IRON = new BaseState(Type.SECONDARY, Blocks.IRON_BLOCK.getDefaultState());
    public static final State NETHERITE = new BaseState(Type.TERTIARY, Blocks.NETHERITE_BLOCK.getDefaultState());
    public static final State LAPIS = new BaseState(Type.TERTIARY, Blocks.LAPIS_BLOCK.getDefaultState());
    public static final State REDSTONE = new BaseState(Type.TERTIARY, Blocks.REDSTONE_BLOCK.getDefaultState());
    public static final State COAL = new BaseState(Type.TERTIARY, Blocks.COAL_BLOCK.getDefaultState());

    public static final State STONE = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.STONE));
    public static final State GABBRO = new StoneState(Type.PRIMARY, getWrapper(BlockGeneratorReference.GABBRO));
//    public static final State GRANODIORITE =

    private static StoneWrapper getWrapper(BlockGeneratorHelper dolomite) {
        return StoneWrapper.fromGenerationHelper(dolomite);
    }

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

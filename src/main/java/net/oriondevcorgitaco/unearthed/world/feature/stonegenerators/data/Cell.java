package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;

import java.util.Map;

public class Cell {
    private final BlockState baseBlock;
    private final BlockState cobbleBlock;
    private final BlockState dirtReplacement;
    private final BlockState grassReplacement;
    private BlockState defaultOreReplacement = null;
    private final Map<IOreType, BlockState> oreMap;

    public Cell(BlockState block, BlockState cobble, BlockState dirt, BlockState grass, Map<IOreType, BlockState> oreMap) {
        this.baseBlock = block;
        this.cobbleBlock = cobble;
        this.dirtReplacement = dirt;
        this.grassReplacement = grass;
        this.oreMap = oreMap;
    }

    public Cell(BlockState defaultBlock, boolean replaceOres, boolean replaceRest) {
        this.baseBlock = defaultBlock;
        this.oreMap = null;
        if (replaceOres) {
            this.defaultOreReplacement = defaultBlock;
        }
        if (replaceRest) {
            cobbleBlock = defaultBlock;
            dirtReplacement = defaultBlock;
            grassReplacement = Blocks.GRASS_BLOCK.getDefaultState();
        }else {
            cobbleBlock = Blocks.COBBLESTONE.getDefaultState();
            dirtReplacement = Blocks.DIRT.getDefaultState();
            grassReplacement = Blocks.GRASS_BLOCK.getDefaultState();
        }
    }

    public BlockState getDefaultState() {
        return baseBlock;
    }

    public BlockState getCobbleReplacement() {
        return cobbleBlock;
    }

    public BlockState getDirtReplacement() {
        return dirtReplacement;
    }

    public BlockState getGrassReplacementDefault() {
        return grassReplacement;
    }

    public boolean replacesOre() {
        return oreMap != null || defaultOreReplacement != null;
    }

    public BlockState getOre(IOreType oreType) {
        if (defaultOreReplacement != null) {
            return defaultOreReplacement;
        } else
            return oreMap.get(oreType);
    }
}

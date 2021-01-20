package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoneWrapper {
    private final String id;

    public static List<StoneWrapper> allStoneWrappers = new ArrayList<>();

    public StoneWrapper(String id) {
        this.id = id;
    }

    private BlockState baseBlock;

    public BlockState getBlock() {
        if (baseBlock == null) {
            Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(this.id));
            if (block == Blocks.AIR) {
                Unearthed.LOGGER.warn(id + " does not exist. Defaulting to stone!");
                baseBlock = Blocks.STONE.getDefaultState();
            } else {
                baseBlock = block.getDefaultState();
            }
        }
        return baseBlock;
    }

    private BlockState cobbleBlock;

    public BlockState getCobbleBlock(BlockState originalState) {
        if (cobbleBlock == null) {
            String cobbleID = this.id + "_cobble";
            Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(cobbleID));
            if (block == Blocks.AIR) {
                Unearthed.LOGGER.warn(cobbleID + " does not exist. Defaulting to stone!");
                cobbleBlock = originalState.getBlock().getDefaultState();

                if (id.contains("minecraft:sand"))
                    cobbleBlock = Blocks.SANDSTONE.getDefaultState();
            } else {
                cobbleBlock = block.getDefaultState();
            }
        }
        return cobbleBlock;
    }

    private Map<IOreType, BlockState> oreMap = new HashMap<>();

    @Nullable
    public BlockState getOre(IOreType ore) {
        if (!oreMap.containsKey(ore)) {
            String oreID = this.id + "_" + ore.getName() + "_ore";
            Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(oreID));
            if (block == Blocks.AIR) {
                Unearthed.LOGGER.debug(ore + " does not exist.");
                oreMap.put(ore, null);
            } else {
                oreMap.put(ore, block.getDefaultState());
            }
        }
        return oreMap.get(ore);
    }
}

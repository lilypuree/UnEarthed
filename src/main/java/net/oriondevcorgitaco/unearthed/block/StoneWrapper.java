package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;
import org.omg.IOP.IOR;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class StoneWrapper {

    private static Map<ResourceLocation, StoneWrapper> allStoneWrappers = new HashMap<>();

    public static StoneWrapper fromGenerationHelper(BlockGeneratorHelper helper) {
        ResourceLocation id = helper.getBaseBlock().getRegistryName();
        return allStoneWrappers.computeIfAbsent(id, k -> {
            Block baseBlock = helper.getBaseBlock();
            BlockGeneratorHelper.Entry entry = helper.getEntry(Variants.COBBLED, Forms.BLOCK);
            Block cobbleBlock = entry != null ? entry.getBlock() : Blocks.COBBLESTONE;
            Block dirtReplacement = Blocks.DIRT;
            Block grassReplacement = Blocks.GRASS_BLOCK;
            entry = helper.getEntry(Variants.OVERGROWN, Forms.BLOCK);
            if (entry != null) {
                dirtReplacement = baseBlock;
                grassReplacement = entry.getBlock();
            } else {
                entry = helper.getEntries().stream().filter(e -> e.getForm() == Forms.REGOLITH).findAny().orElse(null);
                if (entry != null) {
                    dirtReplacement = entry.getBlock();
                    grassReplacement = helper.getEntry(entry.getVariant(), Forms.GRASSY_REGOLITH).getBlock();
                }
            }
            StoneWrapper wrapper = new StoneWrapper(k.toString(), baseBlock, cobbleBlock, dirtReplacement, grassReplacement);
            for (BlockGeneratorHelper.Entry e : helper.getEntries()) {
                if (e.getForm() instanceof Forms.OreForm) {
                    wrapper.oreMap.put(((Forms.OreForm) e.getForm()).getOreType(), e.getBlock().getDefaultState());
                }
            }
            return wrapper;
        });
    }

    public static StoneWrapper getOrCreate(ResourceLocation id) {
        return allStoneWrappers.computeIfAbsent(id, k -> new StoneWrapper(k.toString()));
    }

    private final String id;

    public StoneWrapper(String id) {
        this.id = id;
    }

    public StoneWrapper(String id, Block baseBlock, Block cobbleBlock, Block dirtReplacement, Block grassReplacement) {
        this.id = id;
        this.baseBlock = baseBlock.getDefaultState();
        this.cobbleBlock = cobbleBlock.getDefaultState();
        this.dirtReplacement = dirtReplacement.getDefaultState();
        this.grassReplacement = grassReplacement.getDefaultState();
    }

    private BlockState baseBlock;

    public BlockState getBlock() {
        if (baseBlock == null) {
            baseBlock = getVariantBlock("", Blocks.STONE.getDefaultState());
        }
        return baseBlock;
    }

    private BlockState cobbleBlock;

    public BlockState getCobbleBlock(BlockState originalState) {
        if (cobbleBlock == null) {
            cobbleBlock = getVariantBlock("cobble", originalState);
            if (id.contains("minecraft:sand")) {
                cobbleBlock = Blocks.SANDSTONE.getDefaultState();
            }
        }
        return cobbleBlock;
    }

    private BlockState dirtReplacement;
    private BlockState grassReplacement;

    public BlockState getDirtReplacer(BlockState originalState) {
        if (dirtReplacement == null) {
            dirtReplacement = getVariantBlock("regolith", originalState);
        }
        return dirtReplacement;
    }

    public BlockState getGrassReplacer(BlockState originalState) {
        if (grassReplacement == null) {
            grassReplacement = getVariantBlock("grassy_regolith", originalState);
        }
        return grassReplacement.with(BlockStateProperties.SNOWY, originalState.get(BlockStateProperties.SNOWY));
    }

    private Map<IOreType, BlockState> oreMap = new HashMap<>();

    @Nullable
    public BlockState getOre(IOreType ore) {
        if (!oreMap.containsKey(ore)) {
            String oreID = this.id + "_" + ore.getName() + "_ore";
            Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(oreID));
            if (block == Blocks.AIR) {
                Unearthed.LOGGER.debug(oreID + " does not exist.");
                oreMap.put(ore, null);
            } else {
                oreMap.put(ore, block.getDefaultState());
            }
        }
        return oreMap.get(ore);
    }

    private BlockState getVariantBlock(String suffix, BlockState defaultState) {
        String fullID;
        if (suffix.length() > 0) {
            fullID = this.id + "_" + suffix;
        } else {
            fullID = this.id;
        }
        Block block = Registry.BLOCK.getOrDefault(new ResourceLocation(fullID));
        if (block == Blocks.AIR) {
            Unearthed.LOGGER.debug(fullID + " does not exist.");
            return defaultState;
        } else {
            return block.getDefaultState();
        }
    }
}

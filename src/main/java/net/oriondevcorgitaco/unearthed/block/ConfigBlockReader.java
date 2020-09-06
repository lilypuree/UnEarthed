package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.oriondevcorgitaco.unearthed.Unearthed;

import java.util.ArrayList;
import java.util.List;

public class ConfigBlockReader {
    private final String id;

    public static List<ConfigBlockReader> blocksFromConfig = new ArrayList<>();
    public static List<ConfigBlockReader> iceBlocksFromConfig = new ArrayList<>();
    public static List<ConfigBlockReader> desertBlocksFromConfig = new ArrayList<>();

    public ConfigBlockReader(String id) {
        this.id = id;
    }

    static int blockIdx = 0;


    public Block getBlock() {
        Block block = Registry.BLOCK.get(new Identifier(this.id));
        if (block == Blocks.AIR) {
            if (blockIdx == 0) {
                Unearthed.LOGGER.warn(id + " does not exist. Defaulting to stone!");
                blockIdx++;
            }
            block = Blocks.STONE;
        }
        return block;
    }

    static int coalIdx = 0;

    public Block getCoalOre(BlockState originalState) {
        String ironOreID = this.id + "_coal_ore";
        Block block = Registry.BLOCK.get(new Identifier(ironOreID));
        if (block == Blocks.AIR) {
            if (coalIdx == 0) {
                Unearthed.LOGGER.debug(ironOreID + " does not exist.");
                coalIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }

    static int ironIdx = 0;

    public Block getIronOre(BlockState originalState) {
        String ironOreID = this.id + "_iron_ore";
        Block block = Registry.BLOCK.get(new Identifier(ironOreID));
        if (block == Blocks.AIR) {
            if (ironIdx == 0) {
                Unearthed.LOGGER.debug(ironOreID + " does not exist");
                ironIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }

    static int goldIdx = 0;

    public Block getGoldOre(BlockState originalState) {
        String goldOreID = this.id + "_gold_ore";
        Block block = Registry.BLOCK.get(new Identifier(goldOreID));
        if (block == Blocks.AIR) {
            if (goldIdx == 0) {
                Unearthed.LOGGER.debug(goldOreID + " does not exist.");
                goldIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }

    static int lapisIdx = 0;


    public Block getLapisOre(BlockState originalState) {
        String lapisOreID = this.id + "_lapis_ore";
        Block block = Registry.BLOCK.get(new Identifier(lapisOreID));
        if (block == Blocks.AIR) {
            if (lapisIdx == 0) {
                Unearthed.LOGGER.debug(lapisOreID + " does not exist.");
                lapisIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }

    static int redstoneIdx = 0;


    public Block getRedstoneOre(BlockState originalState) {
        String redstoneOreID = this.id + "_redstone_ore";
        Block block = Registry.BLOCK.get(new Identifier(redstoneOreID));
        if (block == Blocks.AIR) {
            if (redstoneIdx == 0) {
                Unearthed.LOGGER.debug(redstoneOreID + " does not exist.");
                redstoneIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }

    static int diamondIdx = 0;

    public Block getDiamondOre(BlockState originalState) {
        String diamondOreID = this.id + "_diamond_ore";
        Block block = Registry.BLOCK.get(new Identifier(diamondOreID));
        if (block == Blocks.AIR) {
            if (diamondIdx == 0) {
                Unearthed.LOGGER.debug(diamondOreID + " does not exist.");
                diamondIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }

    static int emeraldIdx = 0;


    public Block getEmeraldOre(BlockState originalState) {
        String emeraldOreID = this.id + "_emerald_ore";
        Block block = Registry.BLOCK.get(new Identifier(emeraldOreID));
        if (block == Blocks.AIR) {
            if (emeraldIdx == 0) {
                Unearthed.LOGGER.debug(emeraldOreID + " does not exist.");
                emeraldIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }


    /**************Modded Ores**************/

    public Block getBYGAmetrineOre(BlockState originalState) {
        String ametrineOreID = this.id + "_ametrine_ore";
        Block block = Registry.BLOCK.get(new Identifier(ametrineOreID));
        if (block == Blocks.AIR) {
            if (emeraldIdx == 0) {
                Unearthed.LOGGER.debug("Modded Ore: " + ametrineOreID + " does not exist.");
                emeraldIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }

    public Block getBYGPendoriteOre(BlockState originalState) {
        String pendoriteOreID = this.id + "_pendorite_ore";
        Block block = Registry.BLOCK.get(new Identifier(pendoriteOreID));
        if (block == Blocks.AIR) {
            if (emeraldIdx == 0) {
                Unearthed.LOGGER.debug("Modded Ore: " + pendoriteOreID + " does not exist.");
                emeraldIdx++;
            }
            block = originalState.getBlock();
        }
        return block;
    }
}

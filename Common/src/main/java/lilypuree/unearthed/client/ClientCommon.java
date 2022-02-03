package lilypuree.unearthed.client;

import lilypuree.unearthed.block.properties.ModBlockProperties;
import lilypuree.unearthed.block.schema.*;
import lilypuree.unearthed.core.UEBlocks;
import lilypuree.unearthed.core.UEItems;
import lilypuree.unearthed.util.ColorHelper;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ClientCommon {
    public static void initRenderLayers(BiConsumer<Block, RenderType> handler) {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form instanceof Forms.OreForm || form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    handler.accept(entry.getBlock(), RenderType.cutoutMipped());
                }
            }
        }
        handler.accept(UEBlocks.LICHEN, RenderType.cutoutMipped());
    }

    public static void blockColors(BiConsumer<BlockColor, Block> handler) {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    handler.accept((blockstate, reader, pos, i) -> {
                        return reader != null && pos != null ? BiomeColors.getAverageGrassColor(reader, pos) : GrassColor.get(0.5D, 1.0D);
                    }, entry.getBlock());
                }
            }
        }
        handler.accept((blockstate, reader, pos, i) -> {
            return reader != null && pos != null ? LichenColors.shiftSaturation(reader.getBlockTint(pos, LichenColors.LICHEN_COLOR), pos, blockstate.getValue(ModBlockProperties.WET)) : LichenColors.getLichen();
        }, UEBlocks.LICHEN);
    }

    public static void itemColors(BiConsumer<ItemColor, ItemLike> handler, BiFunction<BlockState, Integer, Integer> blockColors) {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    handler.accept((stack, color) -> {
                        BlockState blockstate = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                        return blockColors.apply(blockstate, color);
                    }, entry.getBlock());
                }
            }
        }
        handler.accept((stack, color) -> {
            return LichenColors.getLichen();
        }, UEItems.LICHEN);
    }
}

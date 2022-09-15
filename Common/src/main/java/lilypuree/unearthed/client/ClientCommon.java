package lilypuree.unearthed.client;

import lilypuree.unearthed.block.properties.ModBlockProperties;
import lilypuree.unearthed.block.schema.*;
import lilypuree.unearthed.core.UEBlocks;
import lilypuree.unearthed.core.UEItems;
import lilypuree.unearthed.platform.Services;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;

public class ClientCommon {
    public static void initRenderLayers() {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form instanceof Forms.OreForm || form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    Services.PLATFORM.setRenderLayer(entry.getBlock(), RenderType.cutoutMipped());
                }
            }
        }
        Services.PLATFORM.setRenderLayer(UEBlocks.LICHEN, RenderType.cutoutMipped());
    }

    public static void blockColors() {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    Services.PLATFORM.setBlockColor(entry.getBlock(), (blockstate, reader, pos, i) -> {
                        return reader != null && pos != null ? BiomeColors.getAverageGrassColor(reader, pos) : GrassColor.get(0.5D, 1.0D);
                    });
                }
            }
        }
        Services.PLATFORM.setBlockColor(UEBlocks.LICHEN, (blockstate, reader, pos, i) -> {
            return reader != null && pos != null ? LichenColors.shiftSaturation(reader.getBlockTint(pos, LichenColors.LICHEN_COLOR), pos, blockstate.getValue(ModBlockProperties.WET)) : LichenColors.getLichen();
        });
    }

    public static void itemColors() {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    Services.PLATFORM.setItemColor(entry.getBlock(), (stack, color) -> {
                        BlockState blockState = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                        return Services.PLATFORM.getBlockColor(blockState, color);
                    });
                }
            }
        }
        Services.PLATFORM.setItemColor(UEItems.LICHEN, (stack, color) -> LichenColors.getLichen());
    }
}

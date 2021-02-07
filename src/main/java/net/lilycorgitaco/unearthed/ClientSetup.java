package net.lilycorgitaco.unearthed;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.lilycorgitaco.unearthed.block.BlockGeneratorHelper;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.block.ModBlockProperties;
import net.lilycorgitaco.unearthed.block.schema.BlockSchema;
import net.lilycorgitaco.unearthed.block.schema.Forms;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import net.lilycorgitaco.unearthed.core.UEItems;
import net.lilycorgitaco.unearthed.util.ColorHelper;
import net.lilycorgitaco.unearthed.world.LichenColors;

public class ClientSetup implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                if (form instanceof Forms.OreForm || form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    if (entry.getBlock() != null)
                        BlockRenderLayerMap.INSTANCE.putBlock(entry.getBlock(), RenderLayer.getCutoutMipped());
                }
            }
        }
        BlockRenderLayerMap.INSTANCE.putBlock(UEBlocks.PUDDLE, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(UEBlocks.LICHEN, RenderLayer.getCutoutMipped());
    }

    public static void onBlockColourHandleEvent(BlockColors colors) {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    colors.registerColorProvider((blockstate, reader, pos, i) -> {
                        return reader != null && pos != null ? BiomeColors.getGrassColor(reader, pos) : GrassColors.getColor(0.5D, 1.0D);
                    }, entry.getBlock());
                }
            }
        }
        colors.registerColorProvider((blockstate, reader, pos, i) -> {
            return reader != null && pos != null ? LichenColors.shiftSaturation(reader.getColor(pos, LichenColors.LICHEN_COLOR), pos, blockstate.get(ModBlockProperties.WET)) : LichenColors.getLichen();
        }, UEBlocks.LICHEN);
//        colors.register((blockstate, reader, pos, i) -> {
//            return reader != null && pos != null ? LichenColors.get2(pos.getX(), pos.getZ()) : LichenColors.getLichen();
//        }, UEBlocks.LICHEN);
        colors.registerColorProvider((blockstate, reader, pos, i) -> {
            return reader != null && pos != null ? ColorHelper.blend(BiomeColors.getWaterColor(reader, pos), 0x7b3f00, 0.25f) : 5670852;
        }, UEBlocks.PUDDLE);
    }

    public static void onItemColourHandlerEvent(ItemColors colors, BlockColors blockColors) {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                if (entry.getBlock() == null)
                    continue;

                BlockSchema.Form form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    colors.register((stack, color) -> {
                        BlockState blockstate = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
                        return blockColors.getColor(blockstate, null, null, color);
                    }, entry.getBlock());
                }
            }
        }
        colors.register((stack, color) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return blockColors.getColor(state, null, null, color);
        }, UEItems.PUDDLE);
        colors.register((stack, color) -> {
            return LichenColors.getLichen();
        }, UEItems.LICHEN);
    }
}
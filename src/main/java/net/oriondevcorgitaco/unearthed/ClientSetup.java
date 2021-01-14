package net.oriondevcorgitaco.unearthed;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ColorCache;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Util;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.level.ColorResolver;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import net.oriondevcorgitaco.unearthed.util.ColorHelper;
import net.oriondevcorgitaco.unearthed.world.LichenColors;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Unearthed.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    public static void init(final FMLClientSetupEvent event) {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                if (form instanceof Forms.OreForm || form == Forms.GRASSY_REGOLITH) {
                    RenderTypeLookup.setRenderLayer(entry.getBlock(), RenderType.getTranslucent());
                }
            }
        }
        RenderTypeLookup.setRenderLayer(UEBlocks.PUDDLE, RenderType.getTranslucent());
        RenderTypeLookup.setRenderLayer(UEBlocks.LICHEN, RenderType.getCutoutMipped());
    }

    @SubscribeEvent
    public static void onBlockColourHandleEvent(final ColorHandlerEvent.Block event) {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH) {
                    event.getBlockColors().register((blockstate, reader, pos, i) -> {
                        return !blockstate.get(BlockStateProperties.SNOWY) && reader != null && pos != null ? BiomeColors.getGrassColor(reader, pos) : GrassColors.get(0.5D, 1.0D);
                    }, entry.getBlock());
                }
            }
        }
        event.getBlockColors().register((blockstate, reader, pos, i) -> {
            return reader != null && pos != null ? LichenColors.shiftSaturation(reader.getBlockColor(pos, LichenColors.LICHEN_COLOR), pos) : LichenColors.getLichen();
        }, UEBlocks.LICHEN);
//        event.getBlockColors().register((blockstate, reader, pos, i) -> {
//            return reader != null && pos != null ? LichenColors.get2(pos.getX(), pos.getZ()) : LichenColors.getLichen();
//        }, UEBlocks.LICHEN);
        event.getBlockColors().register((blockstate, reader, pos, i) -> {
            return reader != null && pos != null ? ColorHelper.blend(BiomeColors.getWaterColor(reader, pos), 0x7b3f00, 0.25f) : 5670852;
        }, UEBlocks.PUDDLE);
    }

    @SubscribeEvent
    public static void onItemColourHandlerEvent(final ColorHandlerEvent.Item event) {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH) {
                    event.getItemColors().register((stack, color) -> {
                        BlockState blockstate = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
                        return event.getBlockColors().getColor(blockstate, null, null, color);
                    }, entry.getBlock());
                }
            }
        }
        event.getItemColors().register((stack, color) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return event.getBlockColors().getColor(state, null, null, color);
        }, UEItems.PUDDLE);
        event.getItemColors().register((stack, color) -> {
            return LichenColors.getLichen();
        }, UEItems.LICHEN);
    }
}
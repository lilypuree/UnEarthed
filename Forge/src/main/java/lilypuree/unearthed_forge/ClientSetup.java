package lilypuree.unearthed_forge;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.properties.ModBlockProperties;
import lilypuree.unearthed.block.schema.*;
import lilypuree.unearthed.core.UEBlocks;
import lilypuree.unearthed.core.UEItems;
import lilypuree.unearthed.util.ColorHelper;
import lilypuree.unearthed.world.LichenColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form instanceof Forms.OreForm || form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    ItemBlockRenderTypes.setRenderLayer(entry.getBlock(), RenderType.cutoutMipped());
                }
            }
        }
        ItemBlockRenderTypes.setRenderLayer(UEBlocks.PUDDLE, RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(UEBlocks.LICHEN, RenderType.cutoutMipped());
    }

    @SubscribeEvent
    public static void onBlockColourHandleEvent(final ColorHandlerEvent.Block event) {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    event.getBlockColors().register((blockstate, reader, pos, i) -> {
                        return reader != null && pos != null ? BiomeColors.getAverageGrassColor(reader, pos) : GrassColor.get(0.5D, 1.0D);
                    }, entry.getBlock());
                }
            }
        }
        event.getBlockColors().register((blockstate, reader, pos, i) -> {
            return reader != null && pos != null ? LichenColors.shiftSaturation(reader.getBlockTint(pos, LichenColors.LICHEN_COLOR), pos, blockstate.getValue(ModBlockProperties.WET)) : LichenColors.getLichen();
        }, UEBlocks.LICHEN);
        event.getBlockColors().register((blockstate, reader, pos, i) -> {
            return reader != null && pos != null ? ColorHelper.blend(BiomeColors.getAverageWaterColor(reader, pos), 0x7b3f00, 0.25f) : 5670852;
        }, UEBlocks.PUDDLE);
    }

    @SubscribeEvent
    public static void onItemColourHandlerEvent(final ColorHandlerEvent.Item event) {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();
                if (form == Forms.GRASSY_REGOLITH || form == Forms.OVERGROWN_ROCK) {
                    event.getItemColors().register((stack, color) -> {
                        BlockState blockstate = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                        return event.getBlockColors().getColor(blockstate, null, null, color);
                    }, entry.getBlock());
                }
            }
        }
        event.getItemColors().register((stack, color) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(state, null, null, color);
        }, UEItems.PUDDLE);
        event.getItemColors().register((stack, color) -> {
            return LichenColors.getLichen();
        }, UEItems.LICHEN);
    }
}
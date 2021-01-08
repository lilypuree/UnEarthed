package net.oriondevcorgitaco.unearthed;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;

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
//        RenderTypeLookup.setRenderLayer(BlockGeneratorReference.BEIGE_LIMESTONE_STALACTITE, RenderType.getCutoutMipped());
//        RenderTypeLookup.setRenderLayer(BlockGeneratorReference.BEIGE_LIMESTONE_STALAGMITE, RenderType.getCutoutMipped());
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
    }


}
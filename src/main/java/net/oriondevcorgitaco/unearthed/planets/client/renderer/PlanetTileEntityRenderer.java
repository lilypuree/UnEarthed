package net.oriondevcorgitaco.unearthed.planets.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.planets.mixininterface.IBlockModelRenderer;
import net.oriondevcorgitaco.unearthed.planets.block.PlanetLavaTile;

public class PlanetTileEntityRenderer extends TileEntityRenderer<PlanetLavaTile> {
    private static BlockRendererDispatcher blockDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

    public PlanetTileEntityRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(PlanetLavaTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        FluidState lava = Fluids.LAVA.getStillFluidState(false);
        int ticks = tileEntityIn.getTicks();
        BlockState block = tileEntityIn.getActualBlock();
        float lavaAlpha = MathHelper.clamp((float) ticks / PlanetLavaTile.getMaxTicks(), 0.0f, 1.0f);
        World world = tileEntityIn.getWorld();
        BlockPos pos = tileEntityIn.getPos();
        if (block != null && world != null) {
            IVertexBuilder buffer = bufferIn.getBuffer(RenderType.getTranslucent());
//            ((IBlockRendererDispatcher) blockDispatcher).renderFluid(pos, world, buffer, lava, lavaAlpha);
            ((IBlockModelRenderer) blockDispatcher.getBlockModelRenderer()).renderBlockWithAlpha(pos, tileEntityIn.getBlockState(), world, matrixStackIn, lavaAlpha, bufferIn);

            ((IBlockModelRenderer) blockDispatcher.getBlockModelRenderer()).renderBlockWithAlpha(pos, block, world, matrixStackIn, 1 - lavaAlpha, bufferIn);
        }
    }


}

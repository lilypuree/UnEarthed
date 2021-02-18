package net.oriondevcorgitaco.unearthed.planets.mixininterface;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public interface IBlockRendererDispatcher {

    boolean renderFluid(BlockPos posIn, IBlockDisplayReader lightReaderIn, IVertexBuilder vertexBuilderIn, FluidState fluidStateIn, float alpha);

}
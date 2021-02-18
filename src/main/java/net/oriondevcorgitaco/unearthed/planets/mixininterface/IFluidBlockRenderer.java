package net.oriondevcorgitaco.unearthed.planets.mixininterface;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;

public interface IFluidBlockRenderer {

    boolean renderWithAlpha(IBlockDisplayReader lightReaderIn, BlockPos posIn, IVertexBuilder vertexBuilderIn, FluidState fluidStateIn, float alpha);
}

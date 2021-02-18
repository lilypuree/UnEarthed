package net.oriondevcorgitaco.unearthed.planets.mixin.client;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.*;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.oriondevcorgitaco.unearthed.planets.mixininterface.IBlockRendererDispatcher;
import net.oriondevcorgitaco.unearthed.planets.mixininterface.IFluidBlockRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//@Mixin(BlockRendererDispatcher.class)
public abstract class MixinBlockRendererDispatcher implements IBlockRendererDispatcher {
//    @Shadow
//    @Final
    private FluidBlockRenderer fluidRenderer;

    @Override
    public boolean renderFluid(BlockPos posIn, IBlockDisplayReader lightReaderIn, IVertexBuilder vertexBuilderIn, FluidState fluidStateIn, float alpha) {
        try {
            return ((IFluidBlockRenderer) this.fluidRenderer).renderWithAlpha(lightReaderIn, posIn, vertexBuilderIn, fluidStateIn, alpha);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating liquid in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, posIn, (BlockState) null);
            throw new ReportedException(crashreport);
        }
    }
}

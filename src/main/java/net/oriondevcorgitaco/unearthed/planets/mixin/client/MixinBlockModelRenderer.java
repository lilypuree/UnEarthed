package net.oriondevcorgitaco.unearthed.planets.mixin.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.*;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;
import net.oriondevcorgitaco.unearthed.planets.mixininterface.IBlockModelRenderer;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

//@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer implements IBlockModelRenderer {
//    @Shadow
    public abstract boolean renderModelFlat(IBlockDisplayReader worldIn, IBakedModel modelIn, BlockState stateIn, BlockPos posIn, MatrixStack matrixStackIn, IVertexBuilder buffer, boolean checkSides, Random randomIn, long rand, int combinedOverlayIn, IModelData modelData);

//    @Shadow
    protected abstract void fillQuadBounds(IBlockDisplayReader blockReaderIn, BlockState stateIn, BlockPos posIn, int[] vertexData, Direction face, @Nullable float[] quadBounds, BitSet boundsFlags);

//    @Shadow
//    @Final
    private BlockColors blockColors;
    private static BlockRendererDispatcher blockDispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

    @Override
    public void renderBlockWithAlpha(BlockPos pos, BlockState state, World world, MatrixStack matrix, float alpha, IRenderTypeBuffer renderTypeBuffer) {
        matrix.push();
        renderModelWithAlpha(
                world,
                Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(state),
                state,
                pos,
                matrix,
                renderTypeBuffer.getBuffer(RenderType.getTranslucent()),
                false,
                world.rand,
                state.getPositionRandom(pos),
                OverlayTexture.NO_OVERLAY,
                alpha,
                net.minecraftforge.client.model.data.EmptyModelData.INSTANCE);
        matrix.pop();
    }

    @Override
    public boolean renderModelWithAlpha(IBlockDisplayReader worldIn, IBakedModel modelIn, BlockState stateIn, BlockPos posIn, MatrixStack matrixIn, IVertexBuilder buffer, boolean checkSides, Random randomIn, long rand, int combinedOverlayIn, float alpha, IModelData modelData) {
        boolean flag = Minecraft.isAmbientOcclusionEnabled() && stateIn.getLightValue(worldIn, posIn) == 0 && modelIn.isAmbientOcclusion();
        Vector3d vector3d = stateIn.getOffset(worldIn, posIn);
        matrixIn.translate(vector3d.x, vector3d.y, vector3d.z);
        modelData = modelIn.getModelData(worldIn, posIn, stateIn, modelData);

        try {
            return flag ? this.renderModelSmoothWithAlpha(worldIn, modelIn, stateIn, posIn, matrixIn, buffer, checkSides, randomIn, rand, combinedOverlayIn, alpha, modelData) : this.renderModelFlat(worldIn, modelIn, stateIn, posIn, matrixIn, buffer, checkSides, randomIn, rand, combinedOverlayIn, modelData);
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, posIn, stateIn);
            crashreportcategory.addDetail("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public boolean renderModelSmoothWithAlpha(IBlockDisplayReader worldIn, IBakedModel modelIn, BlockState stateIn, BlockPos posIn, MatrixStack matrixStackIn, IVertexBuilder buffer, boolean checkSides, Random randomIn, long rand, int combinedOverlayIn, float alpha, IModelData modelData) {
        boolean flag = false;
        float[] afloat = new float[Direction.values().length * 2];
        BitSet bitset = new BitSet(3);
        BlockModelRenderer.AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = ((BlockModelRenderer)((Object)this)).new AmbientOcclusionFace();

        for (Direction direction : Direction.values()) {
            randomIn.setSeed(rand);
            List<BakedQuad> list = modelIn.getQuads(stateIn, direction, randomIn, modelData);
            if (!list.isEmpty() && (!checkSides || Block.shouldSideBeRendered(stateIn, worldIn, posIn, direction))) {
                this.renderQuadsSmoothWithAlpha(worldIn, stateIn, posIn, matrixStackIn, buffer, list, afloat, bitset, blockmodelrenderer$ambientocclusionface, combinedOverlayIn, alpha);
                flag = true;
            }
        }

        randomIn.setSeed(rand);
        List<BakedQuad> list1 = modelIn.getQuads(stateIn, (Direction) null, randomIn, modelData);
        if (!list1.isEmpty()) {
            this.renderQuadsSmoothWithAlpha(worldIn, stateIn, posIn, matrixStackIn, buffer, list1, afloat, bitset, blockmodelrenderer$ambientocclusionface, combinedOverlayIn, alpha);
            flag = true;
        }

        return flag;
    }

    @Override
    public void renderQuadsSmoothWithAlpha(IBlockDisplayReader blockAccessIn, BlockState stateIn, BlockPos posIn, MatrixStack matrixStackIn, IVertexBuilder buffer, List<BakedQuad> list, float[] quadBounds, BitSet bitSet, BlockModelRenderer.AmbientOcclusionFace aoFace, int combinedOverlayIn, float alpha) {
        for (BakedQuad bakedquad : list) {
            this.fillQuadBounds(blockAccessIn, stateIn, posIn, bakedquad.getVertexData(), bakedquad.getFace(), quadBounds, bitSet);
            aoFace.renderBlockModel(blockAccessIn, stateIn, posIn, bakedquad.getFace(), quadBounds, bitSet, bakedquad.applyDiffuseLighting());
            this.renderQuadSmoothWithAlpha(blockAccessIn, stateIn, posIn, buffer, matrixStackIn.getLast(), bakedquad, aoFace.vertexColorMultiplier[0], aoFace.vertexColorMultiplier[1], aoFace.vertexColorMultiplier[2], aoFace.vertexColorMultiplier[3], aoFace.vertexBrightness[0], aoFace.vertexBrightness[1], aoFace.vertexBrightness[2], aoFace.vertexBrightness[3], combinedOverlayIn, alpha);
        }
    }

    @Override
    public void renderQuadSmoothWithAlpha(IBlockDisplayReader blockAccessIn, BlockState stateIn, BlockPos posIn, IVertexBuilder buffer, MatrixStack.Entry matrixEntry, BakedQuad quadIn, float colorMul0, float colorMul1, float colorMul2, float colorMul3, int brightness0, int brightness1, int brightness2, int brightness3, int combinedOverlayIn, float alpha) {
        float f;
        float f1;
        float f2;
        if (quadIn.hasTintIndex()) {
            int i = this.blockColors.getColor(stateIn, blockAccessIn, posIn, quadIn.getTintIndex());
            f = (float) (i >> 16 & 255) / 255.0F;
            f1 = (float) (i >> 8 & 255) / 255.0F;
            f2 = (float) (i & 255) / 255.0F;
        } else {
            f = 1.0F;
            f1 = 1.0F;
            f2 = 1.0F;
        }

        addTransparentQuad(matrixEntry, quadIn, new float[]{colorMul0, colorMul1, colorMul2, colorMul3}, f, f1, f2, alpha, new int[]{brightness0, brightness1, brightness2, brightness3}, combinedOverlayIn, true, buffer);
    }

    // as IVertexBuilder::addQuad except when we add the vertex, we add an opacity float instead of 1.0F
    private static void addTransparentQuad(MatrixStack.Entry matrixEntry, BakedQuad quad, float[] colorMuls, float r, float g, float b, float alphaIn, int[] vertexLights,
                                           int combinedOverlayIn, boolean mulColor, IVertexBuilder buffer) {
        int[] vertexData = quad.getVertexData();
        Vector3i faceVector3i = quad.getFace().getDirectionVec();
        Vector3f faceVector = new Vector3f(faceVector3i.getX(), faceVector3i.getY(), faceVector3i.getZ());
        Matrix4f matrix = matrixEntry.getMatrix();
        faceVector.transform(matrixEntry.getNormal());

        int vertexDataEntries = vertexData.length / 8;

        try (MemoryStack memorystack = MemoryStack.stackPush()) {
            ByteBuffer bytebuffer = memorystack.malloc(DefaultVertexFormats.BLOCK.getSize());
            IntBuffer intbuffer = bytebuffer.asIntBuffer();

            for (int vertexIndex = 0; vertexIndex < vertexDataEntries; ++vertexIndex) {
                ((Buffer) intbuffer).clear();
                intbuffer.put(vertexData, vertexIndex * 8, 8);
                float x = bytebuffer.getFloat(0);
                float y = bytebuffer.getFloat(4);
                float z = bytebuffer.getFloat(8);
                float red = colorMuls[vertexIndex] * r;
                float green = colorMuls[vertexIndex] * g;
                float blue = colorMuls[vertexIndex] * b;


                if (mulColor) {
                    float redMultiplier = (bytebuffer.get(12) & 255) / 255.0F;
                    float greenMultiplier = (bytebuffer.get(13) & 255) / 255.0F;
                    float blueMultiplier = (bytebuffer.get(14) & 255) / 255.0F;
                    red = redMultiplier * red;
                    green = greenMultiplier * green;
                    blue = blueMultiplier * blue;
                }

                int light = buffer.applyBakedLighting(vertexLights[vertexIndex], bytebuffer);
                float texU = bytebuffer.getFloat(16);
                float texV = bytebuffer.getFloat(20);
                Vector4f posVector = new Vector4f(x, y, z, 1.0F);
                posVector.transform(matrix);
                buffer.applyBakedNormals(faceVector, bytebuffer, matrixEntry.getNormal());
                buffer.addVertex(posVector.getX(), posVector.getY(), posVector.getZ(), red, green, blue, alphaIn, texU, texV,
                        combinedOverlayIn, light, faceVector.getX(), faceVector.getY(), faceVector.getZ());
            }
        }
    }
}

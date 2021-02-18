package net.oriondevcorgitaco.unearthed.planets.mixininterface;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.World;

import java.util.BitSet;
import java.util.List;
import java.util.Random;

public interface IBlockModelRenderer {

    void renderBlockWithAlpha(BlockPos pos, BlockState state, World world, MatrixStack matrix, float alpha, IRenderTypeBuffer renderTypeBuffer);

    boolean renderModelWithAlpha(IBlockDisplayReader worldIn, IBakedModel modelIn, BlockState stateIn, BlockPos posIn, MatrixStack matrixIn, IVertexBuilder buffer, boolean checkSides, Random randomIn, long rand, int combinedOverlayIn, float alpha, net.minecraftforge.client.model.data.IModelData modelData);

    boolean renderModelSmoothWithAlpha(IBlockDisplayReader worldIn, IBakedModel modelIn, BlockState stateIn, BlockPos posIn, MatrixStack matrixStackIn, IVertexBuilder buffer, boolean checkSides, Random randomIn, long rand, int combinedOverlayIn, float alpha, net.minecraftforge.client.model.data.IModelData modelData);

    void renderQuadsSmoothWithAlpha(IBlockDisplayReader blockAccessIn, BlockState stateIn, BlockPos posIn, MatrixStack matrixStackIn, IVertexBuilder buffer, List<BakedQuad> list, float[] quadBounds, BitSet bitSet, BlockModelRenderer.AmbientOcclusionFace aoFace, int combinedOverlayIn, float alpha);

    void renderQuadSmoothWithAlpha(IBlockDisplayReader blockAccessIn, BlockState stateIn, BlockPos posIn, IVertexBuilder buffer, MatrixStack.Entry matrixEntry, BakedQuad quadIn, float colorMul0, float colorMul1, float colorMul2, float colorMul3, int brightness0, int brightness1, int brightness2, int brightness3, int combinedOverlayIn, float alpha);


}
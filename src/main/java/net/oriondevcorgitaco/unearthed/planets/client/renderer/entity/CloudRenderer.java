package net.oriondevcorgitaco.unearthed.planets.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.planets.entity.CloudEntity;

public class CloudRenderer extends EntityRenderer<CloudEntity> {
    private static final ResourceLocation CLOUD = new ResourceLocation(Unearthed.MOD_ID, "textures/entity/cloud.png");

    protected CloudModel cloudModel;

    public CloudRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.cloudModel = new CloudModel();
    }

    @Override
    public void render(CloudEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        float f = MathHelper.interpolateAngle(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - f));
        Vector3f dimensions = entityIn.getDimensions();
        matrixStackIn.scale(dimensions.getX(), dimensions.getY(), dimensions.getZ());

        float transparency = 0.95f;
        float brightness = entityIn.getCloudBrightness();

        RenderType rendertype = cloudModel.getRenderType(this.getEntityTexture(entityIn));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(rendertype);
        int i = getPackedOverlay(entityIn, 0);
        cloudModel.render(matrixStackIn, ivertexbuilder, packedLightIn, i, brightness, brightness, brightness, transparency);

        matrixStackIn.pop();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public static int getPackedOverlay(Entity entityIn, float uIn) {
        return OverlayTexture.getPackedUV(OverlayTexture.getU(uIn), OverlayTexture.getV(false));
    }

    @Override
    public ResourceLocation getEntityTexture(CloudEntity entity) {
        return CLOUD;
    }
}

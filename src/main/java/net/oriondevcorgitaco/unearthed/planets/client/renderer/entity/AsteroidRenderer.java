package net.oriondevcorgitaco.unearthed.planets.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.planets.entity.AsteroidEntity;

public class AsteroidRenderer extends EntityRenderer<AsteroidEntity> {
    private static final ResourceLocation ASTEROID = new ResourceLocation(Unearthed.MOD_ID, "textures/entity/asteroid.png");

    protected AsteroidModel asteroidModel;

    public AsteroidRenderer(EntityRendererManager renderManager) {
        super(renderManager);
        this.asteroidModel = new AsteroidModel();
    }

    @Override
    public void render(AsteroidEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        Vector3f speed = new Vector3f(entityIn.getMotion());
        matrixStackIn.translate(-0.25f, -0.25f, -0.25f);
        matrixStackIn.rotate(getRotationBetween(Vector3f.YP, speed));
        matrixStackIn.translate(+0.25f, +0.25f, +0.25f);
        IVertexBuilder builder = bufferIn.getBuffer(asteroidModel.getRenderType(this.getEntityTexture(entityIn)));
        asteroidModel.render(matrixStackIn, builder, packedLightIn, getPackedOverlay(entityIn, 0), 1.0f, 1.0f, 1.0f, 1.0f);
        matrixStackIn.pop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private Quaternion getRotationBetween(Vector3f u, Vector3f v) {
        u.normalize();
        v.normalize();
        float k_cos_theta = u.dot(v);
        Vector3f axis;
        if (k_cos_theta == -1) {
            axis = Vector3f.YP.copy();
            axis.cross(u);
            if (axis.dot(axis) < 0.01) {
                axis = Vector3f.XP.copy();
                axis.cross(u);
            }
            axis.normalize();
            return new Quaternion(axis, 180, true);
        }
        axis = u.copy();
        axis.cross(v);
        float s = (float) Math.sqrt((1 + k_cos_theta) * 2);
        float invs = 1 / s;
        return new Quaternion(axis.getX() * invs, axis.getY() * invs, axis.getZ() * invs, s / 2);
    }


    public static int getPackedOverlay(Entity entityIn, float uIn) {
        return OverlayTexture.getPackedUV(OverlayTexture.getU(uIn), OverlayTexture.getV(false));
    }

    @Override
    public ResourceLocation getEntityTexture(AsteroidEntity entity) {
        return ASTEROID;
    }
}

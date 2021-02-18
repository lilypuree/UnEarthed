package net.oriondevcorgitaco.unearthed.planets.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.oriondevcorgitaco.unearthed.planets.entity.CloudEntity;

public class CloudModel extends Model {
    private ModelRenderer cloud;

    public CloudModel() {
        super(RenderType::getEntityTranslucent);
        this.cloud = new ModelRenderer(this, 0, 0);
        cloud.addBox(0.0f, 0.0f, 0.0f, 4.0f, 4.0f, 4.0f);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        cloud.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}

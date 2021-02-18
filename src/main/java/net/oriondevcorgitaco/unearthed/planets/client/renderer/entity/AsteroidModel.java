package net.oriondevcorgitaco.unearthed.planets.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class AsteroidModel extends Model {
    private ModelRenderer asteroid;

    public AsteroidModel() {
        super(RenderType::getEntityCutoutNoCull);
        this.asteroid = new ModelRenderer(this, 0, 0);
        asteroid.setTextureSize(16, 16);
        asteroid.addBox(0.0f, 0.0f, 0.0f, 8.0f, 8.0f, 8.0f);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        asteroid.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}

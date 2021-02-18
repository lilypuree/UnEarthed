//package net.oriondevcorgitaco.unearthed.planets.mixin.client;
//
//import com.mojang.blaze3d.vertex.IVertexBuilder;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.renderer.FluidBlockRenderer;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.fluid.Fluid;
//import net.minecraft.fluid.FluidState;
//import net.minecraft.tags.FluidTags;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.util.math.shapes.VoxelShapes;
//import net.minecraft.util.math.vector.Vector3d;
//import net.minecraft.world.IBlockDisplayReader;
//import net.minecraft.world.IBlockReader;
//import net.oriondevcorgitaco.unearthed.core.UEBlocks;
//import net.oriondevcorgitaco.unearthed.planets.mixininterface.IFluidBlockRenderer;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Shadow;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import static net.minecraft.client.renderer.FluidBlockRenderer.func_239281_a_;
//
////@Mixin(FluidBlockRenderer.class)
//public abstract class MixinFluidBlockRenderer implements IFluidBlockRenderer {
//
////    @Shadow
//    protected static boolean isAdjacentFluidSameAs(IBlockReader worldIn, BlockPos pos, Direction side, FluidState state) {
//        return false;
//    }
//
////    @Shadow
////    public static boolean func_239281_a_(IBlockDisplayReader p_239281_0_, BlockPos p_239281_1_, FluidState p_239281_2_, BlockState p_239281_3_, Direction p_239281_4_) {
////        return false;
////    }
//
//    //shouldRenderFace
//    private static boolean shouldRenderFace(IBlockDisplayReader world, BlockPos pos, FluidState fluidState, BlockState block, Direction direction) {
//        return !isAdjacentFluidSameAs(world, pos, direction, fluidState);
////        return !isFaceOccludedBySelf(world, pos, block, direction) && !isAdjacentFluidSameAs(world, pos, direction, fluidState);
//    }
//
//
//    //isFaceOccludedBySelf
//    private static boolean isFaceOccludedBySelf(IBlockReader world, BlockPos pos, BlockState block, Direction direction) {
//        return false;
////        return isFaceOccludedByState(world, direction.getOpposite(), 1.0F, pos, block);
//    }
//
//    //isFaceOccludedByState
//    private static boolean isFaceOccludedByState(IBlockReader world, Direction direction, float level, BlockPos pos, BlockState block) {
//        if (block.isSolid()) {
//            VoxelShape voxelshape = VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, (double) level, 1.0D);
//            VoxelShape voxelshape1 = block.getRenderShapeTrue(world, pos);
//            return VoxelShapes.isCubeSideCovered(voxelshape, voxelshape1, direction);
//        } else {
//            return false;
//        }
//    }
//
////    @Shadow
//    protected abstract float getFluidHeight(IBlockReader reader, BlockPos pos, Fluid fluidIn);
//
////    @Shadow
//    protected static boolean func_239283_a_(IBlockReader p_239283_0_, BlockPos p_239283_1_, Direction p_239283_2_, float p_239283_3_) {
//        return false;
//    }
//
////    private static boolean func_239283_a_(IBlockReader world, BlockPos pos, Direction direction, float level) {
////        BlockPos blockpos = pos.offset(direction);
////        BlockState blockstate = world.getBlockState(blockpos);
////        return isFaceOccludedByState(world, direction, level, blockpos, blockstate);
////    }
//
////    @Shadow
//    protected abstract int getCombinedAverageLight(IBlockDisplayReader lightReaderIn, BlockPos posIn);
//
////    @Shadow
//    protected abstract void vertexVanilla(IVertexBuilder vertexBuilderIn, double x, double y, double z, float red, float green, float blue, float alpha, float u, float v, int packedLight);
//
//
////    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
//    private void onRender(IBlockDisplayReader lightReaderIn, BlockPos posIn, IVertexBuilder vertexBuilderIn, FluidState fluidStateIn, CallbackInfoReturnable<Boolean> cir) {
//        if (lightReaderIn.getBlockState(posIn).isIn(UEBlocks.PLANET_WATER)) {
//
//            TextureAtlasSprite[] atextureatlassprite = net.minecraftforge.client.ForgeHooksClient.getFluidSprites(lightReaderIn, posIn, fluidStateIn);
//            BlockState blockstate = lightReaderIn.getBlockState(posIn);
//            int i = fluidStateIn.getFluid().getAttributes().getColor(lightReaderIn, posIn);
//            float alpha = (float) (i >> 24 & 255) / 255.0F;
//            float f = (float) (i >> 16 & 255) / 255.0F;
//            float f1 = (float) (i >> 8 & 255) / 255.0F;
//            float f2 = (float) (i & 255) / 255.0F;
//
//            boolean renderUp = !isAdjacentFluidSameAs(lightReaderIn, posIn, Direction.UP, fluidStateIn);
//            boolean renderDown = func_239281_a_(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.DOWN);
//            boolean renderNorth = func_239281_a_(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.NORTH);
//            boolean renderSouth = func_239281_a_(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.SOUTH);
//            boolean renderWest = func_239281_a_(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.WEST);
//            boolean renderEast = func_239281_a_(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.EAST);
//
////            boolean renderUp = !isAdjacentFluidSameAs(lightReaderIn, posIn, Direction.UP, fluidStateIn);
////            boolean renderDown = !isAdjacentFluidSameAs(lightReaderIn, posIn, Direction.DOWN, fluidStateIn);
////            boolean renderNorth = !isAdjacentFluidSameAs(lightReaderIn, posIn, Direction.NORTH, fluidStateIn);
////            boolean renderSouth = !isAdjacentFluidSameAs(lightReaderIn, posIn, Direction.SOUTH, fluidStateIn);
////            boolean renderWest = !isAdjacentFluidSameAs(lightReaderIn, posIn, Direction.WEST, fluidStateIn);
////            boolean renderEast = !isAdjacentFluidSameAs(lightReaderIn, posIn, Direction.EAST, fluidStateIn);
//            if (!renderUp && !renderDown && !renderEast && !renderWest && !renderNorth && !renderSouth) {
//                cir.setReturnValue(false);
//            } else {
//                boolean flag7 = false;
//                float shadeDown = lightReaderIn.func_230487_a_(Direction.DOWN, true);
//                float shadeUp = lightReaderIn.func_230487_a_(Direction.UP, true);
//                float shadeNorth = lightReaderIn.func_230487_a_(Direction.NORTH, true);
//                float shadeWest = lightReaderIn.func_230487_a_(Direction.WEST, true);
//                float height = 1.0f; //this.getFluidHeight(lightReaderIn, posIn, fluidStateIn.getFluid());
//                float heightS =1.0f; // this.getFluidHeight(lightReaderIn, posIn.south(), fluidStateIn.getFluid());
//                float heightSE =1.0f; // this.getFluidHeight(lightReaderIn, posIn.east().south(), fluidStateIn.getFluid());
//                float heightE =1.0f; // this.getFluidHeight(lightReaderIn, posIn.east(), fluidStateIn.getFluid());
//                double d0 = (double) (posIn.getX() & 15);
//                double d1 = (double) (posIn.getY() & 15);
//                double d2 = (double) (posIn.getZ() & 15);
//                float f11 = 0.001F;
//                float f12 = renderDown ? 0.001F : 0.0F;
//                if (renderUp && !func_239283_a_(lightReaderIn, posIn, Direction.UP, height)) {
//                    flag7 = true;
//                    height -= 0.001F;
//                    heightS -= 0.001F;
//                    heightSE -= 0.001F;
//                    heightE -= 0.001F;
//                    Vector3d vector3d = fluidStateIn.getFlow(lightReaderIn, posIn);
//                    float f13;
//                    float f14;
//                    float f15;
//                    float f16;
//                    float f17;
//                    float f18;
//                    float f19;
//                    float f20;
////                    if (vector3d.x == 0.0D && vector3d.z == 0.0D) {
//                        TextureAtlasSprite textureatlassprite1 = atextureatlassprite[0];
//                        f13 = textureatlassprite1.getInterpolatedU(0.0D);
//                        f17 = textureatlassprite1.getInterpolatedV(0.0D);
//                        f14 = f13;
//                        f18 = textureatlassprite1.getInterpolatedV(16.0D);
//                        f15 = textureatlassprite1.getInterpolatedU(16.0D);
//                        f19 = f18;
//                        f16 = f15;
//                        f20 = f17;
////                    } else {
////                        TextureAtlasSprite textureatlassprite = atextureatlassprite[1];
////                        float f21 = (float) MathHelper.atan2(vector3d.z, vector3d.x) - ((float) Math.PI / 2F);
////                        float f22 = MathHelper.sin(f21) * 0.25F;
////                        float f23 = MathHelper.cos(f21) * 0.25F;
////                        float f24 = 8.0F;
////                        f13 = textureatlassprite.getInterpolatedU((double) (8.0F + (-f23 - f22) * 16.0F));
////                        f17 = textureatlassprite.getInterpolatedV((double) (8.0F + (-f23 + f22) * 16.0F));
////                        f14 = textureatlassprite.getInterpolatedU((double) (8.0F + (-f23 + f22) * 16.0F));
////                        f18 = textureatlassprite.getInterpolatedV((double) (8.0F + (f23 + f22) * 16.0F));
////                        f15 = textureatlassprite.getInterpolatedU((double) (8.0F + (f23 + f22) * 16.0F));
////                        f19 = textureatlassprite.getInterpolatedV((double) (8.0F + (f23 - f22) * 16.0F));
////                        f16 = textureatlassprite.getInterpolatedU((double) (8.0F + (f23 - f22) * 16.0F));
////                        f20 = textureatlassprite.getInterpolatedV((double) (8.0F + (-f23 - f22) * 16.0F));
////                    }
//
//                    float f43 = (f13 + f14 + f15 + f16) / 4.0F;
//                    float f44 = (f17 + f18 + f19 + f20) / 4.0F;
//                    float f45 = (float) atextureatlassprite[0].getWidth() / (atextureatlassprite[0].getMaxU() - atextureatlassprite[0].getMinU());
//                    float f46 = (float) atextureatlassprite[0].getHeight() / (atextureatlassprite[0].getMaxV() - atextureatlassprite[0].getMinV());
//                    float f47 = 4.0F / Math.max(f46, f45);
//                    f13 = MathHelper.lerp(f47, f13, f43);
//                    f14 = MathHelper.lerp(f47, f14, f43);
//                    f15 = MathHelper.lerp(f47, f15, f43);
//                    f16 = MathHelper.lerp(f47, f16, f43);
//                    f17 = MathHelper.lerp(f47, f17, f44);
//                    f18 = MathHelper.lerp(f47, f18, f44);
//                    f19 = MathHelper.lerp(f47, f19, f44);
//                    f20 = MathHelper.lerp(f47, f20, f44);
//                    int j = this.getCombinedAverageLight(lightReaderIn, posIn);
//                    float f25 = shadeUp * f;
//                    float f26 = shadeUp * f1;
//                    float f27 = shadeUp * f2;
//                    this.vertexVanilla(vertexBuilderIn, d0 + 0.0D, d1 + (double) height, d2 + 0.0D, f25, f26, f27, alpha, f13, f17, j);
//                    this.vertexVanilla(vertexBuilderIn, d0 + 0.0D, d1 + (double) heightS, d2 + 1.0D, f25, f26, f27, alpha, f14, f18, j);
//                    this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) heightSE, d2 + 1.0D, f25, f26, f27, alpha, f15, f19, j);
//                    this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) heightE, d2 + 0.0D, f25, f26, f27, alpha, f16, f20, j);
//                    if (fluidStateIn.shouldRenderSides(lightReaderIn, posIn.up())) {
//                        this.vertexVanilla(vertexBuilderIn, d0 + 0.0D, d1 + (double) height, d2 + 0.0D, f25, f26, f27, alpha, f13, f17, j);
//                        this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) heightE, d2 + 0.0D, f25, f26, f27, alpha, f16, f20, j);
//                        this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) heightSE, d2 + 1.0D, f25, f26, f27, alpha, f15, f19, j);
//                        this.vertexVanilla(vertexBuilderIn, d0 + 0.0D, d1 + (double) heightS, d2 + 1.0D, f25, f26, f27, alpha, f14, f18, j);
//                    }
//                }
//
//                if (renderDown) {
//                    float f34 = atextureatlassprite[0].getMinU();
//                    float f35 = atextureatlassprite[0].getMaxU();
//                    float f37 = atextureatlassprite[0].getMinV();
//                    float f39 = atextureatlassprite[0].getMaxV();
//                    int i1 = this.getCombinedAverageLight(lightReaderIn, posIn.down());
//                    float f40 = shadeDown * f;
//                    float f41 = shadeDown * f1;
//                    float f42 = shadeDown * f2;
//                    this.vertexVanilla(vertexBuilderIn, d0, d1 + (double) f12, d2 + 1.0D, f40, f41, f42, alpha, f34, f39, i1);
//                    this.vertexVanilla(vertexBuilderIn, d0, d1 + (double) f12, d2, f40, f41, f42, alpha, f34, f37, i1);
//                    this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) f12, d2, f40, f41, f42, alpha, f35, f37, i1);
//                    this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) f12, d2 + 1.0D, f40, f41, f42, alpha, f35, f39, i1);
//                    flag7 = true;
//                }
//
//                for (int l = 0; l < 4; ++l) {
//                    float f36 = 1.0f;
//                    float f38 = 1.0f;
//                    double d3;
//                    double d4;
//                    double d5;
//                    double d6;
//                    Direction direction;
//                    boolean flag8;
//                    if (l == 0) {
////                        f36 = height;
////                        f38 = heightE;
//                        d3 = d0;
//                        d5 = d0 + 1.0D;
//                        d4 = d2 + (double) 0.001F;
//                        d6 = d2 + (double) 0.001F;
//                        direction = Direction.NORTH;
//                        flag8 = renderNorth;
//                    } else if (l == 1) {
////                        f36 = heightSE;
////                        f38 = heightS;
//                        d3 = d0 + 1.0D;
//                        d5 = d0;
//                        d4 = d2 + 1.0D - (double) 0.001F;
//                        d6 = d2 + 1.0D - (double) 0.001F;
//                        direction = Direction.SOUTH;
//                        flag8 = renderSouth;
//                    } else if (l == 2) {
////                        f36 = heightS;
////                        f38 = height;
//                        d3 = d0 + (double) 0.001F;
//                        d5 = d0 + (double) 0.001F;
//                        d4 = d2 + 1.0D;
//                        d6 = d2;
//                        direction = Direction.WEST;
//                        flag8 = renderWest;
//                    } else {
////                        f36 = heightE;
////                        f38 = heightSE;
//                        d3 = d0 + 1.0D - (double) 0.001F;
//                        d5 = d0 + 1.0D - (double) 0.001F;
//                        d4 = d2;
//                        d6 = d2 + 1.0D;
//                        direction = Direction.EAST;
//                        flag8 = renderEast;
//                    }
//
//                    if (flag8 && !func_239283_a_(lightReaderIn, posIn, direction, 1.0f)) {
//                        flag7 = true;
//                        BlockPos blockpos = posIn.offset(direction);
////                        TextureAtlasSprite textureatlassprite2 = atextureatlassprite[1];
//                        TextureAtlasSprite textureatlassprite2 = atextureatlassprite[0];
//                        if (atextureatlassprite[2] != null) {
//                            if (lightReaderIn.getBlockState(blockpos).shouldDisplayFluidOverlay(lightReaderIn, blockpos, fluidStateIn)) {
//                                textureatlassprite2 = atextureatlassprite[2];
//                            }
//                        }
//
//                        float f48 = textureatlassprite2.getInterpolatedU(0.0D);
////                        float f49 = textureatlassprite2.getInterpolatedU(8.0D);
//                        float f49 = textureatlassprite2.getInterpolatedU(16.0D);
////                        float f50 = textureatlassprite2.getInterpolatedV((double) ((1.0F - f36) * 16.0F * 0.5F));
//                        float f50 = textureatlassprite2.getInterpolatedV((double)0);
////                        float f28 = textureatlassprite2.getInterpolatedV((double) ((1.0F - f38) * 16.0F * 0.5F));
//                        float f28 = textureatlassprite2.getInterpolatedV((double) 0);
////                        float f29 = textureatlassprite2.getInterpolatedV(8.0D);
//                        float f29 = textureatlassprite2.getInterpolatedV(16.0D);
//                        int k = this.getCombinedAverageLight(lightReaderIn, blockpos);
//                        float f30 = l < 2 ? shadeNorth : shadeWest;
//                        float f31 = shadeUp * f30 * f;
//                        float f32 = shadeUp * f30 * f1;
//                        float f33 = shadeUp * f30 * f2;
//                        this.vertexVanilla(vertexBuilderIn, d3, d1 + (double) f36, d4, f31, f32, f33, alpha, f48, f50, k);
//                        this.vertexVanilla(vertexBuilderIn, d5, d1 + (double) f38, d6, f31, f32, f33, alpha, f49, f28, k);
//                        this.vertexVanilla(vertexBuilderIn, d5, d1 + (double) f12, d6, f31, f32, f33, alpha, f49, f29, k);
//                        this.vertexVanilla(vertexBuilderIn, d3, d1 + (double) f12, d4, f31, f32, f33, alpha, f48, f29, k);
//                        if (textureatlassprite2 != atextureatlassprite[2]) {
//                            this.vertexVanilla(vertexBuilderIn, d3, d1 + (double) f12, d4, f31, f32, f33, alpha, f48, f29, k);
//                            this.vertexVanilla(vertexBuilderIn, d5, d1 + (double) f12, d6, f31, f32, f33, alpha, f49, f29, k);
//                            this.vertexVanilla(vertexBuilderIn, d5, d1 + (double) f38, d6, f31, f32, f33, alpha, f49, f28, k);
//                            this.vertexVanilla(vertexBuilderIn, d3, d1 + (double) f36, d4, f31, f32, f33, alpha, f48, f50, k);
//                        }
//                    }
//                }
//
////                return flag7;
//                cir.setReturnValue(flag7);
//            }
//        }
//    }
//
//    @Override
//    public boolean renderWithAlpha(IBlockDisplayReader lightReaderIn, BlockPos posIn, IVertexBuilder vertexBuilderIn, FluidState fluidStateIn, float alphaIn) {
//        boolean flag = fluidStateIn.isTagged(FluidTags.LAVA);
//        TextureAtlasSprite[] atextureatlassprite = net.minecraftforge.client.ForgeHooksClient.getFluidSprites(lightReaderIn, posIn, fluidStateIn);
//        BlockState blockstate = lightReaderIn.getBlockState(posIn);
//        int i = fluidStateIn.getFluid().getAttributes().getColor(lightReaderIn, posIn);
//        float alpha = (float) (i >> 24 & 255) / 255.0F * alphaIn;
//        float f = (float) (i >> 16 & 255) / 255.0F;
//        float f1 = (float) (i >> 8 & 255) / 255.0F;
//        float f2 = (float) (i & 255) / 255.0F;
//        boolean flag1 = !isAdjacentFluidSameAs(lightReaderIn, posIn, Direction.UP, fluidStateIn);
//        boolean flag2 = shouldRenderFace(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.DOWN) && !func_239283_a_(lightReaderIn, posIn, Direction.DOWN, 0.8888889F);
//        boolean flag3 = shouldRenderFace(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.NORTH);
//        boolean flag4 = shouldRenderFace(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.SOUTH);
//        boolean flag5 = shouldRenderFace(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.WEST);
//        boolean flag6 = shouldRenderFace(lightReaderIn, posIn, fluidStateIn, blockstate, Direction.EAST);
//        if (!flag1 && !flag2 && !flag6 && !flag5 && !flag3 && !flag4 && false) {
//            return false;
//        } else {
//            boolean flag7 = false;
//            float f3 = lightReaderIn.func_230487_a_(Direction.DOWN, true);
//            float f4 = lightReaderIn.func_230487_a_(Direction.UP, true);
//            float f5 = lightReaderIn.func_230487_a_(Direction.NORTH, true);
//            float f6 = lightReaderIn.func_230487_a_(Direction.WEST, true);
//            float f7 = this.getFluidHeight(lightReaderIn, posIn, fluidStateIn.getFluid());
//            float f8 = this.getFluidHeight(lightReaderIn, posIn.south(), fluidStateIn.getFluid());
//            float f9 = this.getFluidHeight(lightReaderIn, posIn.east().south(), fluidStateIn.getFluid());
//            float f10 = this.getFluidHeight(lightReaderIn, posIn.east(), fluidStateIn.getFluid());
//            double d0 = (double) (posIn.getX() & 15);
//            double d1 = (double) (posIn.getY() & 15);
//            double d2 = (double) (posIn.getZ() & 15);
//            float f11 = 0.001F;
//            float f12 = flag2 ? 0.001F : 0.0F;
//            if (flag1 && !func_239283_a_(lightReaderIn, posIn, Direction.UP, Math.min(Math.min(f7, f8), Math.min(f9, f10)))) {
//                flag7 = true;
//                f7 -= 0.001F;
//                f8 -= 0.001F;
//                f9 -= 0.001F;
//                f10 -= 0.001F;
//                Vector3d vector3d = fluidStateIn.getFlow(lightReaderIn, posIn);
//                float f13;
//                float f14;
//                float f15;
//                float f16;
//                float f17;
//                float f18;
//                float f19;
//                float f20;
//                if (vector3d.x == 0.0D && vector3d.z == 0.0D) {
//                    TextureAtlasSprite textureatlassprite1 = atextureatlassprite[0];
//                    f13 = textureatlassprite1.getInterpolatedU(0.0D);
//                    f17 = textureatlassprite1.getInterpolatedV(0.0D);
//                    f14 = f13;
//                    f18 = textureatlassprite1.getInterpolatedV(16.0D);
//                    f15 = textureatlassprite1.getInterpolatedU(16.0D);
//                    f19 = f18;
//                    f16 = f15;
//                    f20 = f17;
//                } else {
//                    TextureAtlasSprite textureatlassprite = atextureatlassprite[1];
//                    float f21 = (float) MathHelper.atan2(vector3d.z, vector3d.x) - ((float) Math.PI / 2F);
//                    float f22 = MathHelper.sin(f21) * 0.25F;
//                    float f23 = MathHelper.cos(f21) * 0.25F;
//                    float f24 = 8.0F;
//                    f13 = textureatlassprite.getInterpolatedU((double) (8.0F + (-f23 - f22) * 16.0F));
//                    f17 = textureatlassprite.getInterpolatedV((double) (8.0F + (-f23 + f22) * 16.0F));
//                    f14 = textureatlassprite.getInterpolatedU((double) (8.0F + (-f23 + f22) * 16.0F));
//                    f18 = textureatlassprite.getInterpolatedV((double) (8.0F + (f23 + f22) * 16.0F));
//                    f15 = textureatlassprite.getInterpolatedU((double) (8.0F + (f23 + f22) * 16.0F));
//                    f19 = textureatlassprite.getInterpolatedV((double) (8.0F + (f23 - f22) * 16.0F));
//                    f16 = textureatlassprite.getInterpolatedU((double) (8.0F + (f23 - f22) * 16.0F));
//                    f20 = textureatlassprite.getInterpolatedV((double) (8.0F + (-f23 - f22) * 16.0F));
//                }
//
//                float f43 = (f13 + f14 + f15 + f16) / 4.0F;
//                float f44 = (f17 + f18 + f19 + f20) / 4.0F;
//                float f45 = (float) atextureatlassprite[0].getWidth() / (atextureatlassprite[0].getMaxU() - atextureatlassprite[0].getMinU());
//                float f46 = (float) atextureatlassprite[0].getHeight() / (atextureatlassprite[0].getMaxV() - atextureatlassprite[0].getMinV());
//                float f47 = 4.0F / Math.max(f46, f45);
//                f13 = MathHelper.lerp(f47, f13, f43);
//                f14 = MathHelper.lerp(f47, f14, f43);
//                f15 = MathHelper.lerp(f47, f15, f43);
//                f16 = MathHelper.lerp(f47, f16, f43);
//                f17 = MathHelper.lerp(f47, f17, f44);
//                f18 = MathHelper.lerp(f47, f18, f44);
//                f19 = MathHelper.lerp(f47, f19, f44);
//                f20 = MathHelper.lerp(f47, f20, f44);
//                int j = this.getCombinedAverageLight(lightReaderIn, posIn);
//                float f25 = f4 * f;
//                float f26 = f4 * f1;
//                float f27 = f4 * f2;
//                this.vertexVanilla(vertexBuilderIn, d0 + 0.0D, d1 + (double) f7, d2 + 0.0D, f25, f26, f27, alpha, f13, f17, j);
//                this.vertexVanilla(vertexBuilderIn, d0 + 0.0D, d1 + (double) f8, d2 + 1.0D, f25, f26, f27, alpha, f14, f18, j);
//                this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) f9, d2 + 1.0D, f25, f26, f27, alpha, f15, f19, j);
//                this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) f10, d2 + 0.0D, f25, f26, f27, alpha, f16, f20, j);
//                if (fluidStateIn.shouldRenderSides(lightReaderIn, posIn.up())) {
//                    this.vertexVanilla(vertexBuilderIn, d0 + 0.0D, d1 + (double) f7, d2 + 0.0D, f25, f26, f27, alpha, f13, f17, j);
//                    this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) f10, d2 + 0.0D, f25, f26, f27, alpha, f16, f20, j);
//                    this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) f9, d2 + 1.0D, f25, f26, f27, alpha, f15, f19, j);
//                    this.vertexVanilla(vertexBuilderIn, d0 + 0.0D, d1 + (double) f8, d2 + 1.0D, f25, f26, f27, alpha, f14, f18, j);
//                }
//            }
//
//            if (flag2) {
//                float f34 = atextureatlassprite[0].getMinU();
//                float f35 = atextureatlassprite[0].getMaxU();
//                float f37 = atextureatlassprite[0].getMinV();
//                float f39 = atextureatlassprite[0].getMaxV();
//                int i1 = this.getCombinedAverageLight(lightReaderIn, posIn.down());
//                float f40 = f3 * f;
//                float f41 = f3 * f1;
//                float f42 = f3 * f2;
//                this.vertexVanilla(vertexBuilderIn, d0, d1 + (double) f12, d2 + 1.0D, f40, f41, f42, alpha, f34, f39, i1);
//                this.vertexVanilla(vertexBuilderIn, d0, d1 + (double) f12, d2, f40, f41, f42, alpha, f34, f37, i1);
//                this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) f12, d2, f40, f41, f42, alpha, f35, f37, i1);
//                this.vertexVanilla(vertexBuilderIn, d0 + 1.0D, d1 + (double) f12, d2 + 1.0D, f40, f41, f42, alpha, f35, f39, i1);
//                flag7 = true;
//            }
//
//            for (int l = 0; l < 4; ++l) {
//                float f36;
//                float f38;
//                double d3;
//                double d4;
//                double d5;
//                double d6;
//                Direction direction;
//                boolean flag8;
//                if (l == 0) {
//                    f36 = f7;
//                    f38 = f10;
//                    d3 = d0;
//                    d5 = d0 + 1.0D;
//                    d4 = d2 + (double) 0.001F;
//                    d6 = d2 + (double) 0.001F;
//                    direction = Direction.NORTH;
//                    flag8 = flag3;
//                } else if (l == 1) {
//                    f36 = f9;
//                    f38 = f8;
//                    d3 = d0 + 1.0D;
//                    d5 = d0;
//                    d4 = d2 + 1.0D - (double) 0.001F;
//                    d6 = d2 + 1.0D - (double) 0.001F;
//                    direction = Direction.SOUTH;
//                    flag8 = flag4;
//                } else if (l == 2) {
//                    f36 = f8;
//                    f38 = f7;
//                    d3 = d0 + (double) 0.001F;
//                    d5 = d0 + (double) 0.001F;
//                    d4 = d2 + 1.0D;
//                    d6 = d2;
//                    direction = Direction.WEST;
//                    flag8 = flag5;
//                } else {
//                    f36 = f10;
//                    f38 = f9;
//                    d3 = d0 + 1.0D - (double) 0.001F;
//                    d5 = d0 + 1.0D - (double) 0.001F;
//                    d4 = d2;
//                    d6 = d2 + 1.0D;
//                    direction = Direction.EAST;
//                    flag8 = flag6;
//                }
//
//                if (flag8 && !func_239283_a_(lightReaderIn, posIn, direction, Math.max(f36, f38))) {
//                    flag7 = true;
//                    BlockPos blockpos = posIn.offset(direction);
//                    TextureAtlasSprite textureatlassprite2 = atextureatlassprite[1];
//                    if (atextureatlassprite[2] != null) {
//                        if (lightReaderIn.getBlockState(blockpos).shouldDisplayFluidOverlay(lightReaderIn, blockpos, fluidStateIn)) {
//                            textureatlassprite2 = atextureatlassprite[2];
//                        }
//                    }
//
//                    float f48 = textureatlassprite2.getInterpolatedU(0.0D);
//                    float f49 = textureatlassprite2.getInterpolatedU(8.0D);
//                    float f50 = textureatlassprite2.getInterpolatedV((double) ((1.0F - f36) * 16.0F * 0.5F));
//                    float f28 = textureatlassprite2.getInterpolatedV((double) ((1.0F - f38) * 16.0F * 0.5F));
//                    float f29 = textureatlassprite2.getInterpolatedV(8.0D);
//                    int k = this.getCombinedAverageLight(lightReaderIn, blockpos);
//                    float f30 = l < 2 ? f5 : f6;
//                    float f31 = f4 * f30 * f;
//                    float f32 = f4 * f30 * f1;
//                    float f33 = f4 * f30 * f2;
//                    this.vertexVanilla(vertexBuilderIn, d3, d1 + (double) f36, d4, f31, f32, f33, alpha, f48, f50, k);
//                    this.vertexVanilla(vertexBuilderIn, d5, d1 + (double) f38, d6, f31, f32, f33, alpha, f49, f28, k);
//                    this.vertexVanilla(vertexBuilderIn, d5, d1 + (double) f12, d6, f31, f32, f33, alpha, f49, f29, k);
//                    this.vertexVanilla(vertexBuilderIn, d3, d1 + (double) f12, d4, f31, f32, f33, alpha, f48, f29, k);
//                    if (textureatlassprite2 != atextureatlassprite[2]) {
//                        this.vertexVanilla(vertexBuilderIn, d3, d1 + (double) f12, d4, f31, f32, f33, alpha, f48, f29, k);
//                        this.vertexVanilla(vertexBuilderIn, d5, d1 + (double) f12, d6, f31, f32, f33, alpha, f49, f29, k);
//                        this.vertexVanilla(vertexBuilderIn, d5, d1 + (double) f38, d6, f31, f32, f33, alpha, f49, f28, k);
//                        this.vertexVanilla(vertexBuilderIn, d3, d1 + (double) f36, d4, f31, f32, f33, alpha, f48, f50, k);
//                    }
//                }
//            }
//
//            return flag7;
//        }
//    }
//}

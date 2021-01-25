package net.oriondevcorgitaco.unearthed.datagen;

import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.StoneTiers;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Unearthed.MOD_ID, exFileHelper);
    }

    private void buttonBlock(AbstractButtonBlock block, ResourceLocation texture) {
        getVariantBuilder(block).forAllStates(state -> {
            ModelFile buttonModel = buttonModel(block, texture, state.get(AbstractButtonBlock.POWERED));
            AttachFace face = state.get(AbstractButtonBlock.FACE);
            Direction dir = state.get(AbstractButtonBlock.HORIZONTAL_FACING);
            int xrot = 0;
            if (face == AttachFace.CEILING) {
                dir = dir.getOpposite();
                xrot = 180;
            } else if (face == AttachFace.WALL) {
                xrot = 270;
            }
            return ConfiguredModel.builder().modelFile(buttonModel)
                    .rotationY((int) dir.getHorizontalAngle())
                    .rotationX(xrot)
                    .uvLock(face == AttachFace.WALL).build();
        });
    }

    private ModelFile buttonModel(Block block, ResourceLocation texture, boolean powered) {
        String suffix = (powered ? "_pressed" : "");
        return models().singleTexture(block.getRegistryName().getPath() + suffix, mcLoc("block/button" + suffix), "texture", texture);
    }

    private void buttonInventory(Block block, ResourceLocation texture) {
        models().singleTexture(block.getRegistryName().getPath() + "_inventory", mcLoc("block/button_inventory"), "texture", texture);
    }

    private void pressurePlateBlock(PressurePlateBlock block, ResourceLocation texture) {
        getVariantBuilder(block).forAllStates(state ->
                ConfiguredModel.builder().modelFile(pressurePlateModel(block, texture, state.get(PressurePlateBlock.POWERED))).build());
    }

    private ModelFile pressurePlateModel(Block block, ResourceLocation texture, boolean powered) {
        String modelSuffix = (powered ? "_down" : "");
        String parentSuffix = (powered ? "_down" : "_up");
        return models().withExistingParent(block.getRegistryName().getPath() + modelSuffix, mcLoc("block/pressure_plate" + parentSuffix))
                .texture("texture", texture);
    }

    private ModelFile oreModel(Block baseBlock, Forms.OreForm form) {
        IOreType oreType = form.getOreType();
        String stoneTexture = "block/" + getPath(baseBlock);
        String modelName = getPath(baseBlock) + "_" + oreType.getName() + "_ore";
        String namespace = baseBlock.getRegistryName().getNamespace();
        if (form.isSideTopBlock()) {
            return models().cubeTop(modelName, modLoc("block/ore/" + modelName), new ResourceLocation(namespace, stoneTexture + "_top"));
        } else {
            return models().cubeAll(modelName, modLoc("block/ore/" + modelName));
        }
    }

    private void grassyBlock(Block block, ResourceLocation baseTexture) {
        ResourceLocation grass_top = mcLoc("block/grass_block_top");
        ResourceLocation grass_overlay = mcLoc("block/grass_block_side_overlay");
        String grassName = block.getRegistryName().getPath();
        ModelFile grassyBlock = overlayBlock(grassName, baseTexture, grass_top, modLoc("block/side/" + grassName + "_side"), grass_overlay, true);
        String snowName = block.getRegistryName().getPath() + "_snow";
        ModelFile snowyBlock = models().cubeTop(snowName, modLoc("block/side/" + snowName), grass_top);

        getVariantBuilder(block).partialState().with(BlockStateProperties.SNOWY, false).modelForState()
                .modelFile(grassyBlock).nextModel().modelFile(grassyBlock).rotationY(90).nextModel().modelFile(grassyBlock).rotationY(180).modelFile(grassyBlock).rotationY(270).addModel()
                .partialState().with(BlockStateProperties.SNOWY, true).modelForState()
                .modelFile(snowyBlock).addModel();
    }

    private ModelFile overlayBlock(String name, ResourceLocation bottom, ResourceLocation top, ResourceLocation side, ResourceLocation overlay, boolean tint) {
        ModelFile parent = tint ? new ModelFile.UncheckedModelFile(modLoc("block/ore/tinted_overlay_horizontal")) : new ModelFile.UncheckedModelFile(modLoc("block/ore/overlay_horizontal"));
        return models().getBuilder(name).parent(parent)
                .texture("side", side)
                .texture("top", top)
                .texture("bottom", bottom)
                .texture("overlay", overlay);
    }

    private void sixWayBlock(Block block, float apothem, ResourceLocation texture) {
        String name = "block/" + block.getRegistryName().getPath();
        ModelFile baseCube = models().getBuilder(name)
                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
                .element().allFaces(($, f) -> {
                    f.texture("#texture").tintindex(0);
                    if ($ != Direction.UP) f.cullface($);
                }).from(0, 0, 0).to(16, apothem, 16).end()
                .texture("texture", texture).texture("particle", "#texture");
//        ModelFile sideCube = models().getBuilder(name + "_side")
//                .parent(new ModelFile.UncheckedModelFile(mcLoc("block/block")))
//                .element().allFaces(($, f) -> {
//                    f.texture("#texture").tintindex(0);
//                    if ($.getAxis() == Direction.Axis.Y)
//                }).from(0, 0, 0).to(16, 16, apothem).end()
//                .texture("texture", texture).texture("particle", "#texture");
        getMultipartBuilder(block)
                .part().modelFile(baseCube).addModel().condition(SixWayBlock.DOWN, true).end()
                .part().modelFile(baseCube).rotationX(180).addModel().condition(SixWayBlock.UP, true).end()
                .part().modelFile(baseCube).rotationX(270).addModel().condition(SixWayBlock.NORTH, true).end()
                .part().modelFile(baseCube).rotationX(270).rotationY(270).addModel().condition(SixWayBlock.WEST, true).end()
                .part().modelFile(baseCube).rotationX(270).rotationY(180).addModel().condition(SixWayBlock.SOUTH, true).end()
                .part().modelFile(baseCube).rotationX(270).rotationY(90).addModel().condition(SixWayBlock.EAST, true).end();
    }

    @Override
    protected void registerStatesAndModels() {
        for (BlockGeneratorHelper schema : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : schema.getEntries()) {
                ResourceLocation baseBlock = schema.getBaseBlock(entry.getVariant()).getRegistryName();
                String id = baseBlock.getPath();
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();

                ResourceLocation stoneTexture = modLoc("block/" + id);
                ResourceLocation topTexture = stoneTexture;
                if (form.isSideTopBlock()) {
                    topTexture = modLoc("block/" + id + "_top");
                }

                if (form == Forms.BLOCK || form == Forms.REGOLITH) {
                    simpleBlock(block);
                } else if (form == Forms.GRASSY_REGOLITH) {
                    grassyBlock(block, blockTexture(schema.getEntry(entry.getVariant(), Forms.REGOLITH).getBlock()));
                } else if (form == Forms.SIDETOP_BLOCK) {
                    simpleBlock(block, models().cubeTop(id, stoneTexture, topTexture));
                } else if (form == Forms.AXISBLOCK) {
                    axisBlock((RotatedPillarBlock) block);
                } else if (form == Forms.SLAB || form == Forms.SIDETOP_SLAB) {
                    slabBlock((SlabBlock) block, baseBlock, stoneTexture, topTexture, topTexture);
                } else if (form == Forms.STAIRS || form == Forms.SIDETOP_STAIRS) {
                    stairsBlock((StairsBlock) block, stoneTexture, topTexture, topTexture);
                } else if (form == Forms.WALLS) {
                    wallBlock((WallBlock) block, stoneTexture);
                    models().wallInventory(entry.getId() + "_inventory", stoneTexture);
                } else if (form == Forms.PRESSURE_PLATE) {
                    pressurePlateBlock((PressurePlateBlock) block, stoneTexture);
                } else if (form == Forms.BUTTON) {
                    buttonBlock((AbstractButtonBlock) block, stoneTexture);
                    buttonInventory(block, stoneTexture);
                } else if (form instanceof Forms.OreForm) {
                    if (form == Forms.KIMBERLITE_DIAMOND_ORE) {
                        simpleBlock(block);
                    } else {
                        simpleBlock(block, oreModel(schema.getBaseBlock(), (Forms.OreForm) form));
                    }
                } else if (form == Forms.OVERGROWN_ROCK) {
                    grassyBlock(block, blockTexture(schema.getBaseBlock()));
                }
            }
        }
        sideTopBlock(UEBlocks.LIGNITE_BRIQUETTES);
        sixWayBlock(UEBlocks.LICHEN, 0.5f, modLoc("block/lichen"));
    }

    private void sideTopBlock(Block block) {
        String id = block.getRegistryName().getPath();
        simpleBlock(block, models().cubeTop(id, blockTexture(block), modLoc("block/" + id + "_top")));
    }

    private String getPath(Block ingredient) {
        return ingredient.getRegistryName().getPath();
    }

}

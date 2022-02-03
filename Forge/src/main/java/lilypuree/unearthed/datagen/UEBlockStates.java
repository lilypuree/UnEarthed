package lilypuree.unearthed.datagen;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.properties.ModBlockProperties;
import lilypuree.unearthed.block.schema.BlockForm;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.Forms;
import lilypuree.unearthed.block.type.IOreType;
import lilypuree.unearthed.core.UEBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class UEBlockStates extends BlockStateProvider {

    public UEBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MOD_ID, exFileHelper);
    }

    private ModelFile oreModel(Block baseBlock, Forms.OreForm form, boolean isSideTop) {
        IOreType oreType = form.getOreType();
        String stoneTexture = "block/" + getPath(baseBlock);
        String modelName = getPath(baseBlock) + "_" + oreType.getName() + "_ore";
        String namespace = baseBlock.getRegistryName().getNamespace();
        if (isSideTop) {
            ResourceLocation topTexture = new ResourceLocation(namespace, stoneTexture + "_top");
            return models().cubeBottomTop(modelName, modLoc("block/ore/" + modelName), topTexture, topTexture);
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
                .part().modelFile(baseCube).addModel().condition(PipeBlock.DOWN, true).end()
                .part().modelFile(baseCube).rotationX(180).addModel().condition(PipeBlock.UP, true).end()
                .part().modelFile(baseCube).rotationX(270).addModel().condition(PipeBlock.NORTH, true).end()
                .part().modelFile(baseCube).rotationX(270).rotationY(270).addModel().condition(PipeBlock.WEST, true).end()
                .part().modelFile(baseCube).rotationX(270).rotationY(180).addModel().condition(PipeBlock.SOUTH, true).end()
                .part().modelFile(baseCube).rotationX(270).rotationY(90).addModel().condition(PipeBlock.EAST, true).end();
    }

    public void beamBlock(Block block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        ModelFile beamXModel = beamModel(block, Direction.Axis.X);
        ModelFile beamYModel = beamModel(block, Direction.Axis.Y);
        ModelFile beamZModel = beamModel(block, Direction.Axis.Z);
        builder.partialState().with(BlockStateProperties.AXIS, Direction.Axis.X)
                .modelForState().modelFile(beamXModel).addModel();
        builder.partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(beamYModel).addModel();
        builder.partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(beamZModel).addModel();
    }

    public ModelFile beamModel(Block block, Direction.Axis axis) {
        String name = getPath(block);
        return models().getBuilder(name + "_" + axis.getName()).parent(
                        new ModelFile.UncheckedModelFile(modLoc("block/beam_" + axis.getName())))
                .texture("side", modLoc("block/" + name + "_side"))
                .texture("end", modLoc("block/" + name + "_end"));
    }


    private ModelFile simpleSideTopBottom(String name, ResourceLocation parent, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
        return models().getBuilder(name).parent(new ModelFile.UncheckedModelFile(parent))
                .texture("side", side)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    public void sixwaySlabBlock(Block block, ResourceLocation doubleSlab, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
        models().slab(getPath(block), side, bottom, top);
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);
        DirectionProperty facing = BlockStateProperties.FACING;
        DirectionProperty secondary = ModBlockProperties.SECONDARY_FACING;
        ModelFile[][][] parts = blockEightParts();
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    Direction xDir = x == 0 ? Direction.WEST : Direction.EAST;
                    Direction yDir = y == 0 ? Direction.DOWN : Direction.UP;
                    Direction zDir = z == 0 ? Direction.NORTH : Direction.SOUTH;
                    ModelFile part = models().getBuilder(getPath(block) + "_part_" + x + "_" + y + "_" + z).texture("particle", side).texture("side", side).texture("top", top).texture("bottom", bottom).parent(parts[x][y][z]);
                    builder.part().modelFile(part).addModel().condition(facing, yDir);
                    builder.part().modelFile(part).addModel().condition(facing, yDir.getOpposite()).condition(secondary, xDir);
                    builder.part().modelFile(part).addModel().condition(facing, yDir.getOpposite()).condition(secondary, zDir);
                    builder.part().modelFile(part).addModel().condition(facing, yDir.getOpposite()).condition(secondary, yDir);
                    yDir = y == 0 ? Direction.SOUTH : Direction.NORTH;
                    zDir = z == 0 ? Direction.DOWN : Direction.UP;
                    builder.part().modelFile(part).rotationX(90).addModel().condition(facing, yDir);
                    builder.part().modelFile(part).rotationX(90).addModel().condition(facing, yDir.getOpposite()).condition(secondary, xDir);
                    builder.part().modelFile(part).rotationX(90).addModel().condition(facing, yDir.getOpposite()).condition(secondary, zDir);
                    builder.part().modelFile(part).rotationX(90).addModel().condition(facing, yDir.getOpposite()).condition(secondary, yDir);
                    xDir = x == 0 ? Direction.NORTH : Direction.SOUTH;
                    yDir = y == 0 ? Direction.WEST : Direction.EAST;
                    builder.part().modelFile(part).rotationX(90).rotationY(90).addModel().condition(facing, yDir);
                    builder.part().modelFile(part).rotationX(90).rotationY(90).addModel().condition(facing, yDir.getOpposite()).condition(secondary, xDir);
                    builder.part().modelFile(part).rotationX(90).rotationY(90).addModel().condition(facing, yDir.getOpposite()).condition(secondary, zDir);
                    builder.part().modelFile(part).rotationX(90).rotationY(90).addModel().condition(facing, yDir.getOpposite()).condition(secondary, yDir);
                }
            }
        }
    }

    private ModelFile[][][] blockEightParts() {
        ModelFile[][][] parts = new ModelFile[2][2][2];
        for (int x = 0; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = 0; z < 2; z++) {
                    int finalX = x;
                    int finalY = y;
                    int finalZ = z;
                    parts[x][y][z] = models().getBuilder("block_part_" + x + "_" + y + "_" + z)
                            .element().from(x * 8, y * 8, z * 8).to(8 + x * 8, 8 + y * 8, 8 + z * 8)
                            .allFaces((direction, faceBuilder) -> {
                                if (finalX == 0 && direction == Direction.WEST || finalX == 1 && direction == Direction.EAST ||
                                        finalY == 0 && direction == Direction.DOWN || finalY == 1 && direction == Direction.UP ||
                                        finalZ == 0 && direction == Direction.NORTH || finalZ == 1 && direction == Direction.SOUTH)
                                    faceBuilder.cullface(direction);
                                switch (direction.getAxis()) {
                                    case X:
                                        faceBuilder.uvs(finalZ * 8, 8 - finalY * 8, 8 + finalZ * 8, 16 - finalY * 8);
                                        faceBuilder.texture("#side");
                                        break;
                                    case Y:
                                        faceBuilder.uvs(finalX * 8, finalZ * 8, 8 + finalX * 8, 8 + finalZ * 8);
                                        faceBuilder.texture(direction == Direction.UP ? "#top" : "#bottom");
                                        break;
                                    case Z:
                                        faceBuilder.uvs(finalX * 8, 8 - finalY * 8, 8 + finalX * 8, 16 - finalY * 8);
                                        faceBuilder.texture("#side");
                                        break;
                                }
                            }).end();
                }
            }
        }
        return parts;
    }

    @Override
    protected void registerStatesAndModels() {

        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            schema.forEach((variant, entry) -> {
                ResourceLocation baseBlock = schema.getBaseBlock(variant).getRegistryName();
                String id = baseBlock.getPath();
                BlockForm form = entry.getForm();
                Block block = entry.getBlock();

                ResourceLocation stoneTexture = modLoc("block/" + id);
                ResourceLocation topTexture = stoneTexture;
                if (variant.isSideTop()) {
                    topTexture = modLoc("block/" + id + "_top");
                }
                if (form == Forms.BLOCK) {
                    if (variant.isSideTop()) {
                        simpleBlock(block, models().cubeBottomTop(id, stoneTexture, topTexture, topTexture));
                    } else {
                        simpleBlock(block);
                    }
                } else if (form == Forms.REGOLITH) {
                    simpleBlock(block);
                } else if (form == Forms.GRASSY_REGOLITH) {
                    grassyBlock(block, blockTexture(schema.getEntry(entry.getVariant(), Forms.REGOLITH).getBlock()));
                } else if (form == Forms.BEAM) {
                    beamBlock(block);
                } else if (form == Forms.AXISBLOCK) {
                    if (schema == BlockSchemas.SCHIST) {
                        axisNoHorizontalBlock((RotatedPillarBlock) block);
                    } else {
                        axisBlock((RotatedPillarBlock) block);
                    }
                } else if (form == Forms.SLAB) {
                    slabBlock((SlabBlock) block, baseBlock, stoneTexture, topTexture, topTexture);
                } else if (form == Forms.SIXWAY_SLAB) {
                    ResourceLocation side = extend(stoneTexture, "_side");
                    ResourceLocation end = extend(stoneTexture, "_end");
                    sixwaySlabBlock(block, stoneTexture, side, end, end);
                } else if (form == Forms.STAIRS) {
                    stairsBlock((StairBlock) block, stoneTexture, topTexture, topTexture);
                } else if (form == Forms.WALLS) {
                    wallBlock((WallBlock) block, stoneTexture);
                    models().wallInventory(entry.getId() + "_inventory", stoneTexture);
                } else if (form == Forms.PRESSURE_PLATE) {
                    pressurePlateBlock((PressurePlateBlock) block, stoneTexture);
                } else if (form == Forms.BUTTON) {
                    buttonBlock((ButtonBlock) block, stoneTexture);
                    models().buttonInventory(entry.getId() + "_inventory", stoneTexture);
                } else if (form instanceof Forms.OreForm) {

                    simpleBlock(block, oreModel(schema.getBaseBlock(), (Forms.OreForm) form, variant.isSideTop()));
                } else if (form == Forms.OVERGROWN_ROCK) {
                    grassyBlock(block, blockTexture(schema.getBaseBlock()));
                }
            });
        }
        sideTopBlock(UEBlocks.LIGNITE_BRIQUETTES);
        simpleBlock(UEBlocks.PYROXENE);
        sixWayBlock(UEBlocks.LICHEN, 0.5f, modLoc("block/lichen"));
    }

    public void axisNoHorizontalBlock(RotatedPillarBlock block) {
        axisNoHorizontalBlock(block, blockTexture(block));
    }

    public void axisNoHorizontalBlock(RotatedPillarBlock block, ResourceLocation baseName) {
        axisNoHorizontalBlock(block, extend(baseName, "_side"), extend(baseName, "_end"));
    }

    public void axisNoHorizontalBlock(RotatedPillarBlock block, ResourceLocation side, ResourceLocation end) {
        axisBlock(block, models().cubeColumn(getPath(block), side, end), models().cubeColumn(getPath(block) + "_horizontal", side, end));
    }

    private void sideTopBlock(Block block) {
        String id = block.getRegistryName().getPath();
        simpleBlock(block, models().cubeTop(id, blockTexture(block), modLoc("block/" + id + "_top")));
    }

    private String getPath(Block ingredient) {
        return ingredient.getRegistryName().getPath();
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }
}

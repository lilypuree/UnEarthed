package lilypuree.unearthed_forge.datagen;


import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.*;
import lilypuree.unearthed.core.UEBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class UEItemModels extends ItemModelProvider {
    public UEItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
                BlockForm form = entry.getForm();

                if (form == Forms.BUTTON || form == Forms.WALLS) {
                    blockInventoryModel(entry.getBlock());
                } else if (form == Forms.BEAM) {
                    String name = entry.getBlock().getRegistryName().getPath();
                    getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + name + "_y")));
                } else {
                    blockItemModel(entry.getBlock());
                }
            }
        }
        simpleItem(lilypuree.unearthed.core.UEItems.REGOLITH);
        simpleItem(lilypuree.unearthed.core.UEItems.GOLD_ORE);
        simpleItem(lilypuree.unearthed.core.UEItems.IRON_ORE);
        blockItemModel(UEBlocks.PYROXENE);
        blockItemModel(UEBlocks.LIGNITE_BRIQUETTES);
        blockItemModel(UEBlocks.PUDDLE);
        simpleItem(UEBlocks.LICHEN, modLoc("block/lichen"));
    }

    private void simpleItem(ItemLike provider) {
        String name = provider.asItem().getRegistryName().getPath();
        generated(name, modLoc("item/" + name));
    }

    private void simpleItem(ItemLike provider, ResourceLocation texture) {
        String name = provider.asItem().getRegistryName().getPath();
        generated(name, texture);
    }

    private void generated(String path, ResourceLocation texture) {
        getBuilder(path).parent(new ModelFile.UncheckedModelFile(mcLoc("item/generated"))).texture("layer0", texture);
    }

    private void caveWallModel(Block block) {
        String name = block.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + name + "_0")));
    }

    private void blockItemModel(Block block) {
        String name = block.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + name)));
    }

    private void blockInventoryModel(Block block) {
        String name = block.getRegistryName().getPath();
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(modLoc(ModelProvider.BLOCK_FOLDER + "/" + name + "_inventory")));
    }

    @Override
    public String getName() {
        return "Unearthed Item Models";
    }
}

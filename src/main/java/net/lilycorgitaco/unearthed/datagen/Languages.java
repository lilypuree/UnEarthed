package net.lilycorgitaco.unearthed.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.lilycorgitaco.unearthed.Unearthed;
import net.lilycorgitaco.unearthed.block.BlockGeneratorHelper;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.core.UEBlocks;
import net.lilycorgitaco.unearthed.core.UEItems;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Languages extends LanguageProvider {

    public Languages(DataGenerator gen, String locale) {
        super(gen, Unearthed.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                addBlock(entry.getBlock());
            }
        }
        addBlock(UEBlocks.LIGNITE_BRIQUETTES);
        addBlock(UEBlocks.LICHEN);
        addBlock(UEBlocks.PUDDLE);
        addItem(UEItems.GOLD_ORE);
        addItem(UEItems.IRON_ORE);
        addBlock(UEBlocks.PYROXENE);
        add("itemGroup.unearthed", "Unearthed");
//        addBlockItem(BlockGeneratorReferenceOld.BEIGE_LIMESTONE_STALACTITE, "Beige Limestone Stalactite");
//        addBlockItem(BlockGeneratorReferenceOld.BEIGE_LIMESTONE_STALAGMITE, "Beige Limestone Stalagmite");
    }


    public void addItem(Item item) {
        add(item, cap(item.getRegistryName().getPath()));
    }

    public void addBlock(Block block) {
        add(block, cap(block.getRegistryName().getPath()));
    }

//    public void addBlockItem(Block block, String name) {
//        add(block.getTranslationKey(), name);
//        add(Util.makeTranslationKey("item", cap(Registry.ITEM.getKey(block.asItem()).getPath())), name);
//    }

    private String cap(String string) {
        return Arrays.stream(StringUtils.split(string, "_")).map(StringUtils::capitalize).reduce((str1, str2) -> str1 + " " + str2).get();
    }
}
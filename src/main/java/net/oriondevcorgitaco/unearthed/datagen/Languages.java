package net.oriondevcorgitaco.unearthed.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.LanguageProvider;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEItems;
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
        addItem(UEItems.REGOLITH);
        addBlock(UEBlocks.PYROXENE);
//        addBlock(UEBlocks.MANTLE_CORE);
//        addBlock(UEBlocks.VOLCANO);
//        addBlock(UEBlocks.FAULT);
//        addBlock(UEBlocks.PLANET_WATER);
//        addBlock(UEBlocks.PLANET_LAVA);
//        addBlock(UEBlocks.SURFACE);
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
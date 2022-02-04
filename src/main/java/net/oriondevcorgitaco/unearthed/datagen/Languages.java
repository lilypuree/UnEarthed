package net.oriondevcorgitaco.unearthed.datagen;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEItems;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Languages extends LanguageProvider {
    protected String locale;

    public Languages(DataGenerator gen, String locale) {
        super(gen, Unearthed.MOD_ID, locale);
        this.locale = locale;
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
        addItem(UEItems.GOLD_ORE);
        addItem(UEItems.IRON_ORE);
        addItem(UEItems.REGOLITH);
        addBlock(UEBlocks.PYROXENE);

        add("itemGroup.unearthed", "Unearthed");

    }


    public void addItem(Item item) {
        add(item, getName(item));
    }

    public void addBlock(Block block) {
        add(block, getName(block));
    }

    private String getName(IForgeRegistryEntry<?> entry) {
        String path = entry.getRegistryName().getPath();
        if (locale.equals("en_gb")) {
            path = chiseledToChiselled(path);
        }
        return cap(path);
    }

    private String chiseledToChiselled(String name) {
        return name.replaceAll("chiseled", "chiselled");
    }

    private String cap(String string) {
        return Arrays.stream(StringUtils.split(string, "_")).map(StringUtils::capitalize).reduce((str1, str2) -> str1 + " " + str2).get();
    }
}
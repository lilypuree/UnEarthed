package lilypuree.unearthed.datagen;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.SchemaEntry;
import lilypuree.unearthed.core.UEBlocks;
import lilypuree.unearthed.core.UEItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class UELanguages extends LanguageProvider {
    protected String locale;

    public UELanguages(DataGenerator gen, String locale) {
        super(gen, Constants.MOD_ID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        for (BlockSchema type : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : type.entries()) {
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
        add("itemGroup.unearthed.general", "Unearthed");
//        addBlockItem(BlockGeneratorReferenceOld.BEIGE_LIMESTONE_STALACTITE, "Beige Limestone Stalactite");
//        addBlockItem(BlockGeneratorReferenceOld.BEIGE_LIMESTONE_STALAGMITE, "Beige Limestone Stalagmite");
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
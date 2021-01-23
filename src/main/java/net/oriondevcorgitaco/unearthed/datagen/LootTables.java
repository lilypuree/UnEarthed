package net.oriondevcorgitaco.unearthed.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.Variants;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LootTables extends LootTableProvider {
    private final DataGenerator generator;
    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }


    @Override
    public void act(DirectoryCache cache) {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();
                if (form == Forms.SLAB) {
                    lootTables.put(block, BlockLootTableAccessor.droppingSlab(block));
                } else if (form instanceof Forms.OreForm) {
                    lootTables.put(block, ((Forms.OreForm) form).getOreType().createLootFactory().apply(block));
                } else if (form == Forms.GRASSY_REGOLITH) {
                    lootTables.put(block, BlockLootTableAccessor.regolithGrassBlock(block, type.getEntry(entry.getVariant(), Forms.REGOLITH).getBlock(), Blocks.DIRT));
                } else if (form == Forms.REGOLITH) {
                    lootTables.put(block, BlockLootTableAccessor.droppingWithHoe(block, Blocks.DIRT));
                } else if (form == Forms.OVERGROWN_ROCK) {
                    lootTables.put(block, BlockLootTableAccessor.droppingWithSilkTouch(block, type.getBaseBlock()));
                } else if (entry.isBaseEntry() && type.getSchema().getVariants().contains(Variants.COBBLED)) {
                    lootTables.put(block, BlockLootTableAccessor.droppingWithSilkTouch(block, type.getBaseBlock(Variants.COBBLED)));
                } else {
                    addBlockLoot(block);
                }
            }
        }
        addBlockLoot(UEBlocks.LIGNITE_BRIQUETTES);
        lootTables.put(UEBlocks.LICHEN, BlockLootTableAccessor.onlyWithShears(UEBlocks.LICHEN));
        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParameterSet(LootParameterSets.BLOCK).build());
        }
        writeTables(cache, tables);
    }

    private void addBlockLoot(Block block) {
        lootTables.put(block, BlockLootTableAccessor.dropping(block));
    }

    private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
            } catch (IOException e) {
                e.printStackTrace();
//                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    protected static LootTable.Builder createSimpleTable(String name, Block block) {
        LootPool.Builder builder = LootPool.builder()
                .name(name)
                .rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(block));
        return LootTable.builder().addLootPool(builder);
    }
}
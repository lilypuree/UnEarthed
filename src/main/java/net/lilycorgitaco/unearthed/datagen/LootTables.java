package net.lilycorgitaco.unearthed.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.LootTablesProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.condition.TableBonusLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import net.lilycorgitaco.unearthed.block.BlockGeneratorHelper;
import net.lilycorgitaco.unearthed.block.BlockGeneratorReference;
import net.lilycorgitaco.unearthed.block.schema.BlockSchema;
import net.lilycorgitaco.unearthed.block.schema.Forms;
import net.lilycorgitaco.unearthed.block.schema.Variants;
import net.lilycorgitaco.unearthed.core.UEBlocks;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LootTables extends LootTablesProvider {
    private final DataGenerator generator;
    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public LootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }


    @Override
    public void run(DataCache cache) {
        for (BlockGeneratorHelper type : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : type.getEntries()) {
                BlockSchema.Form form = entry.getForm();
                Block block = entry.getBlock();
                if (form == Forms.SLAB || form == Forms.SIDETOP_SLAB) {
                    lootTables.put(block, BlockLootTableAccessor.slabDrops(block));
                } else if (form == Forms.SIXWAY_SLAB) {
                    lootTables.put(block, BlockLootTableAccessor.droppingSixwaySlab(block));
                } else if (form instanceof Forms.OreForm) {
                    lootTables.put(block, ((Forms.OreForm) form).getOreType().createLootFactory().apply(block));
                } else if (form == Forms.GRASSY_REGOLITH) {
                    lootTables.put(block, BlockLootTableAccessor.regolithGrassBlock(block, type.getEntry(entry.getVariant(), Forms.REGOLITH).getBlock(), Blocks.DIRT));
                } else if (form == Forms.REGOLITH) {
                    lootTables.put(block, BlockLootTableAccessor.droppingWithHoe(block, Blocks.DIRT));
                } else if (form == Forms.OVERGROWN_ROCK) {
                    lootTables.put(block, BlockLootTableAccessor.drops(block, type.getBaseBlock()));
                } else if (entry.isBaseEntry() && type.getSchema().getVariants().contains(Variants.COBBLED)) {
                    lootTables.put(block, BlockLootTableAccessor.drops(block, type.getBaseBlock(Variants.COBBLED)));
                } else {
                    addBlockLoot(block);
                }
            }
        }
        lootTables.put(UEBlocks.PYROXENE, BlockLootTableAccessor.dropsWithSilkTouch(UEBlocks.PYROXENE,
                BlockLootTableAccessor.addSurvivesExplosionCondition(UEBlocks.PYROXENE,
                        ItemEntry.builder(Items.FLINT).conditionally(
                                TableBonusLootCondition.builder(Enchantments.FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F))
                                .alternatively(ItemEntry.builder(UEBlocks.PYROXENE)))));

        addBlockLoot(UEBlocks.LIGNITE_BRIQUETTES);
        lootTables.put(UEBlocks.LICHEN, BlockLootTableAccessor.onlyWithShears(UEBlocks.LICHEN));
        Map<Identifier, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
            tables.put(entry.getKey().getLootTableId(), entry.getValue().type(LootContextTypes.BLOCK).build());
        }

        writeTables(cache, tables);
    }

    private void addBlockLoot(Block block) {
        lootTables.put(block, BlockLootTableAccessor.drops(block));
    }

    private void writeTables(DataCache cache, Map<Identifier, LootTable> tables) {
        Path outputFolder = this.generator.getOutput();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                DataProvider.writeToPath(GSON, cache, LootManager.toJson(lootTable), path);
            } catch (IOException e) {
                e.printStackTrace();
//                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    protected static LootTable.Builder createSimpleTable(String name, Block block) {
        LootPool.Builder builder = LootPool.builder()
                .name(name)
                .rolls(ConstantLootTableRange.create(1))
                .with(ItemEntry.builder(block));
        return LootTable.builder().pool(builder);
    }
}
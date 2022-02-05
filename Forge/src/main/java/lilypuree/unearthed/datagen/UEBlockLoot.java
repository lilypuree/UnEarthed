package lilypuree.unearthed.datagen;

import lilypuree.unearthed.block.SixwaySlabBlock;
import lilypuree.unearthed.block.schema.*;
import lilypuree.unearthed.block.type.IOreType;
import lilypuree.unearthed.block.type.VanillaOreTypes;
import lilypuree.unearthed.core.UEBlocks;
import lilypuree.unearthed.core.UEItems;
import lilypuree.unearthed.core.UETags;
import lilypuree.unearthed.misc.BlockStatePropertiesMatch;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class UEBlockLoot extends BlockLootTableAccessor {
    private static final LootItemCondition.Builder HOES = MatchTool.toolMatches(ItemPredicate.Builder.item().of(UETags.Items.REGOLITH_USABLE));


    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {

        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : schema.entries()) {
                BlockForm form = entry.getForm();
                Block block = entry.getBlock();

                if (form == Forms.SLAB) {
                    add(block, createSlabItemTable(block));
                } else if (form == Forms.SIXWAY_SLAB) {
                    add(block, createSixwaySlabItemTable(block));
                } else if (form instanceof Forms.OreForm oreForm) {
                    add(block, getOreDrop(block, oreForm.getOreType()));
                } else if (form == Forms.GRASSY_REGOLITH) {
                    add(block, regolithGrassBlock(block, UEItems.REGOLITH, Blocks.DIRT));
                } else if (form == Forms.REGOLITH) {
                    add(block, b -> createSingleItemTableWithSilkTouch(b, UEItems.REGOLITH));
                } else if (form == Forms.OVERGROWN_ROCK) {
                    add(block, b -> createSingleItemTableWithSilkTouch(b, schema.getBaseBlock()));
                } else if (entry.isBaseEntry() && schema.variants().contains(Variants.COBBLED)) {
                    add(block, b -> createSingleItemTableWithSilkTouch(b, schema.getBaseBlock(Variants.COBBLED)));
                } else {
                    dropSelf(block);
                }
            }
        }
        this.add(UEBlocks.PYROXENE, (block) -> createSilkTouchDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(Items.FLINT).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.1F, 0.14285715F, 0.25F, 1.0F)).otherwise(LootItem.lootTableItem(block)))));
        dropSelf(UEBlocks.LIGNITE_BRIQUETTES);
        add(UEBlocks.LICHEN, createShearsOnlyDrop(UEItems.LICHEN));
        map.forEach(biConsumer);
    }

    protected static LootTable.Builder getOreDrop(Block block, IOreType oreType) {
        if (oreType == VanillaOreTypes.COPPER) {
            return createCopperOreDrops(block);
        } else if (oreType == VanillaOreTypes.LAPIS) {
            return createLapisOreDrops(block);
        } else if (oreType == VanillaOreTypes.REDSTONE) {
            return createRedstoneOreDrops(block);
        } else {
            return createOreDrop(block, oreType.getOreDrop());
        }
    }


    protected static LootTable.Builder createSixwaySlabItemTable(Block slab) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(applyExplosionDecay(slab, LootItem.lootTableItem(slab).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                        .when(InvertedLootItemCondition.invert(BlockStatePropertiesMatch.builder(slab).propertiesToCompare(SixwaySlabBlock.FACE, SixwaySlabBlock.SECONDARY_FACING)))))));
    }

    public static LootTable.Builder regolithGrassBlock(Block block, ItemLike noSilkTouch, ItemLike withHoe) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block).when(HAS_SILK_TOUCH)
                        .otherwise(LootItem.lootTableItem(withHoe).when(HOES).otherwise(applyExplosionCondition(block, LootItem.lootTableItem(noSilkTouch))))));
    }
}

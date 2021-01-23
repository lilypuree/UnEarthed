package net.oriondevcorgitaco.unearthed.datagen;

import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.functions.ExplosionDecay;
import net.minecraft.util.IItemProvider;
import net.oriondevcorgitaco.unearthed.core.UETags;

public class BlockLootTableAccessor extends BlockLootTables {
    private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));
    private static final ILootCondition.IBuilder HOES = MatchTool.builder(ItemPredicate.Builder.create().tag(UETags.Items.REGOLITH_USABLE));


    public static <T> T withExplosionDecayWithoutImmuneCheck(IItemProvider item, ILootFunctionConsumer<T> function) {
        return function.acceptFunction(ExplosionDecay.builder());
    }

    public static LootTable.Builder droppingItemWithFortune(Block block, Item item) {
        return BlockLootTables.droppingItemWithFortune(block, item);
    }

    public static LootTable.Builder droppingSlab(Block slab) {
        return BlockLootTables.droppingSlab(slab);
    }

    public static LootTable.Builder dropping(IItemProvider item) {
        return BlockLootTables.dropping(item);
    }

    public static LootTable.Builder droppingWithSilkTouch(Block block, LootEntry.Builder<?> builder) {
        return BlockLootTables.dropping(block, SILK_TOUCH, builder);
    }

    public static LootTable.Builder droppingWithSilkTouch(Block block, IItemProvider noSilkTouch) {
        return BlockLootTables.droppingWithSilkTouch(block, noSilkTouch);
    }

    public static LootTable.Builder droppingWithHoe(Block block, IItemProvider hoed) {
        return droppingWithHoe(block, withSurvivesExplosion(block, ItemLootEntry.builder(hoed)));
    }

    public static LootTable.Builder droppingWithHoe(Block block, LootEntry.Builder<?> builder) {
        return BlockLootTables.dropping(block, HOES.inverted(), builder);
    }

    public static LootTable.Builder onlyWithShears(Block block){
        return BlockLootTables.onlyWithShears(block);
    }

    public static LootTable.Builder regolithGrassBlock(Block block, IItemProvider noSilkTouch, IItemProvider withHoe) {
        return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                .addEntry(ItemLootEntry.builder(block).acceptCondition(SILK_TOUCH)
                        .alternatively(ItemLootEntry.builder(withHoe).acceptCondition(HOES).alternatively(withSurvivesExplosion(block, ItemLootEntry.builder(noSilkTouch))))));
    }
}
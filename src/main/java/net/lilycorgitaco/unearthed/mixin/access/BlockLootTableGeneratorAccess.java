package net.lilycorgitaco.unearthed.mixin.access;

import net.minecraft.block.Block;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.LootFunctionConsumingBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockLootTableGenerator.class)
public interface BlockLootTableGeneratorAccess {
    @Invoker
    static LootTable.Builder invokeOreDrops(Block dropWithSilkTouch, Item drop) {
        throw new Error("Mixin failed to apply");
    }

    @Invoker
    static LootTable.Builder invokeSlabDrops(Block drop) {
        throw new Error("Mixin failed to apply");
    }

    @Invoker
    static LootTable.Builder invokeDrops(ItemConvertible item) {
        throw new Error("Mixin failed to apply");
    }

    @Invoker
    static LootTable.Builder invokeDrops(Block drop, LootCondition.Builder conditionBuilder, LootPoolEntry.Builder<?> child) {
        throw new Error("Mixin failed to apply");
    }

    @Invoker
    static LootTable.Builder invokeDrops(Block dropWithSilkTouch, ItemConvertible drop) {
        throw new Error("Mixin failed to apply");
    }

    @Invoker
    static LootTable.Builder invokeDropsWithShears(ItemConvertible drop) {
        throw new Error("Mixin failed to apply");
    }

    @Invoker
    static <T> T invokeApplyExplosionDecay(ItemConvertible drop, LootFunctionConsumingBuilder<T> builder) {
        throw new Error("Mixin failed to apply");
    }
}

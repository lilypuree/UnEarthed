package net.lilycorgitaco.unearthed.datagen;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.*;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionConsumingBuilder;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.function.LootFunctionConsumingBuilder;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.lilycorgitaco.unearthed.block.ModBlockProperties;
import net.lilycorgitaco.unearthed.core.UETags;

import java.util.Set;
import java.util.stream.Stream;

public class BlockLootTableAccessor extends BlockLootTableGenerator {
    private static final LootCondition.Builder SILK_TOUCH = MatchToolLootCondition.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1))));
    private static final LootCondition.Builder HOES = MatchToolLootCondition.builder(ItemPredicate.Builder.create().tag(UETags.Items.REGOLITH_USABLE));
    private static final Set<Item> IMMUNE_TO_EXPLOSIONS = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemConvertible::asItem).collect(ImmutableSet.toImmutableSet());


    public static <T> T withExplosionDecayWithoutImmuneCheck(ItemConvertible item, LootFunctionConsumingBuilder<T> function) {
        return function.apply(ExplosionDecayLootFunction.builder());
    }

    protected static <T> T addSurvivesExplosionCondition(ItemConvertible item, LootConditionConsumingBuilder<T> condition) {
        return (T)(!IMMUNE_TO_EXPLOSIONS.contains(item.asItem()) ? condition.conditionally(SurvivesExplosionLootCondition.builder()) : condition.getThis());
    }

    public static LootTable.Builder oreDrops(Block block, Item item) {
        return BlockLootTableGenerator.oreDrops(block, item);
    }

    public static LootTable.Builder slabDrops(Block slab) {
        return BlockLootTableGenerator.slabDrops(slab);
    }

    protected static LootTable.Builder droppingSixwaySlab(Block slab) {
        return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootTableRange.create(1)).with(applyExplosionDecay(slab, ItemEntry.builder(slab).apply(SetCountLootFunction.builder(ConstantLootTableRange.create(2)).conditionally(BlockStatePropertyLootCondition.builder(slab).properties(StatePredicate.Builder.create().exactMatch(ModBlockProperties.DOUBLE, true)))))));
    }

    public static LootTable.Builder drops(ItemConvertible item) {
        return BlockLootTableGenerator.drops(item);
    }

    public static LootTable.Builder dropsWithSilkTouch(Block block, LootPoolEntry.Builder<?> builder) {
        return BlockLootTableGenerator.drops(block, SILK_TOUCH, builder);
    }

    public static LootTable.Builder drops(Block block, ItemConvertible noSilkTouch) {
        return BlockLootTableGenerator.drops(block, noSilkTouch);
    }

    public static LootTable.Builder droppingWithHoe(Block block, ItemConvertible hoed) {
        return droppingWithHoe(block, addSurvivesExplosionCondition(block, ItemEntry.builder(hoed)));
    }

    public static LootTable.Builder droppingWithHoe(Block block, LootPoolEntry.Builder<?> builder) {
        return BlockLootTableGenerator.drops(block, HOES.invert(), builder);
    }

    public static LootTable.Builder onlyWithShears(Block block){
        return BlockLootTableGenerator.dropsWithShears(block);
    }

    public static LootTable.Builder regolithGrassBlock(Block block, ItemConvertible noSilkTouch, ItemConvertible withHoe) {
        return LootTable.builder().pool(LootPool.builder().rolls(ConstantLootTableRange.create(1))
                .with(ItemEntry.builder(block).conditionally(SILK_TOUCH)
                        .alternatively(ItemEntry.builder(withHoe).conditionally(HOES).alternatively(addSurvivesExplosionCondition(block, ItemEntry.builder(noSilkTouch))))));
    }
}
package lilypuree.unearthed.block.type;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public enum VanillaOreTypes implements IOreType {
    COAL(Blocks.COAL_ORE, Items.COAL, Items.COAL, 0.1f, 200),
    LAPIS(Blocks.LAPIS_ORE, Items.LAPIS_LAZULI, Items.LAPIS_LAZULI, 0.2f, 200),
    REDSTONE(Blocks.REDSTONE_ORE, Items.REDSTONE, Items.REDSTONE, 0.7f, 200),
    DIAMOND(Blocks.DIAMOND_ORE, Items.DIAMOND, Items.DIAMOND, 1.0f, 200),
    EMERALD(Blocks.EMERALD_ORE, Items.EMERALD, Items.EMERALD, 1.0f, 200),
    IRON(Blocks.IRON_ORE, Items.RAW_IRON, Items.IRON_INGOT, 0.7f, 200),
    GOLD(Blocks.GOLD_ORE, Items.RAW_GOLD, Items.GOLD_INGOT, 1.0f, 200),
    COPPER(Blocks.COPPER_ORE, Items.RAW_COPPER, Items.COPPER_INGOT, 0.7f, 200);

    private final Block block;
    private final Item smeltResult;
    private final Item oreDrop;
    private final float experience;
    private final int smeltTime;
    private final int blastTime;

    VanillaOreTypes(Block block, Item oreDrop, Item smeltResult, float experience, int smeltTime) {
        this.block = block;
        this.oreDrop = oreDrop;
        this.smeltResult = smeltResult;
        this.experience = experience;
        this.smeltTime = smeltTime;
        this.blastTime = smeltTime / 2;
    }

    @Override
    public String getName() {
        return toString().toLowerCase();
    }

//    @Override
//    public Function<Block, LootTable.Builder> createLootFactory() {
//        return BlockLootTableAccessor::dropping;
//    }

    @Override
    public int getSmeltTime() {
        return smeltTime;
    }

    @Override
    public float getExperience() {
        return experience;
    }


    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public Item getOreDrop() {
        return oreDrop;
    }

    @Override
    public Item getSmeltResult() {
        return smeltResult;
    }
}
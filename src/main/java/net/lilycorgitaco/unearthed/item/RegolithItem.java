package net.lilycorgitaco.unearthed.item;

import net.lilycorgitaco.unearthed.block.LichenBlock;
import net.lilycorgitaco.unearthed.block.RegolithGrassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegolithItem extends BlockItem {

    public RegolithItem(Settings properties) {
        super(Blocks.AIR, properties);
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        Block block = getDominantBlock(world, pos);
        if (block == null) return null;
        else return block.getDefaultState();
    }

    protected Block getDominantBlock(World world, BlockPos pos) {
        return BlockPos.stream(pos.add(-1, -1, -1), pos.add(1, 1, 1)).map(eachPos -> {
            Block block = world.getBlockState(eachPos).getBlock();
            if (block instanceof RegolithGrassBlock){
                block = ((RegolithGrassBlock)block).getRegolithBlock();
            }
            if (LichenBlock.lichenErosionMap.containsKey(block)) {
                return LichenBlock.lichenErosionMap.get(block);
            } else if (LichenBlock.lichenErosionMap.containsValue(block)) {
                return block;
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.groupingBy(block -> block, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

    }

    @Override
    public String getTranslationKey() {
        return this.getOrCreateTranslationKey();
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }

    @Override
    public void appendBlocks(Map<Block, Item> map, Item item) {
    }
}

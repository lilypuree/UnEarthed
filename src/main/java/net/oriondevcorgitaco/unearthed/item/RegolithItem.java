package net.oriondevcorgitaco.unearthed.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.block.LichenBlock;
import net.oriondevcorgitaco.unearthed.block.RegolithBlock;
import net.oriondevcorgitaco.unearthed.block.RegolithGrassBlock;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegolithItem extends BlockItem {

    public RegolithItem(Properties properties) {
        super(Blocks.AIR, properties);
    }

    @Nullable
    @Override
    protected BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Block block = getDominantBlock(world, pos);
        if (block == null) return null;
        else return block.getDefaultState();
    }

    protected Block getDominantBlock(World world, BlockPos pos) {
        return BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1)).map(eachPos -> {
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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            items.add(new ItemStack(this));
        }
    }

    @Override
    public void addToBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn) {
    }

    @Override
    public void removeFromBlockToItemMap(Map<Block, Item> blockToItemMap, Item itemIn) {
    }
}

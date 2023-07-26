package lilypuree.unearthed.misc;

import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.RegolithGrassBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RegolithItem extends BlockItem {

    public RegolithItem(Properties properties) {
        super(Blocks.AIR, properties);
    }

    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Block block = getDominantBlock(world, pos);
        if (block == null) return null;
        else return block.defaultBlockState();
    }

    protected Block getDominantBlock(Level world, BlockPos pos) {
        return BlockPos.betweenClosedStream(pos.offset(-1, -1, -1), pos.offset(1, 1, 1)).map(eachPos -> {
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
    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }



    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (this.allowedIn(group)) {
            items.add(new ItemStack(this));
        }
    }

    @Override
    public void registerBlocks(Map<Block, Item> blockToItemMap, Item itemIn) {
    }
}

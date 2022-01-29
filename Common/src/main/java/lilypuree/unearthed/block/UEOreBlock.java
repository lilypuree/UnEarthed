package lilypuree.unearthed.block;

import lilypuree.unearthed.core.UETags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class UEOreBlock extends OreBlock {
    public UEOreBlock(Properties Properties) {
        super(Properties);
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack item) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, item) == 0) {
            int xp = xpOnDrop(state, level.random);
            if (xp > 0) {
                this.popExperience(level, pos, xp);
            }
        }
    }

    protected int xpOnDrop(BlockState state, Random random) {
        if (state.is(BlockTags.COAL_ORES)) {
            return Mth.nextInt(random, 0, 2);
        } else if (state.is(BlockTags.DIAMOND_ORES)) {
            return Mth.nextInt(random, 3, 7);
        } else if (state.is(BlockTags.EMERALD_ORES)) {
            return Mth.nextInt(random, 3, 7);
        } else if (state.is(BlockTags.LAPIS_ORES)) {
            return Mth.nextInt(random, 2, 5);
        } else
            return 0;
    }
}

package lilypuree.unearthed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class UEOreBlock extends Block {
    public UEOreBlock(BlockBehaviour.Properties Properties) {
        super(Properties);
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack item, boolean xpDrops) {
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, item) == 0) {
            if(xpDrops) {
                int xp = xpOnDrop(state, level.random);
                if (xp > 0) {
                    this.popExperience(level, pos, xp);
                }
            }
        }
    }

    protected int xpOnDrop(BlockState state, RandomSource random) {
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

    @Override
    public void popExperience(ServerLevel serverLevel, BlockPos pos, int amount) {
        if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            ExperienceOrb.award(serverLevel, Vec3.atCenterOf(pos), amount);
        }
    }
}

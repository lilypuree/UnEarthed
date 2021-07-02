package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;
import net.oriondevcorgitaco.unearthed.core.UETags;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class UEOreBlock extends OreBlock {
    public UEOreBlock(Properties Properties) {
        super(Properties);
    }

    @Override
    protected int xpOnDrop(Random random) {
        if (this.is(UETags.Blocks.COAL_ORE_TAG)) {
            return MathHelper.nextInt(random, 0, 2);
        } else if (this.is(UETags.Blocks.DIAMOND_ORE_TAG)) {
            return MathHelper.nextInt(random, 3, 7);
        } else if (this.is(UETags.Blocks.EMERALD_ORE_TAG)) {
            return MathHelper.nextInt(random, 3, 7);
        } else if (this.is(UETags.Blocks.LAPIS_ORE_TAG)) {
            return MathHelper.nextInt(random, 2, 5);
        } else
            return 0;
    }
}

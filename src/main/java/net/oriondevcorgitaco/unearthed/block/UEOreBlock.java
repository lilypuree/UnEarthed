package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;
import net.oriondevcorgitaco.unearthed.core.UETags;

import java.util.Random;

public class UEOreBlock extends OreBlock {
    public UEOreBlock(Properties Properties) {
        super(Properties);
    }

    @Override
    protected int getExperience(Random random) {
        if (this.isIn(UETags.Blocks.COAL_ORE_TAG)) {
            return MathHelper.nextInt(random, 0, 2);
        } else if (this.isIn(UETags.Blocks.DIAMOND_ORE_TAG)) {
            return MathHelper.nextInt(random, 3, 7);
        } else if (this.isIn(UETags.Blocks.EMERALD_ORE_TAG)) {
            return MathHelper.nextInt(random, 3, 7);
        } else if (this.isIn(UETags.Blocks.LAPIS_ORE_TAG)) {
            return MathHelper.nextInt(random, 2, 5);
        } else
            return 0;
    }
}

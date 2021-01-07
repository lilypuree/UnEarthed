package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.Tags;

import java.util.Random;

public class UEOreBlock extends OreBlock {
    public UEOreBlock(Properties Properties) {
        super(Properties);
    }

    @Override
    protected int getExperience(Random random) {
        if (this.isIn(Tags.Blocks.ORES_COAL)) {
            return MathHelper.nextInt(random, 0, 2);
        } else if (this.isIn(Tags.Blocks.ORES_DIAMOND)) {
            return MathHelper.nextInt(random, 3, 7);
        } else if (this.isIn(Tags.Blocks.ORES_EMERALD)) {
            return MathHelper.nextInt(random, 3, 7);
        } else if (this.isIn(Tags.Blocks.ORES_LAPIS)) {
            return MathHelper.nextInt(random, 2, 5);
        } else
            return 0;
    }
}

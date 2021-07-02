package net.oriondevcorgitaco.unearthed.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;

import java.util.Random;

public class StrataGenerator3 extends Feature<NoFeatureConfig> {
    public static final Feature<NoFeatureConfig> UNDERGROUND_STONE3 = RegistrationHelper.registerFeature("strata_gen3", new StrataGenerator3(NoFeatureConfig.CODEC));

    public StrataGenerator3(Codec<NoFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(pos.getX() + x, 0, pos.getZ() + z);
                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

                for (int y = 0; y < topY; y++) {
                    if (world.getBlockState(mutable).getBlock().is(BlockTags.BASE_STONE_OVERWORLD)) {

                        world.setBlock(mutable, BlockGeneratorHelper.baseStoneBlockArray.get(random.nextInt(BlockGeneratorHelper.baseStoneBlockArray.size())).defaultBlockState(), 2);
                    }
                    mutable.move(Direction.UP);
                }
            }
        }
        return true;
    }
}
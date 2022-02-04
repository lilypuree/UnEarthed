package net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.oriondevcorgitaco.unearthed.UnearthedConfig;

import java.util.Random;

public class TrueMesaGenerator extends Feature<NoFeatureConfig> {
    public TrueMesaGenerator(Codec<NoFeatureConfig> configCodec) {
        super(configCodec);
    }

    BlockState mesaBlockState = Blocks.TERRACOTTA.defaultBlockState();

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(pos.getX() + x, 0, pos.getZ() + z);
                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

                for (int y = 0; y < topY; y++) {
                    setStrataLayerBlock(y);
                    if (useStoneTag(world, mutable))
                        world.setBlock(mutable, mesaBlockState, 2);

                    mutable.move(Direction.UP);
                }
            }
        }
        return true;
    }

    public static boolean useStoneTag(ISeedReader world, BlockPos mutable) {
        boolean stoneTag = UnearthedConfig.replaceableTag.get();
        if (stoneTag)
            return world.getBlockState(mutable).is(BlockTags.BASE_STONE_OVERWORLD);
        else
            return world.getBlockState(mutable) == Blocks.STONE.defaultBlockState();

    }

    public void setStrataLayerBlock(int yPos) {
        if (yPos % 4 == 0)
            mesaBlockState = Blocks.TERRACOTTA.defaultBlockState();
        else if (yPos % 5 == 0)
            mesaBlockState = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
        else if (yPos % 3 == 0)
            mesaBlockState = Blocks.WHITE_TERRACOTTA.defaultBlockState();
    }
}

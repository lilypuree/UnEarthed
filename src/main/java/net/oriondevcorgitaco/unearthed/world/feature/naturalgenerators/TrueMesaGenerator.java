package net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;

import java.util.Random;

public class TrueMesaGenerator extends Feature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> MESA = RegistrationHelper.registerFeature("true_mesa_generator", new TrueMesaGenerator(DefaultFeatureConfig.CODEC));

    public TrueMesaGenerator(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    BlockState mesaBlockState = Blocks.TERRACOTTA.getDefaultState();

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, DefaultFeatureConfig featureConfig) {

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(pos.getX() + x, 0, pos.getZ() + z);
                int topY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

                for (int y = 0; y < topY; y++) {
                    setStrataLayerBlock(y);
                    if (world.getBlockState(mutable).getBlock().isIn(BlockTags.BASE_STONE_OVERWORLD)) {

                        world.setBlockState(mutable, mesaBlockState, 2);
                    }
                    mutable.move(Direction.UP);
                }
            }
        }
        return true;
    }

    public void setStrataLayerBlock(int yPos) {
        if (yPos % 4 == 0)
            mesaBlockState = Blocks.TERRACOTTA.getDefaultState();
        else if(yPos % 5 == 0)
            mesaBlockState = Blocks.ORANGE_TERRACOTTA.getDefaultState();
        else if (yPos % 3 == 0)
            mesaBlockState = Blocks.WHITE_TERRACOTTA.getDefaultState();
    }
}

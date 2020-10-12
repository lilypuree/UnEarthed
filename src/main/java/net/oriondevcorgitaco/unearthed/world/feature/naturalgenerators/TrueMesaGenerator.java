package net.oriondevcorgitaco.unearthed.world.feature.naturalgenerators;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.world.feature.ChunkCoordinatesFeature;

import java.util.Random;

public class TrueMesaGenerator extends ChunkCoordinatesFeature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> MESA = RegistrationHelper.registerFeature("true_mesa_generator", new TrueMesaGenerator(DefaultFeatureConfig.CODEC));

    public TrueMesaGenerator(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    BlockState mesaBlockState = Blocks.TERRACOTTA.getDefaultState();

    @Override
    public boolean generate(StructureWorldAccess world, Random random, Chunk chunk, int x, int z, DefaultFeatureConfig config) {
        int xPos = x & 15;
        int zPos = z & 15;
        BlockPos.Mutable mutable = new BlockPos.Mutable(xPos, 0, zPos);

        int topY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

        for (int y = 0; y < topY; y++) {
            setStrataLayerBlock(y);
            if (useStoneTag(world, mutable))
                chunk.setBlockState(mutable, mesaBlockState, false);

            mutable.move(Direction.UP);
        }
        return true;
    }

    public static boolean useStoneTag(StructureWorldAccess world, BlockPos mutable) {
        boolean stoneTag = Unearthed.UE_CONFIG.generation.stoneTag;
        if (stoneTag)
            return world.getBlockState(mutable).isIn(BlockTags.BASE_STONE_OVERWORLD);
        else
            return world.getBlockState(mutable) == Blocks.STONE.getDefaultState();

    }

    public void setStrataLayerBlock(int yPos) {
        if (yPos % 4 == 0)
            mesaBlockState = Blocks.TERRACOTTA.getDefaultState();
        else if (yPos % 5 == 0)
            mesaBlockState = Blocks.ORANGE_TERRACOTTA.getDefaultState();
        else if (yPos % 3 == 0)
            mesaBlockState = Blocks.WHITE_TERRACOTTA.getDefaultState();
    }
}

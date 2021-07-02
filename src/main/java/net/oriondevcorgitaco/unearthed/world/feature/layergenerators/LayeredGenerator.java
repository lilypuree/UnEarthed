package net.oriondevcorgitaco.unearthed.world.feature.layergenerators;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LayeredGenerator extends Feature<NoFeatureConfig> {

    public LayeredGenerator(Codec<NoFeatureConfig> configCodec) {
        super(configCodec);
    }

    FastNoise fastNoise2D = null;


    public static List<BlockState> blockStates = new ArrayList<>();

    public static BlockState mesaBlockState = Blocks.STONE.defaultBlockState();

    @Override
    public boolean place(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, NoFeatureConfig config) {
        setSeed(world.getSeed());

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(pos.getX() + x, 0, pos.getZ() + z);
                int topY = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());

                double noise2D = fastNoise2D.GetNoise(mutable.getX(), mutable.getZ());

                for (int y = 0; y < topY; y++) {
                    int soilLayerWidth = soilLayerSize(topY, y);
                    int sedimentaryLayerWidth = sedimentaryLayerSize(topY, y, soilLayerWidth);
                    int metamorphicLayerWidth = metamorphicLayerSize(topY, y, sedimentaryLayerWidth);

//                    Unearthed.LOGGER.info("Soil Layer Width:" + soilLayerWidth);
//                    Unearthed.LOGGER.info("Sedimentary Layer Width:" + sedimentaryLayerWidth);
//                    Unearthed.LOGGER.info("Metamorphic Layer Width:" + metamorphicLayerWidth);

                    BlockState mutableState = world.getBlockState(mutable);

                    //Soil Layer
                    if (y > topY - soilLayerWidth)
                        world.setBlock(mutable, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);

                    //Sedimentary Layer
                    else if (y > topY - sedimentaryLayerWidth + noise2D)
                        world.setBlock(mutable, Blocks.EMERALD_BLOCK.defaultBlockState(), 2);

                    //Metamorphic Layer
                    else if (y > topY - metamorphicLayerWidth)
                        world.setBlock(mutable, Blocks.COAL_BLOCK.defaultBlockState(), 2);

                    //Igneous Intrusive Layer
                    else
                        world.setBlock(mutable, Blocks.REDSTONE_BLOCK.defaultBlockState(), 2);


                    mutable.move(Direction.UP);


                }
            }
        }
        return true;
    }


    private void setSeed(long seed) {
        if (fastNoise2D == null) {
            fastNoise2D = new FastNoise((int) seed);
            fastNoise2D.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            fastNoise2D.SetFractalOctaves(4);
            fastNoise2D.SetGradientPerturbAmp(150);
            fastNoise2D.SetFrequency(0.01F);
        }
    }


    private int soilLayerSize(int topY, int yPosition) {

        if (topY > 128)
            return getPercentage(0.00, topY);
        else if(topY < 75)
            return getPercentage(0.25, topY);
        else
            return getPercentage(0.1, topY);


    }

    private int sedimentaryLayerSize(int topY, int yPosition, int previousLayer) {
        if (topY > 128)
            return getPercentage(0.01, topY);
        else if(topY < 75)
            return getPercentage(0.6, topY);
        else
            return getPercentage(0.4, topY);

    }

    private int metamorphicLayerSize(int topY, int yPosition, int previousLayer) {

        if (topY > 128)
            return getPercentage(0.31, topY);
        else
            return getPercentage(0.85, topY);

    }

    public static int getPercentage(double desiredPercent, int value) {
        return (int) (desiredPercent * value);
    }


    static {
        blockStates.add(Blocks.COAL_BLOCK.defaultBlockState());
        blockStates.add(Blocks.DIAMOND_BLOCK.defaultBlockState());
        blockStates.add(Blocks.EMERALD_BLOCK.defaultBlockState());
    }
}

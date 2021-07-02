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
import net.oriondevcorgitaco.unearthed.util.RegistrationHelper;
import net.oriondevcorgitaco.unearthed.util.noise.FNVector3f;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LayeredGeneratorOLD extends Feature<NoFeatureConfig> {
    public static final Feature<NoFeatureConfig> UNDERGROUND_STONE = RegistrationHelper.registerFeature("layer_gen1", new LayeredGeneratorOLD(NoFeatureConfig.CODEC));

    public LayeredGeneratorOLD(Codec<NoFeatureConfig> configCodec) {
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
                FNVector3f perturb3f = new FNVector3f(mutable.getX(), mutable.getY(), mutable.getX());
                fastNoise2D.GradientPerturb(perturb3f);
                double modifiedTopY = topY - (fastNoise2D.GetNoise(perturb3f.x, perturb3f.z) * 18);

                for (int y = 0; y < modifiedTopY; y++) {
                    if (world.getBlockState(mutable) == Blocks.STONE.defaultBlockState()) {
                        if (y > modifiedTopY - 5)
                            world.setBlock(mutable, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
                        else if (y > modifiedTopY - 15)
                            world.setBlock(mutable, Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
                        else if (y > modifiedTopY - 25)
                            world.setBlock(mutable, Blocks.LAPIS_BLOCK.defaultBlockState(), 2);
                        else if (y > modifiedTopY - 35)
                            world.setBlock(mutable, Blocks.COAL_BLOCK.defaultBlockState(), 2);
                        else if (y > modifiedTopY - 45)
                            world.setBlock(mutable, Blocks.REDSTONE_BLOCK.defaultBlockState(), 2);
                    }
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

    static {
        blockStates.add(Blocks.COAL_BLOCK.defaultBlockState());
        blockStates.add(Blocks.DIAMOND_BLOCK.defaultBlockState());
        blockStates.add(Blocks.EMERALD_BLOCK.defaultBlockState());
    }
}

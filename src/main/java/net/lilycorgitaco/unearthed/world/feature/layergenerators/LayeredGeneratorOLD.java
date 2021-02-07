package net.lilycorgitaco.unearthed.world.feature.layergenerators;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.lilycorgitaco.unearthed.util.RegistrationHelper;
import net.lilycorgitaco.unearthed.util.noise.FNVector3f;
import net.lilycorgitaco.unearthed.util.noise.FastNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LayeredGeneratorOLD extends Feature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> UNDERGROUND_STONE = RegistrationHelper.registerFeature("layer_gen1", new LayeredGeneratorOLD(DefaultFeatureConfig.CODEC));

    public LayeredGeneratorOLD(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    FastNoise fastNoise2D = null;


    public static List<BlockState> blockStates = new ArrayList<>();

    public static BlockState mesaBlockState = Blocks.STONE.getDefaultState();

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, DefaultFeatureConfig config) {
        setSeed(world.getSeed());

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                mutable.set(pos.getX() + x, 0, pos.getZ() + z);
                int topY = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, mutable.getX(), mutable.getZ());
                FNVector3f perturb3f = new FNVector3f(mutable.getX(), mutable.getY(), mutable.getX());
                fastNoise2D.GradientPerturb(perturb3f);
                double modifiedTopY = topY - (fastNoise2D.GetNoise(perturb3f.x, perturb3f.z) * 18);

                for (int y = 0; y < modifiedTopY; y++) {
                    if (world.getBlockState(mutable) == Blocks.STONE.getDefaultState()) {
                        if (y > modifiedTopY - 5)
                            world.setBlockState(mutable, Blocks.DIAMOND_BLOCK.getDefaultState(), 2);
                        else if (y > modifiedTopY - 15)
                            world.setBlockState(mutable, Blocks.EMERALD_BLOCK.getDefaultState(), 2);
                        else if (y > modifiedTopY - 25)
                            world.setBlockState(mutable, Blocks.LAPIS_BLOCK.getDefaultState(), 2);
                        else if (y > modifiedTopY - 35)
                            world.setBlockState(mutable, Blocks.COAL_BLOCK.getDefaultState(), 2);
                        else if (y > modifiedTopY - 45)
                            world.setBlockState(mutable, Blocks.REDSTONE_BLOCK.getDefaultState(), 2);
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
        blockStates.add(Blocks.COAL_BLOCK.getDefaultState());
        blockStates.add(Blocks.DIAMOND_BLOCK.getDefaultState());
        blockStates.add(Blocks.EMERALD_BLOCK.getDefaultState());
    }
}

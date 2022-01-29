package lilypuree.unearthed.world.feature;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import lilypuree.unearthed.core.UETags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Set;

public class UEStoneReplacer extends Feature<NoneFeatureConfiguration> {
    public UEStoneReplacer() {
        super(NoneFeatureConfiguration.CODEC);
    }

    private boolean isSeedSet = false;

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();

        if (!isSeedSet) {
            ReplacementNoiseSampler.setSeed((int) level.getSeed(), (int) level.getSeed());
        }

        BlockPos pos = ctx.origin();
        ChunkAccess chunkAccess = level.getChunk(pos);

        int minY = -8;
        int maxHeight = Integer.MIN_VALUE;
        int[][] heights = new int[17][17];
        ReplacementNoiseSampler[][] noiseSamplers = new ReplacementNoiseSampler[17][17];
        for (int x = 0; x < 17; x++) {
            for (int z = 0; z < 17; z++) {
                int height = 0;
                if (x < 16 && z < 16) {
                    height = chunkAccess.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x, z);
                } else
                    height = level.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, x + pos.getX(), z + pos.getZ());
                maxHeight = Math.max(height, maxHeight);
                noiseSamplers[x][z] = new ReplacementNoiseSampler(x + pos.getX(), z + pos.getZ(), minY, height);
            }
        }
        ChunkFiller filler = new ChunkFiller(level.getSeed(), new BlockPos(pos.getX(), minY, pos.getZ()), maxHeight - minY + 1);
        filler.run(v -> noiseSamplers[v.getX() - pos.getX()][v.getZ() - pos.getZ()].select(v.getX(), v.getY(), v.getZ()));
        replaceAll(chunkAccess, minY, noiseSamplers, filler.stateArray);
//        Set<LevelChunkSection> chunkSections = Sets.newHashSet();
//        for (int i = chunkAccess.getSectionIndex(minY); i <= chunkAccess.getSectionIndex(maxHeight); i++) {
//            LevelChunkSection levelChunkSection = chunkAccess.getSection(i);
//            levelChunkSection.acquire();
//            chunkSections.add(levelChunkSection);
//        }
//        replace(pos, chunkAccess, minY);
//        for (LevelChunkSection section : chunkSections) {
//            section.release();
//        }
        return true;
    }

    private void createChunkFiller() {
    }

    private void replaceAll(ChunkAccess chunkAccess, int minY, ReplacementNoiseSampler[][] noiseSamplers, ChunkFiller.State[][][] results) {
        LevelChunkSection chunkSection = chunkAccess.getSection(chunkAccess.getSectionIndex(minY));
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int posY = minY; posY <= noiseSamplers[x][z].getHeight(); posY++) {
                    int sectionIndex = chunkAccess.getSectionIndex(posY);
                    if (chunkAccess.getSectionIndex(chunkSection.bottomBlockY()) != sectionIndex) {
                        chunkSection = chunkAccess.getSection(sectionIndex);
                    }
                    BlockState block = chunkSection.getBlockState(x, posY & 15, z);
                    if (replaceable(block)) {
                        BlockState state = results[x][posY - minY][z].block();
                        chunkSection.setBlockState(x, posY & 15, z, state);
                    } else {
//                        chunkSection.setBlockState(x, posY & 15, z, Blocks.GLASS.defaultBlockState());
                    }
                }
            }
        }
    }

    private boolean replaceable(BlockState block) {
        return block.is(UETags.Blocks.REPLACABLE);
    }
}

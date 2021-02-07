package net.lilycorgitaco.unearthed.world.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.lilycorgitaco.unearthed.block.LichenBlock;
import net.lilycorgitaco.unearthed.core.UEBlocks;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LichenFeature extends Feature<LichenConfig> {
    public LichenFeature(Codec<LichenConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(StructureWorldAccess seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, LichenConfig arg) {
        if (!canReplace(seedReader.getBlockState(blockPos))) {
            return false;
        } else {
            List<Direction> growthDirs = getValidDirections(arg, random);
            if (tryGeneration(seedReader, blockPos, seedReader.getBlockState(blockPos), arg, random, growthDirs)) {
                return true;
            } else {
                BlockPos.Mutable mutable = blockPos.mutableCopy();
                for (Direction direction : growthDirs) {
                    mutable.set(blockPos);
                    List<Direction> list2 = getValidDirectionsWithout(arg, random, direction.getOpposite());

                    for (int i = 0; i < arg.searchRange; ++i) {
                        mutable.set(blockPos, direction);
                        BlockState blockState = seedReader.getBlockState(mutable);
                        if (!canReplace(blockState) && !blockState.isOf(UEBlocks.LICHEN)) {
                            break;
                        }

                        if (tryGeneration(seedReader, mutable, blockState, arg, random, list2)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    public static boolean tryGeneration(StructureWorldAccess reader, BlockPos blockPos, BlockState blockState, LichenConfig arg, Random random, List<Direction> list) {
        BlockPos.Mutable mutable = blockPos.mutableCopy();
        Iterator<Direction> iterator = list.iterator();
        Direction direction;
        BlockState newState;
        do {
            if (!iterator.hasNext()) {
                return false;
            }
            direction = iterator.next();
            newState = reader.getBlockState(mutable.set(blockPos, direction));
        } while (!arg.isValidBlock(newState.getBlock()));

        LichenBlock block = (LichenBlock) UEBlocks.LICHEN;
        BlockState lichenState = block.tryPlaceOnto(blockState, reader, blockPos, direction);
        if (lichenState == null) {
            return false;
        } else {
            reader.setBlockState(blockPos, lichenState, 3);
            if (random.nextFloat() < arg.spreadChance) {
                block.tryGrowFrom(lichenState, reader, blockPos, direction, random);
            }
            return true;
        }
    }

    //method_33392
    public static List<Direction> getValidDirections(LichenConfig arg, Random random) {
        List<Direction> list = Lists.newArrayList(arg.validDirections);
        Collections.shuffle(list, random);
        return list;
    }

    //method_33393
    public static List<Direction> getValidDirectionsWithout(LichenConfig arg, Random random, Direction direction) {
        List<Direction> list = arg.validDirections.stream().filter((dir) -> dir != direction).collect(Collectors.toList());
        Collections.shuffle(list, random);
        return list;
    }

    //method_33395
    private static boolean canReplace(BlockState blockState) {
        return blockState.isAir() || blockState.isOf(Blocks.WATER);
    }
}

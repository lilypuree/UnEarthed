package net.oriondevcorgitaco.unearthed.world.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.oriondevcorgitaco.unearthed.block.LichenBlock;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;

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
    public boolean place(ISeedReader seedReader, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, LichenConfig arg) {
        if (!canReplace(seedReader.getBlockState(blockPos))) {
            return false;
        } else {
            List<Direction> growthDirs = getValidDirections(arg, random);
            if (tryGeneration(seedReader, blockPos, seedReader.getBlockState(blockPos), arg, random, growthDirs)) {
                return true;
            } else {
                BlockPos.Mutable mutable = blockPos.mutable();
                for (Direction direction : growthDirs) {
                    mutable.set(blockPos);
                    List<Direction> list2 = getValidDirectionsWithout(arg, random, direction.getOpposite());

                    for (int i = 0; i < arg.searchRange; ++i) {
                        mutable.setWithOffset(blockPos, direction);
                        BlockState blockState = seedReader.getBlockState(mutable);
                        if (!canReplace(blockState) && !blockState.is(UEBlocks.LICHEN)) {
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

    public static boolean tryGeneration(ISeedReader reader, BlockPos blockPos, BlockState blockState, LichenConfig arg, Random random, List<Direction> list) {
        BlockPos.Mutable mutable = blockPos.mutable();
        Iterator<Direction> iterator = list.iterator();
        Direction direction;
        BlockState newState;
        do {
            if (!iterator.hasNext()) {
                return false;
            }
            direction = iterator.next();
            newState = reader.getBlockState(mutable.setWithOffset(blockPos, direction));
        } while (!arg.isValidBlock(newState.getBlock()));

        LichenBlock block = (LichenBlock) UEBlocks.LICHEN;
        BlockState lichenState = block.tryPlaceOnto(blockState, reader, blockPos, direction);
        if (lichenState == null) {
            return false;
        } else {
            reader.setBlock(blockPos, lichenState, 3);
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
        return blockState.isAir() || blockState.is(Blocks.WATER);
    }
}

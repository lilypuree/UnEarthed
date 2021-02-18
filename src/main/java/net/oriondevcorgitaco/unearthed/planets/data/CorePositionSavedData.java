package net.oriondevcorgitaco.unearthed.planets.data;

import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.planets.planetcore.MantleCoreTile;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class CorePositionSavedData extends WorldSavedData {
    private static final String DATA_NAME = Unearthed.MOD_ID + "_CorePositionData";
    private List<Pair<BlockPos, Integer>> allPositions;

    public CorePositionSavedData() {
        super(DATA_NAME);
        allPositions = new ArrayList<>();
    }

    @Override
    public void read(CompoundNBT nbt) {
        long[] array = nbt.getLongArray("positions");
        int[] distances = nbt.getIntArray("distances");
        for (int i = 0; i < array.length; i++) {
            allPositions.add(Pair.of(BlockPos.fromLong(array[i]), distances[i]));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        List<Long> array = new ArrayList<>();
        List<Integer> distances = new ArrayList<>();
        allPositions.forEach(pair -> {
            array.add(pair.getLeft().toLong());
            distances.add(pair.getRight());
        });
        compound.putLongArray("positions", array);
        compound.putIntArray("distances", distances);
        return compound;
    }

    public static CorePositionSavedData get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getSavedData();
        return storage.getOrCreate(CorePositionSavedData::new, DATA_NAME);
    }


    public void add(BlockPos pos, int distance) {
        if (allPositions.stream().noneMatch(pair -> pair.getKey().equals(pos))) {
            allPositions.add(Pair.of(pos, distance));
            markDirty();
        }
    }

    public void remove(BlockPos pos) {
        if (allPositions.removeIf(pair -> pair.getKey().equals(pos))) {
            markDirty();
        }
    }

    private boolean isRoughlyNear(BlockPos corePos, BlockPos pos, int distance) {
        return corePos.manhattanDistance(pos) < distance * 3;
    }

    public Stream<MantleCoreTile> findCore(World world, BlockPos pos) {
        return allPositions.stream().map(pair -> {
            BlockPos corePos = pair.getLeft();
            if (isRoughlyNear(corePos, pos, pair.getRight())) {
                TileEntity tileEntity = world.getTileEntity(corePos);
                if (tileEntity instanceof MantleCoreTile && ((MantleCoreTile) tileEntity).isInsideSphere(pos)) {
                    return (MantleCoreTile) tileEntity;
                }
            }
            return null;
        }).filter(Objects::nonNull);
    }

}

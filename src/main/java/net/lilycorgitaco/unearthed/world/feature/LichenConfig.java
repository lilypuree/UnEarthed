package net.lilycorgitaco.unearthed.world.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.FeatureConfig;
import java.util.Collections;
import java.util.List;

public class LichenConfig implements FeatureConfig {
    public static final Codec<LichenConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.intRange(1, 64).fieldOf("search_range").orElse(10).forGetter((arg) -> {
            return arg.searchRange;
        }), Codec.BOOL.fieldOf("can_place_on_floor").orElse(false).forGetter((arg) -> {
            return arg.canPlaceOnFloor;
        }), Codec.BOOL.fieldOf("can_place_on_ceiling").orElse(false).forGetter((arg) -> {
            return arg.canPlaceOnCeiling;
        }), Codec.BOOL.fieldOf("can_place_on_wall").orElse(false).forGetter((arg) -> {
            return arg.canPlaceOnWall;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spreading").orElse(0.5F).forGetter((arg) -> {
            return arg.spreadChance;
        }), BlockState.CODEC.listOf().fieldOf("can_be_placed_on").forGetter((arg) -> {
            return arg.canBePlacedOn;
        })).apply(instance, LichenConfig::new);
    });
    public final int searchRange;
    public final boolean canPlaceOnFloor;
    public final boolean canPlaceOnCeiling;
    public final boolean canPlaceOnWall;
    public final float spreadChance;
    public final List<BlockState> canBePlacedOn;
    public final List<Direction> validDirections;

    public LichenConfig(int searchRange, boolean canPlaceOnFloor, boolean canPlaceOnCeiling, boolean canPlaceOnWall, float spreadChance, List<BlockState> canBePlacedOn) {
        this.searchRange = searchRange;
        this.canPlaceOnFloor = canPlaceOnFloor;
        this.canPlaceOnCeiling = canPlaceOnCeiling;
        this.canPlaceOnWall = canPlaceOnWall;
        this.spreadChance = spreadChance;
        this.canBePlacedOn = canBePlacedOn;
        List<Direction> validDirections = Lists.newArrayList();
        if (canPlaceOnCeiling) {
            validDirections.add(Direction.UP);
        }

        if (canPlaceOnFloor) {
            validDirections.add(Direction.DOWN);
        }

        if (canPlaceOnWall) {
            Direction.Type.HORIZONTAL.forEach(validDirections::add);
        }

        this.validDirections = Collections.unmodifiableList(validDirections);
    }

    public boolean isValidBlock(Block block) {
        return this.canBePlacedOn.stream().anyMatch((blockState) -> {
            return blockState.isOf(block);
        });
    }
}

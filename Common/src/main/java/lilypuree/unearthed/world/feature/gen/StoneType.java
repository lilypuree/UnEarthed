package lilypuree.unearthed.world.feature.gen;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.core.UETags;
import lilypuree.unearthed.misc.UEDataLoaders;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StoneType {
    public static final StoneType EMPTY = new StoneType(null);
    public static final StoneType NO_REPLACE = new StoneType(null) {
        @Override
        public BlockState replace(BlockState original) {
            return original;
        }
    };

    private static final Codec<BlockState> BLOCK_OR_BLOCKSTATE_CODEC = Codec.either(ResourceLocation.CODEC.comapFlatMap(loc ->
                    Registry.BLOCK.getOptional(loc).map(DataResult::success).orElse(DataResult.error(String.format("block %s doesn't exist!", loc))), Registry.BLOCK::getKey), BlockState.CODEC)
            .xmap(either -> either.map(Block::defaultBlockState, b -> b), Either::right);
    public static final Codec<StoneType> CODEC = Codec.STRING.comapFlatMap(string -> {
        StoneType stoneType = UEDataLoaders.getStoneType(string);
        if (stoneType == null) return DataResult.error("Stone type " + string + " doesn't exist!", StoneType.NO_REPLACE);
        else return DataResult.success(stoneType);
    }, UEDataLoaders::getNameForStoneType);

    public static Codec<StoneType> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BLOCK_OR_BLOCKSTATE_CODEC.fieldOf("base").forGetter(state -> state.baseBlock),
            BLOCK_OR_BLOCKSTATE_CODEC.optionalFieldOf("cobble").forGetter(state -> Optional.ofNullable(state.cobbleBlock)),
            BLOCK_OR_BLOCKSTATE_CODEC.optionalFieldOf("dirt").forGetter(state -> Optional.ofNullable(state.dirtReplace)),
            BLOCK_OR_BLOCKSTATE_CODEC.optionalFieldOf("grass").forGetter(state -> Optional.ofNullable(state.grassReplace)),
            Codec.unboundedMap(ResourceLocation.CODEC, BLOCK_OR_BLOCKSTATE_CODEC).optionalFieldOf("ores", Collections.emptyMap()).forGetter(StoneType::getOreMap),
            Codec.BOOL.optionalFieldOf("ignore_biome", false).forGetter(state -> state.ignoreBiome)
    ).apply(instance, StoneType::new));

    public StoneType(BlockState baseBlock, Optional<BlockState> cobbleBlock, Optional<BlockState> dirtReplace, Optional<BlockState> grassReplace, Map<ResourceLocation, BlockState> oreMap, boolean ignoreBiome) {
        this.baseBlock = baseBlock;
        this.cobbleBlock = Constants.CONFIG.disableCobbleReplacement() ? null : cobbleBlock.orElse(null);
        this.dirtReplace = Constants.CONFIG.disableDirtReplacement() ? null : dirtReplace.orElse(null);
        this.grassReplace = Constants.CONFIG.disableDirtReplacement() ? null : grassReplace.orElse(null);
        if (Constants.CONFIG.disableOreReplacement()) {
            this.oreMap = ImmutableMap.<Block, BlockState>builder().build();
        } else {
            ImmutableMap.Builder<Block, BlockState> builder = ImmutableMap.builder();
            oreMap.forEach((key, blockState) -> {
                Block block = Registry.BLOCK.get(key);
                builder.put(block, blockState);
            });
            this.oreMap = builder.build();
        }
        this.ignoreBiome = ignoreBiome;
    }

    public StoneType(BlockState baseBlock) {
        this.baseBlock = baseBlock;
        this.cobbleBlock = baseBlock;
        this.dirtReplace = baseBlock;
        this.grassReplace = baseBlock;
        this.oreMap = ImmutableMap.<Block, BlockState>builder().build();
        this.ignoreBiome = false;
    }

//    protected StoneType(BlockState baseBlock, boolean ignoreBiome) {
//        this.baseBlock = baseBlock;
//        this.cobbleBlock = null;
//        this.dirtReplace = null;
//        this.grassReplace = null;
//        this.oreMap = ImmutableMap.<Block, BlockState>builder().build();
//        this.ignoreBiome = ignoreBiome;
//    }

    private final BlockState baseBlock;
    private final BlockState cobbleBlock;
    private final BlockState dirtReplace;
    private final BlockState grassReplace;
    private final ImmutableMap<Block, BlockState> oreMap;
    private final boolean ignoreBiome;

    public boolean ignoresBiome() {
        return ignoreBiome;
    }

    private BlockState getGrassReplace(BlockState original) {
        if (grassReplace.hasProperty(BlockStateProperties.SNOWY)) {
            return grassReplace.setValue(BlockStateProperties.SNOWY, original.getValue(BlockStateProperties.SNOWY));
        } else {
            return grassReplace;
        }
    }

    private Map<ResourceLocation, BlockState> getOreMap() {
        Map<ResourceLocation, BlockState> newMap = new HashMap<>();
        oreMap.forEach((block, state) -> {
            newMap.put(Registry.BLOCK.getKey(block), state);
        });
        return newMap;
    }

    public BlockState replace(BlockState original) {
        if (original.is(UETags.Blocks.REPLACABLE)) {
            return baseBlock;
        } else if (original.is(Blocks.COBBLESTONE) && cobbleBlock != null) {
            return cobbleBlock;
        } else if (original.is(UETags.Blocks.REPLACE_DIRT) && dirtReplace != null) {
            return dirtReplace;
        } else if (original.is(UETags.Blocks.REPLACE_GRASS) && grassReplace != null) {
            return getGrassReplace(original);
        }
        for (Block block : oreMap.keySet()) {
            if (original.is(block)) {
                return oreMap.get(block);
            }
        }
        return original;
    }
}

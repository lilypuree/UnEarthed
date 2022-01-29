package lilypuree.unearthed.util;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import lilypuree.unearthed.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class BlockStatePropertiesMatch implements LootItemCondition {
    public static void init() {

    }

    public static LootItemConditionType BLOCK_STATE_PROPERTIES_MATCH = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Constants.MOD_ID, "block_state_properties_match"), new LootItemConditionType(new BSPSerializer()));
    private final Block block;
    private final String propertyNameA;
    private final String propertyNameB;

    public BlockStatePropertiesMatch(Block block, String propertyNameA, String propertyNameB) {
        this.block = block;
        this.propertyNameA = propertyNameA;
        this.propertyNameB = propertyNameB;
    }

    @Override
    public LootItemConditionType getType() {
        return BLOCK_STATE_PROPERTIES_MATCH;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.BLOCK_STATE);
    }

    @Override
    public boolean test(LootContext lootContext) {
        BlockState blockState = lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (blockState != null && this.block == blockState.getBlock()) {
            StateDefinition<Block, BlockState> container = blockState.getBlock().getStateDefinition();
            Property<?> propertyA = container.getProperty(propertyNameA);
            Property<?> propertyB = container.getProperty(propertyNameB);
            if (propertyA != null && propertyB != null) {
                return blockState.getValue(propertyA).equals(blockState.getValue(propertyB));
            }
        }
        return false;
    }

    public static Builder builder(Block blockIn) {
        return new Builder(blockIn);
    }

    public static class Builder implements LootItemCondition.Builder {
        private final Block block;
        private String propertyNameA;
        private String propertyNameB;

        public Builder(Block blockIn) {
            this.block = blockIn;
        }

        public Builder propertiesToCompare(Property<?> propertyA, Property<?> propertyB) {
            propertyNameA = propertyA.getName();
            propertyNameB = propertyB.getName();
            return this;
        }

        @Override
        public LootItemCondition build() {
            return new BlockStatePropertiesMatch(block, propertyNameA, propertyNameB);
        }
    }

    public static class BSPSerializer implements Serializer<BlockStatePropertiesMatch> {
        @Override
        public void serialize(JsonObject jsonObject, BlockStatePropertiesMatch lootCondition, JsonSerializationContext context) {
            jsonObject.addProperty("block", Registry.BLOCK.getKey(lootCondition.block).toString());
            jsonObject.addProperty("propertyA", lootCondition.propertyNameA);
            jsonObject.addProperty("propertyB", lootCondition.propertyNameB);
        }

        @Override
        public BlockStatePropertiesMatch deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            ResourceLocation resourcelocation = new ResourceLocation(GsonHelper.getAsString(jsonObject, "block"));
            Block block = Registry.BLOCK.getOptional(resourcelocation).orElseThrow(() -> {
                return new IllegalArgumentException("Can't find block " + resourcelocation);
            });
            String nameA = GsonHelper.getAsString(jsonObject, "propertyA");
            String nameB = GsonHelper.getAsString(jsonObject, "propertyB");
            return new BlockStatePropertiesMatch(block, nameA, nameB);
        }
    }
}

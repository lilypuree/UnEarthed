package net.lilycorgitaco.unearthed.misc;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.lilycorgitaco.unearthed.Unearthed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.registry.Registry;

import java.util.Set;

public class BlockStatePropertiesMatch implements LootCondition {
    public static void init() {

    }

    public static LootConditionType BLOCK_STATE_PROPERTIES_MATCH = Registry.register(Registry.LOOT_CONDITION_TYPE, new Identifier(Unearthed.MOD_ID, "block_state_properties_match"), new LootConditionType(new Serializer()));
    private final Block block;
    private final String propertyNameA;
    private final String propertyNameB;

    public BlockStatePropertiesMatch(Block block, String propertyNameA, String propertyNameB) {
        this.block = block;
        this.propertyNameA = propertyNameA;
        this.propertyNameB = propertyNameB;
    }

    @Override
    public LootConditionType getType() {
        return BLOCK_STATE_PROPERTIES_MATCH;
    }


    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.BLOCK_STATE);
    }


    @Override
    public boolean test(LootContext lootContext) {
        BlockState blockState = lootContext.get(LootContextParameters.BLOCK_STATE);
        if (blockState != null && this.block == blockState.getBlock()) {
            StateManager<Block, BlockState> container = blockState.getBlock().getStateManager();
            Property<?> propertyA = container.getProperty(propertyNameA);
            Property<?> propertyB = container.getProperty(propertyNameB);
            if (propertyA != null && propertyB != null) {
                return blockState.get(propertyA).equals(blockState.get(propertyB));
            }
        }
        return false;
    }

    public static Builder builder(Block blockIn) {
        return new Builder(blockIn);
    }

    public static class Builder implements LootCondition.Builder {
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
        public LootCondition build() {
            return new BlockStatePropertiesMatch(block, propertyNameA, propertyNameB);
        }
    }

    public static class Serializer implements JsonSerializer<BlockStatePropertiesMatch> {
        @Override
        public void toJson(JsonObject json, BlockStatePropertiesMatch object, JsonSerializationContext context) {
            json.addProperty("block", Registry.BLOCK.getKey(object.block).toString());
            json.addProperty("propertyA", object.propertyNameA);
            json.addProperty("propertyB", object.propertyNameB);
        }

        @Override
        public BlockStatePropertiesMatch fromJson(JsonObject json, JsonDeserializationContext context) {
                Identifier resourcelocation = new Identifier(JsonHelper.getString(json, "block"));
            Block block = Registry.BLOCK.getOrEmpty(resourcelocation).orElseThrow(() -> {
                return new IllegalArgumentException("Can't find block " + resourcelocation);
            });
            String nameA = JsonHelper.getString(json, "propertyA");
            String nameB = JsonHelper.getString(json, "propertyB");
            return new BlockStatePropertiesMatch(block, nameA, nameB);
        }
    }
}

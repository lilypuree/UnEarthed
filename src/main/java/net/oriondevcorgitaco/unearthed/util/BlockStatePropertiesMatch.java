package net.oriondevcorgitaco.unearthed.util;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.oriondevcorgitaco.unearthed.Unearthed;

import java.util.Set;

public class BlockStatePropertiesMatch implements ILootCondition {
    public static void init(){

    }

    public static LootConditionType BLOCK_STATE_PROPERTIES_MATCH = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Unearthed.MOD_ID,"block_state_properties_match"), new LootConditionType(new BlockStatePropertiesMatch.Serializer()));
    private final Block block;
    private final String propertyNameA;
    private final String propertyNameB;

    public BlockStatePropertiesMatch(Block block, String propertyNameA, String propertyNameB) {
        this.block = block;
        this.propertyNameA = propertyNameA;
        this.propertyNameB = propertyNameB;
    }

    @Override
    public LootConditionType func_230419_b_() {
        return BLOCK_STATE_PROPERTIES_MATCH;
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.BLOCK_STATE);
    }

    @Override
    public boolean test(LootContext lootContext) {
        BlockState blockState = lootContext.get(LootParameters.BLOCK_STATE);
        if (blockState != null && this.block == blockState.getBlock()) {
            StateContainer<Block, BlockState> container = blockState.getBlock().getStateContainer();
            Property<?> propertyA = container.getProperty(propertyNameA);
            Property<?> propertyB = container.getProperty(propertyNameB);
            if (propertyA != null && propertyB != null) {
                return blockState.get(propertyA).equals(blockState.get(propertyB));
            }
        }
        return false;
    }
    public static Builder builder(Block blockIn) {
        return new  Builder(blockIn);
    }
    public static class Builder implements ILootCondition.IBuilder{
        private final Block block;
        private String propertyNameA;
        private String propertyNameB;

        public Builder(Block blockIn){
            this.block = blockIn;
        }
        public Builder propertiesToCompare(Property<?> propertyA, Property<?> propertyB){
            propertyNameA = propertyA.getName();
            propertyNameB = propertyB.getName();
            return this;
        }

        @Override
        public ILootCondition build() {
            return new BlockStatePropertiesMatch(block, propertyNameA, propertyNameB);
        }
    }

    public static class Serializer implements ILootSerializer<BlockStatePropertiesMatch> {
        @Override
        public void serialize(JsonObject jsonObject, BlockStatePropertiesMatch lootCondition, JsonSerializationContext context) {
            jsonObject.addProperty("block", Registry.BLOCK.getKey(lootCondition.block).toString());
            jsonObject.addProperty("propertyA", lootCondition.propertyNameA);
            jsonObject.addProperty("propertyB", lootCondition.propertyNameB);
        }

        @Override
        public BlockStatePropertiesMatch deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getString(jsonObject, "block"));
            Block block = Registry.BLOCK.getOptional(resourcelocation).orElseThrow(() -> {
                return new IllegalArgumentException("Can't find block " + resourcelocation);
            });
            String nameA = JSONUtils.getString(jsonObject, "propertyA");
            String nameB = JSONUtils.getString(jsonObject, "propertyB");
            return new BlockStatePropertiesMatch(block, nameA, nameB);
        }
    }
}

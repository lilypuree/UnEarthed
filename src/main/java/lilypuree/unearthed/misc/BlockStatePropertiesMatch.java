//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package lilypuree.unearthed.misc;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
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
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockStatePropertiesMatch implements LootItemCondition {
    public static RegistryObject<LootItemConditionType> BLOCK_STATE_PROPERTIES_MATCH;
    private final Block block;
    private final String propertyNameA;
    private final String propertyNameB;

    public static void init() {
    }

    public BlockStatePropertiesMatch(Block block, String propertyNameA, String propertyNameB) {
        this.block = block;
        this.propertyNameA = propertyNameA;
        this.propertyNameB = propertyNameB;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.BLOCK_STATE);
    }

    public boolean test(LootContext lootContext) {
        BlockState blockState = (BlockState)lootContext.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (blockState != null && this.block == blockState.getBlock()) {
            StateDefinition<Block, BlockState> container = blockState.getBlock().getStateDefinition();
            Property<?> propertyA = container.getProperty(this.propertyNameA);
            Property<?> propertyB = container.getProperty(this.propertyNameB);
            if (propertyA != null && propertyB != null) {
                return blockState.getValue(propertyA).equals(blockState.getValue(propertyB));
            }
        }

        return false;
    }

    public static Builder builder(Block blockIn) {
        return new Builder(blockIn);
    }

    static {
        BLOCK_STATE_PROPERTIES_MATCH = HoeDig.LOOT_CONDITIONS.register("block_state_properties_match", () -> new LootItemConditionType(new BSPSerializer()));
    }

    @Override
    public LootItemConditionType getType() {
        return BLOCK_STATE_PROPERTIES_MATCH.get();
    }

    public static class Builder implements LootItemCondition.Builder {
        private final Block block;
        private String propertyNameA;
        private String propertyNameB;

        public Builder(Block blockIn) {
            this.block = blockIn;
        }

        public Builder propertiesToCompare(Property<?> propertyA, Property<?> propertyB) {
            this.propertyNameA = propertyA.getName();
            this.propertyNameB = propertyB.getName();
            return this;
        }

        @Override
        public LootItemCondition build() {
            return new BlockStatePropertiesMatch(this.block, this.propertyNameA, this.propertyNameB);
        }
    }

    public static class BSPSerializer implements Serializer<BlockStatePropertiesMatch> {
        public void serialize(JsonObject jsonObject, BlockStatePropertiesMatch lootCondition, JsonSerializationContext context) {
            jsonObject.addProperty("block", ForgeRegistries.BLOCKS.getKey(lootCondition.block).toString());
            jsonObject.addProperty("propertyA", lootCondition.propertyNameA);
            jsonObject.addProperty("propertyB", lootCondition.propertyNameB);
        }

        public BlockStatePropertiesMatch deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            ResourceLocation resourcelocation = ResourceLocation.parse(GsonHelper.getAsString(jsonObject, "block"));
            Block block = ForgeRegistries.BLOCKS.getValue(resourcelocation);
            String nameA = GsonHelper.getAsString(jsonObject, "propertyA");
            String nameB = GsonHelper.getAsString(jsonObject, "propertyB");
            return new BlockStatePropertiesMatch(block, nameA, nameB);
        }
    }
}

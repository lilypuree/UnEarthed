package lilypuree.unearthed.misc;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class HoeDig implements LootItemCondition {
    public static void init() {

    }

    static final HoeDig INSTANCE = new HoeDig();
    public static LootItemConditionType HOE_DIG = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Constants.MOD_ID, "hoe_dig"), new LootItemConditionType(new HoeSerializer()));

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.TOOL);
    }

    @Override
    public LootItemConditionType getType() {
        return HOE_DIG;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack testTool = lootContext.getParamOrNull(LootContextParams.TOOL);
        return testTool != null && Services.PLATFORM.isDiggingHoe(testTool);
    }

    public static Builder builder() {
        return () -> INSTANCE;
    }

    public static class HoeSerializer implements Serializer<HoeDig> {
        @Override
        public void serialize(JsonObject jsonObject, HoeDig hoeDig, JsonSerializationContext jsonSerializationContext) {

        }

        @Override
        public HoeDig deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return HoeDig.INSTANCE;
        }
    }
}

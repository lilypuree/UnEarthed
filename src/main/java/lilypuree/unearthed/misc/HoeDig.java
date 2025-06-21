package lilypuree.unearthed.misc;

import com.google.common.collect.ImmutableSet;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import lilypuree.unearthed.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class HoeDig implements LootItemCondition {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS;
    static final HoeDig INSTANCE;
    static final RegistryObject<LootItemConditionType> HOE_DIG;

    public static void init(IEventBus bus) {
        LOOT_CONDITIONS.register(bus);
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.TOOL);
    }

    public boolean test(LootContext lootContext) {
        ItemStack testTool = lootContext.getParamOrNull(LootContextParams.TOOL);
        return testTool != null && Services.PLATFORM.isDiggingHoe(testTool);
    }

    public static LootItemCondition.Builder builder() {
        return () -> INSTANCE;
    }

    static {
        LOOT_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, "unearthed");
        INSTANCE = new HoeDig();
        HOE_DIG = LOOT_CONDITIONS.register("hoe_dig", () -> new LootItemConditionType(new HoeSerializer()));
    }

    @Override
    public LootItemConditionType getType() {
        return HOE_DIG.get();
    }

    public static class HoeSerializer implements Serializer<HoeDig> {
        public void serialize(JsonObject jsonObject, HoeDig hoeDig, JsonSerializationContext jsonSerializationContext) {
        }

        public HoeDig deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return HoeDig.INSTANCE;
        }
    }
}

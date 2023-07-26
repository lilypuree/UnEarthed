package lilypuree.unearthed.mixin.server;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.core.Registration;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onApply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler, CallbackInfo ci, Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, Recipe<?>>> recipeMap, ImmutableMap.Builder<ResourceLocation, Recipe<?>> byNameMapBuilder) {

        if (Constants.CONFIG.enableRegolithToDirt()) {
            ResourceLocation location = new ResourceLocation(Constants.MOD_ID, "dirt_from_regolith");
            NonNullList<Ingredient> nonNullList = NonNullList.create();
            nonNullList.add(Ingredient.of(Registration.REGOLITH.get()));
            Recipe<?> regolithToDirt = new ShapelessRecipe(location, "", new ItemStack(Items.DIRT), nonNullList);

            recipeMap.computeIfAbsent(regolithToDirt.getType(), x -> ImmutableMap.builder()).put(location, regolithToDirt);
            byNameMapBuilder.put(location, regolithToDirt);
        }
    }
}

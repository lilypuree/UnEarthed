package lilypuree.unearthed.mixin;

import lilypuree.unearthed.misc.UEDataLoaders;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerResources.class)
public class ServerResourcesMixin {

    @Shadow
    @Final
    private ReloadableResourceManager resources;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(RegistryAccess registryAccess, Commands.CommandSelection commandSelection, int p_180004_, CallbackInfo ci) {
        this.resources.registerReloadListener(UEDataLoaders.STONE_TYPE_LOADER);
        this.resources.registerReloadListener(UEDataLoaders.REGION_LOADER);
    }
}

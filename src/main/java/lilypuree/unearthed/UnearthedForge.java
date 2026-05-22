package lilypuree.unearthed;

import lilypuree.unearthed.compat.projecte.ProjectECompat;
import lilypuree.unearthed.core.Registration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.ModList;

@Mod(Constants.MOD_ID)
public class UnearthedForge {
    public UnearthedForge(FMLJavaModLoadingContext context) {
        CommonSetup.init();
        Constants.CONFIG = new UEForgeConfigs();
        context.registerConfig(ModConfig.Type.COMMON, UEForgeConfigs.COMMON_CONFIG);
        IEventBus modbus = context.getModEventBus();
        Registration.init(modbus);
        Registration.registerLootConditions(modbus);
        modbus.addListener(this::commonSetup);
        if (ModList.get().isLoaded("projecte")) {
            modbus.addListener(ProjectECompat::init);
        }

    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CommonSetup.commonSetup();
    }
}

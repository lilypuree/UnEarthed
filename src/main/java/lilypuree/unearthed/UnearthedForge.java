package lilypuree.unearthed;

import lilypuree.unearthed.core.Registration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class UnearthedForge {
    public UnearthedForge() {
        CommonSetup.init();
        Constants.CONFIG = new UEForgeConfigs();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, UEForgeConfigs.COMMON_CONFIG);
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.init(modbus);
        modbus.addListener(this::commonSetup);

    }

    public void commonSetup(FMLCommonSetupEvent event) {
        CommonSetup.commonSetup();
        Registration.registerLootConditions();
    }
}

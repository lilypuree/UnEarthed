package lilypuree.unearthed;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class UnearthedFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        AutoConfig.register(DecaptConfigs.class, JanksonConfigSerializer::new);
        Constants.config = AutoConfig.getConfigHolder(DecaptConfigs.class).getConfig();

        CommonMod.init();
    }
    
}

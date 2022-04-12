package lilypuree.unearthed;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Constants.MOD_ID)
public class UEFabricConfigs implements ConfigData, CommonConfig {

    private boolean enableRegolithToDirt = false;

    @Override
    public boolean enableRegolithToDirt() {
        return enableRegolithToDirt;
    }
}

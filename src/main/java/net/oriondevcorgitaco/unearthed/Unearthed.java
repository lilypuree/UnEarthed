package net.oriondevcorgitaco.unearthed;

import net.fabricmc.api.ModInitializer;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.util.BlockAssetHelper;
import net.oriondevcorgitaco.unearthed.util.FeatureAdder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Unearthed implements ModInitializer {
    public static final String MOD_ID = "unearthed";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        BlockGeneratorReference.init();
        FeatureAdder.strataGeneratorForAllBiomes();
        BlockAssetHelper.jsonPrinter();
        BlockAssetHelper.printBlockIDs();
    }
}

package net.oriondevcorgitaco.hextension;

import net.fabricmc.api.ModInitializer;

public class Hextension implements ModInitializer {
	public static final String MOD_ID = "hextension";

	@Override
	public void onInitialize() {
		BlockGenerators.addToSedimentaryList();
		BlockGenerators.generateStoneBlocks();
	}
}

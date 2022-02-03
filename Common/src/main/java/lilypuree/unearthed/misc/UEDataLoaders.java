package lilypuree.unearthed.misc;

import commoble.databuddy.data.CodecJsonDataManager;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.world.feature.StoneRegion;
import lilypuree.unearthed.world.feature.gen.StoneType;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class UEDataLoaders {
    public static final CodecJsonDataManager<StoneType> STONE_TYPE_LOADER = new CodecJsonDataManager<>(
            "uedata/stone_types", StoneType.DIRECT_CODEC, Constants.LOG);
    public static final CodecJsonDataManager<StoneRegion> REGION_LOADER = new CodecJsonDataManager<>(
            "uedata/regions", StoneRegion.CODEC, Constants.LOG);

    private static StoneType getStoneType(ResourceLocation rl) {
        return STONE_TYPE_LOADER.getData().get(rl);
    }

    public static StoneType getStoneType(String name) {
        if (name.equals("")) return StoneType.NO_REPLACE;
        if (name.contains(":")) return getStoneType(new ResourceLocation(name));
        return getStoneType(new ResourceLocation(Constants.MOD_ID, name));
    }

    public static String getNameForStoneType(StoneType state) {
        ResourceLocation key = STONE_TYPE_LOADER.getData().inverse().get(state);
        if (key != null) return key.toString();
        return null;
    }

    public static ResourceLocation getNameForRegion(StoneRegion region) {
        return REGION_LOADER.getData().inverse().get(region);
    }

    public static Optional<StoneRegion> getRegion(ResourceLocation name) {
        return REGION_LOADER.getData().containsKey(name) ? Optional.of(REGION_LOADER.getData().get(name)) : Optional.empty();
    }
}

package lilypuree.unearthed.core;

import lilypuree.unearthed.Constants;
import net.minecraft.resources.ResourceLocation;

public class UENames {

    public static final ResourceLocation PYROXENE = getName("pyroxene");
    public static final ResourceLocation LICHEN = getName("lichen");
    public static final ResourceLocation LIGNITE_BRIQUETTES = getName("lignite_briquettes");
    public static final ResourceLocation GOLD_ORE = getName("gold_ore");
    public static final ResourceLocation IRON_ORE = getName("iron_ore");
    public static final ResourceLocation REGOLITH = getName("regolith");


    public static final ResourceLocation STONE_REPLACER = getName("stone_replacer");

    private static ResourceLocation getName(String name) {
        return new ResourceLocation(Constants.MOD_ID, name);
    }

}

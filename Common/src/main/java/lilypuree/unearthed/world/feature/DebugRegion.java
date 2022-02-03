package lilypuree.unearthed.world.feature;

import lilypuree.unearthed.world.feature.gen.PrimaryNoiseSampler;
import lilypuree.unearthed.world.feature.gen.SecondaryNoiseSampler;
import lilypuree.unearthed.world.feature.gen.StoneType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class DebugRegion {
    private static StoneType white = stoneType(Blocks.WHITE_CONCRETE);
    private static StoneType black = stoneType(Blocks.BLACK_CONCRETE);
    private static List<StoneType> tertiary = List.of(
            stoneType(Blocks.DIAMOND_BLOCK),
            stoneType(Blocks.EMERALD_BLOCK),
            stoneType(Blocks.NETHERITE_BLOCK),
            stoneType(Blocks.LAPIS_BLOCK));
    private static List<StoneType> colored = List.of(
            stoneType(Blocks.LIGHT_GRAY_CONCRETE),
            stoneType(Blocks.PINK_CONCRETE),
            stoneType(Blocks.MAGENTA_CONCRETE),
            stoneType(Blocks.PURPLE_CONCRETE),
            stoneType(Blocks.BLUE_CONCRETE),
            stoneType(Blocks.LIGHT_BLUE_CONCRETE),
            stoneType(Blocks.CYAN_CONCRETE),
            stoneType(Blocks.GREEN_CONCRETE),
            stoneType(Blocks.LIME_CONCRETE),
            stoneType(Blocks.YELLOW_CONCRETE),
            stoneType(Blocks.ORANGE_CONCRETE),
            stoneType(Blocks.RED_CONCRETE),
            stoneType(Blocks.BROWN_CONCRETE),
            stoneType(Blocks.GRAY_CONCRETE)
    );
    private static List<StoneType> colored_wool = List.of(
            stoneType(Blocks.LIGHT_GRAY_WOOL),
            stoneType(Blocks.PINK_WOOL),
            stoneType(Blocks.MAGENTA_WOOL),
            stoneType(Blocks.PURPLE_WOOL),
            stoneType(Blocks.BLUE_WOOL),
            stoneType(Blocks.LIGHT_BLUE_WOOL),
            stoneType(Blocks.CYAN_WOOL),
            stoneType(Blocks.GREEN_WOOL),
            stoneType(Blocks.LIME_WOOL),
            stoneType(Blocks.YELLOW_WOOL),
            stoneType(Blocks.ORANGE_WOOL),
            stoneType(Blocks.RED_WOOL),
            stoneType(Blocks.BROWN_WOOL),
            stoneType(Blocks.GRAY_WOOL)
    );

    public static StoneRegion DEBUG_REGION = getDebug();

    public static StoneRegion getDebug() {
        return new StoneRegion(
                new PrimaryNoiseSampler.Settings(white, colored, StoneType.NO_REPLACE, black, -0.875f, 0.875f, 0.875f, 0),
                new SecondaryNoiseSampler.Settings(colored_wool, white, black, 50, 100),
                tertiary, 0.99f, 0);
    }

    private static StoneType stoneType(Block block) {
        return new StoneType(block.defaultBlockState());
    }
}

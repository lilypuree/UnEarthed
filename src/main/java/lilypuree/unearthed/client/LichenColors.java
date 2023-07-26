package lilypuree.unearthed.client;

import lilypuree.unearthed.util.ColorHelper;
import lilypuree.unearthed.util.FastNoiseLite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;

public class LichenColors {
    private static int levels = 2;
    //    private static final PerlinNoiseGenerator NOISE_LICHEN_COLOR = new PerlinNoiseGenerator(new SharedSeedRandom("NOISE_LICHEN_COLOR".hashCode()), IntStream.range(0, levels));
//    private static final PerlinNoiseGenerator NOISE_LICHEN_VALUE = new PerlinNoiseGenerator(new SharedSeedRandom("NOISE_LICHEN_VALUE".hashCode()), IntStream.range(0, levels));
//    private static final PerlinNoiseGenerator NOISE_LICHEN_OFFSET = new PerlinNoiseGenerator(new SharedSeedRandom("LICHEN_NOISE_OFFSET".hashCode()), IntStream.range(0, levels));
    private static int LICHEN_COLOR_WET = 0x95cca0; //7063173
    private static int LICHEN_COLOR_DRY = 11314315;
    private static int LICHEN_COLOR_RED = 14305316;
    private static int LICHEN_COLOR_YELLOW = 14338340;


    private static final FastNoiseLite NOISE_LICHEN_SAT;
    private static final FastNoiseLite NOISE_LICHEN_COLOR;
    private static final FastNoiseLite NOISE_LICHEN_OFFSET_POS;
    private static final FastNoiseLite NOISE_LICHEN_OFFSET;

    public static final ColorResolver LICHEN_COLOR = LichenColors::getBiomeColor;

    static {
        NOISE_LICHEN_SAT = new FastNoiseLite(8656282);
        NOISE_LICHEN_SAT.SetFrequency(0.18f);
        NOISE_LICHEN_SAT.SetFractalGain(0.7f);
        NOISE_LICHEN_SAT.SetFractalLacunarity(2.4f);

        NOISE_LICHEN_COLOR = new FastNoiseLite(39912828);
        NOISE_LICHEN_COLOR.SetFractalOctaves(2);
        NOISE_LICHEN_COLOR.SetFrequency(0.025f);

        NOISE_LICHEN_OFFSET_POS = new FastNoiseLite(10296719);
        NOISE_LICHEN_OFFSET_POS.SetFractalType(FastNoiseLite.FractalType.Ridged);
        NOISE_LICHEN_OFFSET_POS.SetFractalOctaves(2);
        NOISE_LICHEN_OFFSET_POS.SetFrequency(0.012f);

        NOISE_LICHEN_OFFSET = new FastNoiseLite(88129387);
        NOISE_LICHEN_OFFSET.SetFractalOctaves(3);
        NOISE_LICHEN_OFFSET.SetFrequency(0.08f);
    }

    public static int get2(float posX, float posZ) {
        float satNoise = NOISE_LICHEN_SAT.GetNoise(posX, posZ);
        float offset_pos = NOISE_LICHEN_OFFSET_POS.GetNoise(posX, posZ);
        float colNoise = NOISE_LICHEN_COLOR.GetNoise(posX, posZ);
        if (offset_pos > 0.7f) {
            float offset = NOISE_LICHEN_OFFSET.GetNoise(posX, posZ);
            if (offset > 0.2f) {
                return LICHEN_COLOR_YELLOW;
            } else if (offset < -0.2f) {
                return LICHEN_COLOR_RED;
            }
        }

        final float hueshift_range = 18;
        return ColorHelper.shiftColorHSL(LICHEN_COLOR_WET,
                colNoise * colNoise * hueshift_range + 5,
                (float) hyperbolicRemap(satNoise * satNoise, -1.1f, 1.1f, -0.25f, 0.2f, -1.2f),
//                    (float) remap(valNoise, -1.0f, 1.0f, -0.08f, 0.08f));
                0);
    }

    public static int getBiomeColor(Biome biome, double posX, double posZ) {
        float offset_pos = NOISE_LICHEN_OFFSET_POS.GetNoise((float) posX, (float) posZ);
        float colNoise = NOISE_LICHEN_COLOR.GetNoise((float) posX, (float) posZ);
        if (offset_pos > 0.7f) {
            float offset = NOISE_LICHEN_OFFSET.GetNoise((float) posX, (float) posZ);
            if (offset > 0.2f) {
                return LICHEN_COLOR_YELLOW;
            } else if (offset < -0.2f) {
                return LICHEN_COLOR_RED;
            }
        }
        final float hueshift_range = 18;
        return ColorHelper.shiftColorHSL(LICHEN_COLOR_WET,
                colNoise * colNoise * hueshift_range + 5, 0, 0);
    }

    public static int shiftSaturation(int color, BlockPos pos, boolean wet) {
        float satNoise = NOISE_LICHEN_SAT.GetNoise(pos.getX(), pos.getZ());
        if (wet) {
            return ColorHelper.shiftColorHSL(color, 0,
                    (float) hyperbolicRemap(satNoise * satNoise, -1.1f, 1.1f, -0.25f, 0.2f, -1.2f), 0);
        } else {
            return ColorHelper.shiftColorHSL(color, -10,
                    (float) hyperbolicRemap(satNoise * satNoise, -1.0f, 1.0f, -0.4f, -0.2f, -1.2f), 0.04f);
        }
    }

    public static float hyperbolicRemap(float value, float minX, float maxX, float minY, float maxY, float curvature) {
        if (curvature > 0) curvature = -curvature;
        value = Mth.clamp(value, minX, maxX);
        float xsum = minX + maxX;
        float xdiff = maxX - minX;
        float ydiff = maxY - minY;
        float b = 0.5f * (xsum + (float) Math.sqrt(xsum * xsum - 4 * (minX * maxX + xdiff / ydiff * curvature)));
        float c = (maxX * maxY - minX * minY - b * ydiff) / xdiff;
        return curvature / (value - b) + c;
    }

    public static double remap(final double value, final double currentLow, final double currentHigh, final double newLow, final double newHigh) {
        return newLow + (value - currentLow) * (newHigh - newLow) / (currentHigh - currentLow);
    }

    public static int getLichen() {
        return LICHEN_COLOR_WET;
    }
}

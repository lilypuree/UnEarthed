package lilypuree.unearthed.world.feature.gen;

import lilypuree.unearthed.util.noise.FastNoiseLite;
import lilypuree.unearthed.world.feature.StoneRegion;
import net.minecraft.util.Mth;

public class RoughNoiseSampler implements ChunkFiller.StonetypeGetter {

    private static FastNoiseLite tertiaryNoise;
    private static FastNoiseLite unconformityNoise;

    private SecondaryNoiseSampler secondaryNoiseSampler;
    private StoneRegion region;

    public RoughNoiseSampler(StoneRegion region, int posX, int posZ, int minY, int height) {
        float unconformity = unconformityNoise.GetNoise(posX, posZ);
        float cutoff = region.getUnconformityCutoff();
        int unconformityBoundary = minY + (int) Mth.map(unconformity, -0.2f + cutoff, 0.2f + cutoff, 0, 160);
        this.secondaryNoiseSampler = new SecondaryNoiseSampler(region.secondary(), unconformityBoundary, height);

        this.region = region;
    }

    @Override
    public StoneType get(int posX, int posY, int posZ) {
        StoneType tertiary = selectTertiary(tertiaryNoise.GetNoise(posX, posY, posZ));
        if (tertiary != null) return tertiary;

        return secondaryNoiseSampler.get(posX, posY, posZ);
    }


    protected StoneType selectTertiary(float tertiaryNoise) {
        float cubed = tertiaryNoise * tertiaryNoise * tertiaryNoise;
        float cutOffOriginal = (float) Math.pow(region.getTertiaryCutoff(), 1 / 3.0);
        if (cubed > region.getTertiaryCutoff()) {
            return region.tertiary().get((int) Mth.clampedMap(tertiaryNoise, cutOffOriginal, 1, 0, region.tertiary().size()));
        }
        return null;
    }


    public static void setSeed(int seed, float tertiaryFreq, float unconformityFreq) {
        tertiaryNoise = new FastNoiseLite(seed);
        tertiaryNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        tertiaryNoise.SetFractalType(FastNoiseLite.FractalType.None);
        tertiaryNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        tertiaryNoise.SetFrequency(tertiaryFreq);

        unconformityNoise = new FastNoiseLite(seed - 55791);
        unconformityNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        unconformityNoise.SetFractalOctaves(2);
        unconformityNoise.SetFrequency(unconformityFreq);
    }
}

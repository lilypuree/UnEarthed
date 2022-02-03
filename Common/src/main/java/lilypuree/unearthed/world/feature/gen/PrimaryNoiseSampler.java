package lilypuree.unearthed.world.feature.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lilypuree.unearthed.util.noise.FastNoiseLite;
import net.minecraft.util.Mth;

import java.util.List;

public class PrimaryNoiseSampler implements ChunkFiller.StonetypeGetter {

    private static FastNoiseLite primaryNoiseA;
    private static FastNoiseLite primaryNoiseB;

    private float primary;
    private Settings settings;

    private int minY;

    public PrimaryNoiseSampler(Settings settings, int posX, int posZ, int minY) {
        this.settings = settings;
        this.primary = getPrimaryNoise(posX, posZ);
        this.minY = minY;
    }

    @Override
    public StoneType get(int posX, int posY, int posZ) {
        int y = posY - minY;

        float modifiedPrimary = primary + Mth.map(y, 120, 0, 0, settings.strataFlatness);
        if (modifiedPrimary > settings.deepBound) {
            return settings.deep;
        } else if (modifiedPrimary >= settings.strataBound) {
            return settings.base;
        } else if (modifiedPrimary < settings.highBound) {
            return settings.high;
        } else if (settings.strataSize() > 0) {
            int strataIndex = (int) Mth.clampedMap(modifiedPrimary, settings.highBound, settings.strataBound, 0, settings.strataSize());
            return settings.stratas.get(strataIndex);
        } else {
            return settings.base;
        }
    }

    public float getPrimaryNoise(int posX, int posZ) {
        float threshold = -0.5f;
        float noiseA = Math.max(primaryNoiseA.GetNoise(posX, posZ), threshold);
        float noiseB = Math.max(primaryNoiseB.GetNoise(posX, posZ), threshold);
        return Mth.map(noiseA + noiseB, threshold * 2, 2, -1, 1);
    }

    public static void setSeed(int seed, float primaryFrequency) {
        primaryNoiseA = new FastNoiseLite(seed);
        primaryNoiseA.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        primaryNoiseA.SetFractalType(FastNoiseLite.FractalType.FBm);
        primaryNoiseA.SetFractalOctaves(3);
        primaryNoiseA.SetFrequency(primaryFrequency);

        primaryNoiseB = new FastNoiseLite(seed + 2347);
        primaryNoiseB.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        primaryNoiseB.SetFractalType(FastNoiseLite.FractalType.FBm);
        primaryNoiseB.SetFractalOctaves(3);
        primaryNoiseB.SetFrequency(primaryFrequency * 1.01f);
    }

    public record Settings(StoneType high, List<StoneType> stratas, StoneType base, StoneType deep,
                           float highBound, float strataBound, float deepBound, float strataFlatness) {
        public static Codec<Settings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                StoneType.CODEC.fieldOf("high").forGetter(Settings::high),
                StoneType.CODEC.listOf().fieldOf("stratas").forGetter(Settings::stratas),
                StoneType.CODEC.fieldOf("base").forGetter(Settings::base),
                StoneType.CODEC.fieldOf("deep").forGetter(Settings::deep),
                Codec.floatRange(-1, 1).fieldOf("high_bound").forGetter(Settings::highBound),
                Codec.floatRange(-1, 1).fieldOf("strata_bound").forGetter(Settings::strataBound),
                Codec.floatRange(-1, 1).fieldOf("deep_bound").forGetter(Settings::deepBound),
                Codec.floatRange(0, 10).fieldOf("strata_flatness").forGetter(Settings::strataFlatness)
        ).apply(instance, Settings::new));

        public int strataSize() {
            return stratas.size();
        }
    }
}

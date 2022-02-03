package lilypuree.unearthed.world.feature.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lilypuree.unearthed.util.noise.FastNoiseLite;
import net.minecraft.util.Mth;

import java.util.List;

public class SecondaryNoiseSampler implements ChunkFiller.StonetypeGetter {
    private static FastNoiseLite typeNoise;
    private static FastNoiseLite warpNoise;

    private Settings settings;
    private int unconformityBoundary;
    private int primaryBoundary;
    private int deepBoundary;

    public SecondaryNoiseSampler(Settings settings, int unconformityBoundary, int height) {
        this.settings = settings;
        this.unconformityBoundary = unconformityBoundary;

        this.primaryBoundary = height - settings.firstLayerThickness;
        this.deepBoundary = height - settings.deepLayerDepth;
    }

    @Override
    public StoneType get(int posX, int posY, int posZ) {
        if (unconformityBoundary < posY) {
            if (posY > primaryBoundary) {
                FastNoiseLite.Vector3 v3 = new FastNoiseLite.Vector3(posX, posY / 4.0f, posZ);
                warpNoise.DomainWarp(v3);
                return select(typeNoise.GetNoise(v3.x, v3.y, v3.z));
            } else if (posY > deepBoundary) {
                return settings.base;
            } else {
                return settings.deep;
            }
        }
        return StoneType.EMPTY;
    }

    protected StoneType select(float primaryNoise) {
        return settings.stoneTypes().get((int) Mth.clampedMap(primaryNoise, -1, 1, 0, settings.count()));
    }

    public record Settings(List<StoneType> stoneTypes, StoneType base, StoneType deep, int firstLayerThickness, int deepLayerDepth) {
        public static Codec<Settings> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                StoneType.CODEC.listOf().fieldOf("top_layer_types").forGetter(Settings::stoneTypes),
                StoneType.CODEC.fieldOf("base").forGetter(Settings::base),
                StoneType.CODEC.fieldOf("deep").forGetter(Settings::deep),
                Codec.intRange(0, 4096).fieldOf("top_layer_thickness").forGetter(Settings::firstLayerThickness),
                Codec.intRange(0, 4096).fieldOf("deep_layer_depth").forGetter(Settings::deepLayerDepth)
        ).apply(instance, Settings::new));

        public int count() {
            return stoneTypes.size();
        }
    }

    public static void setSeed(int seed, float secondaryFreq) {
        typeNoise = new FastNoiseLite(seed);
        typeNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        typeNoise.SetFractalType(FastNoiseLite.FractalType.None);
        typeNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        typeNoise.SetCellularJitter(0.9f);
        typeNoise.SetFrequency(secondaryFreq);

        warpNoise = new FastNoiseLite(seed + 2881);
        warpNoise.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
        warpNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        warpNoise.SetDomainWarpAmp(30);
        warpNoise.SetFractalOctaves(3);
        warpNoise.SetFrequency(secondaryFreq * 0.9f);
    }
}

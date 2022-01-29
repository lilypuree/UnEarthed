package lilypuree.unearthed.world.feature;

import lilypuree.unearthed.util.noise.FastNoiseLite;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class ReplacementNoiseSampler {

    private static FastNoiseLite regionNoise;
    private static FastNoiseLite oldStrataNoiseA;
    private static FastNoiseLite oldStrataNoiseB;
    private static FastNoiseLite unconformityNoise;
    private static FastNoiseLite newStrataThicknessNoise;
    private static FastNoiseLite biomeModifier;
    private static FastNoiseLite newStrataStoneType;
    private static FastNoiseLite newStrataStoneTypeWarp;

    private static final List<ChunkFiller.State> oldStrataBlocks;
    private static final List<ChunkFiller.State> newStrataBlocks;
    private static float oldStrataFlatness = 0.6f; //0 is completely vertical
    private static float oldStrataCutoffMin = -0.7f;
    private static float oldStrataCutoffMax = 0.1f;
    private static float oldStrataDeepStart = 0.5f;
    private static int oldStrataCount = 6;

    private float unconformity;
    private float oldStrata;
    private float newStrataThickness;

    static {
        List<ChunkFiller.State> blocks = List.of(States.PHYLLITE, States.GABBRO, States.GRANODIORITE, States.PILLOW_BASALT, States.WHITE_GRANITE, States.SLATE);
        oldStrataBlocks = new ArrayList<>(oldStrataCount);
        for (int i = 0; i < oldStrataCount; i++) {
            oldStrataBlocks.add(blocks.get(i % blocks.size()));
        }
        newStrataBlocks = List.of(States.BEIGE_LIMESTONE, States.SILTSTONE, States.MUDSTONE, States.LIMESTONE);
    }

    private int minY;
    private int height;

    public ReplacementNoiseSampler(int posX, int posZ, int minY, int height) {
        this.unconformity = unconformityNoise.GetNoise(posX, posZ);
        this.oldStrata = getOldStrataNoise(posX, posZ);
        this.newStrataThickness = getNewStrataThickness(posX, posZ);
        this.minY = minY;
        this.height = height;
    }

    public void fillColumn(int minY, int height) {
        int unconformityBoundary = (int) Mth.map(unconformity, -0.2f, 0.2f, 0, 160);
        if (unconformityBoundary < minY) {
            //only new strata

        } else if (unconformityBoundary > height) {
            //only old strata
        } else {
            for (int y = minY; y < height; y++) {
                if (y < unconformityBoundary) {
                    //old strata
                } else {
                    //new strata
                }
            }
        }
    }

    public ChunkFiller.State select(int posX, int posY, int posZ) {
        int y = posY - minY;
        if (unconformity > Mth.map(y, 0, 160, -0.2f, 0.2f)) {
//        if (true) {
            //old strata
            float oldStrataModified = oldStrata + Mth.map(y, 120, 0, 0, oldStrataFlatness);
//            float oldStrataModified = oldStrata;
            if (oldStrataModified > oldStrataDeepStart) {
                return States.GOLD;
            } else if (oldStrataModified >= oldStrataCutoffMax) {
                return States.STONE;
            } else if (oldStrataModified < oldStrataCutoffMin) {
                return States.CONGLOMERATE;
            } else {
                int strataIndex = (int) Mth.map(oldStrataModified, oldStrataCutoffMin, oldStrataCutoffMax, 0, oldStrataCount);
                return oldStrataBlocks.get(strataIndex);
            }
        } else {
            return States.SILTSTONE;
            //new strata
//            if (yFromTop < newStrataThickness) {
//                float newStrataType = getNewStrataStoneTypeNoise(posX, y, posZ);
//                int strataIndex = Math.min((int) Mth.map(newStrataType, -1, 1, 0, newStrataBlocks.size()), newStrataBlocks.size() - 1);
//                return newStrataBlocks.get(strataIndex);
//            } else {
//                return Blocks.GLASS.defaultBlockState();
//            }
        }
    }

    public int getNewStrataThickness(int posX, int posZ) {
        int baseThickness = 40;
        float noise = newStrataThicknessNoise.GetNoise(posX, posZ);
        return baseThickness;
    }

    public float getOldStrataNoise(int posX, int posZ) {
        float threshold = -0.5f;
        float noiseA = Math.max(oldStrataNoiseA.GetNoise(posX, posZ), threshold);
        float noiseB = Math.max(oldStrataNoiseB.GetNoise(posX, posZ), threshold);
        return Mth.map(noiseA + noiseB, threshold * 2, 2, -1, 1);
    }

    private float getNewStrataStoneTypeNoise(int posX, int posY, int posZ) {
        FastNoiseLite.Vector3 v = new FastNoiseLite.Vector3(posX, posY, posZ);
        newStrataStoneTypeWarp.DomainWarp(v);
        return newStrataStoneType.GetNoise(v.x, v.z);
    }

    public int getHeight() {
        return height;
    }

    public static void setSeed(int seed, int newSeed) {
        float globalFreq = 0.0015f;
        float stoneTypeFreq = 0.008f;
        regionNoise = new FastNoiseLite(seed - 193864);
        regionNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        regionNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        regionNoise.SetFrequency(globalFreq / 20);

        unconformityNoise = new FastNoiseLite(seed);
        unconformityNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        unconformityNoise.SetFractalOctaves(2);
        unconformityNoise.SetFrequency(globalFreq);

        oldStrataNoiseA = new FastNoiseLite(seed + 777261);
        oldStrataNoiseA.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        oldStrataNoiseA.SetFractalType(FastNoiseLite.FractalType.FBm);
        oldStrataNoiseA.SetFractalOctaves(3);
        oldStrataNoiseA.SetFrequency(globalFreq * 1.5f);

        oldStrataNoiseB = new FastNoiseLite(seed + 999173);
        oldStrataNoiseB.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        oldStrataNoiseB.SetFractalType(FastNoiseLite.FractalType.FBm);
        oldStrataNoiseB.SetFractalOctaves(3);
        oldStrataNoiseB.SetFrequency(globalFreq * 1.55f);

        newStrataThicknessNoise = new FastNoiseLite(newSeed);
        newStrataThicknessNoise.SetFractalOctaves(2);
        newStrataThicknessNoise.SetFractalType(FastNoiseLite.FractalType.FBm);
        newStrataThicknessNoise.SetFrequency(globalFreq * 0.5f);

        biomeModifier = new FastNoiseLite(newSeed - 1026497);
        biomeModifier.SetFrequency(globalFreq * 2);
        biomeModifier.SetFractalOctaves(2);

        newStrataStoneType = new FastNoiseLite(newSeed + 390271);
        newStrataStoneType.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
        newStrataStoneType.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        newStrataStoneType.SetCellularJitter(0.8f);
        newStrataStoneType.SetFrequency(stoneTypeFreq);

        newStrataStoneTypeWarp = new FastNoiseLite(newSeed + 2881);
        newStrataStoneTypeWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
        newStrataStoneTypeWarp.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        newStrataStoneTypeWarp.SetDomainWarpAmp(40);
        newStrataStoneTypeWarp.SetFractalOctaves(3);
        newStrataStoneTypeWarp.SetFrequency(stoneTypeFreq / 15);
    }
}

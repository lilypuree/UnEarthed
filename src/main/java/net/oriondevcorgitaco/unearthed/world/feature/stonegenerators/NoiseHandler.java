package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators;

import com.ibm.icu.impl.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.Heightmap;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoiseLite;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoiseLite.Vector3;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoiseLite.Vector2;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen.NoiseUtil;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.regions.Region;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.regions.Regions;

import java.util.Random;

public class NoiseHandler {
    private static float primaryFreq = 0.02f;
    private static float secondaryFreq = 0.002f;
    private static float batolithFreq = 0.002f;
    private static float tertiaryFreq = 0.05f;
    private static float stratumFreq = 0.01f;
    public static float orogenWarpAmplitude = 400;

    private static FastNoiseLite primaryNoise;
    private static FastNoiseLite primaryWarp;
    private static FastNoiseLite orogenNoise;
    private static FastNoiseLite orogenWarp;

    private static FastNoiseLite secondaryNoise;
    private static FastNoiseLite secondaryWarp;

    private static FastNoiseLite batolithTypeQF;
    private static FastNoiseLite batolithTypeAP;
    private static FastNoiseLite batolithHeight;

    private static FastNoiseLite tertiaryNoise;

    private static FastNoiseLite stratumBorders;
    private static FastNoiseLite stratumWarp;
    public static float strataWarpAmplitude = 60;

    private ISeedReader world;
    private Region[][] regions;
    private int[][] maxHeights;
    private int[][] secondaryDepths;
    private int[][] batolithHeights;
    private State[][] batolithStates;
    private BlockPos basePos;

    public NoiseHandler(ISeedReader world, BlockPos pos) {
        this.world = world;
        this.basePos = pos;
        this.maxHeights = new int[17][17];
        regions = new Region[17][17];
        secondaryDepths = new int[17][17];
        batolithHeights = new int[17][17];
        batolithStates = new State[17][17];
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 17; j++) {
                secondaryDepths[i][j] = -1;
                batolithHeights[i][j] = -1;
                batolithStates[i][j] = null;
                regions[i][j] = null;
                maxHeights[i][j] = world.getHeight(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX() + i, pos.getZ() + i);
            }
        }
    }

    public State getState(int x, int z, int y) {
        int xPos = basePos.getX() + x;
        int zPos = basePos.getZ() + z;

        float tertiary = tertiaryNoise.GetNoise(xPos, zPos, y);
        tertiary *= tertiary * tertiary;
        if (tertiary > 0.95f) {
            return Regions.getTertiaryState((tertiary-0.9f) * 10);
        }
        if (regions[x][z] == null) {
            regions[x][z] = getRegion(xPos, zPos);
        }
        Region region = regions[x][z];
        if (secondaryDepths[x][z] < 0) {
            secondaryDepths[x][z] = getSecondaryDepth(region, xPos, zPos);
        }
        boolean isSecondary = secondaryDepths[x][z] != 0 && (maxHeights[x][z] - y) < secondaryDepths[x][z];
        if (batolithHeights[x][z] < 0) {
            batolithHeights[x][z] = (int) (getBatolithPercentage(region, xPos, zPos) * maxHeights[x][z]);
        }
        boolean isBatolith = y < batolithHeights[x][z] || batolithHeights[x][z] > maxHeights[x][z] * 0.9f;
        if (isSecondary && (!isBatolith || !region.batholithIntrudes())) {
            return region.getSecondaryState();
        } else if (isBatolith && (!isSecondary || region.batholithIntrudes())) {
            if (batolithStates[x][z] == null) {
                batolithStates[x][z] = getBatolithState(xPos, zPos);
            }
            return batolithStates[x][z];
        } else {
            Vector3 originalPos = new Vector3(xPos, zPos, y / 3.0f);
            Vector3 orogenPerturb = new Vector3(xPos, zPos, y / 3.0f);
            orogenWarp.DomainWarp(orogenPerturb);
            if (region.isOrogen(originalPos, orogenPerturb)) {
                return region.getOrogenState(orogenNoise.GetNoise(orogenPerturb.x, orogenPerturb.y, orogenPerturb.z));
            } else {
                Vector3 primaryPertub = new Vector3(xPos, zPos, y);
                primaryWarp.DomainWarp(primaryPertub);
                return region.getPrimaryState(primaryNoise.GetNoise(primaryPertub.x, primaryPertub.y));
            }
        }
    }

    public Pair<State, Float> getReplaceState(int x, int z, int y, State state, int size) {
        int xPos = basePos.getX() + x;
        int zPos = basePos.getZ() + z;
        if (regions[x][z] == null) {
            regions[x][z] = getRegion(xPos, zPos);
        }
        Region region = regions[x][z];
        return region.getReplacement(state, size);
    }

    private static Random random = new Random();
    public static Pair<State, Integer> getStrata(int xPos, int zPos, int strataLevel, long worldSeed) {

        Vector2 v = new Vector2(xPos, zPos);
        stratumWarp.SetSeed((int) worldSeed + strataLevel * 100811);
        stratumBorders.SetSeed((int) worldSeed + strataLevel * 100811);
        stratumWarp.DomainWarp(v);
        Pair<Vector2, Float> cenAndDist = stratumBorders.GetCellularCenter2Distance(v.x, v.y);
        Region region = getRegion((int) cenAndDist.first.x, (int) cenAndDist.first.y);

        random.setSeed(NoiseUtil.getSeed((int) cenAndDist.first.x, (int) cenAndDist.first.y, worldSeed));
        if (cenAndDist.second * cenAndDist.second > region.getStratumPercentage()) {
            State state = region.getStrataState(random.nextFloat(), strataLevel);
            if (state != null) {
                return Pair.of(state, region.getStratumDepth((int) cenAndDist.first.x, (int) cenAndDist.first.y, strataLevel));
            }
        }
        return null;
    }

    private static Region getRegion(int xPos, int zPos) {
        return new Region();
    }

    private int getSecondaryDepth(Region region, int xPos, int zPos) {
        Vector2 xzPos = new Vector2(xPos, zPos);
        secondaryWarp.DomainWarp(xzPos);
        return region.getSecondaryDepth(secondaryNoise.GetNoise(xzPos.x, xzPos.y));
    }

    private State getBatolithState(int xPos, int zPos) {
        return Regions.getBatolithState(batolithTypeQF.GetNoise(xPos, zPos), batolithTypeAP.GetNoise(xPos, zPos));
    }

    private float getBatolithPercentage(Region region, int xPos, int zPos) {
        return region.getBatolithPercentage(batolithHeight.GetNoise(xPos, zPos));
    }

    public static void setSeed(long seed) {
        if (primaryNoise == null) {
            primaryNoise = new FastNoiseLite((int) seed + 39921723);
            primaryNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            primaryNoise.SetFractalType(FastNoiseLite.FractalType.None);
            primaryNoise.SetFrequency(primaryFreq);
            primaryNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        }
        if (orogenNoise == null) {
            orogenNoise = new FastNoiseLite((int) seed + 39921723);
            orogenNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            orogenNoise.SetFractalType(FastNoiseLite.FractalType.None);
            orogenNoise.SetFrequency(primaryFreq / 2);
            orogenNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
        }
        if (orogenWarp == null) {
            orogenWarp = new FastNoiseLite((int) seed + 9484585);
            orogenWarp.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            orogenWarp.SetFractalOctaves(2);
            orogenWarp.SetDomainWarpAmp(orogenWarpAmplitude);
            orogenWarp.SetFrequency(primaryFreq / 15);
            orogenWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
        }
        if (primaryWarp == null) {
            primaryWarp = new FastNoiseLite((int) seed + 98941);
            primaryWarp.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            primaryWarp.SetFractalOctaves(3);
            primaryWarp.SetDomainWarpAmp(40);
            primaryWarp.SetFrequency(primaryFreq / 15);
            primaryWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
        }
        if (secondaryNoise == null) {
            secondaryNoise = new FastNoiseLite((int) seed + 7874837);
            secondaryNoise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            secondaryNoise.SetFractalType(FastNoiseLite.FractalType.None);
            secondaryNoise.SetFrequency(secondaryFreq);
        }
        if (secondaryWarp == null) {
            secondaryWarp = new FastNoiseLite((int) seed + 399918);
            secondaryWarp.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            secondaryWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
            secondaryWarp.SetFractalOctaves(3);
            secondaryWarp.SetDomainWarpAmp(40);
            secondaryWarp.SetFrequency(secondaryFreq * 2f);
        }

        if (batolithTypeQF == null) {
            batolithTypeQF = new FastNoiseLite((int) seed + 211030);
            batolithTypeQF.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            batolithTypeQF.SetFractalOctaves(2);
            batolithTypeQF.SetFrequency(batolithFreq / 2);
        }
        if (batolithTypeAP == null) {
            batolithTypeAP = new FastNoiseLite((int) seed + 22991135);
            batolithTypeAP.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            batolithTypeAP.SetFractalOctaves(2);
            batolithTypeAP.SetFrequency(batolithFreq / 2);
        }
        if (batolithHeight == null) {
            batolithHeight = new FastNoiseLite((int) seed + 6303439);
            batolithHeight.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            batolithHeight.SetCellularReturnType(FastNoiseLite.CellularReturnType.Distance);
            batolithHeight.SetFractalType(FastNoiseLite.FractalType.FBm);
            batolithHeight.SetFractalLacunarity(2.2f);
            batolithHeight.SetFractalOctaves(4);
            batolithHeight.SetFrequency(batolithFreq);
        }
        if (stratumBorders == null) {
            stratumBorders = new FastNoiseLite(((int) seed));
            stratumBorders.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            stratumBorders.SetFractalType(FastNoiseLite.FractalType.None);
            stratumBorders.SetFrequency(stratumFreq);
        }
        if (stratumWarp == null) {
            stratumWarp = new FastNoiseLite((int) seed);
            stratumWarp.SetDomainWarpType(FastNoiseLite.DomainWarpType.OpenSimplex2);
            stratumWarp.SetFractalType(FastNoiseLite.FractalType.DomainWarpIndependent);
            stratumWarp.SetDomainWarpAmp(strataWarpAmplitude);
            stratumWarp.SetFrequency(stratumFreq * 2f);
            stratumWarp.SetFractalOctaves(3);
        }
        if (tertiaryNoise == null) {
            tertiaryNoise = new FastNoiseLite((int) seed + 19929411);
            tertiaryNoise.SetNoiseType(FastNoiseLite.NoiseType.Cellular);
            tertiaryNoise.SetFractalType(FastNoiseLite.FractalType.None);
            tertiaryNoise.SetCellularReturnType(FastNoiseLite.CellularReturnType.CellValue);
            tertiaryNoise.SetFrequency(tertiaryFreq);
        }
    }


}

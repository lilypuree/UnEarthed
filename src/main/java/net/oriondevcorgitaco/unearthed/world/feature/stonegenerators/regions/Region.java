package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.regions;

import com.ibm.icu.impl.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.oriondevcorgitaco.unearthed.block.StoneWrapper;
import net.oriondevcorgitaco.unearthed.util.noise.FastNoiseLite.Vector3;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.States;

import static net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.NoiseHandler.orogenWarpAmplitude;

public class Region {
    protected static final BlockState STONE = Blocks.STONE.getDefaultState();
    float sqrt3Inverse = 1.0f / (float) Math.sqrt(3);

    public StoneWrapper getPrimaryBlock(float level) {
        return StoneWrapper.allStoneWrappers.get((int) (((level * 0.5 + 0.5f)) * StoneWrapper.allStoneWrappers.size()));
    }

    public boolean isOrogen(Vector3 originalPos, Vector3 perturbed) {
        float xDiff = (perturbed.x - originalPos.x) / orogenWarpAmplitude;
        float yDiff = (perturbed.y - originalPos.y) / orogenWarpAmplitude;
        float zDiff = (perturbed.z - originalPos.z) / orogenWarpAmplitude;
        float scale = (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
        float similarity = Math.abs(xDiff + yDiff + zDiff) * sqrt3Inverse;

        if (scale < 0.07f || similarity > 0.95f) {
            return true;
        } else {
            return false;
        }
    }

    public int getSecondaryDepth(float noiseValue) {
        noiseValue = noiseValue * noiseValue * noiseValue;
        if (noiseValue > 0.6) {
            return ((int) ((noiseValue - 0.6f) / 0.4f * 120));
        } else if (noiseValue < -0.8) {
            return (int) ((-0.8f - noiseValue) / 0.2f * 80);
        } else {
            return 0;
        }
    }

    public State getSecondaryState() {
        return States.IRON;
    }

    public State getPrimaryState(float level) {
        return States.GLASS;
    }

    public State getOrogenState(float level) {
        return States.EMERALD;
    }

    public State getStrataState(float value, int strataLevel) {
        if (value < 0.9f) {
            return  null;
        }
        return States.DIAMOND;
    }

    public Pair<State, Float> getTertiary(State existing) {
        return Pair.of(States.NETHERITE, 0.001f);
    }

    public Pair<State, Float> getReplacement(State existing, int size) {
        switch (size) {
            default:
            case 1:
                return Pair.of(States.NETHERITE, 0.001f);
            case 2:
                return Pair.of(States.LAPIS, 0.01f);
            case 3:
                return Pair.of(States.REDSTONE, 0.001f);
        }

    }

    public float getBatolithPercentage(float noiseValue) {
        float cutOff = -0.25f;
        noiseValue = noiseValue * noiseValue * noiseValue;
        if (noiseValue < -0.7f) {
            noiseValue = (-0.7f - noiseValue) / 0.3f;
            return ((float) Math.sqrt(noiseValue));
        } else if (noiseValue > cutOff) {
            noiseValue = (noiseValue - cutOff) / 0.25f + 0.15f;
            return (float) Math.sqrt(noiseValue) * 1.3f;
        } else {
            return 0;
        }
    }

    public float getStratumPercentage() {
        return 0.2f;
    }

    //magnitude : 0 to 1
    //returns : stratum thickness
    public int getStratumDepth(int x, int z, int strataLevel) {
        return 3;
    }

    public int getStratumLevelSize() {
        return 40;
    }

    public boolean batholithIntrudes() {
        return true;
    }
}

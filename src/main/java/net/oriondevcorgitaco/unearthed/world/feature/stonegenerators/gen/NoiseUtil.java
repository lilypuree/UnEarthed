package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;

public class NoiseUtil {
    public static long getSeed(int x, int z, long worldSeed) {
        return (long) (x) * 341873128712L + (long) (z) * 132897987541L + worldSeed;
    }
}

package lilypuree.unearthed;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lilypuree.unearthed.core.UENames;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.apache.logging.log4j.Level;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BiomeInjector {

    public static void apply(BiomeSource source) {
        source.featuresPerStep = source.buildFeaturesPerStep(source.possibleBiomes().stream().toList(), true);
    }

    public static void apply(Iterable<Biome> biomes, RegistryAccess registryAccess) {
        Constants.LOG.log(Level.INFO, "Unearthed stone replacer injection started.");
        long start = System.currentTimeMillis();

        for (Biome biome : biomes) {
            if (biome.getBiomeCategory() != Biome.BiomeCategory.NETHER && biome.getBiomeCategory() != Biome.BiomeCategory.THEEND) {
                PlacedFeature STONE_REPLACER = registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY).get(UENames.STONE_REPLACER);
                addFeatureToBiome(biome, GenerationStep.Decoration.TOP_LAYER_MODIFICATION, STONE_REPLACER);
            }
        }
        long timeTook = System.currentTimeMillis() - start;
        Constants.LOG.log(Level.INFO, "Unearthed stone replacer injection took {} ms to complete.", timeTook);
    }

    private static void addFeatureToBiome(Biome biome, GenerationStep.Decoration step, PlacedFeature placedFeature) {
        List<List<Supplier<PlacedFeature>>> biomeFeatures = convertImmutableFeatures(biome);
        while (biomeFeatures.size() <= step.ordinal()) {
            biomeFeatures.add(Lists.newArrayList());
        }
        biomeFeatures.get(step.ordinal()).add(() -> placedFeature);
        biome.getGenerationSettings().features = biomeFeatures.stream().map(ImmutableList::copyOf).collect(ImmutableList.toImmutableList());
    }

    private static List<List<Supplier<PlacedFeature>>> convertImmutableFeatures(Biome biome) {
        List<List<Supplier<PlacedFeature>>> features = biome.getGenerationSettings().features();
        if (features instanceof ImmutableList) {
            return features.stream().map(Lists::newArrayList).collect(Collectors.toList());
        } else return features;
    }
}

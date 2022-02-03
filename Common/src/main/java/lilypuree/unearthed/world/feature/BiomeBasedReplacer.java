package lilypuree.unearthed.world.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lilypuree.unearthed.world.feature.gen.StoneType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;

public class BiomeBasedReplacer {

    public static final BiomeBasedReplacer NONE = new BiomeBasedReplacer(Collections.emptyList(), Collections.emptyList());
    public static final Codec<BiomeBasedReplacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registry.BIOME_REGISTRY).listOf().fieldOf("biomes").forGetter(x -> x.biomes),
            Strata.CODEC.listOf().fieldOf("stratum").forGetter(x -> x.stratas)
    ).apply(instance, BiomeBasedReplacer::new));

    public BiomeBasedReplacer(List<ResourceKey<Biome>> biomes, List<Strata> stratas) {
        this.biomes = biomes;
        this.stratas = stratas;
        this.totalThickness = stratas.stream().map(Strata::thickness).reduce(Integer::sum).orElse(0);
    }

    public record Strata(StoneType type, int thickness) {
        public static final Codec<Strata> CODEC = Codec.pair(StoneType.CODEC.fieldOf("stone_type").codec(), Codec.intRange(0, 4096).fieldOf("thickness").codec()).xmap(pair -> new Strata(pair.getFirst(), pair.getSecond()), strata -> Pair.of(strata.type, strata.thickness));
    }

    private final List<ResourceKey<Biome>> biomes;
    private final List<Strata> stratas;
    private final int totalThickness;

    public boolean apply(ResourceKey<Biome> biomeKey) {
        final Set<ResourceKey<Biome>> set = Set.copyOf(this.biomes);
        return set.contains(biomeKey);
    }

    public int getTotalThickness() {
        return totalThickness;
    }

    public StoneType compute(int depth) {
        int combined = 0;
        for (Strata strata : stratas) {
            combined += strata.thickness;
            if (depth < combined) {
                return strata.type;
            }
        }
        return null;
    }
}

package lilypuree.unearthed.block.schema;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class BlockSchema {
    private String name;
    private Multimap<BlockVariant, SchemaEntry> variantEntries;
    private Map<BlockVariant, SchemaEntry> variantBaseEntries;
    private SchemaEntry baseEntry;
    private Block defaultBlock = Blocks.STONE;

    private BlockSchema(String name, Multimap<BlockVariant, SchemaEntry> entries) {
        this.name = name;
        this.variantEntries = entries;
        this.variantBaseEntries = new HashMap<>();
        for (SchemaEntry entry : entries.values()) {
            if (entry.form.isBaseForm()) {
                variantBaseEntries.put(entry.variant, entry);
                if (entry.variant.isBaseVariant()) {
                    baseEntry = entry;
                }
            }
        }
    }

//    public BlockSchema(BlockVariant... variants) {
//        this(Lists.newArrayList(variants));
//    }


//    public Map<BlockVariant, List<SchemaEntry>> getEntries() {
//        return variantEntries;
//    }

    public void forEach(BiConsumer<BlockVariant, SchemaEntry> biConsumer) {
        variantEntries.forEach(biConsumer);
    }

    public Collection<BlockVariant> variants() {
        return variantEntries.keys();
    }

    public Collection<SchemaEntry> entries() {
        return variantEntries.values();
    }

    public SchemaEntry getEntry(BlockVariant variant, BlockForm form) {
        return variantEntries.get(variant).stream().filter(entry -> entry.form == form).findAny().orElse(null);
    }

    public SchemaEntry getBaseEntry() {
        return baseEntry;
    }

    public Block getBaseBlock(BlockVariant variant) {
        if (variantBaseEntries.containsKey(variant)) {
            return variantBaseEntries.get(variant).block;
        } else return defaultBlock;
    }

    public void setDefaultBlock(Block defaultBlock) {
        this.defaultBlock = defaultBlock;
    }

    public Block getBaseBlock() {
        if (baseEntry != null) {
            return baseEntry.getBlock();
        } else {
            return defaultBlock;
        }
    }

    public static Builder builder(String name, List<BlockVariant> variants) {
        return new Builder(name, variants);
    }

    public static Builder builder(String name, BlockVariant... variants) {
        return new Builder(name, List.of(variants));
    }

    public static class Builder {
        private String name;
        private List<BlockVariant> variants;
        private BlockBehaviour.Properties defaultProperty;
        private BlockBehaviour dummyBlockBehavior;
        private Multimap<BlockVariant, SchemaEntry> entries;

        private Builder(String name, List<BlockVariant> variants) {
            this.name = name;
            this.entries = MultimapBuilder.hashKeys(variants.size()).arrayListValues().build();
            this.variants = variants;
        }

        public Builder defaultProperty(BlockBehaviour.Properties defaultProperty) {
            this.defaultProperty = defaultProperty;
            this.dummyBlockBehavior = new Block(defaultProperty);
            for (BlockVariant variant : variants) {
                for (BlockForm form : variant.getForms()) {
                    entries.put(variant, new SchemaEntry(name, variant, form, defaultProperty));
                }
            }
            return this;
        }

        public Builder setProperty(BlockVariant variant, BlockBehaviour.Properties property) {
            entries.get(variant).forEach(entry -> entry.property = property);
            return this;
        }

        public Builder setProperty(BlockForm form, BlockBehaviour.Properties property) {
            entries.values().stream().filter(entry -> entry.form == form).forEach(entry -> entry.property = property);
            return this;
        }

        public Builder setProperty(BlockVariant variant, BlockForm form, BlockBehaviour.Properties property) {
            entries.get(variant).stream().filter(entry -> entry.form == form).forEach(entry -> entry.property = property);
            return this;
        }

        public Builder createProperties() {
            entries.values().forEach(entry -> {
                BlockForm form = entry.form;
                BlockVariant variant = entry.variant;
                if (form instanceof Forms.OreForm) {
                    entry.property = newProperty().strength(3.0F, 3.0F); //4.5F, 3.0F for deepslate
                } else if (variant == Variants.COBBLED || variant == Variants.MOSSY_COBBLED) {
                    entry.property = newProperty().strength(2.0f, 6.0f); //3.5F, 6.0F for deepslate
                } else if (form == Forms.REGOLITH) {
                    entry.property = newProperty().strength(0.6f).sound(SoundType.GRAVEL);
                } else if (form == Forms.GRASSY_REGOLITH) {
                    entry.property = BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK);
                } else if (form == Forms.BUTTON) {
                    entry.property = BlockBehaviour.Properties.of(Material.DECORATION).noCollission().strength(0.5f);
                } else if (form == Forms.PRESSURE_PLATE) {
                    entry.property = newProperty().noCollission().strength(0.5F);
                }
            });
            return this;
        }

        private BlockBehaviour.Properties newProperty() {
            return BlockBehaviour.Properties.copy(dummyBlockBehavior);
        }


        public BlockSchema build() {
            createProperties();
            return new BlockSchema(name, entries);
        }
    }
}

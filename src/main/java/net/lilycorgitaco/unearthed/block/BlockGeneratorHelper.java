package net.lilycorgitaco.unearthed.block;


import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.sound.BlockSoundGroup;
import net.lilycorgitaco.unearthed.block.schema.*;
import net.lilycorgitaco.unearthed.datagen.type.IOreType;
import net.lilycorgitaco.unearthed.datagen.type.VanillaOreTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockGeneratorHelper {
    public static List<Block> baseStoneBlockArray = new ArrayList<>();

    private String name;
    private BlockSchema schema;
    private PressurePlateBlock.ActivationRule pressurePlateSensitivity;
    private List<Entry> blocks;
    private Entry baseEntry;
    private Map<BlockSchema.Variant, Entry> variantBaseEntries;
    //    private Map<IOreType, BlockState> oreMap;
    private StoneTiers tier;
    private StoneClassification classification;
    private Block defaultBlock = Blocks.STONE;

    private BlockGeneratorHelper(String name, BlockSchema blockSchema, PressurePlateBlock.ActivationRule sensitivity, List<Entry> blocks, StoneTiers tier, StoneClassification classification) {
        this.name = name;
        this.schema = blockSchema;
        this.pressurePlateSensitivity = sensitivity;
        this.blocks = blocks;
        this.tier = tier;
        this.classification = classification;

        baseEntry = blocks.stream().filter(Entry::isBaseEntry).findAny().orElse(null);
        variantBaseEntries = blocks.stream().filter(entry -> entry.getForm().isBaseForm()).collect(Collectors.toMap(Entry::getVariant, entry -> entry));
        BlockGeneratorReference.ROCK_TYPES.add(this);
//        StoneWrapper.allStoneWrappers.add(new StoneWrapper(Unearthed.MOD_ID + ":" + name));
    }

    public String getName() {
        return name;
    }

    public List<Entry> getEntries() {
        return blocks;
    }

    public Block getBaseBlock() {
        if (baseEntry != null) {
            return baseEntry.getBlock();
        } else {
            return defaultBlock;
        }
    }

    public void setDefaultBlock(Block block) {
        this.defaultBlock = block;
    }

    public Entry getBaseEntry() {
        return baseEntry;
    }

    public Block getBaseBlock(BlockSchema.Variant variant) {
        if (variantBaseEntries.containsKey(variant)) {
            return variantBaseEntries.get(variant).getBlock();
        } else {
            return defaultBlock;
        }
    }

    public Entry getEntry(BlockSchema.Variant variant, BlockSchema.Form form) {
        return blocks.stream().filter(entry -> entry.form == form && entry.variant == variant).findAny().orElse(null);
    }

    public PressurePlateBlock.ActivationRule getPressurePlateSensitivity() {
        return pressurePlateSensitivity;
    }

    public BlockSchema getSchema() {
        return schema;
    }

    public StoneTiers getTier() {
        return tier;
    }

    public StoneClassification getClassification() {
        return classification;
    }

    public static class Entry {
        private String id;
        private BlockSchema.Variant variant;
        private BlockSchema.Form form;
        private AbstractBlock.Settings property;
        private Block block;

        public Entry(String name, BlockSchema.Variant variant, BlockSchema.Form form, AbstractBlock.Settings property) {
            this.variant = variant;
            this.form = form;
            this.id = variant.concatID(name, form.getName());
            this.property = property;
        }

        public String getId() {
            return id;
        }

        public Block createBlock(BlockGeneratorHelper blockGeneratorHelper) {
            this.block = form.getBlockCreator(blockGeneratorHelper, variant).apply(property);
            if (this.isBaseEntry()) {
                baseStoneBlockArray.add(block);
            }
            return block;
        }

        public BlockSchema.Form getForm() {
            return form;
        }

        public BlockSchema.Variant getVariant() {
            return variant;
        }

        public Block getBlock() {
            return block;
        }

        public boolean isBaseEntry() {
            return variant.isBaseVariant() && form.isBaseForm();
        }
    }

    public static class Builder {
        private String name;
        private BlockSchema schema;
        private StoneTiers tier = StoneTiers.PRIMARY;
        private StoneClassification classification = StoneClassification.IGNEOUS;
        private AbstractBlock.Settings defaultProperty;
        private PressurePlateBlock.ActivationRule pressurePlateSensitivity = PressurePlateBlock.ActivationRule.MOBS;
        private List<Entry> entries;

        public Builder(String name, BlockSchema schema) {
            this.name = name;
            this.schema = schema;
        }

        public Builder setTier(StoneTiers tier) {
            this.tier = tier;
            return this;
        }

        public Builder setClassification(StoneClassification classification) {
            this.classification = classification;
            return this;
        }

        public Builder defaultProperty(AbstractBlock.Settings property) {
            this.defaultProperty = property;
            this.entries = new ArrayList<>();
            for (BlockSchema.Variant variant : schema.getVariants()) {
                for (BlockSchema.Form form : variant.getForms()) {
                    entries.add(new Entry(name, variant, form, defaultProperty));
                }
            }
            return this;
        }

        public Builder setProperty(BlockSchema.Variant variant, AbstractBlock.Settings property) {
            entries.stream().filter(entry -> entry.variant == variant).forEach(entry -> {
                entry.property = property;
            });
            return this;
        }

        public Builder setProperty(BlockSchema.Form form, AbstractBlock.Settings property) {
            entries.stream().filter(entry -> entry.form == form).forEach(entry -> {
                entry.property = property;
            });
            return this;
        }

        public Builder setProperty(BlockSchema.Variant variant, BlockSchema.Form form, AbstractBlock.Settings property) {
            entries.stream().filter(entry -> entry.form == form && entry.variant == variant).forEach(entry -> {
                entry.property = property;
            });
            return this;
        }


        public Builder createOreProperties() {
            for (IOreType oreType : VanillaOreTypes.values()) {
                FabricBlockSettings newProp = FabricBlockSettings.copyOf(new Block(defaultProperty));
                newProp.requiresTool().breakByTool(FabricToolTags.PICKAXES, oreType.getHarvestLevel()).strength(3.0f);
                entries.stream().filter(entry -> {
                    return entry.form instanceof Forms.OreForm && ((Forms.OreForm) entry.form).getOreType() == oreType;
                }).forEach(entry -> entry.property = newProp);
            }
            return this;
        }

        public Builder createCobbleProperties() {
            AbstractBlock.Settings cobbleProp = AbstractBlock.Settings.copy(new Block(defaultProperty));
            cobbleProp.strength(2.0f, 6.0f);
            setProperty(Variants.COBBLED, cobbleProp);
            setProperty(Variants.MOSSY_COBBLED, cobbleProp);
            return this;
        }

        public Builder createRegolithProperties() {
            FabricBlockSettings regolithProp = FabricBlockSettings.copyOf(new Block(defaultProperty));
            FabricBlockSettings grassProp = FabricBlockSettings.copyOf(Blocks.GRASS_BLOCK);
            regolithProp.strength(0.6f).sounds(BlockSoundGroup.GRAVEL).breakByTool(FabricToolTags.PICKAXES).requiresTool();
            setProperty(Forms.REGOLITH, regolithProp);
            setProperty(Forms.GRASSY_REGOLITH, grassProp);
            return this;
        }

        public Builder createMiscProperties() {
            AbstractBlock.Settings buttonProp = AbstractBlock.Settings.of(Material.SUPPORTED).noCollision().strength(0.5F);
            AbstractBlock.Settings pressurePlateProp = AbstractBlock.Settings.copy(new Block(defaultProperty));
            pressurePlateProp.noCollision().strength(0.5f);
            setProperty(Forms.BUTTON, buttonProp);
            setProperty(Forms.PRESSURE_PLATE, pressurePlateProp);
            return this;
        }

        public BlockGeneratorHelper build() {
            createRegolithProperties();
            return new BlockGeneratorHelper(name, schema, pressurePlateSensitivity, entries, tier, classification);
        }
    }


}

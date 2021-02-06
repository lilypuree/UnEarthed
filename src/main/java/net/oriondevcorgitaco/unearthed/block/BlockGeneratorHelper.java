package net.oriondevcorgitaco.unearthed.block;


import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.oriondevcorgitaco.unearthed.block.schema.*;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockGeneratorHelper {
    public static List<Block> baseStoneBlockArray = new ArrayList<>();

    private String name;
    private BlockSchema schema;
    private PressurePlateBlock.Sensitivity pressurePlateSensitivity;
    private List<Entry> blocks;
    private Entry baseEntry;
    private Map<BlockSchema.Variant, Entry> variantBaseEntries;
    //    private Map<IOreType, BlockState> oreMap;
    private StoneTiers tier;
    private StoneClassification classification;
    private Block defaultBlock = Blocks.STONE;

    private BlockGeneratorHelper(String name, BlockSchema blockSchema, PressurePlateBlock.Sensitivity sensitivity, List<Entry> blocks, StoneTiers tier, StoneClassification classification) {
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

//    public BlockState getOreBlock(IOreType oreType) {
//        if (!oreMap.containsKey(oreType)) {
//            Optional<Entry> optional = blocks.stream().filter(entry -> {
//                return entry.getForm() instanceof Forms.OreForm && ((Forms.OreForm) entry.getForm()).getOreType() == oreType;
//            }).findAny();
//            if (optional.isPresent()) {
//                BlockState oreBlock = optional.get().getBlock().getDefaultState();
//                oreMap.put(oreType, oreBlock);
//                return oreBlock;
//            } else {
//                oreMap.put(oreType, null);
//                return null;
//            }
//        }
//        return oreMap.get(oreType);
//    }

    public PressurePlateBlock.Sensitivity getPressurePlateSensitivity() {
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
        private AbstractBlock.Properties property;
        private Block block;

        public Entry(String name, BlockSchema.Variant variant, BlockSchema.Form form, AbstractBlock.Properties property) {
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
        private AbstractBlock.Properties defaultProperty;
        private PressurePlateBlock.Sensitivity pressurePlateSensitivity = PressurePlateBlock.Sensitivity.MOBS;
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

        public Builder defaultProperty(AbstractBlock.Properties property) {
            this.defaultProperty = property;
            this.entries = new ArrayList<>();
            for (BlockSchema.Variant variant : schema.getVariants()) {
                for (BlockSchema.Form form : variant.getForms()) {
                    entries.add(new Entry(name, variant, form, defaultProperty));
                }
            }
            return this;
        }

        public Builder setProperty(BlockSchema.Variant variant, AbstractBlock.Properties property) {
            entries.stream().filter(entry -> entry.variant == variant).forEach(entry -> {
                entry.property = property;
            });
            return this;
        }

        public Builder setProperty(BlockSchema.Form form, AbstractBlock.Properties property) {
            entries.stream().filter(entry -> entry.form == form).forEach(entry -> {
                entry.property = property;
            });
            return this;
        }

        public Builder setProperty(BlockSchema.Variant variant, BlockSchema.Form form, AbstractBlock.Properties property) {
            entries.stream().filter(entry -> entry.form == form && entry.variant == variant).forEach(entry -> {
                entry.property = property;
            });
            return this;
        }


        public Builder createOreProperties() {
            for (IOreType oreType : VanillaOreTypes.values()) {
                AbstractBlock.Properties newProp = AbstractBlock.Properties.from(new Block(defaultProperty));
                newProp.setRequiresTool().harvestLevel(oreType.getHarvestLevel()).harvestTool(ToolType.PICKAXE).hardnessAndResistance(3.0f);
                entries.stream().filter(entry -> {
                    return entry.form instanceof Forms.OreForm && ((Forms.OreForm) entry.form).getOreType() == oreType;
                }).forEach(entry -> entry.property = newProp);
            }
            return this;
        }

        public Builder createCobbleProperties() {
            AbstractBlock.Properties cobbleProp = AbstractBlock.Properties.from(new Block(defaultProperty));
            cobbleProp.hardnessAndResistance(2.0f, 6.0f);
            setProperty(Variants.COBBLED, cobbleProp);
            setProperty(Variants.MOSSY_COBBLED, cobbleProp);
            return this;
        }

        public Builder createRegolithProperties() {
            AbstractBlock.Properties regolithProp = AbstractBlock.Properties.from(new Block(defaultProperty));
            AbstractBlock.Properties grassProp = AbstractBlock.Properties.from(Blocks.GRASS_BLOCK);
            regolithProp.hardnessAndResistance(0.6f).sound(SoundType.GROUND).harvestTool(ToolType.SHOVEL).setRequiresTool();
            setProperty(Forms.REGOLITH, regolithProp);
            setProperty(Forms.GRASSY_REGOLITH, grassProp);
            return this;
        }

        public Builder createMiscProperties() {
            AbstractBlock.Properties buttonProp = AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F);
            AbstractBlock.Properties pressurePlateProp = AbstractBlock.Properties.from(new Block(defaultProperty));
            pressurePlateProp.doesNotBlockMovement().hardnessAndResistance(0.5f);
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

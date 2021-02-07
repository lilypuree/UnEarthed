package net.lilycorgitaco.unearthed.block.schema;

import com.google.common.collect.Lists;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.lilycorgitaco.unearthed.block.BlockGeneratorHelper;

import java.util.List;
import java.util.function.Function;

public class BlockSchema {

    private List<Variant> variants;

    public BlockSchema(List<Variant> variants) {
        this.variants = variants;
    }

    public BlockSchema(Variant... variants) {
        this.variants = Lists.newArrayList(variants);
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public static class Variant {
        private String name;
        private List<Form> forms;
        private boolean derivative = false;

        public Variant(String name, List<Form> forms) {
            this.name = name;
            this.forms = forms;
        }

        public Variant(String name, Form... forms) {
            this.name = name;
            this.forms = Lists.newArrayList(forms);
        }

        public String getName() {
            return name;
        }

        public List<Form> getForms() {
            return forms;
        }

        public String concatID(String baseName, String suffix) {
            if (!name.equals("")) {
                baseName = name + "_" + baseName;
            }
            if (!suffix.equals("")) {
                baseName = baseName + "_" + suffix;
            }
            return baseName;
        }

        public Variant setDerivative(){
            this.derivative = true;
            return this;
        }

        public boolean isDerivative() {
            return derivative;
        }

        public boolean isBaseVariant() {
            return name.equals("");
        }
    }

    public static abstract class Form {
        private String name;
        private boolean sideTopBlock = false;

        public Form(String name) {
            this.name = name;
        }

        public boolean isSideTopBlock() {
            return sideTopBlock;
        }

        public Form sideTopBlock() {
            this.sideTopBlock = true;
            return this;
        }

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public abstract Function<AbstractBlock.Settings, Block> getBlockCreator(BlockGeneratorHelper schema, Variant variant);


        public boolean isBaseForm() {
            return name.equals("");
        }
    }


}

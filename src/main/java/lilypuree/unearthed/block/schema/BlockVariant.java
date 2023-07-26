package lilypuree.unearthed.block.schema;

import com.google.common.collect.Lists;

import java.util.List;

public class BlockVariant {
    private String name;
    private List<BlockForm> forms;
    private boolean derivative = false;
    private boolean isSideTop = false;

    public BlockVariant(String name, List<BlockForm> forms) {
        this.name = name;
        this.forms = forms;
    }

    public BlockVariant(String name, BlockForm... forms) {
        this.name = name;
        this.forms = Lists.newArrayList(forms);
    }

    public String getName() {
        return name;
    }

    public List<BlockForm> getForms() {
        return forms;
    }

    public BlockVariant setDerivative() {
        this.derivative = true;
        return this;
    }

    public BlockVariant sideTop() {
        isSideTop = true;
        return this;
    }

    public boolean isSideTop() {
        return isSideTop;
    }

    public boolean isDerivative() {
        return derivative;
    }

    public boolean isBaseVariant() {
        return name.equals("");
    }

    public String getBlockId(String baseName, String suffix) {
        if (!name.equals("")) {
            baseName = name + "_" + baseName;
        }
        if (!suffix.equals("")) {
            baseName = baseName + "_" + suffix;
        }
        return baseName;
    }
}

package lilypuree.unearthed.block.schema;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SchemaEntry {
    protected String id;
    protected BlockVariant variant;
    protected BlockForm form;
    protected BlockBehaviour.Properties property;
    protected Block block;

    public SchemaEntry(String name, BlockVariant variant, BlockForm form, BlockBehaviour.Properties property) {
        this.id = variant.getBlockId(name, form.getName());
        this.property = property;
        this.variant = variant;
        this.form = form;
    }

    public Block createBlock(BlockSchema schema) {
        this.block = form.getBlockCreator(schema, variant).apply(property);
        return block;
    }

    public String getId() {
        return id;
    }

    public BlockForm getForm() {
        return form;
    }

    public BlockVariant getVariant() {
        return variant;
    }

    public Block getBlock() {
        return block;
    }

    public boolean isBaseEntry() {
        return variant.isBaseVariant() && form.isBaseForm();
    }
}

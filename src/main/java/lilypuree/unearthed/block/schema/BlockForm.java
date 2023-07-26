package lilypuree.unearthed.block.schema;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public abstract class BlockForm {
    private String name;

    public BlockForm(String name){
        this.name = name;
    }

    public abstract Function<BlockBehaviour.Properties, Block> getBlockCreator(BlockSchema schema, BlockVariant variant);

    public String getName() {
        return name;
    }

    public boolean isBaseForm() {
        return name.equals("");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

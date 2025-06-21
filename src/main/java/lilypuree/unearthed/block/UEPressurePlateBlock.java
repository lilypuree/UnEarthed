package lilypuree.unearthed.block;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class UEPressurePlateBlock extends PressurePlateBlock {
    public UEPressurePlateBlock(Properties properties){
        super(Sensitivity.MOBS ,properties, BlockSetType.STONE);
    }
}

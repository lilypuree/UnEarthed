package lilypuree.unearthed.mixin.server;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StairBlock.class)
public interface StairBlockInvoker {

    @Invoker("<init>")
    static StairBlock onConstruction(BlockState base, BlockBehaviour.Properties properties){
        throw new AssertionError();
    }

}

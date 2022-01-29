package lilypuree.unearthed.mixin.server;

import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PressurePlateBlock.class)
public interface PressurePlateBlockInvoker {

    @Invoker("<init>")
    static PressurePlateBlock invokeInit(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties){
        throw new AssertionError();
    }
}

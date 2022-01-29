package lilypuree.unearthed.mixin.server;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.StoneButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StoneButtonBlock.class)
public interface ButtonBlockInvoker {

    @Invoker("<init>")
    static StoneButtonBlock invokeInit(BlockBehaviour.Properties properties) {
        throw new AssertionError();
    }
}

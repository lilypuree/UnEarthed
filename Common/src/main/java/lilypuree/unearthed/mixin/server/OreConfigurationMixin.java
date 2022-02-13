package lilypuree.unearthed.mixin.server;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(OreConfiguration.class)
public class OreConfigurationMixin {

    @ModifyVariable(method = "<init>(Ljava/util/List;IF)V", at = @At(value = "HEAD"), argsOnly = true)
    private static List<OreConfiguration.TargetBlockState> unearthed_OreConfigInit(List<OreConfiguration.TargetBlockState> targetStates) {
        ImmutableList.Builder<OreConfiguration.TargetBlockState> builder = ImmutableList.builder();
        for (OreConfiguration.TargetBlockState targetBlockState : targetStates) {
            if (targetBlockState.target instanceof BlockMatchTest test) {
                if (test.test(Blocks.DIORITE.defaultBlockState(), null)) continue;
                if (test.test(Blocks.GRANITE.defaultBlockState(), null)) continue;
                if (test.test(Blocks.ANDESITE.defaultBlockState(), null)) continue;
            }
            builder.add(targetBlockState);
        }
        return builder.build();
    }
}

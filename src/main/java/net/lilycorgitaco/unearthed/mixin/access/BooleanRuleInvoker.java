package net.lilycorgitaco.unearthed.mixin.access;

import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRules.BooleanRule.class)
public interface BooleanRuleInvoker {

    @Invoker
    public static GameRules.Type<GameRules.BooleanRule> callCreate(boolean initialValue){
        throw new AssertionError();
    }
}

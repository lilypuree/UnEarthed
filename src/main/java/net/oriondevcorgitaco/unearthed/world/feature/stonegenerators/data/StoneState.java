package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;
import net.oriondevcorgitaco.unearthed.block.StoneWrapper;

public class StoneState implements State {

    private final Type type;
    private final StoneWrapper wrapper;

    public StoneState(Type type, StoneWrapper block) {
        this.type = type;
        this.wrapper = block;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public BlockState getDefaultState() {
        return wrapper.getBlock();
    }

    public StoneWrapper getWrapper() {
        return wrapper;
    }

    @Override
    public int compareTo(State o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StoneState) {
            return ((StoneState) obj).getType() == getType() && wrapper == ((StoneState) obj).wrapper;
        }
        return false;
    }
}

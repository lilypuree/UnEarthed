package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;

public class BaseState {
    private final Type type;
    private final BlockState block;

    public BaseState(Type type, BlockState block) {
        this.type = type;
        this.block = block;
    }

    public BlockState getBlock(){
        return block;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof BaseState) {
            return type == ((State) obj).getType() && block == ((BaseState) obj).block;
        }
        return false;
    }


    @Override
    public int compareTo(State o) {
        return 0;
    }
}

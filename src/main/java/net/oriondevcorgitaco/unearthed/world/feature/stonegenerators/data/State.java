package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data;

import net.minecraft.block.BlockState;

public class State {
    private Type type;
    private Cell cell;
    public State(Type type, Cell cell){
        this.type = type;
        this.cell = cell;
    }
    public Type getType(){
        return type;
    }

    public Cell getCell(){
        return cell;
    }
}

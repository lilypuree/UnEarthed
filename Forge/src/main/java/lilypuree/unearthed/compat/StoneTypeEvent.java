package lilypuree.unearthed.compat;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired for every stone type being loaded from a json
 * cancelling the event will replace the given stone type with NO_REPLACE
 */
@Cancelable
public class StoneTypeEvent extends Event {

    private final IStoneType stoneType;

    public StoneTypeEvent(IStoneType stoneType) {
        this.stoneType = stoneType;
    }

    public IStoneType getStoneType() {
        return stoneType;
    }
}

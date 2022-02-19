package lilypuree.unearthed.compat;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Callback for every stone type being loaded from a json
 * returning false will replace the given stone type with NO_REPLACE
 */
public interface StoneTypeCallback {

    Event<StoneTypeCallback> EVENT = EventFactory.createArrayBacked(StoneTypeCallback.class,
            (listeners) -> (stonetype) -> {
                for (StoneTypeCallback listener : listeners) {
                    if (!listener.apply(stonetype)) return false;
                }
                return true;
            });

    boolean apply(IStoneType type);
}

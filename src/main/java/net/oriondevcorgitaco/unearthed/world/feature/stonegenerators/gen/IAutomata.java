package net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.gen;

import com.google.common.base.Functions;
import net.oriondevcorgitaco.unearthed.world.feature.stonegenerators.data.State;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IAutomata {
    int randomInt(int bound);

    default State selectStates(State state1, State state2) {
        if (state1 == state2) return state1;
        return randomInt(2) == 0 ? state1 : state2;
    }

    default State selectStates(State state1, State state2, State state3, State state4) {
        int i = randomInt(4);
        if (i == 0) {
            return state1;
        } else if (i == 1) {
            return state2;
        } else {
            return i == 2 ? state3 : state4;
        }
    }

    default State determineState(State up, State down, State x1, State x2, State z1, State z2, Supplier<State> stateGetter) {
//        boolean ySame = up.equals(down);
//        boolean xSame = x1.equals(x2);
//        boolean zSame = z1.equals(z2);
//        if (xSame && ySame && zSame) {
//            int i = randomInt(3);
//            if (i == 0) return up;
//            else return (i == 1) ? x1 : z1;
//        } else if (!xSame && zSame && ySame) {
//            return selectStates(up, z1);
//        } else if (xSame && !zSame && ySame) {
//            return selectStates(up, x1);
//        } else if (xSame && zSame && !ySame) {
//            return selectStates(x1, z1);
//        } else {
//            randomInt(3);
//            return stateGetter.get();
//        }
        randomInt(2);
        Optional<State> state = Stream.of(up, down, x1, x2, z1, z2)
//                .sorted()
                .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey);
        return state.orElseGet(stateGetter::get);
    }
}

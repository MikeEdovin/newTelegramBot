package States;

import Commands.Command;
import org.springframework.beans.factory.annotation.Autowired;

public class StateFactoryImpl implements StateFactory{
    @Autowired
    State mainState;
    @Autowired
    State setCityState;
    @Autowired
    State newInputState;
    @Override
    public State getState(StateEnum stateEnum) {

        return switch (stateEnum) {
            case MAIN -> mainState;
            case SETTINGS -> setCityState;
            case NEWINPUT -> newInputState;
            default -> mainState;
        };
    }
}

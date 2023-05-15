package States;

import Commands.Command;
import org.springframework.beans.factory.annotation.Autowired;

public class StateFactoryImpl implements StateFactory{
    @Autowired
    State mainState;
    @Autowired
    State setCityState;
    @Override
    public State getState(StateEnum stateEnum) {

        return switch (stateEnum) {
            case MAIN -> mainState;
            case SETTINGS -> setCityState;
            default -> mainState;
        };
    }
}

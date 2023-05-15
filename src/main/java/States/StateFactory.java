package States;

import Commands.Command;

public interface StateFactory {
    public State getState(StateEnum stateEnum);
}

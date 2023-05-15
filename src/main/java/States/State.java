package States;

import Commands.Command;
import Entities.User;

public interface State {

    String getTitle();
    void gotInput(User user, Command command);
    void sendStateMessage(User user, StateEnum state);
    void execute();


}

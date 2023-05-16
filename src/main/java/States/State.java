package States;

import Commands.Command;
import Commands.ParsedCommand;
import Entities.User;

public interface State {

    String getTitle();
    void gotInput(User user, ParsedCommand parsedCommand);
    void sendStateMessage(User user, StateEnum state);
    void execute();


}

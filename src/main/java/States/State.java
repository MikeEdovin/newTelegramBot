package States;

import Commands.ParsedCommand;
import Entities.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface State {

    void gotInput(User user, ParsedCommand parsedCommand, Update update);
    void sendStateMessage(User user, StateEnum state);

    void gotCallBack(User user, Update update);



}

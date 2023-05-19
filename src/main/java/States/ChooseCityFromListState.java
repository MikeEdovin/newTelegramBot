package States;

import Commands.ParsedCommand;
import Entities.User;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ChooseCityFromListState implements State{
    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {

    }

    @Override
    public void sendStateMessage(User user, StateEnum state) {

    }

    @Override
    public void gotCallBack(User user, Update update) {

    }
}

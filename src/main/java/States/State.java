package States;

import Commands.ParsedCommand;
import Entities.User;
import MessageCreator.StateMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface State {

    void gotInput(User user, ParsedCommand parsedCommand, Update update) throws TelegramApiException;
    void gotCallBack(User user, Update update) throws TelegramApiException;
    default SendMessage getStateMessage(User user){
        return new StateMessage.MessageBuilder(user).
                setText().setKeyBoard().build().getSendMessage();
    }




}

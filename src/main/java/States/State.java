package States;

import BotServices.Observable;
import Commands.ParsedCommand;
import Entities.User;
import MessageCreator.StateMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface State extends Observable {

    void gotInput(User user, ParsedCommand parsedCommand, Update update);

    void gotCallBack(User user, Update update);
    default SendMessage getStateMessage(User user){
        return new StateMessage.MessageBuilder(user.getUserId()).
                setText(user.getCurrentState())
                .setKeyBoard(user.getCurrentState()).build().getSendMessage();
    };



}

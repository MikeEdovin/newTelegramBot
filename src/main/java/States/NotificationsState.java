package States;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.User;
import MessageCreator.StateMessageBuilder;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;


public class NotificationsState implements State{

    @Autowired
    Bot bot;
    @Autowired
    UserService userService;
    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();

        switch (command) {
            case SET_NOTIFICATIONS_CITY -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.SETTINGS);
                user = userService.update(user);
                sendStateMessage(user, user.getCurrentState());
            }
            case BACK -> {
                user.setCurrentState(StateEnum.MAIN);
                user.setNotif(false);
                userService.update(user);
                sendStateMessage(user, user.getCurrentState());
            }
        }
    }

    @Override
    public void sendStateMessage(User user, StateEnum state) {
        bot.sendQueue.add(new StateMessageBuilder.MessageBuilder(user.getUserId()).
                setText(state).
                setKeyBoard(state).build().getSendMessage());
    }

    @Override
    public void gotCallBack(User user, Update update) {

    }
}

package States;

import BotPackage.Bot;
import Commands.Command;
import Entities.User;
import MessageCreator.StateMessageBuilder;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class SetCityState implements State{
    @Autowired
    Bot bot;
    @Autowired
    UserService userService;

    private final String TITLE="setCity";

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void gotInput(User user, Command command) {
        switch (command){
            case SET_CITY -> {

            }
            case BACK -> {
                user.setCurrentState(user.getPreviousState());
                user=userService.update(user);
                sendStateMessage(user,user.getCurrentState());
            }

        }

    }

    @Override
    public void sendStateMessage(User user,StateEnum state) {
        bot.sendQueue.add(new StateMessageBuilder.MessageBuilder(user.getUserId()).
                setKeyBoard(state).build().getSendMessage());
        System.out.println("set state");

    }



    @Override
    public void execute() {

    }
}

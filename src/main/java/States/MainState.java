package States;

import BotPackage.Bot;
import Commands.Command;
import Entities.User;
import MessageCreator.StateMessageBuilder;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class MainState implements State {
    @Autowired
    Bot bot;
    @Autowired
    UserService userService;

    private final String TITLE="Main menu";

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void gotInput(User user, Command command) {

        switch (command){
            case START->sendStateMessage(user,user.getCurrentState());
            case HELP -> {
                bot.sendQueue.add(new MessageCreator.SystemMessage.MessageBuilder(user.getUserId()).
                        setText(command).build().getSendMessage());
                System.out.println("help in main state");
            }
            case SETTINGS -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.SETTINGS);
                user=userService.update(user);
                sendStateMessage(user,user.getCurrentState());
                System.out.println("settings in main state");





            }


        }


    }

    @Override
    public void sendStateMessage(User user,StateEnum state) {
        bot.sendQueue.add(new StateMessageBuilder.MessageBuilder(user.getUserId()).
                setKeyBoard(state).build().getSendMessage());

    }

    @Override
    public void execute() {

    }
}

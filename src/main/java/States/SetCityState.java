package States;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.StateMessageBuilder;
import MessageCreator.WeatherMessage;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class SetCityState implements State{
    @Autowired
    Bot bot;
    @Autowired
    UserService userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;

    private final String TITLE="setCity";

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand) {
        Command command=parsedCommand.getCommand();
        switch (command){
            case SET_CITY -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.NEWINPUT);
                user=userService.update(user);
                sendStateMessage(user,user.getCurrentState());
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
                setText(state)
                .setKeyBoard(state).build().getSendMessage());


    }



    @Override
    public void execute() {

    }
}

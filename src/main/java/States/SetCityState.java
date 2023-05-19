package States;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.StateMessageBuilder;
import Service.CityService;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SetCityState implements State{
    @Autowired
    Bot bot;
    @Autowired
    UserService userService;
    @Autowired
    CityService cityService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;



    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command=parsedCommand.getCommand();
        switch (command){
            case SET_CITY -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.NEWINPUT);
                user=userService.update(user);
                sendStateMessage(user,user.getCurrentState());
            }
            case SEND_LOCATION -> {
                double latitude=update.getMessage().getLocation().getLatitude();
                double longitude=update.getMessage().getLocation().getLongitude();
                CityData cityData=geoWeatherProvider.getCityData(latitude,longitude);
                user.setCurrentCity(cityData);
                System.out.println("current city "+user.getCurrentCity().getName());
                cityService.save(user.getCurrentCity());
                user.setCurrentState(StateEnum.MAIN);
                userService.update(user);
                sendStateMessage(user, user.getCurrentState());
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
    public void gotCallBack(User user, Update update) {


    }


}

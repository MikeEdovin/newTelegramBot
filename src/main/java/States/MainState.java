package States;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import Entities.WeatherData;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.StateMessageBuilder;
import MessageCreator.SystemMessage;
import MessageCreator.WeatherMessage;
import Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MainState implements State {
    @Autowired
    Bot bot;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;




    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command=parsedCommand.getCommand();

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
            case NONE -> {
                bot.sendQueue.add(new SystemMessage.MessageBuilder(user.getUserId())
                        .setText(command).build().getSendMessage());

            }
            case CURRENT_WEATHER,FOR_48_HOURS,FOR_7_DAYS -> {
                int nrOfDays;
                if (command == Command.CURRENT_WEATHER) {
                    nrOfDays = 1;
                } else if (command == Command.FOR_48_HOURS) {
                    nrOfDays = 2;
                } else {
                    nrOfDays = 7;
                }
                CityData currentCity=user.getCurrentCity();
                if(currentCity!=null) {
                    WeatherData weatherData = geoWeatherProvider
                            .getWeatherData(currentCity.getLat(), currentCity.getLon());
                    System.out.println("weather"+weatherData.getCurrent().getDate());
                    bot.sendQueue.add(new WeatherMessage.MessageBuilder(user.getUserId()).setForecastText(weatherData,currentCity,nrOfDays).build().getSendMessage());
                }
            }
            case NOTIFICATION -> {

            }


        }


    }

    @Override
    public void sendStateMessage(User user,StateEnum state) {
        bot.sendQueue.add(new StateMessageBuilder.MessageBuilder(user.getUserId()).
                setText(state).
                setKeyBoard(state).build().getSendMessage());

    }

    @Override
    public void gotCallBack(User user, Update update) {

    }


}

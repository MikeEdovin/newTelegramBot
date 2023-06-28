package States;

import BotPackage.Bot;
import BotServices.MessageSender;
import BotServices.Observer;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import Entities.WeatherData;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainState implements State {
    //add Logger
    @Autowired
    Bot bot;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    MessageSender messageSender;
    private List<Observer> observers=new ArrayList<>();

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command=parsedCommand.getCommand();

        switch (command){
            case START->messageSender.sendMessageAsync(getStateMessage(user));
            case HELP,NONE,SET_TIME -> messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user).
                    setText(command).build().getSendMessage());
            case SETTINGS -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.SETTINGS);
                userService.updateAsync(user);
                messageSender.sendMessageAsync(getStateMessage(user));
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
                    CompletableFuture<WeatherData> weatherData = geoWeatherProvider
                            .getWeatherDataAsync(currentCity.getLat(), currentCity.getLon());
                        try {
                            if (currentCity.getTimezone() == null) {
                                currentCity.setTimezone(weatherData.get().getTimezone());
                                userService.updateAsync(user);
                            }
                            messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                                    .setForecastText(weatherData.get(), currentCity, nrOfDays).build().getSendMessage());

                        }catch(InterruptedException|ExecutionException e){
                            e.printStackTrace();
                            //add systemMessage -service is temporary unavailable
                    }
                }
                else{
                    messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                            .noCurrentCity().build().getSendMessage());
                    user.setPreviousState(user.getCurrentState());
                    user.setCurrentState(StateEnum.SETTINGS);
                    userService.updateAsync(user);
                    messageSender.sendMessageAsync(getStateMessage(user));

                }
            }
            case NOTIFICATION -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.NOTIF);
                user.setNotif(true);
                userService.updateAsync(user);
                messageSender.sendMessageAsync(getStateMessage(user));
            }
        }
    }

    @Override
    public void gotCallBack(User user, Update update) {
    }
}

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
import MessageCreator.StateMessage;
import MessageCreator.SystemMessage;
import Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class MainState implements State {
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
            case START->notifyObservers(getStateMessage(user));
            case HELP,NONE,SET_TIME -> notifyObservers(new SystemMessage.MessageBuilder(user).
                    setText(command).build().getSendMessage());
            case SETTINGS -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.SETTINGS);
                userService.update(user);
                notifyObservers(getStateMessage(user));
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
                    if(currentCity.getTimezone()==null){
                        currentCity.setTimezone(weatherData.getTimezone());
                        userService.update(user);
                    }
                notifyObservers(new SystemMessage.MessageBuilder(user)
                        .setForecastText(weatherData,currentCity,nrOfDays).build().getSendMessage());

                }
                else{
                    notifyObservers(new SystemMessage.MessageBuilder(user)
                            .noCurrentCity().build().getSendMessage());
                    user.setPreviousState(user.getCurrentState());
                    user.setCurrentState(StateEnum.SETTINGS);
                    userService.update(user);
                    notifyObservers(getStateMessage(user));

                }
            }
            case NOTIFICATION -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.NOTIF);
                user.setNotif(true);
                userService.update(user);
                notifyObservers(getStateMessage(user));
            }
        }
    }

    @Override
    public void gotCallBack(User user, Update update) {
    }


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);

    }

    @Override
    public void notifyObservers(Object object) {
        for(Observer observer:observers){
            observer.gotUpdate(object);
        }

    }
}

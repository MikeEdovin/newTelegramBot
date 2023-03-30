package Handlers;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.*;
import GeoWeatherPackage.IGeoWeatherProvider;
import MessageCreator.WeatherMessage;
import Service.ICityService;
import Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class WeatherHandler implements IHandler {

    @Autowired
    IGeoWeatherProvider geoWeatherProvider;
    @Autowired
    IUserService userService;
    @Autowired
    ICityService cityService;


    @Override
    public SendMessage operate(ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();
        long userID = update.getMessage().getFrom().getId();
        User user = userService.saveIfNotExist(new User(userID));
        CityData currentCity = user.getCurrentCity();
        WeatherData weatherData;
        int nrOfDays;

        switch (command) {
            case WEATHER_NOW, FOR_48_HOURS, FOR_7_DAYS -> {
                if (command == Command.WEATHER_NOW) {
                    nrOfDays = 1;
                } else if (command == Command.FOR_48_HOURS) {
                    nrOfDays = 2;
                } else {
                    nrOfDays = 7;
                }
                if (currentCity != null) {
                    weatherData=geoWeatherProvider
                            .getWeatherData(currentCity.getLat(), currentCity.getLon());
                    return new WeatherMessage.MessageBuilder(userID)
                            .setForecastText(weatherData,currentCity,nrOfDays)
                            .build().getSendMessage();
                }
            }


        }
        return null;//delete
    }
}

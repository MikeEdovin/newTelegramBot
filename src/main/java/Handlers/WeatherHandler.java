package Handlers;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.*;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.WeatherMessage;
import Service.CityService;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class WeatherHandler implements IHandler {

    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    UserService userService;
    @Autowired
    CityService cityService;
    @Autowired
    Bot bot;


    @Override
    public void operate(ParsedCommand parsedCommand, long userId) {
        Command command = parsedCommand.getCommand();
        User user = userService.saveIfNotExist(new User(userId));
        CityData currentCity = user.getCurrentCity();
        WeatherData weatherData;
        int nrOfDays;

        switch (command) {
            case CURRENT_WEATHER, FOR_48_HOURS, FOR_7_DAYS -> {
                if (command == Command.CURRENT_WEATHER) {
                    nrOfDays = 1;
                } else if (command == Command.FOR_48_HOURS) {
                    nrOfDays = 2;
                } else {
                    nrOfDays = 7;
                }
                if (currentCity != null) {
                    weatherData=geoWeatherProvider
                            .getWeatherData(currentCity.getLat(), currentCity.getLon());
                    bot.sendQueue.add(new WeatherMessage.MessageBuilder(userId)
                            .setForecastText(weatherData,currentCity,nrOfDays)
                            .build().getSendMessage());
                }
            }


        }

    }
}

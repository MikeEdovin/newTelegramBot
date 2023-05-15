package Handlers;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.*;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.WeatherMessage;
import Service.ICityService;
import Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class WeatherHandler implements IHandler {

    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    IUserService userService;
    @Autowired
    ICityService cityService;
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
                    bot.sendQueue.add(new WeatherMessage.MessageBuilder(userId)
                            .setForecastText(weatherData,currentCity,nrOfDays)
                            .build().getSendMessage());
                }
            }


        }

    }
}

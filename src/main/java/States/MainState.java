package States;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import Entities.WeatherData;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainState implements State {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    Bot bot;
    @Value("${bot.admin}") long botAdmin;
    final static Logger logger= LoggerFactory.getLogger(MainState.class);

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) throws TelegramApiException {
        Command command=parsedCommand.getCommand();
        logger.info("Got message from user with id "+user.getUserId()+". Command: "+command);
        switch (command){
            case START->bot.executeAsync(getStateMessage(user));
            case HELP,NONE,SET_TIME -> bot.executeAsync(new SystemMessage.MessageBuilder(user).
                    setText(command).build().getSendMessage());
            case SETTINGS -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.SETTINGS);
                userService.updateAsync(user);
                bot.executeAsync(getStateMessage(user));
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
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .setForecastText(weatherData.get(), currentCity, nrOfDays).build().getSendMessage());

                    }catch(InterruptedException|ExecutionException e){
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .serviceNotAvailable().build().getSendMessage());
                        bot.executeAsync(new SystemMessage.MessageBuilder(new User(botAdmin))
                                .sendErrorMessage(e.getMessage()).build().getSendMessage());
                    }
                }
                else{
                    bot.executeAsync(new SystemMessage.MessageBuilder(user)
                            .noCurrentCity().build().getSendMessage());
                    user.setPreviousState(user.getCurrentState());
                    user.setCurrentState(StateEnum.SETTINGS);
                    userService.updateAsync(user);
                    bot.executeAsync(getStateMessage(user));
                }
            }
            case NOTIFICATION -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.NOTIF);
                user.setNotif(true);
                userService.updateAsync(user);
                bot.executeAsync(getStateMessage(user));
            }
        }
    }

    @Override
    public void gotCallBack(User user, Update update) {
    }
}

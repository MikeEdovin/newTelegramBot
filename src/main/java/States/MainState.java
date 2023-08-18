package States;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.ReactiveGeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.ReactiveUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MainState implements State {
    @Autowired
    //UserServiceImpl userService;
    ReactiveUserService userService;
    @Autowired
    //GeoWeatherProvider geoWeatherProvider;
    ReactiveGeoWeatherProvider geoWeatherProvider;
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
                //userService.updateAsync(user);
                userService.update(user).subscribe(u->{
                    try {
                        bot.executeAsync(getStateMessage(u));
                    } catch (TelegramApiException e) {
                        logger.warn(e.getMessage());
                    }
                });
                //bot.executeAsync(getStateMessage(user));
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
                    /*
                    CompletableFuture<WeatherData> weatherData = geoWeatherProvider
                            .getWeatherDataAsync(currentCity.getLat(), currentCity.getLon());

                     */
                    geoWeatherProvider.getWeatherData(currentCity.getLat(), currentCity.getLon()).subscribe(w->{
                        if (currentCity.getTimezone() == null) {
                            currentCity.setTimezone(w.getTimezone());
                            //userService.updateAsync(user);
                            userService.update(user).subscribe(u->{
                                try {
                                    bot.executeAsync(new SystemMessage.MessageBuilder(u)
                                            .setForecastText(w, currentCity, nrOfDays).build().getSendMessage());
                                } catch (TelegramApiException e) {
                                    logger.warn(e.getMessage());
                                }
                            });

                        }
                    });
                    /*
                    try {
                        WeatherData weather=weatherData.get(1000, TimeUnit.MILLISECONDS);
                        if (currentCity.getTimezone() == null) {
                            currentCity.setTimezone(weather.getTimezone());
                            //userService.updateAsync(user);
                            userService.update(user).subscribe(u->{
                                try {
                                    bot.executeAsync(new SystemMessage.MessageBuilder(u)
                                            .setForecastText(weather, currentCity, nrOfDays).build().getSendMessage());
                                } catch (TelegramApiException e) {
                                    logger.warn(e.getMessage());
                                }
                            });
                        }
                        //bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                //.setForecastText(weather, currentCity, nrOfDays).build().getSendMessage());
                    }catch(InterruptedException|ExecutionException e){
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .serviceNotAvailable().build().getSendMessage());
                        bot.executeAsync(new SystemMessage.MessageBuilder(new User(botAdmin))
                                .sendErrorMessage(e.getMessage()).build().getSendMessage());
                    } catch (TimeoutException e) {
                        logger.warn("timeout "+e.getMessage());
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .serviceNotAvailable().build().getSendMessage());
                        bot.executeAsync(new SystemMessage.MessageBuilder(new User(botAdmin))
                                .sendErrorMessage(e.getMessage()).build().getSendMessage());

                    }

                     */
                }
                else{
                    //bot.executeAsync(new SystemMessage.MessageBuilder(user)
                            //.noCurrentCity().build().getSendMessage());
                    user.setPreviousState(user.getCurrentState());
                    user.setCurrentState(StateEnum.SETTINGS);
                    //userService.updateAsync(user);
                    userService.update(user).subscribe(u->{
                        try {
                            bot.executeAsync(new SystemMessage.MessageBuilder(u)
                                    .noCurrentCity().build().getSendMessage());
                            bot.executeAsync(getStateMessage(u));
                        } catch (TelegramApiException e) {
                            logger.warn(e.getMessage());
                        }

                    });
                    //bot.executeAsync(getStateMessage(user));
                }
            }
            case NOTIFICATION -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.NOTIF);
                user.setNotif(true);
                userService.update(user).subscribe(u->{
                    try {
                        bot.executeAsync(getStateMessage(u));
                    } catch (TelegramApiException e) {
                        logger.warn(e.getMessage());
                    }
                });
                //userService.updateAsync(user);
                //bot.executeAsync(getStateMessage(user));
            }
        }
    }

    @Override
    public void gotCallBack(User user, Update update) {
    }
}

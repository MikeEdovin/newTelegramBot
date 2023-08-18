package States;

import BotPackage.Bot;
import BotServices.Notifier;
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
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;

public class NewInputState implements State {
    @Autowired
    Bot bot;
    @Autowired
    Notifier notifier;
    @Autowired
    //GeoWeatherProvider geoWeatherProvider;
    ReactiveGeoWeatherProvider geoWeatherProvider;
    @Autowired
    //UserServiceImpl userService;
    ReactiveUserService userService;
    final static Logger logger= LoggerFactory.getLogger(NewInputState.class);
    private List<CityData> cities;

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) throws TelegramApiException {
        Command command = parsedCommand.getCommand();
        logger.info("Got message from user with id "+user.getUserId()+". Command: "+command);
        switch (command) {
            case NONE -> {
                geoWeatherProvider.getCityData(parsedCommand.getText()).subscribe(c->{
                    cities.add(c);
                    try {
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .sendInlineCityChoosingKeyboard(cities).build().getSendMessage());
                    } catch (TelegramApiException e) {
                        logger.warn(e.getMessage());
                    }
                });
                /*
                try {
                    CompletableFuture<CityData[]> futureCities = geoWeatherProvider.getCityDataAsync(parsedCommand.getText());
                    cities = List.of(futureCities.get());
                    bot.executeAsync(new SystemMessage.MessageBuilder(user)
                            .sendInlineCityChoosingKeyboard(cities).build().getSendMessage());
                }catch(InterruptedException|ExecutionException e){
                    logger.warn(e.getMessage());
                }

                 */
            }
            case SET_TIME -> bot.executeAsync(new SystemMessage.MessageBuilder(user)
                        .setText(Command.NONE).build().getSendMessage());

            case BACK -> {
                user.setCurrentState(user.getPreviousState());
                //userService.updateAsync(user);
                //bot.executeAsync(getStateMessage(user));
                userService.update(user).subscribe(u->{
                    try {
                        bot.executeAsync(getStateMessage(u));
                    } catch (TelegramApiException e) {
                        logger.warn(e.getMessage());
                    }
                });
            }
            case START -> {
                user.setCurrentState(StateEnum.MAIN);
                //userService.updateAsync(user);
                //bot.executeAsync(getStateMessage(user));
                userService.update(user).subscribe(u->{
                    try {
                        bot.executeAsync(getStateMessage(u));
                    } catch (TelegramApiException e) {
                        logger.warn(e.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void gotCallBack(User user, Update update) throws TelegramApiException {
        logger.info("got call back "+update.getCallbackQuery().getMessage());
        int cityIndex = Integer.parseInt(update.getCallbackQuery().getData());
        Message message = update.getCallbackQuery().getMessage();
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(null);
        editMessageReplyMarkup.setMessageId(message.getMessageId());
        editMessageReplyMarkup.setChatId(message.getChatId());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());
        if (cityIndex < 0) {
            editMessageText.setText("Back");
            if (user.isNotif()) {
                user.setCurrentState(StateEnum.NOTIF);
            } else {
                user.setCurrentState(StateEnum.SETTINGS);
            }
        } else {
            if (user.isNotif()) {
                user.setNotificationCity(cities.get(cityIndex));
                user.setCurrentState(StateEnum.NOTIF);
                notifier.gotNotifListUpdate(user);
                editMessageText.setText("Notifications city was set to "
                        + cities.get(cityIndex).getName() + ", " + cities.get(cityIndex).getCountry());
            } else {
                user.setCurrentCity(cities.get(cityIndex));
                user.setCurrentState(StateEnum.MAIN);
                editMessageText.setText("Current city was set to "
                        + cities.get(cityIndex).getName() + ", " + cities.get(cityIndex).getCountry());
            }
            user.addCityToLastCitiesList(cities.get(cityIndex));
            logger.info(user.toString());
        }
        //userService.updateAsync(user);

        try {
            bot.executeAsync(editMessageText);
            bot.executeAsync(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            logger.warn(e.getMessage());
        }
        userService.update(user).subscribe(u->{
            try {
                bot.executeAsync(getStateMessage(u));
            } catch (TelegramApiException e) {
                logger.warn(e.getMessage());
            }
        });
        //bot.executeAsync(getStateMessage(user));
    }
}


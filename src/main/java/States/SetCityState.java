package States;

import BotPackage.Bot;
import BotServices.Notifier;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SetCityState implements State {
    @Autowired
    Bot bot;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    Notifier notifier;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    private List<CityData> cities;
    final static Logger logger= LoggerFactory.getLogger(NotificationsState.class);

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) throws TelegramApiException {
        Command command = parsedCommand.getCommand();
        logger.info("Got message from user with id "+user.getUserId()+". Command: "+command);
        switch (command) {
            case SET_CITY -> {
                user.setPreviousState(StateEnum.MAIN);
                user.setCurrentState(StateEnum.NEWINPUT);
                userService.updateAsync(user);
                bot.executeAsync(getStateMessage(user));
            }
            case SEND_LOCATION -> {
                double latitude = update.getMessage().getLocation().getLatitude();
                double longitude = update.getMessage().getLocation().getLongitude();
                CompletableFuture<CityData>futureCity=geoWeatherProvider.getCityDataAsync(latitude, longitude);
                try {
                        CityData city = futureCity.get();
                        if (user.isNotif()) {
                            user.setNotificationCity(city);
                            user.setCurrentState(StateEnum.NOTIF);
                            notifier.gotNotifListUpdate(user);
                        } else {
                            user.setCurrentCity(city);
                            user.setCurrentState(StateEnum.MAIN);
                        }
                        user.addCityToLastCitiesList(city);
                        userService.updateAsync(user).get();
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .setCityWasSetText(city, user.isNotif()).build().getSendMessage());
                        bot.executeAsync(getStateMessage(user));
                    }catch(InterruptedException | ExecutionException e){
                        logger.warn(e.getMessage());
                }
            }
            case CHOOSE_FROM_LAST_THREE -> {
                cities = user.getCities();
                bot.executeAsync(new SystemMessage.MessageBuilder(user)
                        .sendInlineCityChoosingKeyboard(cities).build().getSendMessage());
            }
            case NONE, SET_TIME -> bot.executeAsync(new SystemMessage.MessageBuilder(user)
                    .setText(Command.NONE).build().getSendMessage());
            case BACK -> {
                user.setCurrentState(user.getPreviousState());
                userService.updateAsync(user);
                bot.executeAsync(getStateMessage(user));
            }
            case START -> {
                user.setCurrentState(StateEnum.MAIN);
                userService.updateAsync(user);
                bot.executeAsync(getStateMessage(user));
            }
        }
    }


    @Override
    public void gotCallBack(User user, Update update) throws TelegramApiException {
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
        }
        userService.updateAsync(user);
        bot.executeAsync(getStateMessage(user));
        try {
            bot.executeAsync(editMessageText);
            bot.executeAsync(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            logger.warn(e.getMessage());
        }
    }
}

package States;

import BotPackage.Bot;
import BotServices.MessageSender;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.UserServiceImpl;
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
    MessageSender messageSender;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    private List<CityData> cities;

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();
        switch (command) {
            case SET_CITY -> {
                user.setPreviousState(StateEnum.MAIN);
                user.setCurrentState(StateEnum.NEWINPUT);
                userService.updateAsync(user);
                messageSender.sendMessageAsync(getStateMessage(user));
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
                        } else {
                            user.setCurrentCity(city);
                            user.setCurrentState(StateEnum.MAIN);
                        }
                        user.addCityToLastCitiesList(city);
                        userService.updateAsync(user);
                        messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                                .setCityWasSetText(city, user.isNotif()).build().getSendMessage());
                        messageSender.sendMessageAsync(getStateMessage(user));
                    }catch(InterruptedException | ExecutionException e){
                        e.printStackTrace();
                        //add service temporary unavailable message
                }
            }
            case CHOOSE_FROM_LAST_THREE -> {
                cities = user.getLastThreeCities();
                messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                        .sendInlineCityChoosingKeyboard(cities).build().getSendMessage());
            }
            case NONE, SET_TIME -> messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                    .setText(Command.NONE).build().getSendMessage());
            case BACK -> {
                user.setCurrentState(user.getPreviousState());
                userService.updateAsync(user);
                messageSender.sendMessageAsync(getStateMessage(user));
            }

        }

    }


    @Override
    public void gotCallBack(User user, Update update) {
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
        messageSender.sendMessageAsync(getStateMessage(user));
        try {
            bot.executeAsync(editMessageText);
            bot.executeAsync(editMessageReplyMarkup);
        } catch (TelegramApiException e) {

        }

    }
}

package States;

import BotPackage.Bot;
import BotServices.MessageSender;
import BotServices.Observer;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.UserServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NewInputState implements State {
    @Autowired
    Bot bot;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    MessageSender messageSender;

    private List<CityData> cities;
    private List<Observer> observers = new ArrayList<>();

    @SneakyThrows
    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();
        switch (command) {
            case NONE, SET_TIME -> {
                CompletableFuture<CityData[]> futureCities = geoWeatherProvider.getCityDataAsync(parsedCommand.getText());
                    cities = List.of(futureCities.get());
                messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                            .sendInlineCityChoosingKeyboard(cities).build().getSendMessage());
            }
            case BACK -> {
                user.setCurrentState(user.getPreviousState());
                user = userService.updateAsync(user).get();
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
        try {
            bot.executeAsync(editMessageText);
            bot.executeAsync(editMessageReplyMarkup);
        } catch (TelegramApiException e) {

        }
        messageSender.sendMessageAsync(getStateMessage(user));

    }
}


package States;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.StateMessageBuilder;
import MessageCreator.WeatherMessage;
import Service.CityServiceImpl;
import Service.UserServiceImpl;
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
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CityServiceImpl cityService;

    private List<CityData> cities;

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();
        switch (command) {
            case NONE -> {
                cities = List.of(geoWeatherProvider.getCityData(parsedCommand.getText()));
                bot.sendQueue.add(new WeatherMessage
                        .MessageBuilder(user)
                        .sendInlineCityChoosingKeyboard(cities).build().getSendMessage());
            }

            case BACK -> {
                user.setCurrentState(user.getPreviousState());
                user = userService.update(user);
                sendStateMessage(user, user.getCurrentState());
            }
        }
    }

        @Override
        public void sendStateMessage (User user, StateEnum state){
            bot.sendQueue.add(new StateMessageBuilder.MessageBuilder(user.getUserId()).
                    setText(state)
                    .setKeyBoard(state)
                    .build().getSendMessage());

        }

    @Override
    public void gotCallBack(User user, Update update) {
        int citiIndex= Integer.parseInt(update.getCallbackQuery().getData());
        Message message=update.getCallbackQuery().getMessage();
        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(null);
        editMessageReplyMarkup.setMessageId(message.getMessageId());
        editMessageReplyMarkup.setChatId(message.getChatId());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());
        System.out.println("Citydata position "+citiIndex);
        if(user.isNotif()){
            user.setNotificationCity(cities.get(citiIndex));
            user.setCurrentState(StateEnum.NOTIF);
            editMessageText.setText("Notifications city was set to "
                    +cities.get(citiIndex).getName()+", "+cities.get(citiIndex).getCountry());
        }
        else{
            user.setCurrentCity(cities.get(citiIndex));
            user.setCurrentState(StateEnum.MAIN);
            editMessageText.setText("Current city was set to "
                    +cities.get(citiIndex).getName()+", "+cities.get(citiIndex).getCountry());
        }
        user.addCityToLastCitiesList(cities.get(citiIndex));
        //System.out.println("current city "+user.getCurrentCity().getName());
        //cityService.save(user.getCurrentCity());

        userService.update(user);


        try {
            bot.execute(editMessageText);
            bot.execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {

        }

        sendStateMessage(user, user.getCurrentState());

    }


}


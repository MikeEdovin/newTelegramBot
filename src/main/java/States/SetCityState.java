package States;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.CityData;
import Entities.User;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.StateMessageBuilder;
import MessageCreator.SystemMessage;
import MessageCreator.WeatherMessage;
import Service.CityServiceImpl;
import Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class SetCityState implements State{
    @Autowired
    Bot bot;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CityServiceImpl cityService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;

    private List<CityData> cities;


    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command=parsedCommand.getCommand();

        switch (command){
            case SET_CITY -> {
                user.setPreviousState(user.getCurrentState());
                user.setCurrentState(StateEnum.NEWINPUT);
                userService.update(user);
                sendStateMessage(user,user.getCurrentState());
            }
            case SEND_LOCATION -> {
                double latitude=update.getMessage().getLocation().getLatitude();
                double longitude=update.getMessage().getLocation().getLongitude();
                CityData city=geoWeatherProvider.getCityData(latitude,longitude);
                if(user.isNotif()){
                    user.setNotificationCity(city);
                    user.setCurrentState(StateEnum.NOTIF);
                }
                else{
                    user.setCurrentCity(city);
                    user.setCurrentState(StateEnum.MAIN);
                }
                user.addCityToLastCitiesList(city);
                userService.update(user);
                //System.out.println("current city "+user.getCurrentCity().getName());
                bot.sendQueue.add(new WeatherMessage.MessageBuilder(user).setCityWasSetText(city, user.isNotif()).build().getSendMessage() );
                sendStateMessage(user, user.getCurrentState());
            }
            case CHOOSE_FROM_LAST_THREE -> {
                cities=user.getLastThreeCities();
                bot.sendQueue.add(new WeatherMessage
                        .MessageBuilder(user)
                        .sendInlineCityChoosingKeyboard(cities).build().getSendMessage());
            }
            case NONE -> bot.sendQueue.add(new SystemMessage.MessageBuilder(user)
                    .setText(Command.NONE).build().getSendMessage());
            case BACK -> {
                user.setCurrentState(user.getPreviousState());
                userService.update(user);
                sendStateMessage(user,user.getCurrentState());
            }

        }

    }

    @Override
    public void sendStateMessage(User user,StateEnum state) {
        bot.sendQueue.add(new StateMessageBuilder.MessageBuilder(user.getUserId()).
                setText(state)
                .setKeyBoard(state).build().getSendMessage());
    }

    @Override
    public void gotCallBack(User user, Update update)  {
        int citiIndex= Integer.parseInt(update.getCallbackQuery().getData());
        Message message=update.getCallbackQuery().getMessage();
        EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
        editMessageReplyMarkup.setReplyMarkup(null);
        editMessageReplyMarkup.setMessageId(message.getMessageId());
        editMessageReplyMarkup.setChatId(message.getChatId());
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(message.getChatId());
        editMessageText.setMessageId(message.getMessageId());
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
        //user.addCityToLastCitiesList(cities.get(citiIndex));
        //System.out.println("current city "+user.getCurrentCity().getName());
        userService.update(user);
        sendStateMessage(user,user.getCurrentState());

        try {
            bot.execute(editMessageText);
            bot.execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {

        }

    }


}

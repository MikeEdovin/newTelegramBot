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
import org.telegram.telegrambots.meta.api.objects.Update;

public class NewInputState implements State {
    @Autowired
    Bot bot;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    CityServiceImpl cityService;

    private CityData[] cities;

    @Override
    public void gotInput(User user, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();
        switch (command) {
            case NONE -> {
                cities = geoWeatherProvider.getCityData(parsedCommand.getText());
                bot.sendQueue.add(new WeatherMessage
                        .MessageBuilder(user.getUserId())
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
        int cityNumber= Integer.parseInt(update.getCallbackQuery().getData());
        System.out.println("Citydata position "+cityNumber);
        user.setCurrentCity(cities[cityNumber]);
        user.addCityToLastCitiesList(cities[cityNumber]);
        System.out.println("current city "+user.getCurrentCity().getName());
        cityService.save(user.getCurrentCity());
        user.setCurrentState(StateEnum.MAIN);
        userService.update(user);

        sendStateMessage(user, user.getCurrentState());

    }


}


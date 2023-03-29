package Handlers;

import Commands.Command;
import org.springframework.beans.factory.annotation.Autowired;

public class HandlerFactory implements IHandlerFactory{
    @Autowired
    IHandler systemHandler;
    @Autowired
    IHandler weatherHandler;
    @Autowired
    IHandler defaultHandler;
    @Override
    public IHandler GetHandlerForCommand(Command command) {
        return switch (command) {
            case START, HELP, SETTINGS, BACK, WRONG_TIME_INPUT -> this.systemHandler;
            /*
            case NOTIFICATION, SET_NOTIFICATION_TIME, SEND_TIME_SETTING_MESSAGE, RESET_NOTIFICATIONS, SET_TIME ->
                    this.notifyHandler;

             */
            case WEATHER_NOW, GET_CITY_FROM_INPUT, ADD_CITY_TO_USER, CHOOSE_FROM_LAST_THREE , SET_CITY, FOR_48_HOURS, FOR_7_DAYS ->
                    this.weatherHandler;
            default -> this.defaultHandler;
        };
    }
}

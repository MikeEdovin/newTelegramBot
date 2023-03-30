package Handlers;

import Commands.Command;
import Commands.ParsedCommand;
import Commands.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.Future;

public class HandlerFactory implements IHandlerFactory{
    @Autowired
    IHandler systemHandler;
    @Autowired
    IHandler weatherHandler;
    @Autowired
    IHandler defaultHandler;
    @Override
    @Async
    public Future<IHandler> getHandlerForCommand(Update update) {
        Command command=analyze(update).getCommand();
        System.out.println("command "+command);
        return switch (command) {
            case START, HELP, SETTINGS, BACK, WRONG_TIME_INPUT -> new AsyncResult<>(systemHandler);
            /*
            case NOTIFICATION, SET_NOTIFICATION_TIME, SEND_TIME_SETTING_MESSAGE, RESET_NOTIFICATIONS, SET_TIME ->
                    this.notifyHandler;

             */
            case WEATHER_NOW, GET_CITY_FROM_INPUT, ADD_CITY_TO_USER, CHOOSE_FROM_LAST_THREE , SET_CITY, FOR_48_HOURS, FOR_7_DAYS ->
                    new AsyncResult<>(weatherHandler);
            default -> new AsyncResult<>(defaultHandler);
        };
    }

    public ParsedCommand analyze(Update update)  {
        ParsedCommand parsedCommand=new ParsedCommand(Command.NONE,"");
            Message message= update.getMessage();
            System.out.println("message "+message);
            if(message.hasText()){
                parsedCommand= Parser.GetParsedCommand(message.getText());
            }
            else if(message.hasLocation()){
                parsedCommand.setCommand(Command.ADD_CITY_TO_USER);
            }

        return parsedCommand;
    }







    }


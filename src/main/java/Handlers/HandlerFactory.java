package Handlers;

import Commands.Command;
import Commands.ParsedCommand;
import Commands.Parser;
import Service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HandlerFactory implements IHandlerFactory {
    @Autowired
    IHandler systemHandler;
    @Autowired
    IHandler weatherHandler;
    @Autowired
    IHandler defaultHandler;
    @Autowired
    UserServiceImpl userService;
    @Override

    public IHandler getHandlerForCommand(ParsedCommand parsedCommand) {


        Command command=parsedCommand.getCommand();

        System.out.println("command "+command);
        return switch (command) {
            case START, HELP, SETTINGS, BACK, WRONG_TIME_INPUT ->systemHandler;
            /*
            case NOTIFICATION, SET_NOTIFICATION_TIME, SEND_TIME_SETTING_MESSAGE, RESET_NOTIFICATIONS, SET_TIME ->
                    this.notifyHandler;

             */
            case CURRENT_WEATHER, GET_CITY_FROM_INPUT, ADD_CITY_TO_USER, CHOOSE_FROM_LAST_THREE , SET_CITY, FOR_48_HOURS, FOR_7_DAYS ->
                    weatherHandler;
            default -> defaultHandler;
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


package Handlers;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import GeoWeatherPackage.GeoWeatherProvider;
import Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

public class SystemHandler implements IHandler{
    @Autowired
    UserService userService;
    @Autowired
    Bot bot;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;

    @Override
    public void operate(ParsedCommand parsedCommand, long userId) {
        Command command=parsedCommand.getCommand();
        switch (command) {
            case START,BACK, SETTINGS -> {
                bot.sendQueue.add(new MessageCreator.SystemMessage.MessageBuilder(userId).
                        setKeyBoard(command).build().getSendMessage());
            }

            case HELP, WRONG_TIME_INPUT -> {
                bot.sendQueue.add(new MessageCreator.SystemMessage.MessageBuilder(userId).
                        setText(command).build().getSendMessage());
            }
        }
    }
}

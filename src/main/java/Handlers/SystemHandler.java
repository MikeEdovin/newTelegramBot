package Handlers;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Entities.User;
import MessageCreator.SystemMessage;
import Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SystemHandler implements IHandler{
    @Autowired
    IUserService userService;

    @Override
    public SendMessage operate(ParsedCommand parsedCommand, Update update) {
        Command command=parsedCommand.getCommand();
        long userID=update.getMessage().getFrom().getId();
        userService.saveIfNotExist(new User(userID));
        switch (command) {
            case START, BACK, SETTINGS -> {
                return new SystemMessage.MessageBuilder(userID).
                        setKeyBoard(command).build().getSendMessage();
            }
            case HELP, WRONG_TIME_INPUT -> {
                return new SystemMessage.MessageBuilder(userID).
                        setText(command).build().getSendMessage();
            }
        }
return null;//nwe\\
    }
}

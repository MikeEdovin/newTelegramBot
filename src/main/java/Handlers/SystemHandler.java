package Handlers;

import Commands.Command;
import Commands.ParsedCommand;
import Entities.User;
import Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SystemHandler implements IHandler{
    @Autowired
    IUserService userService;
    @Override
    public void operate(ParsedCommand parsedCommand, Update update) {
        Command command=parsedCommand.getCommand();
        long userID=update.getMessage().getFrom().getId();
        userService.saveIfNotExist(new User(userID));

    }
}

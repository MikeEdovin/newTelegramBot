package Handlers;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class SystemHandler implements IHandler{
    @Autowired
    IUserService userService;
    @Autowired
    Bot bot;

    @Override
    public void operate(ParsedCommand parsedCommand, Update update) {
        Command command=parsedCommand.getCommand();
        long userID=update.getMessage().getFrom().getId();
        switch (command) {
            case START, BACK, SETTINGS -> {
                bot.sendQueue.add(new MessageCreator.SystemMessage.MessageBuilder(userID).
                        setKeyBoard(command).build().getSendMessage());
            }
            case HELP, WRONG_TIME_INPUT -> {
                bot.sendQueue.add(new MessageCreator.SystemMessage.MessageBuilder(userID).
                        setText(command).build().getSendMessage());
            }
        }
    }
}

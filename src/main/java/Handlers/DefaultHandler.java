package Handlers;

import Commands.ParsedCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DefaultHandler implements IHandler{
    @Override
    public SendMessage operate(ParsedCommand parsedCommand, Update update) {
return null;//delete
    }
}

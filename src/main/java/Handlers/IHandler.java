package Handlers;

import Commands.ParsedCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IHandler {
    SendMessage operate(ParsedCommand parsedCommand, Update update);
}

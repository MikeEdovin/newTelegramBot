package Handlers;

import Commands.ParsedCommand;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface IHandler {
    void operate(ParsedCommand parsedCommand, Update update);
}

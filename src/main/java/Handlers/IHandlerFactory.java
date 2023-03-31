package Handlers;

import Commands.ParsedCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.Future;

public interface IHandlerFactory {
    IHandler getHandlerForCommand(ParsedCommand parsedCommand);
}

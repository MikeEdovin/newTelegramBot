package Handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.Future;

public interface IHandlerFactory {
    Future<IHandler> getHandlerForCommand(Update update);
}

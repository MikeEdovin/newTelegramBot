package Handlers;

import Commands.Command;

public interface IHandlerFactory {
    IHandler GetHandlerForCommand(Command command);
}

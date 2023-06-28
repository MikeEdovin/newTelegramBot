package BotServices;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Commands.Parser;
import Entities.User;
import Service.UserServiceImpl;
import States.State;
import States.StateFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageReceiverImpl implements MessageReceiver {
    @Autowired
    Bot bot;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    StateFactory stateFactory;

    @SneakyThrows
    @Override
    @Async
    public void gotUpdateAsync(Object object) {
        System.out.println("update threaD receiver "+Thread.currentThread().getName());
        if (object instanceof Update) {
            Update update=(Update)object;
            State state;
            User user;
            if (update.hasMessage()) {
                ParsedCommand parsedCommand = Parser.GetParsedCommand(update.getMessage().getText());
                long userId = update.getMessage().getFrom().getId();
                user = userService.saveIfNotExistAsync(new User(userId)).get();
                System.out.println("State " + user.getCurrentState());
                state = stateFactory.getState(user.getCurrentState());
                if (update.getMessage().hasLocation()) {
                    parsedCommand.setCommand(Command.SEND_LOCATION);
                }
                state.gotInput(user, parsedCommand, update);
                System.out.println(parsedCommand.getCommand().description + " new " + " state " + user.getCurrentState().description);
            }

            if (update.hasCallbackQuery()) {
                long userId = update.getCallbackQuery().getFrom().getId();
                user = userService.getUserByIdAsync(userId).get();
                state = stateFactory.getState(user.getCurrentState());
                state.gotCallBack(user, update);
            }

        }
    }
}


package BotServices;
import Commands.Command;
import Commands.ParsedCommand;
import Commands.Parser;
import Entities.User;
import Service.UserService;
import States.State;
import States.StateFactory;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.ExecutionException;

public class WorkerImpl implements Worker {
    @Autowired
    UserService userService;
    @Autowired
    StateFactory stateFactory;
    final static Logger logger= LoggerFactory.getLogger(WorkerImpl.class);
    @Override
    @Async
    public void gotUpdateAsync(Update update) {
            operate(update);
        }
    @SneakyThrows
    @Override
    public void operate(Update update) {
        State state;
        User user;
        if(update.hasMyChatMember()) {
            long userId=update.getMyChatMember().getChat().getId();
            if (update.getMyChatMember().getNewChatMember().getStatus().equals("kicked")) {
                logger.info("Requesting delete&block from user with id "+userId);
                userService.removeUserById(userId);
            }else{
                logger.info("Requesting unblock from user with id "+userId);
            }
        }
        if (update.hasMessage()) {
            ParsedCommand parsedCommand = Parser.GetParsedCommand(update.getMessage().getText());
            long userId = update.getMessage().getFrom().getId();
            user = userService.saveIfNotExistAsync(new User(userId)).get();
            state = stateFactory.getState(user.getCurrentState());

            if (update.getMessage().hasLocation()) {
                parsedCommand.setCommand(Command.SEND_LOCATION);
            }
                state.gotInput(user, parsedCommand, update);
        }
        if (update.hasCallbackQuery()) {
            long userId = update.getCallbackQuery().getFrom().getId();
            user = userService.getUserByIdAsync(userId).get();
            state = stateFactory.getState(user.getCurrentState());
            state.gotCallBack(user, update);
        }
    }
}



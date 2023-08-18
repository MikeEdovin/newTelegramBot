package BotServices;
import Commands.Command;
import Commands.ParsedCommand;
import Commands.Parser;
import Entities.User;
import Service.ReactiveUserService;
import States.StateFactory;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class WorkerImpl implements Worker {
    @Autowired
    //UserService userService;

    ReactiveUserService userService;
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
        //State state;
        //Mono<User> user;
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
            //user = userService.saveIfNotExistAsync(new User(userId)).get();
           userService.existsById(userId).subscribe(exist->{
               if(exist){
                   userService.getUserById(userId).subscribe(user -> {
                       try {
                           stateFactory.getState(user.getCurrentState()).gotInput(user,parsedCommand,update);
                       } catch (TelegramApiException e) {
                           logger.warn(e.getMessage());
                       }
                   });
               }
               else{
                   userService.save(new User(userId)).subscribe(user->{
                       try {
                           stateFactory.getState(user.getCurrentState()).gotInput(user,parsedCommand,update);
                       } catch (TelegramApiException e) {
                           logger.warn(e.getMessage());
                       }
                   });
               }
           });
            //State state = stateFactory.getState(user.getCurrentState());
            if (update.getMessage().hasLocation()) {
                parsedCommand.setCommand(Command.SEND_LOCATION);
                userService.getUserById(userId).subscribe(user->{
                    try {
                        stateFactory.getState(user.getCurrentState()).gotInput(user,parsedCommand,update);
                    } catch (TelegramApiException e) {
                        logger.warn(e.getMessage());
                    }
                });
            }
                //state.gotInput(user, parsedCommand, update);
        }
        if (update.hasCallbackQuery()) {
            long userId = update.getCallbackQuery().getFrom().getId();
            //user = userService.getUserByIdAsync(userId).get();
               userService.getUserById(userId)
                       .subscribe(u -> {
                                   try {
                                       stateFactory.getState(u.getCurrentState()).gotCallBack(u, update);
                                   } catch (TelegramApiException e) {
                                       logger.warn(e.getMessage());
                               }});

            //state = stateFactory.getState(user.getCurrentState());
            //state.gotCallBack(user, update);
        }
    }
}



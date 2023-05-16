package BotServices;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Commands.Parser;
import Entities.User;
import Handlers.IHandler;
import Handlers.IHandlerFactory;
import Service.UserService;
import States.State;
import States.StateEnum;
import States.StateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.sql.SQLException;
import java.util.Optional;

public class MessageReceiver implements IMessageReceiver {
    @Autowired
    Bot bot;
    @Autowired
    IHandlerFactory handlerFactory;
    @Autowired
    UserService userService;
    @Autowired
    StateFactory stateFactory;

    private boolean doStop = false;

    public synchronized void doStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }
        @Override
        public void run () {
            while (true) {
                for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                    Update update = (Update) object;
                    State state;

                    User user;
                    /*
                    ParsedCommand parsedCommand = Parser.GetParsedCommand(update.getMessage().getText());
                    long userId = update.getMessage().getFrom().getId();
                    userService.saveIfNotExist(new User(userId));
                    IHandler handler = handlerFactory.getHandlerForCommand(parsedCommand);
                    handler.operate(parsedCommand, userId);

                     */
                    ParsedCommand parsedCommand=Parser.GetParsedCommand(update.getMessage().getText());
                    //Command command=Parser.getCommand(update.getMessage().getText());
                    long userId=update.getMessage().getFrom().getId();
                    user=userService.saveIfNotExist(new User(userId));
                    System.out.println("State "+user.getCurrentState());
                    state= stateFactory.getState(user.getCurrentState());


                    state.gotInput(user,parsedCommand);
                    System.out.println(parsedCommand.getCommand().description+" new "+" state "+user.getCurrentState().description);


                }
                try {
                    int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
                    Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
                } catch (InterruptedException e) {
                    return;
                }
            }


        }

        @Override
        public void stop () {

        }
    }


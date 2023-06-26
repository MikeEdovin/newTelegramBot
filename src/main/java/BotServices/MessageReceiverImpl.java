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
import org.telegram.telegrambots.meta.api.objects.Update;

public class MessageReceiverImpl implements MessageReceiver {
    @Autowired
    Bot bot;
    @Autowired
    UserServiceImpl userService;
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

        /*
            while (true) {
                for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                    Update update = (Update) object;
                    State state;
                    User user;
                    if(update.hasMessage()) {
                        ParsedCommand parsedCommand = Parser.GetParsedCommand(update.getMessage().getText());
                        long userId = update.getMessage().getFrom().getId();
                        user = userService.saveIfNotExist(new User(userId));
                        System.out.println("State " + user.getCurrentState());
                        state = stateFactory.getState(user.getCurrentState());
                        if(update.getMessage().hasLocation()){
                            parsedCommand.setCommand(Command.SEND_LOCATION);

                        }
                        state.gotInput(user, parsedCommand,update );
                        System.out.println(parsedCommand.getCommand().description + " new " + " state " + user.getCurrentState().description);
                    }

                    if(update.hasCallbackQuery()){
                        long userId=update.getCallbackQuery().getFrom().getId();
                        user=userService.getUserById(userId).get();
                        state = stateFactory.getState(user.getCurrentState());
                        state.gotCallBack(user,update);



                    }


                }
                try {
                    int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
                    Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
                } catch (InterruptedException e) {
                    return;
                }
            }

         */


        }

        @Override
        public void stop () {

        }

    @SneakyThrows
    @Override
    public void gotUpdate(Object object) {
        if (object instanceof Update) {
            Update update=(Update)object;
            System.out.println("got observer " + Thread.currentThread().getName() + Thread.currentThread().isDaemon());
            State state;
            User user;
            if (update.hasMessage()) {
                ParsedCommand parsedCommand = Parser.GetParsedCommand(update.getMessage().getText());
                long userId = update.getMessage().getFrom().getId();
                user = userService.saveIfNotExist(new User(userId)).get();
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
                user = userService.getUserById(userId).get();
                state = stateFactory.getState(user.getCurrentState());
                state.gotCallBack(user, update);
            }

        }
    }
}


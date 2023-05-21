package BotServices;

import BotPackage.Bot;
import Commands.Command;
import Commands.ParsedCommand;
import Commands.Parser;
import Entities.User;
import Handlers.IHandlerFactory;
import Service.UserServiceImpl;
import States.State;
import States.StateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageReceiver implements IMessageReceiver {
    @Autowired
    Bot bot;
    @Autowired
    IHandlerFactory handlerFactory;
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
                        //AnswerCallbackQuery answerCallbackQuery=new AnswerCallbackQuery();
                        //answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());


                    }


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


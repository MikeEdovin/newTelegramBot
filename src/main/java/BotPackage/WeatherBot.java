package BotPackage;

import Commands.Command;
import Commands.ParsedCommand;
import Handlers.IHandler;
import Handlers.IHandlerFactory;
import Service.ICityService;
import Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WeatherBot extends Bot {
    private boolean started;

    private final String botName;
    private final String botToken;
    @Autowired
    ICityService cityService;
    @Autowired
    IUserService userService;
    @Autowired
    IHandlerFactory handlerFactory;
    Logger logger=Logger.getLogger("BotLogger");

    public WeatherBot(String botName,String botToken){
        this.botName=botName;
        this.botToken=botToken;


        ConsoleHandler handler=new ConsoleHandler();
        handler.setLevel(Level.INFO);
        logger.addHandler(handler);
    }
    @Override
    public boolean isStarted() {
        return started;
    }

    @Override
    public void setStartedStatus(boolean status) {
        this.started=status;
    }

    @Override
    public String getBotUsername() {
        return this.botName;
    }

    @Override
    public String getBotToken() {
        return this.botToken;
    }

    @Override
    public void onRegister() {

    }


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {
            receiveQueue.add(update);
            System.out.println("Got message "+update.getMessage());
        }
        if(update.hasCallbackQuery()){
            Message message=update.getCallbackQuery().getMessage();
            String chatID=String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            AnswerCallbackQuery answerCallbackQuery=new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(update.getCallbackQuery().getId());
            int messageID=message.getMessageId();
            CallbackQuery query=update.getCallbackQuery();
            EditMessageReplyMarkup editMessageReplyMarkup=new EditMessageReplyMarkup();
            InlineKeyboardMarkup keyboardMarkup = updateInlineKeyBoard(query);
            editMessageReplyMarkup.setReplyMarkup(keyboardMarkup);
            editMessageReplyMarkup.setMessageId(messageID);
            editMessageReplyMarkup.setChatId(chatID);
            try{
                execute(answerCallbackQuery);
                execute(editMessageReplyMarkup);
            } catch (TelegramApiException e) {
                logger.log(Level.FINE,e.getMessage());
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        System.out.println("Got message ");
        Future<IHandler> futureHandler=handlerFactory.getHandlerForCommand(updates.get(0));
        for(Update update:updates){
            System.out.println(update.getClass());
        }


            if(futureHandler.isDone()){
                try {
                    IHandler handler= futureHandler.get();
                    this.executeSend(
                            handler.operate(new ParsedCommand(Command.START,""), updates.get(0)));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }

    }




    @Override
    public InlineKeyboardMarkup updateInlineKeyBoard(CallbackQuery query) {
        return null;
    }

    @Override
    public void botConnect() throws TelegramApiException {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            setStartedStatus(true);

    }

    @Override
    public void executeSend(BotApiMethod<Message> message) {
        try {
            this.execute(message);
        }catch(TelegramApiException ignored){
        }


    }
}

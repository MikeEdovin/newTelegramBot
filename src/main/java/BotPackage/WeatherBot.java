package BotPackage;

import BotServices.Observer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WeatherBot extends Bot {
    private final List<Observer> observers=new ArrayList<>();
    private boolean started;
    private final String botName;
    private final String botToken;


    Logger logger=Logger.getLogger("BotLogger");

    public WeatherBot(String botName,String botToken){
        this.botName=botName;
        this.botToken=botToken;
        ConsoleHandler handler=new ConsoleHandler();
        handler.setLevel(Level.INFO);
        logger.addHandler(handler);
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
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
            for (Update update : updates) {
                notifyObservers(update);
            }

    }
    @Override
    public void botConnect() throws TelegramApiException {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            setStartedStatus(true);

    }

    @Override
    public void add(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(Update update) {
        for(Observer o:observers){
            o.gotUpdateAsync(update);
}
    }
}

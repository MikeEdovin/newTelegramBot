package BotPackage;

import BotServices.Observer;
import Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WeatherBot extends Bot {
    private boolean started;
    private List<Observer> observers;

    private final String botName;
    private final String botToken;
    @Autowired
    UserService userService;

    Logger logger=Logger.getLogger("BotLogger");

    public WeatherBot(String botName,String botToken){
        this.botName=botName;
        this.botToken=botToken;
        this.observers=new ArrayList<>();
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
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        System.out.println("Got message ");
            for (Update update : updates) {
                //receiveQueue.add(update);
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
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(Object object) {
        for(Observer observer:observers){
            observer.gotUpdate(object);
        }
    }
}

package BotPackage;

import BotServices.Observable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class Bot extends TelegramLongPollingBot implements Observable {

    abstract void setStartedStatus(boolean status);
    abstract public String getBotToken();
    abstract public void botConnect() throws TelegramApiException;
}

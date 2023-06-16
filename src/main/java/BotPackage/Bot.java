package BotPackage;

import BotServices.Observable;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Bot extends TelegramLongPollingBot implements Observable {
    public Queue<Object> sendQueue=new ConcurrentLinkedQueue<>();
    public Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();
    abstract boolean isStarted();
    abstract void setStartedStatus(boolean status);
    abstract public String getBotToken();

    abstract public void onUpdatesReceived(List<Update> updates);
    abstract public void botConnect() throws TelegramApiException;
}

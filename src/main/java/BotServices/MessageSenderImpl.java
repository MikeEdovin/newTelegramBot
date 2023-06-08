package BotServices;

import BotPackage.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageSenderImpl implements MessageSender {
    @Autowired
    Bot bot;

    private boolean doStop = false;

    public synchronized void doStop() {
        this.doStop = true;
    }

    private synchronized boolean keepRunning() {
        return this.doStop == false;
    }

    @Override
    public void run() {
        while (true) {
            for (Object object = bot.sendQueue.poll(); object != null; object = bot.sendQueue.poll()) {
                if (object instanceof BotApiMethod) {
                    BotApiMethod<Message> message = (BotApiMethod<Message>) object;
                    try {
                        bot.execute(message);
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                int SENDER_SLEEP_TIME = 1000;
                Thread.sleep(SENDER_SLEEP_TIME);
            } catch (InterruptedException e) {
            }
        }
    }

        @Override
        public void stop () {

        }
    }


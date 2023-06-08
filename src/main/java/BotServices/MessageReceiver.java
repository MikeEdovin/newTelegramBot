package BotServices;

public interface MessageReceiver extends Runnable {
    void run();
    void stop();


}

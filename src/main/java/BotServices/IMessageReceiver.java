package BotServices;

public interface IMessageReceiver extends Runnable {
    void run();
    void stop();


}

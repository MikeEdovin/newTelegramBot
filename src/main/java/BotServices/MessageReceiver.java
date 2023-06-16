package BotServices;

public interface MessageReceiver extends Runnable,Observer {
    void run();
    void stop();


}

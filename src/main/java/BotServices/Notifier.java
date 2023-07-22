package BotServices;

import Entities.User;

public interface Notifier{
    void gotNotifListUpdate(User user);
    void sendNotifications();
}

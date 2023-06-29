package BotServices;

import Entities.User;

import java.util.ArrayList;
import java.util.List;

public interface Notifier{
    void gotNotifListUpdate(User user);
    void sendNotifications();
}

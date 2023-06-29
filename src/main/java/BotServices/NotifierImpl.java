package BotServices;

import Entities.CityData;
import Entities.User;
import Entities.WeatherData;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.FixedRateTask;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class NotifierImpl implements Notifier {
    @Autowired
    UserService userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    MessageSender messageSender;

    private List<User> usersWithNotifications = new ArrayList<>();


    @Override
    public void gotNotifListUpdate(User user) {
        System.out.println("notifier was update");
        try {
            usersWithNotifications = userService.getAllUsersWithNotificationsAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    @Scheduled(fixedRate = 60000)
    public void sendNotifications() {
        System.out.println("notif thread "+Thread.currentThread().getName());
        if (usersWithNotifications != null) {
            for (User user : usersWithNotifications) {
                System.out.println(user.getUserId() + " not/");
                if (user.getNotificationTime().getHour() == LocalTime.now().getHour()
                &&user.getNotificationTime().getMinute()==LocalTime.now().getMinute()) {
                    CityData notificationsCity = user.getNotificationCity();
                    CompletableFuture<WeatherData> futureWeatherData =
                            geoWeatherProvider.getWeatherDataAsync(
                                    notificationsCity.getLat(), notificationsCity.getLon());
                    try {
                        WeatherData weatherData=futureWeatherData.get();
                        messageSender.sendMessageAsync(new SystemMessage.MessageBuilder(user)
                                .setForecastText(weatherData, notificationsCity, 1).build().getSendMessage());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        //add service temporary unavailable message
                    }
                }
            }
        }

    }
}



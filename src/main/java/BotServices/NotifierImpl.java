package BotServices;

import BotPackage.Bot;
import Entities.CityData;
import Entities.User;
import Entities.WeatherData;
import GeoWeatherPackage.GeoWeatherProvider;
import MessageCreator.SystemMessage;
import Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@Service
public class NotifierImpl implements Notifier {
    @Autowired
    UserService userService;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Autowired
    Bot bot;
    final static Logger logger= LoggerFactory.getLogger(NotifierImpl.class);
    private List<User> usersWithNotifications = new ArrayList<>();
    @Override
    public void gotNotifListUpdate(User user) {
        try {
            usersWithNotifications = userService.getAllUsersWithNotificationsAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.warn(e.getMessage());
        }
    }
    @Override
    @Async
    @Scheduled(fixedRate = 60000)
    public void sendNotifications() {
        if (usersWithNotifications != null) {
            for (User user : usersWithNotifications) {
                if (user.getNotificationTime().getHour() == LocalTime.now().getHour()
                &&user.getNotificationTime().getMinute()==LocalTime.now().getMinute()) {
                    CityData notificationsCity = user.getNotificationCity();
                    CompletableFuture<WeatherData> futureWeatherData =
                            geoWeatherProvider.getWeatherDataAsync(
                                    notificationsCity.getLat(), notificationsCity.getLon());
                    try {
                        WeatherData weatherData=futureWeatherData.get();
                        bot.executeAsync(new SystemMessage.MessageBuilder(user)
                                .setForecastText(weatherData, notificationsCity, 1).build().getSendMessage());
                    } catch (TelegramApiException|InterruptedException|ExecutionException e) {
                        logger.warn(e.getMessage());
                    }
                }
            }
        }

    }
}



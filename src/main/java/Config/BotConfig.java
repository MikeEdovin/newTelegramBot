package Config;

import BotPackage.Bot;
import BotPackage.WeatherBot;
import BotServices.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("config.properties")
//@EnableAsync
public class BotConfig {
    @Value("${bot.name}") String botName;
    @Value("${bot.token}") String botToken;
    @Bean
    public Bot getWeatherBot(){
        return new WeatherBot(botName,botToken);
    }
    @Bean
    public MessageReceiver getMessageReceiver(){
        return new MessageReceiverImpl();
    }
    @Bean
    public MessageSender getMessageSender(){
        return new MessageSenderImpl();
    }
    @Bean
    public Notifier getNotifier(){return new NotifierImpl();}

}

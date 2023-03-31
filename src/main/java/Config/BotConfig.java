package Config;

import BotPackage.Bot;
import BotPackage.WeatherBot;
import BotServices.IMessageReceiver;
import BotServices.IMessageSender;
import BotServices.MessageReceiver;
import BotServices.MessageSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
//@EnableAsync
public class BotConfig {
    String botName="EdovinWeatherBot";
    String botToken="5295256851:AAHhA46BhJUzvzIE29rpjE8KlVCsDobF-is";

    @Bean
    public Bot getWeatherBot(){
        return new WeatherBot(botName,botToken);
    }
    @Bean
    public IMessageReceiver messageReceiver(){
        return new MessageReceiver();
    }
    @Bean
    public IMessageSender messageSender(){
        return new MessageSender();
    }

}

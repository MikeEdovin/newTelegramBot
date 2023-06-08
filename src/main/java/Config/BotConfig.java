package Config;

import BotPackage.Bot;
import BotPackage.WeatherBot;
import BotServices.MessageReceiver;
import BotServices.MessageSender;
import BotServices.MessageReceiverImpl;
import BotServices.MessageSenderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public MessageReceiver messageReceiver(){
        return new MessageReceiverImpl();
    }
    @Bean
    public MessageSender messageSender(){
        return new MessageSenderImpl();
    }

}

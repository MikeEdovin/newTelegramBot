package Config;

import Handlers.DefaultHandler;
import Handlers.IHandler;
import Handlers.SystemHandler;
import Handlers.WeatherHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlersConfig {
    @Bean(name="defaultHandler")
    public IHandler getDefaultHandler(){
        return new DefaultHandler();
    }
    @Bean(name="systemHandler")
    public IHandler getSystemHandler(){
        return new SystemHandler();
    }
    @Bean(name="weatherHandler")
    public IHandler getWeatherHandler(){
        return new WeatherHandler();
    }
}

package Config;

import Service.CityServiceImpl;
import Service.CityService;
import Service.UserService;
import Service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public CityService getCityService(){
        return new CityServiceImpl();
    }
    @Bean
    public UserService getUserService(){
        return new UserServiceImpl();
    }
}

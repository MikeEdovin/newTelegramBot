package Config;

import Service.CityService;
import Service.ICityService;
import Service.IUserService;
import Service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public ICityService getCityService(){
        return new CityService();
    }
    @Bean
    public IUserService getUserService(){
        return new UserService();
    }
}

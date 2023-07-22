package Config;

import Service.UserService;
import Service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {
    @Bean
    public UserService getUserService(){
        return new UserServiceImpl();
    }
}

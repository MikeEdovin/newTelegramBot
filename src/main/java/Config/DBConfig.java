package Config;

import Repository.CityRepository;
import Repository.UserRepository;
import Repository.WeatherRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {CityRepository.class, UserRepository.class, WeatherRepository.class})
@EntityScan(basePackages = {"Entities"})
public class DBConfig {
}

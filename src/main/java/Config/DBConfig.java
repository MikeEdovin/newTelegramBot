package Config;

import Repository.ReactiveUserRepository;
import Repository.UserRepository;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
//@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ReactiveUserRepository.class})
//@EnableR2dbcRepositories(basePackageClasses = {ReactiveUserRepository.class})
//@EntityScan(basePackages = {"Entities"})
//@EnableR2dbcRepositories
public class DBConfig  {

}

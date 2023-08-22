package Repository;

import Entities.User;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
//@EntityScan(basePackages = {"Entities"})
public interface ReactiveUserRepository extends R2dbcRepository<User,Long> {
    @Query(value="SELECT * FROM Users WHERE user_id= :userId")
    Mono<User> findById(long userId);
    @Query(value = "SELECT * FROM Users  WHERE notification_time IS NOT NULL and notification_city_lat IS NOT NULL")
    Flux<User> getAllUsersWithNotifications();
    @Query(value="select exists (select 1 from users where user_id = :userId)")
    Mono<Boolean>existsById(long userId);

}



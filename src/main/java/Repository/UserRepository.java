package Repository;

import Entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {
    @Query(value = "SELECT * FROM Users  WHERE notification_time IS NOT NULL and notification_city_lat IS NOT NULL",nativeQuery = true)
    List<User> getAllUsersWithNotifications();

}

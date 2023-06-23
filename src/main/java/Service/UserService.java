package Service;

import Entities.User;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long userId);
    User update(User user);
    User saveIfNotExist(User user);
    List<User> getAllUsersWithNotifications();
}

package Service;

import Entities.User;

import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> getUserById(long userId);
    User update(User user);
}

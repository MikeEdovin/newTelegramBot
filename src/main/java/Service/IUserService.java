package Service;

import Entities.User;

import java.util.Optional;

public interface IUserService {
    User save(User user);
    Optional<User> getUserById(long userId);
    User update(User user);
    User saveIfNotExist(User user);
}

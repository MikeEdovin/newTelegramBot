package Service;

import Entities.User;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<User> getUserById(long userId);
    CompletableFuture<User> update(User user);
    CompletableFuture<User> saveIfNotExist(User user);
    CompletableFuture<List<User>>getAllUsersWithNotifications();
}

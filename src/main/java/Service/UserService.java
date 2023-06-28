package Service;

import Entities.User;


import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserService {
    CompletableFuture<User> getUserByIdAsync(long userId);
    CompletableFuture<User> updateAsync(User user);
    CompletableFuture<User> saveIfNotExistAsync(User user);
    CompletableFuture<List<User>> getAllUsersWithNotificationsAsync();
}

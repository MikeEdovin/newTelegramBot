package Service;

import Entities.User;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.concurrent.CompletableFuture;
@Service
public interface UserService {
    CompletableFuture<User> getUserByIdAsync(long userId);
    CompletableFuture<User> updateAsync(User user);
    CompletableFuture<User> saveIfNotExistAsync(User user);
    CompletableFuture<List<User>> getAllUsersWithNotificationsAsync();
    CompletableFuture<Boolean> removeUserById(long userId);
    CompletableFuture<List<User>>getAllUsers();

}

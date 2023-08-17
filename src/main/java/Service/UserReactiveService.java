package Service;

import Entities.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserReactiveService {
    Mono<User> getUserById(long userId);
    Mono<User> update(User user);
    Mono<User> saveIfNotExist(User user);
    Flux<User> getAllUsersWithNotifications();
    Mono<Void> removeUserById(long userId);
}

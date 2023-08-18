package Service;

import Entities.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveUserService {
    Mono<User> getUserById(long userId);
    Mono<User> update(User user);
    Mono<User> save(User user);
    Flux<User> getAllUsersWithNotifications();
    Mono<Void> removeUserById(long userId);
    Mono<Boolean>existsById(long userId);
}

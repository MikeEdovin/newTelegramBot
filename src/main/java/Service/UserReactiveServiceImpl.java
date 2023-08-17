package Service;

import Entities.User;
import Repository.UserReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class UserReactiveServiceImpl implements UserReactiveService{
    @Autowired
    UserReactiveRepository repository;
    @Override
    public Mono<User> getUserById(long userId) {
        return repository.findById(userId);
    }

    @Override
    public Mono<User> update(User user) {
        return repository.save(user);
    }

    @Override
    public Mono<User> saveIfNotExist(User user) {

        return null;
    }

    @Override
    public Flux<User> getAllUsersWithNotifications() {
        return null;
    }

    @Override
    public Mono<Void> removeUserById(long userId) {
        return null;
    }
}

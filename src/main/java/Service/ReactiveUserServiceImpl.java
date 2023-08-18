package Service;

import Entities.User;
import Repository.ReactiveUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class ReactiveUserServiceImpl implements ReactiveUserService {
    static final Logger logger= LoggerFactory.getLogger(ReactiveUserServiceImpl.class);
    //@Autowired
    ReactiveUserRepository repository;
    @Override
    public Mono<User> getUserById(long userId) {
        return repository.findById(userId);
    }

    @Override
    public Mono<User> update(User user) {
        return repository.save(user);
    }

    @Override
    public Mono<User> save(User user) {return repository.save(user);}

    @Override
    public Flux<User> getAllUsersWithNotifications() {
        return repository.getAllUsersWithNotifications();
    }

    @Override
    public Mono<Void> removeUserById(long userId) {
        return repository.deleteById(userId);
    }

    @Override
    public Mono<Boolean> existsById(long userId) {
        return repository.existsById(userId);
    }
}

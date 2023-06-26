package Service;

import Entities.User;
import Repository.UserRepository;
import States.StateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;


    @Override
    @Transactional
    @Async
    public CompletableFuture<User> getUserById(long userId) {
        return CompletableFuture.completedFuture(repository.findById(userId).get());
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<User> update(User user) {
        return CompletableFuture.completedFuture(repository.save(user));
    }
    @Override
    @Transactional
    @Async
    public CompletableFuture<User> saveIfNotExist(User user) {
        if(repository.existsById(user.getUserId())){
            return CompletableFuture.completedFuture(repository.findById(user.getUserId()).get());
        }
        else{
            user.setCurrentState(StateEnum.MAIN);
            return CompletableFuture.completedFuture(repository.save(user));
        }

    }

    @Override
    @Async
    public CompletableFuture<List<User>> getAllUsersWithNotifications() {
        return CompletableFuture.completedFuture(repository.getAllUsersWithNotifications());
    }


}

package Service;

import Entities.User;
import Repository.UserRepository;
import States.StateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;


    @Override
    @Transactional
    @Async
    public CompletableFuture<User> getUserByIdAsync(long userId) {
        System.out.println("user Thread getById"+Thread.currentThread().getName());
        return CompletableFuture.completedFuture(repository.findById(userId).get());
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<User> updateAsync(User user) {
        System.out.println("user Thread update"+Thread.currentThread().getName());
        return CompletableFuture.completedFuture(repository.save(user));
    }
    @Override
    @Transactional
    @Async
    public CompletableFuture<User> saveIfNotExistAsync(User user) {
        System.out.println("user Thread saveIfNotExist"+Thread.currentThread().getName());
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
    public CompletableFuture<List<User>> getAllUsersWithNotificationsAsync() {
        System.out.println("user Thread getAll"+Thread.currentThread().getName());
        return CompletableFuture.completedFuture(repository.getAllUsersWithNotifications());
    }


}

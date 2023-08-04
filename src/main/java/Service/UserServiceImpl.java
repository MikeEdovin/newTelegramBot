package Service;

import Entities.User;
import Repository.UserRepository;
import States.StateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@Service
public class UserServiceImpl implements UserService {
    static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepository repository;
    @Override
    @Transactional
    @Async
    public CompletableFuture<User> getUserByIdAsync(long userId) {
        logger.info("Requested user with id "+userId+" from DB");
        return CompletableFuture.completedFuture(repository.findById(userId).get());
    }
    @Override
    @Transactional
    @Async
    public CompletableFuture<User> updateAsync(User user) {
        logger.info("Updated user with id "+user.getUserId());

        return CompletableFuture.completedFuture(repository.save(user));
    }
    @Override
    @Transactional
    @Async
    public CompletableFuture<User> saveIfNotExistAsync(User user) {
        logger.info("Requested adding of user with id "+user.getUserId()+" to DB");
        if(repository.existsById(user.getUserId())){
            logger.info("user with id "+user.getUserId()+" is already in DB");
            return CompletableFuture.completedFuture(repository.findById(user.getUserId()).get());
        }
        else{
            logger.info("user with id "+user.getUserId()+" was added in DB");
            user.setCurrentState(StateEnum.MAIN);
            return CompletableFuture.completedFuture(repository.save(user));
        }
    }
    @Override
    @Async
    public CompletableFuture<List<User>> getAllUsersWithNotificationsAsync() {
        logger.info("Requested all users with notifications");
        return CompletableFuture.completedFuture(repository.getAllUsersWithNotifications());
    }

    @Override
    @Transactional
    @Async
    public CompletableFuture<Boolean> removeUserById(long userId) {
        logger.info("Requested removing from DB user with id "+userId);
        if(repository.existsById(userId)){
            repository.deleteById(userId);
            logger.info("User was deleted");
            return CompletableFuture.completedFuture(true);
        }
        logger.info("User with id " + userId + " wasn't found");
        return CompletableFuture.completedFuture(false);
    }
}

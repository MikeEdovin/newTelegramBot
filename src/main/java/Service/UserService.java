package Service;

import Entities.User;
import Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserService implements IUserService {
    @Autowired
    UserRepository repository;
    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return repository.findById(userId);
    }

    @Override
    public User update(User user) {

        return null;
    }
    @Override
    public User saveIfNotExist(User user) {
        if(repository.existsById(user.getUserId())){
            return user;
        }
        else{
            return repository.save(user);
        }

    }

}

package Service;

import Entities.User;
import Repository.UserRepository;
import States.StateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;
    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> getUserById(long userId) {
            return repository.findById(userId);
    }

    @Override
    @Transactional
    public User update(User user) {
       return repository.save(user);
    }
    @Override
    @Transactional
    public User saveIfNotExist(User user) {
        if(repository.existsById(user.getUserId())){
            return repository.findById(user.getUserId()).get();
        }
        else{
            user.setCurrentState(StateEnum.MAIN);
            return repository.save(user);
        }

    }

}

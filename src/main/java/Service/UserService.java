package Service;

import Entities.User;
import Repository.UserRepository;
import States.StateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
        if(repository.existsById(userId)){
            return repository.findById(userId);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public User update(User user) {
        repository.findById(user.getUserId());
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

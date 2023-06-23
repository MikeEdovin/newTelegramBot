package Service;

import Entities.User;
import Repository.UserRepository;
import States.StateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;


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

    @Override
    public List<User> getAllUsersWithNotifications() {
        return repository.getAllUsersWithNotifications();
        //return null;
    }


}

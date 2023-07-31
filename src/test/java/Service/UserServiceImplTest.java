package Service;

import Entities.CityData;
import Entities.User;
import Repository.UserRepository;
import States.StateEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository mockRepository;
    @InjectMocks
    UserServiceImpl userService;
    @Test
    void getUserByIdAsyncShouldReturnExpectedValue() throws ExecutionException, InterruptedException {
        User user=new User(123456789L);
        Mockito.when(mockRepository.findById(123456789L)).thenReturn(Optional.of(user));
        Mockito.when(mockRepository.findById(987654321L)).thenThrow(NoSuchElementException.class);
        Assertions.assertEquals(123456789L,userService.getUserByIdAsync(123456789L).get().getUserId());
        Assertions.assertThrows(NoSuchElementException.class,()->userService.getUserByIdAsync(987654321L));
    }
    @Test
    void updateAsync() throws ExecutionException, InterruptedException {
        User user=new User(123456789L);
        user.setNotif(true);
        Mockito.when(mockRepository.save(user)).thenReturn(user);
        Assertions.assertTrue(userService.updateAsync(user).get().isNotif());
    }
    @Test
    void saveIfNotExistAsync() throws ExecutionException, InterruptedException {
        User user1=new User(123456789L);
        User user2=new User(987654321L);
        Mockito.when(mockRepository.existsById(123456789L)).thenReturn(true);
        Mockito.when(mockRepository.findById(123456789L)).thenReturn(Optional.of(user1));
        Mockito.when(mockRepository.existsById(987654321L)).thenReturn(false);
        Mockito.when(mockRepository.save(user2)).thenReturn(user2);
        Assertions.assertEquals(user1.getUserId(),userService.saveIfNotExistAsync(user1).get().getUserId());
        Assertions.assertEquals(user2.getUserId(),userService.saveIfNotExistAsync(user2).get().getUserId());
    }
    @Test
    void getAllUsersWithNotificationsAsync() throws ExecutionException, InterruptedException {
        CityData notificationsCity=new CityData("Piter",30.22,50.23,"Russia","St.Petersburg","Europe",null);
        int[]days={1,2,3};
        User user1=new User(123456789L, StateEnum.MAIN,StateEnum.MAIN,null,notificationsCity,null, LocalTime.NOON,days,false);
        User user2= new User(987654321L, StateEnum.MAIN,StateEnum.MAIN,null,notificationsCity,null, LocalTime.MIDNIGHT,days,false);
        User user3=new User(567891234L, StateEnum.MAIN,StateEnum.MAIN,null,notificationsCity,null, LocalTime.NOON,days,false);
        List<User> usersWithNotifications= List.of(new User[]{user1, user2, user3});
        Mockito.when(mockRepository.getAllUsersWithNotifications()).thenReturn(usersWithNotifications);
        Assertions.assertEquals(3,userService.getAllUsersWithNotificationsAsync().get().size());
        Assertions.assertEquals(987654321L,userService.getAllUsersWithNotificationsAsync().get().get(1).getUserId());
    }
    @Test
    void removeUserById() throws ExecutionException, InterruptedException {
        Mockito.when(mockRepository.existsById(123456789L)).thenReturn(true);
        Mockito.when(mockRepository.existsById(987654321L)).thenReturn(false);
        Assertions.assertEquals(true,userService.removeUserById(123456789L).get());
        Assertions.assertEquals(false,userService.removeUserById(987654321L).get());
    }
}
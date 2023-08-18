package Repository;

import Config.DBConfig;
import Entities.CityData;
import Entities.User;
import States.StateEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.Optional;

@DataJpaTest()
@Testcontainers
@ContextConfiguration(classes = DBConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/testData.sql"})
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer database = new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("testDb")
            .withPassword("password")
            .withUsername("user");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
    }

    @Autowired
    UserRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    //@Test
    //void getAllUsersWithNotifications() {
        //Assertions.assertEquals(1,repository.getAllUsersWithNotifications().size());
   // }
    @Test
    void existsByIdShouldReturnExpectedValue(){
        Assertions.assertEquals(true,repository.existsById(123456789L));
        Assertions.assertEquals(false,repository.existsById(44444444L));
    }
    @Test
    void findByIdShouldReturnExpectedValue() {
    Assertions.assertNotNull(repository.findById(123456789L).get());
    Assertions.assertTrue(repository.findById(123456789L).get().hasAtLeastOneNotDay());
    Assertions.assertEquals(Optional.empty(),repository.findById(99999999L));
    }
    @Test
    void saveShouldReturnExpectedValue(){
        CityData city=new CityData("Saint Petersburg",59.938732,30.316229,"RU","Saint Petersburg","Europe/Moscow");
        User user=new User(55555555L);
        user.setCurrentState(StateEnum.NOTIF);
        user.setCurrentCity(city);
        User user2=new User(11111111L);
        user2.setCurrentState(StateEnum.MAIN);
        user2.setCurrentCity(city);
        User savedUser=repository.save(user);
        User savedUser2=repository.save(user2);
        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(StateEnum.NOTIF,savedUser.getCurrentState());
        Assertions.assertNotNull(savedUser2);
        Assertions.assertEquals(StateEnum.MAIN,savedUser2.getCurrentState());
    }


}
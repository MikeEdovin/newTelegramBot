package Entities;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Stack;


@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @Column(name="user_id")
    private long userId;
    @Column(name="current_city")
    private String currentCity;
    @Column(name="last_three_cities")
    private String[] lastThreeCities;
    @Column(name="notification_time")
    private LocalTime notificationTime;
    @Column(name="notification_city")
    private String notificationCity;
    @Column(name="notification_days")
    private int[]notificationDays;

    public User(){}
    public User(long userId){
        this.userId = userId;
        this.lastThreeCities=new String[3];
        this.notificationDays=new int[7];
    }
    public void addNotificationDay(int day){notificationDays[day]=day;}
    public void deleteNotificationDay(int day){notificationDays[day]=0;}
    public boolean isNotificationDay(int day) {
        return notificationDays[day] == day;
    }

    public boolean hasAtLeastOneNotDay(){
        for(int i =1;i<notificationDays.length;i++){
            if(notificationDays[i]==i){
                return true;
            }
        }
        return false;
    }


    public void addCityToLastCitiesList(String city){
        boolean alreadyInList=false;
        for(String item:lastThreeCities){
            if (item != null && item.equalsIgnoreCase(city)) {
                alreadyInList = true;
                break;
            }
        }
        if(!alreadyInList) {
            Stack<String> stack = new Stack<>();
            for (String lastThreeCity : lastThreeCities) {
                if (lastThreeCity != null) {
                    stack.add(lastThreeCity);
                }
            }
            stack.add(city);
            for (int i = 0; i < lastThreeCities.length; i++) {
                if (!stack.isEmpty() && stack.peek() != null) {
                    lastThreeCities[i] = stack.pop();
                }
            }
        }
    }
}

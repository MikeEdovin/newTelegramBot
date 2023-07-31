package Entities;
import States.StateEnum;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@ToString
@Table(name="users")

public class User {
    @Id
    @Column(name="user_id")
    private long userId;
    private StateEnum currentState;
    private StateEnum previousState;

    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "current_city_lat", referencedColumnName = "lat"),
            @JoinColumn(name = "current_city_lon", referencedColumnName = "lon") })
    @Cascade({CascadeType.PERSIST,CascadeType.REMOVE})
    private CityData currentCity;

    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "notification_city_lat", referencedColumnName = "lat"),
            @JoinColumn(name = "notification_city_lon", referencedColumnName = "lon") })
    @Cascade({CascadeType.PERSIST,CascadeType.REMOVE})
    private CityData notificationCity;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinTable(name="last_three_cities",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "user_id")
    ,inverseJoinColumns = {@JoinColumn(name="last_three_cities_lat",referencedColumnName = "lat")
            ,@JoinColumn(name="last_three_cities_lon",referencedColumnName = "lon")})
    private List<CityData> lastThreeCities=new ArrayList<>();
    @Column(name="notification_time")
    private LocalTime notificationTime;


    @Column(name="notification_days")
    private int[]notificationDays=new int[7];

    @Column(name="isNotif")
    private boolean isNotif;

    public User(long userId){
        this.userId=userId;

    }



    public void addNotificationDay(int day){notificationDays[day-1]=day;}
    public void deleteNotificationDay(int day){notificationDays[day-1]=0;}
    public boolean isNotificationDay(int day) {
        return notificationDays[day-1] == day;
    }
    public void clearNotifications(){
        this.setNotificationCity(null);
        this.setNotificationTime(null);
        Arrays.fill(notificationDays, 0);
    }

    public boolean hasAtLeastOneNotDay(){
        for(int i =1;i<notificationDays.length;i++){
            if(notificationDays[i-1]==i){
                return true;
            }
        }
        return false;
    }


    public void addCityToLastCitiesList(CityData city){
       if(lastThreeCities.contains(city)){
           return;
        }
            Stack<CityData> stack = new Stack<>();
            for (CityData item : lastThreeCities) {
                if (item != null) {
                    stack.add(item);
                }
            }
            stack.add(city);
            lastThreeCities.clear();
            for (int i = 0; i < 3; i++) {
                if (!stack.isEmpty() && stack.peek() != null) {
                    lastThreeCities.add(stack.pop());
                }
            }
        }





}

package Entities;
import States.StateEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.Stack;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")

public class User {


    @Id
    @Column(name="user_id")
    private long userId;

    private StateEnum currentState;
    private StateEnum previousState;

    @OneToOne
    @JoinColumns({ @JoinColumn(name = "current_city_lat", referencedColumnName = "lat"),
            @JoinColumn(name = "current_city_lon", referencedColumnName = "lon") })
    private CityData currentCity;

    @Embedded
    @ElementCollection
    @JoinTable(name="lastThreeCities")
    @OrderColumn(name="lastThreeCities_index")
    private CityData[] lastThreeCities=new CityData[3];
    @Column(name="notification_time")
    private LocalTime notificationTime;
    @OneToOne
    @JoinColumns({ @JoinColumn(name = "notification_city_lat", referencedColumnName = "lat"),
            @JoinColumn(name = "notification_city_lon", referencedColumnName = "lon") })
    private CityData notificationCity;
    @Column(name="notification_days")
    private int[]notificationDays=new int[7];


    public User(long userId){
        this.userId=userId;
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


    public void addCityToLastCitiesList(CityData city){
        boolean alreadyInList=false;
        for(CityData item:lastThreeCities){
            if (item != null && item.getLat()== city.getLat()&&item.getLon()==city.getLon()) {
                alreadyInList = true;
                break;
            }
        }
        if(!alreadyInList) {
            Stack<CityData> stack = new Stack<>();
            for (CityData item : lastThreeCities) {
                if (item != null) {
                    stack.add(item);
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

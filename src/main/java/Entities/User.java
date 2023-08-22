package Entities;
import States.StateEnum;
//import jakarta.persistence.*;
//import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import reactor.core.publisher.Mono;
//import org.hibernate.annotations.*;
//import org.hibernate.annotations.CascadeType;
//import org.springframework.data.annotation.Id;

import java.time.LocalTime;
import java.util.*;

//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@ToString
@Table(name="users")
@Data
@Builder

public class User {
    @Id
    @Column("user_id")
    private long userId;

    private StateEnum currentState;

    private StateEnum previousState;
/*

    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "current_city_lat", referencedColumnName = "lat"),
            @JoinColumn(name = "current_city_lon", referencedColumnName = "lon") })
    @Cascade({CascadeType.PERSIST,CascadeType.MERGE})

 */
    private CityData currentCity;
/*
    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "notification_city_lat", referencedColumnName = "lat"),
            @JoinColumn(name = "notification_city_lon", referencedColumnName = "lon") })
    @Cascade({CascadeType.PERSIST,CascadeType.REFRESH})

 */
    private CityData notificationsCity;
/*
    @ManyToMany(fetch = FetchType.EAGER)
    @Cascade({CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.DELETE_ORPHAN})
    @JoinTable(name="last_three_cities",joinColumns = @JoinColumn(name="user_id",referencedColumnName = "user_id")
    ,inverseJoinColumns = {@JoinColumn(name="last_three_cities_lat",referencedColumnName = "lat")
            ,@JoinColumn(name="last_three_cities_lon",referencedColumnName = "lon")})

 */
    @Builder.Default
    private List<CityData> cities =new ArrayList<>();

    public Optional<CityData>getCurrentCity(){
        return Optional.ofNullable(this.currentCity);
    }
    public Optional<CityData>getNotificationsCity(){
        return Optional.ofNullable(this.notificationsCity);
    }

    public static Mono<User> fromRows(List<Map<String,Object>> rows){
        return Mono.just(User.builder()
                .userId(Long.parseLong(rows.get(0).get("user_id").toString()))
                .currentState((StateEnum) rows.get(0).get("current_state"))
                .previousState((StateEnum) rows.get(0).get("previous_state"))
                .currentCity(CityData.fromRow(rows.get(0)))
                .notificationsCity(CityData.fromRow(rows.get(0)))
                .notificationTime((LocalTime) rows.get(0).get("notification_time"))
                .notificationDays((int[]) rows.get(0).get("notification_days"))
                .isNotif((Boolean) rows.get(0).get("isNotif"))
                .cities(rows.stream()
                        .map(CityData::fromRow)
                        .filter(Objects::nonNull)
                        .toList())
                .build());
    }

    @Column("notification_time")
    private LocalTime notificationTime;

    @Column("notification_days")
    private int[]notificationDays=new int[7];

    @Column("isNotif")
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
        this.setNotificationsCity(null);
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
       if(cities.contains(city)){
           return;
        }
            Stack<CityData> stack = new Stack<>();
            for (CityData item : cities) {
                if (item != null) {
                    stack.add(item);
                }
            }
            stack.add(city);
            cities.clear();
            for (int i = 0; i < 3; i++) {
                if (!stack.isEmpty() && stack.peek() != null) {
                    cities.add(stack.pop());
                }
            }
        }
    @Override
    public boolean equals(Object o) {
         if (o instanceof User user) {
             return user.getUserId() == this.getUserId();
         }
         return false;
        }
    @Override
    public int hashCode(){
        return 31;
    }






}

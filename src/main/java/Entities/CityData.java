package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
//@EqualsAndHashCode
@Embeddable
@IdClass(CityId.class)
@Table(name="city_data")
public class CityData implements Serializable {

    @JsonProperty("name")
    private String name;
    @JsonProperty("lat")
    @Id
    private double lat;
    @JsonProperty("lon")
    @Id
    private double lon;
    @JsonProperty("country")
    private String country;
    @JsonProperty("state")
    private String state;
    private String timezone;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="last_three_cities",joinColumns = {@JoinColumn(name="last_three_cities_lat",referencedColumnName = "lat")
            ,@JoinColumn(name="last_three_cities_lon",referencedColumnName = "lon")}
    ,inverseJoinColumns = @JoinColumn(name = "user_id",referencedColumnName = "user_id"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","last_three_cities_lat","last_three_cities_lon"}))
    private List<User> users=new ArrayList<>();

    @Override
    public boolean equals(Object o){
        if(o instanceof CityData){
            CityData city=(CityData) o;
            if(city.lat==this.lat&&city.lon==this.lon){
                return true;
            }
        }
        return false;
    }
    @Override
    public int hashCode(){
        return Objects.hashCode(this.name)+Objects.hashCode(this.lat)+Objects.hashCode(this.lon);
    }
}

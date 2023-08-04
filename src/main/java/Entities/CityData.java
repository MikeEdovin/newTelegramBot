package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
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

    @Override
    public boolean equals(Object o){
        if(o instanceof CityData city){
            return city.lat == this.lat && city.lon == this.lon;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return 31;
    }
}

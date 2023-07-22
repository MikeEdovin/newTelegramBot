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
@EqualsAndHashCode
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

    public CityId getCityId(){
        return new CityId(lat,lon);
    }


}

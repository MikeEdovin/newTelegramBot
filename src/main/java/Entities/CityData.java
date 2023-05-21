package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Embeddable
@IdClass(WeatherId.class)
@Table(name="city_data")
public class CityData  {

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

    public WeatherId getWeatherId(){
        return new WeatherId(lat,lon);
    }

}

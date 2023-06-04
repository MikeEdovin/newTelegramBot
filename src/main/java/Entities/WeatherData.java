package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData {
    private double lat;
    private double lon;
    private String timezone;
    private Current current;
    private Daily[] daily;
    public void setLatLon(double lat, double lon){
        this.lat=lat;
        this.lon=lon;
    }


}



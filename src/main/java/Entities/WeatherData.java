package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

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


}



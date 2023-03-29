package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Current {

    @JsonProperty("dt")
    private long currentDt;
    @JsonProperty("sunrise")
    private long currentSunrise;
    @JsonProperty("sunset")
    private long currentSunset;
    private float currentTemp;
    @JsonProperty("feels_like")
    private float currentFeelsLike;
    @JsonProperty("pressure")
    private int currentPressure;
    @JsonProperty("humidity")
    private int currentHumidity;
    @JsonProperty("dew_point")
    private float currentDewPoint;
    @JsonProperty("uvi")
    private float currentUvi;
    @JsonProperty("clouds")
    private int currentClouds;
    private int visibility;
    @JsonProperty("wind_speed")
    private float currentWindSpeed;
    @JsonProperty("wind_degree")
    private int currentWindDegree;
    @JsonProperty("wind_gust")
    private float currentWindGust;
    @JsonProperty("weather")
    private Weather[] currentWeather;



    public String getDate(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yy");
        String date = Instant.ofEpochSecond(this.currentDt).
                atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
        return date;}
    public String getFormattedSunrise(){
        return Instant.ofEpochSecond(this.currentSunrise).
                atZone(ZoneId.systemDefault()).toLocalDateTime().
                format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    }
    public String getFormattedSunset(){
        return Instant.ofEpochSecond(this.currentSunset).
                atZone(ZoneId.systemDefault()).toLocalDateTime().
                format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    }

}

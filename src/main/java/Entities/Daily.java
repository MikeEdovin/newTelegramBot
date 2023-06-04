package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Daily implements Serializable {
    private long dt;
    private long sunrise;
    private long sunset;
    private long moonrise;
    private long moonset;
    @JsonProperty("moon_phase")
    private float moonPhase;
    private Temp temp;
    @JsonProperty("feels_like")
    //@Embedded
    private FeelsLike feelsLike;
    private int pressure;
    private int humidity;
    @JsonProperty("dew_point")
    private float dewPoint;
    @JsonProperty("wind_speed")
    private float windSpeed;
    @JsonProperty("wind_degree")
    private int windDegree;
    @JsonProperty("wind_gust")
    private float windGust;
    private Weather[] weather;
    private int clouds;
    private float pop;
    private float rain;
    private float uvi;

    public String getDate(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yy");
        String date = Instant.ofEpochSecond(this.dt).
                atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
        return date;}
    public String getFormattedSunrise(){
        return Instant.ofEpochSecond(this.sunrise).
                atZone(ZoneId.systemDefault()).toLocalDateTime().
                format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    }
    public String getFormattedSunset(){
        return Instant.ofEpochSecond(this.sunset).
                atZone(ZoneId.systemDefault()).toLocalDateTime().
                format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    }
    public String getWindDirection(){
        if (11.25 < windDegree && windDegree < 33.75) {
            return "NNE";
        }
        if (33.75 < windDegree && windDegree < 56.25) {
            return "NE";
        }
        if (56.25 < windDegree && windDegree < 78.75) {
            return "ENE";
        }
        if (78.75 < windDegree && windDegree < 101.25) {
            return "E";
        }
        if (101.25 < windDegree && windDegree < 123.75) {
            return "ESE";
        }
        if (123.75 < windDegree && windDegree < 146.25) {
            return "SE";
        }
        if (146.25 < windDegree && windDegree < 168.75) {
            return "SSE";
        }
        if (168.75 < windDegree && windDegree < 191.25) {
            return "S";
        }
        if (191.25 < windDegree && windDegree < 213.75) {
            return "SSW";
        }
        if (213.75 < windDegree && windDegree < 236.25) {
            return "SW";
        }
        if (236.25 < windDegree && windDegree < 258.75) {
            return "WSW";
        }
        if (258.75 < windDegree && windDegree < 281.25) {
            return "W";
        }
        if (281.25 < windDegree && windDegree < 303.75) {
            return "WNW";
        }
        if (303.75 < windDegree && windDegree < 326.25) {
            return "NW";
        }
        if (326.25 < windDegree && windDegree < 348.75) {
            return "NNW";
        }
        if (windDegree > 348.75 || windDegree < 11.25) {
            return "N";
        } else {
            return null;
        }
    }

}

package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current {
    @JsonProperty("dt")
    private long currentDt;
    @JsonProperty("sunrise")
    private long currentSunrise;
    @JsonProperty("sunset")
    private long currentSunset;
    @JsonProperty("temp")
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
    @JsonProperty("wind_deg")
    private int currentWindDegree;
    @JsonProperty("wind_gust")
    private float currentWindGust;
    @JsonProperty("weather")
    private Weather[] currentWeather;

    public String getWindDirection() {
        if (11.25 < currentWindDegree && currentWindDegree < 33.75) {
            return "NNE";
        }
        if (33.75 < currentWindDegree && currentWindDegree < 56.25) {
            return "NE";
        }
        if (56.25 < currentWindDegree && currentWindDegree < 78.75) {
            return "ENE";
        }
        if (78.75 < currentWindDegree && currentWindDegree < 101.25) {
            return "E";
        }
        if (101.25 < currentWindDegree && currentWindDegree < 123.75) {
            return "ESE";
        }
        if (123.75 < currentWindDegree && currentWindDegree < 146.25) {
            return "SE";
        }
        if (146.25 < currentWindDegree && currentWindDegree < 168.75) {
            return "SSE";
        }
        if (168.75 < currentWindDegree && currentWindDegree < 191.25) {
            return "S";
        }
        if (191.25 < currentWindDegree && currentWindDegree < 213.75) {
            return "SSW";
        }
        if (213.75 < currentWindDegree && currentWindDegree < 236.25) {
            return "SW";
        }
        if (236.25 < currentWindDegree && currentWindDegree < 258.75) {
            return "WSW";
        }
        if (258.75 < currentWindDegree && currentWindDegree < 281.25) {
            return "W";
        }
        if (281.25 < currentWindDegree && currentWindDegree < 303.75) {
            return "WNW";
        }
        if (303.75 < currentWindDegree && currentWindDegree < 326.25) {
            return "NW";
        }
        if (326.25 < currentWindDegree && currentWindDegree < 348.75) {
            return "NNW";
        }
        if (currentWindDegree > 348.75 || currentWindDegree < 11.25) {
            return "N";
        } else {
            return null;
        }
    }
}

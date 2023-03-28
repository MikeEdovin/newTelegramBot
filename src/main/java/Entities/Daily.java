package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Daily implements Serializable {
    private long dt;
    private long sunrise;
    private long sunset;
    private long moonrise;
    private long moonset;
    @JsonProperty("moon_phase")
    private float moonPhase;
    @Embedded
    private Temp temp;
    @JsonProperty("feels_like")
    @Embedded
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

}

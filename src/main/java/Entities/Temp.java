package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)

public class Temp  {
    private long tempId;
    @JsonProperty("day")
    private float dayTemp;
    @JsonProperty("min")
    private float minTemp;
    @JsonProperty("max")
    private float maxTemp;
    @JsonProperty("night")
    private float nightTemp;
    @JsonProperty("eve")
    private float eveTemp;
    @JsonProperty("morn")
    private float mornTemp;



}

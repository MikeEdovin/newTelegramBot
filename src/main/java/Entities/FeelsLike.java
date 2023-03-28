package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class FeelsLike  {
    private long id;
    @JsonProperty("day")
    private float dayFeelsLike;
    @JsonProperty("night")
    private float nightFeelsLike;
    @JsonProperty("eve")
    private float eveFeelsLike;
    @JsonProperty("morn")
    private float mornFeelsLike;

}

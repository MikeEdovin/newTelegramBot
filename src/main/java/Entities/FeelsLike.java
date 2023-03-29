package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

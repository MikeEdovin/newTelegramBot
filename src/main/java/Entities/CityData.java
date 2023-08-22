package Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Getter
//@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
//@Entity
//@IdClass(CityId.class)
@Table(name="city_data")
@Data
public class CityData implements Serializable {
    @Id
    long Id;

    @JsonProperty("name")
    private String name;
    @JsonProperty("lat")
    //@Id
    private double lat;
    @JsonProperty("lon")
    //@Id
    private double lon;
    @JsonProperty("country")
    private String country;
    @JsonProperty("state")
    private String state;
    private String timezone;

    public static CityData fromRow(Map<String,Object> row){
        if(row.get("id")!=null){
            return CityData.builder()
                    .Id(Long.parseLong(row.get("id").toString()))
                    .name(row.get("name").toString())
                    .lat(Float.parseFloat(row.get("lat").toString()))
                    .lon(Float.parseFloat(row.get("lon").toString()))
                    .country(row.get("country").toString())
                    .state(row.get("state").toString())
                    .timezone(row.get("timezone").toString())
                    .build();
        }
        return null;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof CityData city){
            return city.lat == this.lat && city.lon == this.lon;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return 31;
    }
}

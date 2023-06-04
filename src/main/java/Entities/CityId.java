package Entities;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class CityId implements Serializable {
    private double lat,lon;

    public CityId(double lat, double lon){
        this.lat=lat;
        this.lon=lon;
    }
    public CityId(){}

    @Override
    public boolean equals(Object o){
        if(!(o instanceof CityId)){
            return false;
        }
        else{
            CityId id=(CityId) o;
            return this.lat==id.lat&&this.lon==id.lon;
        }
    }
    @Override
    public int hashCode(){
        int hash=17;
        hash= 31 * hash + Double.hashCode(lat);
        hash=31*hash+Double.hashCode(lon);
        return hash;
    }
}

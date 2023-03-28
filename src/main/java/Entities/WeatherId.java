package Entities;

import lombok.Getter;

import java.io.Serializable;
@Getter
public class WeatherId implements Serializable {
    private double lat,lon;

    public WeatherId(double lat,double lon){
        this.lat=lat;
        this.lon=lon;
    }
    public WeatherId(){}

    @Override
    public boolean equals(Object o){
        if(!(o instanceof WeatherId)){
            return false;
        }
        else{
            WeatherId id=(WeatherId) o;
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

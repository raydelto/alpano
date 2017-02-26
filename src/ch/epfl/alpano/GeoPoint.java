package ch.epfl.alpano;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;
import static java.lang.Math.atan2;
import static java.lang.Math.PI;
import static ch.epfl.alpano.Math2.haversin;
import java.util.Locale;

public final class GeoPoint {
    
    private double longitude;
    private double latitude;
    
    public GeoPoint(double longitude, double latitude){
        
        this.longitude = toRad(longitude);
        this.latitude = toRad(latitude);
        
        if(!(this.longitude >= -Math.PI && this.longitude <= Math.PI && this.latitude >= -Math.PI/2 && this.latitude <=Math.PI/2)){
            throw new IllegalArgumentException();
        }
        
    }
    
    public double longitude(){
        return longitude;
    }
    
    public double latitude(){
        return latitude;
    }
    
    public double distanceTo(GeoPoint that){
        double angle = 2*(asin(sqrt(haversin(this.latitude()-that.latitude())+cos(this.latitude())*cos(that.latitude())*haversin(this.longitude()-that.longitude()))));
        return Distance.toMeters(angle);
    }
    
    //Retourne en degrés ou en radians ?
    public double azimuthTo(GeoPoint that){
        
        double angle = atan2((sin(this.longitude()-that.longitude())*cos(that.latitude())),(cos(this.latitude())*sin(that.latitude()))-sin(this.latitude())*cos(that.latitude())*cos(this.longitude()-that.longitude()));
        return toDeg(Azimuth.fromMath(Azimuth.canonicalize(angle)));
    }
    
    public String toString(){
        Locale l = null;
        String s = String.format(l, "(%.4f,%.4f)", toDeg(this.longitude()), toDeg(this.latitude()));
        return s;
    }
    
    public double toDeg(double angleInRad){
        return (angleInRad*180/PI);
    }
    
    public double toRad(double angleInDeg){
        return (angleInDeg*PI/180);
    }

}

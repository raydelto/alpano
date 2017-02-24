package ch.epfl.alpano;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;
import static java.lang.Math.atan2;
import static ch.epfl.alpano.Math2.haversin;


public final class GeoPoint {
    
    private double longitude;
    private double latitude;
    
    public GeoPoint(double longitude, double latitude){
        if(!(longitude >= -Math.PI && longitude <= Math.PI && latitude >= -Math.PI/2 && latitude <=Math.PI/2)){
            throw new IllegalArgumentException();
        }
        
        this.longitude = longitude;
        this.latitude = latitude;
    }
    
    public double longitude(){
        return longitude;
    }
    
    public double latitude(){
        return latitude;
    }
    
    public double distanceTo(GeoPoint that){
        double angle = 2*asin(sqrt(haversin(this.latitude()-that.latitude())+cos(this.latitude())*cos(that.latitude())*haversin(this.longitude()-that.longitude())));
        return Distance.toMeters(angle);
    }
    
    public double azimuthTo(GeoPoint that){
        
        double angle = atan2((sin(this.longitude()-that.longitude())*cos(that.latitude())),(cos(this.latitude())*sin(that.latitude())-sin(this.latitude())*cos(that.latitude())*cos(this.longitude()-that.longitude())));
        return Azimuth.fromMath(Azimuth.canonicalize(angle));
    }
    
    public String toString(){
        
    }

}

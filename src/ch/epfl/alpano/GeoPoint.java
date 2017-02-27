package ch.epfl.alpano;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;
import static java.lang.Math.atan2;
import static java.lang.Math.PI;
import static ch.epfl.alpano.Math2.haversin;
import java.util.Locale;
import static java.lang.Math.toDegrees;


public final class GeoPoint {
    
    private double longitude;
    private double latitude;
    
    //Radians 
    /**
     * Creates a GeoPoint
     * @param longitude, the longitude of the point in radians
     * @param latitude, the latitude of the point in radians
     */
    public GeoPoint(double longitude, double latitude){
           
        if(!(longitude >= -PI && longitude <= PI && latitude >= -PI/2 && latitude <=PI/2)){
            throw new IllegalArgumentException();
        }
        
        this.longitude = longitude;
        this.latitude = latitude;
        
    }
    
    /**
     * 
     * @return the longitude of the point in radians
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * 
     * @return the latitude of the point in radians
     */
    public double latitude(){
        return latitude;
    }
    
    //Précision ?
    /**
     * This method calculates a distance between two geopoints
     * @param that, a geopoint
     * @return the distance between the two geopoints, in meters
     */
    public double distanceTo(GeoPoint that){
        double angle = 2*(asin(sqrt(haversin(this.latitude()-that.latitude())+cos(this.latitude())*cos(that.latitude())*haversin(this.longitude()-that.longitude()))));
        return Distance.toMeters(angle);
    }
    
    /**
     * Calculate the azimuth between two geopoints
     * @param that, a geopoint
     * @return the azimuth between the two geopoints, in radians
     */
    public double azimuthTo(GeoPoint that){
        
        double angle = atan2((sin(this.longitude()-that.longitude())*cos(that.latitude())),(cos(this.latitude())*sin(that.latitude()))-sin(this.latitude())*cos(that.latitude())*cos(this.longitude()-that.longitude()));
        return Azimuth.fromMath(Azimuth.canonicalize(angle));
    }
    
    /**
     * Redefinition of the method toString from Object
     * @return a String that indicates the longitude and the latitude of the geopoint in degrees
     */
    @Override
    public String toString(){
        Locale l = null;
        String s = String.format(l, "(%.4f,%.4f)", toDegrees(this.longitude()), toDegrees(this.latitude()));
        return s;
    }
    
   
}

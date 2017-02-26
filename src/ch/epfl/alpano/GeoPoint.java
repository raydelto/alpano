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
    
    //Radians or degree ?
    /**
     * Creates a GeoPoint
     * @param longitude, the longitude of the point in degrees
     * @param latitude, the latitude of the point in degrees
     */
    public GeoPoint(double longitude, double latitude){
        
        this.longitude = toRad(longitude);
        this.latitude = toRad(latitude);
        
        if(!(this.longitude >= -Math.PI && this.longitude <= Math.PI && this.latitude >= -Math.PI/2 && this.latitude <=Math.PI/2)){
            throw new IllegalArgumentException();
        }
        
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
    
    //Retourne en degrés ou en radians ?
    /**
     * Calculate the azimuth between two geopoints
     * @param that, a geopoint
     * @return the azimuth between the two geopoints, in degrees
     */
    public double azimuthTo(GeoPoint that){
        
        double angle = atan2((sin(this.longitude()-that.longitude())*cos(that.latitude())),(cos(this.latitude())*sin(that.latitude()))-sin(this.latitude())*cos(that.latitude())*cos(this.longitude()-that.longitude()));
        return toDeg(Azimuth.fromMath(Azimuth.canonicalize(angle)));
    }
    
    /**
     * Redefinition of the method toString89 from Object
     * @return a String that indicates the longitude and the latitude of the geopoint in degrees
     */
    public String toString(){
        Locale l = null;
        String s = String.format(l, "(%.4f,%.4f)", toDeg(this.longitude()), toDeg(this.latitude()));
        return s;
    }
    
    /**
     * Converts an angle from radians to degrees
     * @param angleInRad, the angle in radians to be converted
     * @return the angle in degree
     */
    public double toDeg(double angleInRad){
        return (angleInRad*180/PI);
    }
    
    /**
     * Converts an angle from degrees to radians
     * @param angleInDeg, the angle in degrees to be converted
     * @return the angle in radians
     */
    public double toRad(double angleInDeg){
        return (angleInDeg*PI/180);
    }

}

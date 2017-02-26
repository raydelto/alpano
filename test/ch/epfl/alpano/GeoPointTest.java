package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;
import static java.lang.Math.PI;

public class GeoPointTest {

    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionForLongitude() {
        GeoPoint g = new GeoPoint(-360, 180);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionForLatitude() {
        GeoPoint g = new GeoPoint(-180, 180);
    }
    
    @Test
    public void longitudeWorks(){
        GeoPoint g = new GeoPoint(-180, 90);
        assertEquals(-PI, g.longitude(), 0);
    }
    
    @Test
    public void latitudeWorks(){
        GeoPoint g = new GeoPoint(-180, 90);
        assertEquals(PI/2, g.latitude(), 0);
    }
    
    @Test
    public void distanceToWorks(){
        GeoPoint lausanne = new GeoPoint(6.631, 46.521);
        GeoPoint moscow = new GeoPoint(37.623, 55.753);
        GeoPoint vevey = new GeoPoint(6.85, 46.45);
        
        assertEquals(2370000, lausanne.distanceTo(moscow), 3000);
        assertEquals(18000, lausanne.distanceTo(vevey), 1000);
    }
    
    @Test
    public void azimuthToWorks(){
        GeoPoint lausanne = new GeoPoint(6.631, 46.521);
        GeoPoint moscow = new GeoPoint(37.623, 55.753);
        
        assertEquals(52.95, lausanne.azimuthTo(moscow), 0.1);
        
    }
    
   
    

}

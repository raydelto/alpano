package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;
import static java.lang.Math.PI;
import static ch.epfl.alpano.Math2.toRad;
import static ch.epfl.alpano.Math2.toDeg;

public class GeoPointTest {

    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionForLongitude() {
        GeoPoint g = new GeoPoint(-2*PI, PI);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsExceptionForLatitude() {
        GeoPoint g = new GeoPoint(-PI, PI);
    }
    
    @Test
    public void longitudeWorks(){
        GeoPoint g = new GeoPoint(-PI, PI/2);
        assertEquals(-PI, g.longitude(), 0);
    }
    
    @Test
    public void latitudeWorks(){
        GeoPoint g = new GeoPoint(-PI, PI/2);
        assertEquals(PI/2, g.latitude(), 0);
    }
    
    @Test
    public void distanceToWorks(){
        GeoPoint lausanne = new GeoPoint(toRad(6.631), toRad(46.521));
        GeoPoint moscow = new GeoPoint(toRad(37.623), toRad(55.753));
        GeoPoint vevey = new GeoPoint(toRad(6.85), toRad(46.45));
        
        assertEquals(2370000, lausanne.distanceTo(moscow), 3000);
        assertEquals(18000, lausanne.distanceTo(vevey), 1000);
    }
    
    @Test
    public void azimuthToWorks(){
        GeoPoint lausanne = new GeoPoint(toRad(6.631), toRad(46.521));
        GeoPoint moscow = new GeoPoint(toRad(37.623), toRad(55.753));
        
        assertEquals(toRad(52.95), lausanne.azimuthTo(moscow), 0.1);
        
    }
    
    @Test
    public void toStringWorks(){
        GeoPoint lausanne = new GeoPoint(toRad(6.631), toRad(46.521));
        String s = "(6.6310,46.5210)";
        assertTrue(s.equals(lausanne.toString()));
        
    }
    
   
    

}

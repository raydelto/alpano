package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;
import static java.lang.Math.PI;

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
        GeoPoint lausanne = new GeoPoint(Math.toRadians(6.631), Math.toRadians(46.521));
        GeoPoint moscow = new GeoPoint(Math.toRadians(37.623), Math.toRadians(55.753));
        GeoPoint vevey = new GeoPoint(Math.toRadians(6.85), Math.toRadians(46.45));
        
        assertEquals(2370000, lausanne.distanceTo(moscow), 3000);
        assertEquals(18000, lausanne.distanceTo(vevey), 1000);
    }
    
    @Test
    public void azimuthToWorks(){
        GeoPoint lausanne = new GeoPoint(Math.toRadians(6.631), Math.toRadians(46.521));
        GeoPoint moscow = new GeoPoint(Math.toRadians(37.623), Math.toRadians(55.753));
        
        assertEquals(Math.toRadians(52.95), lausanne.azimuthTo(moscow), 1e-3);
        
    }
    
    @Test
    public void toStringWorks(){
        GeoPoint lausanne = new GeoPoint(Math.toRadians(6.631), Math.toRadians(46.521));
        String s = "(6.6310,46.5210)";
        assertTrue(s.equals(lausanne.toString()));
        
    }
    
   
    

}
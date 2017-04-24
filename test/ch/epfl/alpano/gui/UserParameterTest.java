package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserParameterTest {

    @Test
    public void longitudeTest() {
        assertEquals(60000, UserParameter.OBSERVER_LONGITUDE.sanitize(50000), 0);
        assertEquals(60000, UserParameter.OBSERVER_LONGITUDE.sanitize(60000), 0);
        assertEquals(80000, UserParameter.OBSERVER_LONGITUDE.sanitize(80000), 0);
        assertEquals(120000, UserParameter.OBSERVER_LONGITUDE.sanitize(120000), 0);
        assertEquals(120000, UserParameter.OBSERVER_LONGITUDE.sanitize(130000), 0);
    }

    @Test
    public void latitudeTest(){
        assertEquals(450000, UserParameter.OBSERVER_LATITUDE.sanitize(150000), 0);
        assertEquals(450000, UserParameter.OBSERVER_LATITUDE.sanitize(450000), 0);
        assertEquals(470000, UserParameter.OBSERVER_LATITUDE.sanitize(470000), 0);
        assertEquals(480000, UserParameter.OBSERVER_LATITUDE.sanitize(480000), 0);
        assertEquals(480000, UserParameter.OBSERVER_LATITUDE.sanitize(500000), 0);
    }
    
    @Test
    public void elevationTest(){
        assertEquals(300, UserParameter.OBSERVER_ELEVATION.sanitize(102), 0);
        assertEquals(300, UserParameter.OBSERVER_ELEVATION.sanitize(300), 0);
        assertEquals(1051, UserParameter.OBSERVER_ELEVATION.sanitize(1051), 0);
        assertEquals(10_000, UserParameter.OBSERVER_ELEVATION.sanitize(10_000), 0);
        assertEquals(10_000, UserParameter.OBSERVER_ELEVATION.sanitize(15_000), 0);
    }
    
    @Test
    public void azimuthTest(){
        assertEquals(0, UserParameter.CENTER_AZIMUTH.sanitize(-1), 0);
        assertEquals(0, UserParameter.CENTER_AZIMUTH.sanitize(0), 0);
        assertEquals(202, UserParameter.CENTER_AZIMUTH.sanitize(202), 0);
        assertEquals(359, UserParameter.CENTER_AZIMUTH.sanitize(359), 0);
        assertEquals(359, UserParameter.CENTER_AZIMUTH.sanitize(360), 0);
    }
    
    @Test
    public void horizontalFieldOfViewTest(){
        assertEquals(1, UserParameter.HORIZONTAL_FIELD_OF_VIEW.sanitize(0), 0);
        assertEquals(1, UserParameter.HORIZONTAL_FIELD_OF_VIEW.sanitize(1), 0);
        assertEquals(202, UserParameter.HORIZONTAL_FIELD_OF_VIEW.sanitize(202), 0);
        assertEquals(360, UserParameter.HORIZONTAL_FIELD_OF_VIEW.sanitize(360), 0);
        assertEquals(360, UserParameter.HORIZONTAL_FIELD_OF_VIEW.sanitize(361), 0);
    }
    
    @Test
    public void maxDistanceTest(){
        assertEquals(10, UserParameter.MAX_DISTANCE.sanitize(5), 0);
        assertEquals(10, UserParameter.MAX_DISTANCE.sanitize(10), 0);
        assertEquals(562, UserParameter.MAX_DISTANCE.sanitize(562), 0);
        assertEquals(600, UserParameter.MAX_DISTANCE.sanitize(600), 0);
        assertEquals(600, UserParameter.MAX_DISTANCE.sanitize(601), 0);
    }
    
    @Test
    public void widthTest(){
        assertEquals(30, UserParameter.WIDTH.sanitize(15), 0);
        assertEquals(30, UserParameter.WIDTH.sanitize(30), 0);
        assertEquals(562, UserParameter.WIDTH.sanitize(562), 0);
        assertEquals(16000, UserParameter.WIDTH.sanitize(16000), 0);
        assertEquals(16000, UserParameter.WIDTH.sanitize(16001), 0);
    }
   
    @Test
    public void heightTest(){
        assertEquals(10, UserParameter.HEIGHT.sanitize(5), 0);
        assertEquals(10, UserParameter.HEIGHT.sanitize(10), 0);
        assertEquals(562, UserParameter.HEIGHT.sanitize(562), 0);
        assertEquals(4000, UserParameter.HEIGHT.sanitize(4000), 0);
        assertEquals(4000, UserParameter.HEIGHT.sanitize(4001), 0);
    }
    
    @Test
    public void superSamplingExponentTest(){
        assertEquals(0, UserParameter.SUPER_SAMPLING_EXPONENT.sanitize(-1), 0);
        assertEquals(0, UserParameter.SUPER_SAMPLING_EXPONENT.sanitize(0), 0);
        assertEquals(1, UserParameter.SUPER_SAMPLING_EXPONENT.sanitize(1), 0);
        assertEquals(2, UserParameter.SUPER_SAMPLING_EXPONENT.sanitize(2), 0);
        assertEquals(2, UserParameter.SUPER_SAMPLING_EXPONENT.sanitize(3), 0);
    }
    
    
}

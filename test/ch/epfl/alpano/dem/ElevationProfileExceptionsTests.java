package ch.epfl.alpano.dem;

import static java.lang.Math.toRadians;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;

public class ElevationProfileExceptionsTests {

    final static File HGT_FILE = new File("N46E006.hgt");
    final static double AZIMUTH = toRadians(27.97);
    final static double LONGITUDE = toRadians(6.15432);
    final static double LATITUDE = toRadians(46.20562);
    
    @Test (expected = IllegalArgumentException.class)
    public void elevationAtThrowsException() throws Exception{
        DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE);
              ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
              GeoPoint o = new GeoPoint(LONGITUDE, LATITUDE);
              ElevationProfile p = new ElevationProfile(cDEM, o, AZIMUTH, 5000);
              p.elevationAt(5001);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void positionAtThrowsException() throws Exception {
        DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        GeoPoint o = new GeoPoint(LONGITUDE, LATITUDE);
        ElevationProfile p = new ElevationProfile(cDEM, o, AZIMUTH, 200000);
        p.elevationAt(200000.1);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void slopeAtThrowsException() throws Exception{
        DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(HGT_FILE);
        ContinuousElevationModel cDEM = new ContinuousElevationModel(dDEM);
        GeoPoint o = new GeoPoint(LONGITUDE, LATITUDE);
        ElevationProfile p = new ElevationProfile(cDEM, o, AZIMUTH, 10);
        p.elevationAt(200);
    }

}

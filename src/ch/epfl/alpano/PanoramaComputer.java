package ch.epfl.alpano;

import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;

public final class PanoramaComputer {
    private final ContinuousElevationModel dem;
    public PanoramaComputer(ContinuousElevationModel dem)
    {
        this.dem=Objects.requireNonNull(dem);
    }
    public Panorama computePanorama(PanoramaParameters parameters)
    {
          
        //kontrollo comment te firstUntervalContainingRoot
        double lowerBound=Math2.firstIntervalContainingRoot(rayToGroundDistance(ep, parameters.observerElevation(), parameters.altitudeForY(parameters.height()-1)), 0, parameters.maxDistance(), 64);
        if(lowerBound==Double.POSITIVE_INFINITY);//what should we do?;
       double intersectionWithGroundx= Math2.improveRoot(rayToGroundDistance(ep, parameters.observerElevation(), parameters.altitudeForY(parameters.height()-1)), lowerBound, lowerBound+64, 4);
        return new Panorama.Builder(parameters).build();
    }
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope)
    {
        return  x -> ray0+x*raySlope-profile.elevationAt(x)+(0.87*x*x)/(2*ray0);//ok?
    }
    

}

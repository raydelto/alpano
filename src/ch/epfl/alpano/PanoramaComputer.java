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
        double tempx=0;
        double heighttemp=parameters.height();
        double raySlopeTemp=parameters.altitudeForY(parameters.height()-1);
        DoubleUnaryOperator rayTemp = (xInMeters) -> {return parameters.observerElevation()+xInMeters*raySlopeTemp;};
        heighttemp=rayTemp.applyAsDouble(tempx);
        double disttoGround=rayToGroundDistance(ep, rayTemp, raySlopeTemp).applyAsDouble(tempx);
        Math2.firstIntervalContainingRoot(rayToGroundDistance(ep, rayTemp, raySlopeTemp), 0, parameters.maxDistance(), 64);
        return new Panorama.Builder(parameters).build();
    }
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope)
    {
        return  x -> ray0+x*raySlope-profile.elevationAt(x)+(0.87*x*x)/(2*ray0);//ok?
    }
    

}

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
        return new Panorama.Builder(parameters).build();
    }
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope)
    {
        return  x -> ray0+x*raySlope-profile.elevationAt(x)+(0.87*x*x)/(2*ray0);//ok?
    }

}

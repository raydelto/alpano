/**
 * PanoramaComputer
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */

package ch.epfl.alpano;

import ch.epfl.alpano.Panorama.Builder;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import static ch.epfl.alpano.Math2.sq;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static ch.epfl.alpano.Math2.improveRoot;
import static ch.epfl.alpano.Distance.EARTH_RADIUS;


import static java.lang.Double.POSITIVE_INFINITY;
import static java.util.Objects.requireNonNull;

import java.util.function.DoubleUnaryOperator;

public final class PanoramaComputer {

    private final ContinuousElevationModel dem;
    private final static double K = 0.13;
    private final static int ROOTSTEP=64;
    private final static int RAYSTEP=4;
    private final static double CALCULATIONCONSTANT=(1 - K) / (2 * EARTH_RADIUS);

    /**
     * Creates a PanoramaComputer
     * @param dem the ContinuousElevationModel used to build the PanoramaComputer (non null)
     * @throws NullPointerException if dem is null
     */
    public PanoramaComputer(ContinuousElevationModel dem) {
        
        this.dem = requireNonNull(dem,"null ContinuousElevationModel");
    }

    /**
     * Computes the panorama from the given parameters
     * @param parameters the parameters used in the calculation of the panorama
     * @return the computed panorama
     */
    public Panorama computePanorama(PanoramaParameters parameters) {
        
        double currentAzimuth = parameters.azimuthForX(0);
        Builder builder = new Builder(parameters);

        int posX = 0;

        while (posX <= (parameters.width() - 1)) {
            currentAzimuth = parameters.azimuthForX(posX);
            ElevationProfile ep = new ElevationProfile(dem, parameters.observerPosition(), currentAzimuth, parameters.maxDistance());
            double intersectionWithGroundx = 0;
            double intersectionWithGroundTemp = 0;
            int posY = parameters.height() - 1;

            while (posY >= 0) {

                DoubleUnaryOperator op = rayToGroundDistance(ep, parameters.observerElevation(),Math.tan(parameters.altitudeForY(posY)));
                double lowerBound = firstIntervalContainingRoot(op, intersectionWithGroundTemp,parameters.maxDistance(), ROOTSTEP);

                if (lowerBound == POSITIVE_INFINITY) {
                    break;
                }

                else {
                    intersectionWithGroundx = improveRoot(op, lowerBound, lowerBound + ROOTSTEP, RAYSTEP);
                    intersectionWithGroundTemp = intersectionWithGroundx;
                    double distance = intersectionWithGroundx / Math.cos(parameters.altitudeForY(posY));

                    builder.setDistanceAt(posX, posY, (float) (distance));
                    builder.setLongitudeAt(posX, posY, (float) ep.positionAt(intersectionWithGroundx).longitude());
                    builder.setLatitudeAt(posX, posY, (float) ep.positionAt(intersectionWithGroundx).latitude());
                    builder.setElevationAt(posX, posY, (float) (ep.elevationAt(intersectionWithGroundx)));
                    builder.setSlopeAt(posX, posY, (float) ep.slopeAt(intersectionWithGroundx));
                    
                }

                --posY;
            }

            posX++;
        }

        return builder.build();
    }

    /**
     * Represents the function of the distance from ground of an elevation profile 
     * @param profile the ElevationProfile used in the calculation
     * @param ray0 the initial ray launched for the function
     * @param raySlope the slope of the ray
     * @return return the DoubleUnaryOperator that represents the function of the distance from ground of an elevation profile 
     */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope) {

        return x -> (ray0 + x * raySlope - profile.elevationAt(x) + d(x));
    }

    /**
     * Calculates the "real life" distance 
     * @param x the horizontal distance 
     * @return the distance calculated using the formula of "real life" distance 
     */
    private static double d(double x) {
        
        return CALCULATIONCONSTANT * sq(x);
    }
}

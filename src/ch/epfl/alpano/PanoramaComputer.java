package ch.epfl.alpano;

import ch.epfl.alpano.Panorama.Builder;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import static ch.epfl.alpano.Math2.sq;
import static ch.epfl.alpano.Azimuth.canonicalize;


import static java.util.Objects.requireNonNull;

import java.util.function.DoubleUnaryOperator;

public final class PanoramaComputer {
    
    private final ContinuousElevationModel dem;
    private final static double EARTH_RADIUS = 6371000;
    private final static double K = 0.13;
    
    public PanoramaComputer(ContinuousElevationModel dem){
        this.dem = requireNonNull(dem);
    }
    
    public Panorama computePanorama(PanoramaParameters parameters){
        
        double currentAzimuth = parameters.azimuthForX(0);
        Builder builder = new Builder(parameters);
        
        int posX=0;
        
        while(posX <= (parameters.width()-1)){
          currentAzimuth = parameters.azimuthForX(posX);
          ElevationProfile ep = new ElevationProfile(dem, parameters.observerPosition(), currentAzimuth, parameters.maxDistance());
          double intersectionWithGroundx =0;
          int posY =0; 
          while(intersectionWithGroundx <=parameters.maxDistance()){
              
              
              double lowerBound=Math2.firstIntervalContainingRoot(rayToGroundDistance(ep, parameters.observerElevation(), parameters.altitudeForY(parameters.height()-1)), 0, parameters.maxDistance(), 64);
              
              if(lowerBound==Double.POSITIVE_INFINITY){
                  intersectionWithGroundx = lowerBound;
              }
              
              else{
                  intersectionWithGroundx= Math2.improveRoot(rayToGroundDistance(ep, parameters.observerElevation(), parameters.altitudeForY(parameters.height()-1)), lowerBound, lowerBound+64, 4);
                  builder.setDistanceAt(posX, posY, (float)intersectionWithGroundx);
                  builder.setLongitudeAt(posX, posY, (float)ep.positionAt(intersectionWithGroundx).longitude());
                  builder.setLatitudeAt(posX, posY, (float)ep.positionAt(intersectionWithGroundx).latitude());
                  builder.setElevationAt(posX, posY, (float)(ep.elevationAt(intersectionWithGroundx) - d(intersectionWithGroundx)));
                  builder.setSlopeAt(posX, posY, (float)ep.slopeAt(intersectionWithGroundx));
              }
          }
   
          
          
          posX ++;
         
        }
        
        return builder.build();
    }
    
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope){
        
        return x -> (ray0 + x*raySlope - profile.elevationAt(x) + d(x));
    }
    
    private static double d(double x){
        return ((1-K)/2*EARTH_RADIUS)*sq(x);
    }

}

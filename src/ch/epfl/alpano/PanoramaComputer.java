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
          double intersectionWithGroundTemp=0;
          int posY =parameters.height()-1; 
          
          while(posY >=0){
              
              DoubleUnaryOperator op = rayToGroundDistance(ep, parameters.observerElevation(), Math.tan(parameters.altitudeForY(posY)));
              double lowerBound=Math2.firstIntervalContainingRoot(op, intersectionWithGroundTemp, parameters.maxDistance(), 64);
              
              
              if(lowerBound==Double.POSITIVE_INFINITY){
                 break;
              }
              
              else{
                  intersectionWithGroundx= Math2.improveRoot(op, lowerBound, lowerBound+64, 4);
                 
                  intersectionWithGroundTemp=intersectionWithGroundx;
                
                  double distance = intersectionWithGroundx/Math.cos(parameters.altitudeForY(posY));
                  
                  
                  builder.setDistanceAt(posX, posY, (float)(distance));
                  //System.out.println("posX : "+posX+", posY : "+posY+", distance : "+(float)(Math.cos(parameters.altitudeForY(posY))*intersectionWithGroundx));
                  builder.setLongitudeAt(posX, posY, (float)ep.positionAt(intersectionWithGroundx).longitude());
                  //System.out.println(" Longitude : "+(float)ep.positionAt(intersectionWithGroundx).longitude());
                  builder.setLatitudeAt(posX, posY, (float)ep.positionAt(intersectionWithGroundx).latitude());
                  //System.out.println(" Latitude : "+(float)ep.positionAt(intersectionWithGroundx).latitude());
                  builder.setElevationAt(posX, posY, (float)(ep.elevationAt(intersectionWithGroundx)));
                  //System.out.println(" Elevation : "+(float)(ep.elevationAt(intersectionWithGroundx)));
                  builder.setSlopeAt(posX, posY, (float)ep.slopeAt(intersectionWithGroundx));
                  //System.out.println(" Slope : " +(float)ep.slopeAt(intersectionWithGroundx));
              }
              
             
              --posY;          
              
          }
   
          
          
          posX ++;
         
        }
        
        return builder.build();
    }
    
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope){
        
        return x -> (ray0 + x*raySlope - profile.elevationAt(x) + d(x));
    }
    
    private static double d(double x){
        return ((1-K)/(2*EARTH_RADIUS))*sq(x);
    }

}

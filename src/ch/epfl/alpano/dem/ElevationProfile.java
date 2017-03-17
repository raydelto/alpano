package ch.epfl.alpano.dem;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Preconditions;
import static ch.epfl.alpano.Azimuth.isCanonical;
import static ch.epfl.alpano.Azimuth.toMath;
import static ch.epfl.alpano.Distance.toRadians;
import static ch.epfl.alpano.Distance.toMeters;
import static java.lang.Math.asin;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.PI;
import static ch.epfl.alpano.Math2.floorMod;
import static ch.epfl.alpano.Math2.PI2;
import static ch.epfl.alpano.Math2.lerp;

import java.util.ArrayList;

public final class ElevationProfile {
    
    private final ContinuousElevationModel elevationModel;
    private final GeoPoint origin;
    private final double azimuth;
    private final double length;
    private final GeoPoint[] table;
    private final int STEP = 4096;
    
    public ElevationProfile(ContinuousElevationModel elevationModel, GeoPoint origin, double azimuth, double length){
        Preconditions.checkArgument(isCanonical(azimuth));
        if(length<=0){
            throw new IllegalArgumentException();
        }
        if(elevationModel == null || origin == null){
            throw new NullPointerException();
        }
        
        this.elevationModel = elevationModel;
        this.origin = origin;
        this.azimuth = azimuth;
        this.length = length;
        
        table = new GeoPoint[(int)(Math.ceil(length/STEP)+1)];
        double phi0 = origin.latitude();
        double lambda0 = origin.longitude();
        double direction = toMath(azimuth);
        
        for(int i=0; i<table.length; i++){
            
            double x = toRadians(STEP*i);
            double phi = asin(sin(phi0)*cos(x)+cos(phi0)*sin(x)*cos(direction));
            double lambda = (floorMod((lambda0-asin((sin(direction)*sin(x))/cos(phi))+ PI), PI2) - PI);
          
            table[i] = new GeoPoint(lambda, phi);
          
        }
    }
    
    public double elevationAt(double x){
        isInBounds(x);
        
        return elevationModel.elevationAt(positionAt(x));
    }
    
    public GeoPoint positionAt(double x){
        isInBounds(x);
        int lowerBound=0;
        int upperBound=0;
                
        lowerBound = (int)Math.floor(x/4096);
        upperBound = lowerBound +1;
        
        double longitude = lerp(table[lowerBound].longitude(), table[upperBound].longitude(), x/STEP-lowerBound);
        double latitude= lerp(table[lowerBound].latitude(), table[upperBound].latitude(), x/STEP-lowerBound);
        
        GeoPoint p = new GeoPoint(longitude, latitude);
       System.out.println(p);
        return p;        
    }
    
    public double slopeAt(double x){
        isInBounds(x);
        return elevationModel.slopeAt(positionAt(x));
    }
    
    private void isInBounds(double x){
        if(!(x>=0 && x<= length)){
            throw new IllegalArgumentException();
        }
    }

}

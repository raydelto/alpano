/**
 * ContinuousElevationModel
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.dem;
import static ch.epfl.alpano.Math2.bilerp;
import static ch.epfl.alpano.dem.DiscreteElevationModel.SAMPLES_PER_RADIAN;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import static ch.epfl.alpano.Math2.sq;
import static java.util.Objects.requireNonNull;



import ch.epfl.alpano.GeoPoint;


import static ch.epfl.alpano.Distance.toMeters;

public final class ContinuousElevationModel {
    
    private final DiscreteElevationModel dem;
    private final static double d = toMeters(1/SAMPLES_PER_RADIAN);
    
    /**
     * Constructs a new continuous dem based on a discrete dem
     * @param dem the dem
     */
    public ContinuousElevationModel(DiscreteElevationModel dem){
        
        this.dem = requireNonNull(dem,"null DiscreteElevationModel");        
    }
    
    /**
     * Calculates the elevation of a geopoint by doing a blininear interpolation of the extent of the dem given to the constructor
     * @param p, the geopoint 
     * @return the elevation of the geopoint p, in meters
     */
    public double elevationAt(GeoPoint p){
        double indexLongitude = DiscreteElevationModel.sampleIndex(p.longitude());
        double indexLatitude = DiscreteElevationModel.sampleIndex(p.latitude());
        int x = ((int)Math.floor(indexLongitude));
        int y = ((int)Math.floor(indexLatitude));
                
        return bilerp(checkSample(x, y), checkSample(x+1, y), checkSample(x, y+1), checkSample(x+1, y+1), indexLongitude-x, indexLatitude-y);
    }
    
    
    /**
     * Calculate the slope at the given geopoint
     * @param p, the geopoint
     * @return the slope at the geopoint p, in radians
     */
    public double slopeAt(GeoPoint p){
        
        double indexLongitude = DiscreteElevationModel.sampleIndex(p.longitude());
        double indexLatitude = DiscreteElevationModel.sampleIndex(p.latitude());
        int x = ((int)Math.floor(indexLongitude));
        int y = ((int)Math.floor(indexLatitude));
        
        return bilerp(doTheta(x, y), doTheta(x+1, y), doTheta(x, y+1), doTheta(x+1, y+1), indexLongitude-x, indexLatitude-y);
          
    }
    
    /**
     * Calculates the slope at index (x, y)
     * @param x, index
     * @param y, index
     * @return the slope in radians
     */
    private double doTheta(int x, int y){
        double za = checkSample(x+1, y)-checkSample(x, y);
        double zb = checkSample(x, y+1)-checkSample(x, y);
        
        return acos(d/(sqrt(sq(za)+sq(zb)+sq(d))));
    }
    
    /**
     * Checks that the index (x, y) is is contained in the dem given to the constructor
     * @param x, index
     * @param y, index
     * @return the elevation at index(x, y) if contained in the dem, 0 otherwise
     */
    private double checkSample(int x, int y){
      
        if(dem.extent().contains(x, y)){
            return dem.elevationSample(x, y);
        }
        else{
            return 0;
        }
        
        
        
    }

}

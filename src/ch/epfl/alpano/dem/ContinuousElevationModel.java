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
    
    private DiscreteElevationModel dem;
    private static double d = toMeters(1/SAMPLES_PER_RADIAN);
    
    
    public ContinuousElevationModel(DiscreteElevationModel dem){
        
        
        this.dem = requireNonNull(dem);
        
    }
    
    public double elevationAt(GeoPoint p){
        double indexLongitude = DiscreteElevationModel.sampleIndex(p.longitude());
        double indexLatitude = DiscreteElevationModel.sampleIndex(p.latitude());
        int x = ((int)Math.floor(indexLongitude));
        int y = ((int)Math.floor(indexLatitude));
                
        return bilerp(checkSample(x, y), checkSample(x+1, y), checkSample(x, y+1), checkSample(x+1, y+1), indexLongitude-x, indexLatitude-y);
    }
    
    public double slopeAt(GeoPoint p){
        
        double indexLongitude = DiscreteElevationModel.sampleIndex(p.longitude());
        double indexLatitude = DiscreteElevationModel.sampleIndex(p.latitude());
        int x = ((int)Math.floor(indexLongitude));
        int y = ((int)Math.floor(indexLatitude));
        
        return bilerp(doTheta(x, y), doTheta(x+1, y), doTheta(x, y+1), doTheta(x+1, y+1), indexLongitude-x, indexLatitude-y);
          
    }
    
    private double doTheta(int x, int y){
        double za = checkSample(x+1, y)-checkSample(x, y);
        double zb = checkSample(x, y+1)-checkSample(x, y);
        
        return acos(d/(sqrt(sq(za)+sq(zb)+sq(d))));
    }
    
    private double checkSample(int x, int y){
        try{
            return dem.elevationSample(x, y);
                        
        }catch(IllegalArgumentException e){
            return 0;
        }
    }

}

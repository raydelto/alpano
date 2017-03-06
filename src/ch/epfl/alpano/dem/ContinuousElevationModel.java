package ch.epfl.alpano.dem;
import static ch.epfl.alpano.Math2.bilerp;
import static ch.epfl.alpano.dem.DiscreteElevationModel.SAMPLES_PER_RADIAN;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;
import static ch.epfl.alpano.Math2.sq;



import ch.epfl.alpano.GeoPoint;


import static ch.epfl.alpano.Distance.toMeters;

public final class ContinuousElevationModel {
    
    private DiscreteElevationModel dem;
    private static double d = toMeters(1/SAMPLES_PER_RADIAN);
    
    
    public ContinuousElevationModel(DiscreteElevationModel dem){
        if(dem == null){
            throw new NullPointerException();
        }
        
        this.dem = dem;
        
    }
    
    public double elevationAt(GeoPoint p){
        double indexLongitude = DiscreteElevationModel.sampleIndex(p.longitude());
        double indexLatitude = DiscreteElevationModel.sampleIndex(p.latitude());
        int x = ((int)Math.floor(indexLongitude));
        int y = ((int)Math.floor(indexLatitude));
        
        return bilerp(dem.elevationSample(x, y), dem.elevationSample(x+1, y), dem.elevationSample(x, y+1), dem.elevationSample(x+1, y+1), indexLongitude, indexLatitude);
    }
    
    public double slopeAt(GeoPoint p){
        
        int x = ((int)Math.floor(DiscreteElevationModel.sampleIndex(p.longitude())));
        int y = ((int)Math.floor(DiscreteElevationModel.sampleIndex(p.latitude())));
        
        double za = dem.elevationSample(x+1, y)-dem.elevationSample(x, y);
        double zb = dem.elevationSample(x, y+1)-dem.elevationSample(x, y);
        
        
        return acos(d/(sqrt(sq(za)+sq(zb)+sq(d))));
        
          
    }

}

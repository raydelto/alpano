package ch.epfl.alpano.dem;
import static java.lang.Math.PI;

import ch.epfl.alpano.Interval2D;

public interface DiscreteElevationModel extends AutoCloseable{
    
    static final int SAMPLES_PER_DEGREE = 3600;
    static final double SAMPLES_PER_RADIAN = 648000/PI;
    
    /**
     * Calculates the index of the given angle in radians
     * @param angle, in radians
     * @return the index of the given angle 
     */
    static double sampleIndex(double angle){
        return angle*SAMPLES_PER_RADIAN;
    }

    /**
     * Calculates and return the area of the MNT
     * @return an interval2D that corresponds to the area of the MNT
     */
    abstract Interval2D extent();
    /**
     * 
     * @param x
     * @param y
     * @return the elevation sample in meters
     */
    abstract double elevationSample(int x, int y);
    
    /**
     * 
     * @param that
     * @return
     */
    default DiscreteElevationModel union(DiscreteElevationModel that){
        if(!(this.extent().isUnionableWith(that.extent()))){
            throw new IllegalArgumentException();
        }
        return new CompositeDiscreteElevationModel(this, that);
        
    }
}

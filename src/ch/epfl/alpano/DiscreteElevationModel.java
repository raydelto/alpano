package ch.epfl.alpano;
import static java.lang.Math.PI;

public interface DiscreteElevationModel extends AutoCloseable{
    
    static final int SAMPLES_PER_DEGREE = 3600;
    static final double SAMPLES_PER_RADIAN = 648000/PI;
    
    static double sampleIndex(double angle){
        return angle*SAMPLES_PER_RADIAN;
    }

    abstract Interval2D extent();
    abstract double elevationSample(int x, int y);
    
    default DiscreteElevationModel union(DiscreteElevationModel that){
        if(!(this.extent().isUnionableWith(that.extent()))){
            throw new IllegalArgumentException();
        }
        return new CompositeDiscreteElevationModel(this, that);
        
    }
}

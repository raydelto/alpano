package ch.epfl.alpano.dem;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import static java.util.Objects.requireNonNull;

public final class CompositeDiscreteElevationModel implements DiscreteElevationModel
{
    private DiscreteElevationModel dem1,dem2;
    CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2)
    {
        
        this.dem1=requireNonNull(dem1);
        this.dem2=requireNonNull(dem2);
        
    }

    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
        
    }

    @Override
    public Interval2D extent() {
        Interval2D bidim=dem1.extent().union(dem2.extent());
       return new Interval2D(new Interval1D(bidim.iX().includedFrom()*SAMPLES_PER_DEGREE, bidim.iX().includedTo()*SAMPLES_PER_DEGREE),
               new Interval1D(bidim.iY().includedFrom()*SAMPLES_PER_DEGREE,  bidim.iY().includedTo()*SAMPLES_PER_DEGREE));
    }

    @Override
    public double elevationSample(int x, int y) {
       if(dem1.extent().iX().contains(x)&&dem1.extent().iY().contains(y))return dem1.elevationSample(x, y);
       if(dem2.extent().iX().contains(x)&&dem2.extent().iY().contains(y))return dem2.elevationSample(x, y);
       return 0;//???
       
    }
    

}

package ch.epfl.alpano.dem;


import ch.epfl.alpano.Interval2D;
import static java.util.Objects.requireNonNull;

public final class CompositeDiscreteElevationModel implements DiscreteElevationModel
{
    private DiscreteElevationModel dem1,dem2;
    private Interval2D ext;
    
    /**
     *  Creates a new  CompositeDiscreteElevationModel from the union of two DiscreteElevationModels
     * @param dem1 the first DiscreteElevationModel, non null
     * @param dem2 the second DiscreteElevationModel, non null
     */
    CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2)
    {
        
        this.dem1=requireNonNull(dem1);
        this.dem2=requireNonNull(dem2);
        ext=dem1.extent().union(dem2.extent());
        
    }

    @Override
      public void close() throws Exception {
        dem1.close();
        dem2.close();
        
    }

    @Override
    public Interval2D extent() {
       
       return ext;
    }

    @Override
    public double elevationSample(int x, int y) {
       if(dem1.extent().iX().contains(x)&&dem1.extent().iY().contains(y))return dem1.elevationSample(x, y);
       if(dem2.extent().iX().contains(x)&&dem2.extent().iY().contains(y))return dem2.elevationSample(x, y);
       throw new IllegalArgumentException();
       

    }
    

}

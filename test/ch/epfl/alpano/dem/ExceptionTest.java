package ch.epfl.alpano.dem;


import org.junit.Test;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;

public class ExceptionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testUnion() throws Exception {
        DiscreteElevationModel dem1 = new WavyDEM(new Interval2D(new Interval1D(0, 6),new Interval1D(0,6)));
        DiscreteElevationModel dem2 = new WavyDEM(new Interval2D(new Interval1D(0, 6),new Interval1D(18,30)));
        
        dem1.union(dem2);
        dem1.close();
        dem2.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testElevationSampleComposite() throws Exception {
        DiscreteElevationModel dem1 = new WavyDEM(new Interval2D(new Interval1D(0, 6),new Interval1D(0,6)));
        DiscreteElevationModel dem2 = new WavyDEM(new Interval2D(new Interval1D(0, 6),new Interval1D(18,30)));
        
        DiscreteElevationModel demComp = dem1.union(dem2);
        
        demComp.elevationSample(18, 15);
        
        dem1.close();
     }
    
    @Test(expected = NullPointerException.class)
    public void testComposite1() throws Exception {
        CompositeDiscreteElevationModel dem1 = new CompositeDiscreteElevationModel(null,new WavyDEM(new Interval2D(new Interval1D(0, 6),new Interval1D(0,6))));
        
        dem1.close();
     }

    @Test(expected = NullPointerException.class)
    public void testComposite2() throws Exception {
        CompositeDiscreteElevationModel dem1 = new CompositeDiscreteElevationModel(new WavyDEM(new Interval2D(new Interval1D(0, 6),new Interval1D(0,6))),null);
        
        dem1.close();
    }
    
    @Test(expected = NullPointerException.class)
    public void testContinuous() throws Exception {
        new ContinuousElevationModel(null);
    }
}

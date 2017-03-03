package ch.epfl.alpano;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AzimuthTest.class, AzimuthTestJM.class, DistanceTest.class,
        DistanceTestJM.class, GeoPointTest.class, GeoPointTestBaPh.class,
        GeoPointTestSuperficialJM.class, Interval1DTest.class,
        Interval1DTestBaPh.class, Interval1DTestJM.class, Interval2DTest.class,
        Interval2DTestBaPh.class, Interval2DTestJM.class, Math2Test.class,
        PreconditionsTest.class, PreconditionsTestJM.class })
public class AllTests {

}

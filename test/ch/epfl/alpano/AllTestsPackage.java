package ch.epfl.alpano;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GeoPointTest.class, Interval1DTest.class, Interval2DTest.class,
        Math2Test.class, PanoramaComputerTest.class,
        PanoramaParametersTest.class, PanoramaTest.class })
public class AllTestsPackage {

}

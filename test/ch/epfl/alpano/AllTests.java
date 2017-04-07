package ch.epfl.alpano;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ch.epfl.alpano.dem.AllTestsDEM;
import ch.epfl.alpano.summit.AllTestsSummit;

@RunWith(Suite.class)
@SuiteClasses({AllTestsPackage.class, AllTestsDEM.class, AllTestsSummit.class})
public class AllTests {

}

package ch.epfl.alpano;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

public class HgtTest {

    @Test(expected = IllegalArgumentException.class)
    public void FileDoesntExist()  {
        new HgtDiscreteElevationModel(new File("C:\\Users\\gsula\\Desktop\\N57E006.hgt"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongSuffix()  {
        new HgtDiscreteElevationModel(new File("C:\\Users\\gsula\\Desktop\\N45E008.txt"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongFirstLetter()  {
        new HgtDiscreteElevationModel(new File("C:\\Users\\gsula\\Desktop\\M45E008.hgt"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void wrongSeccondLetter()  {
        new HgtDiscreteElevationModel(new File("C:\\Users\\gsula\\Desktop\\N45R008.hgt"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void wrongLatitude()  {
        new HgtDiscreteElevationModel(new File("C:\\Users\\gsula\\Desktop\\N99E008.hgt"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void wrongLongitude()  {
        new HgtDiscreteElevationModel(new File("C:\\Users\\gsula\\Desktop\\N45E200.hgt"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void wrongLength()  {
        new HgtDiscreteElevationModel(new File("C:\\Users\\gsula\\Desktop\\N45E0064.hgt"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void wrongSize()  {
        new HgtDiscreteElevationModel(new File("C:\\Users\\gsula\\Desktop\\N45E009.hgt"));
    }
    @Test(expected = IllegalArgumentException.class)
    public void outOfExtent1()  {
        HgtDiscreteElevationModel a=new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        a.elevationSample(0,46*3600  );
    }
    @Test(expected = IllegalArgumentException.class)
    public void outOfExtent2()  {
        HgtDiscreteElevationModel a=new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        a.elevationSample(99*3600, 6*3600  );
    }
    @Test
    public void outOfExtent3()  {
        HgtDiscreteElevationModel a=new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        a.elevationSample( 6*3600, 46*3600 );
    }
    @Test
    public void outOfExtent4()  {
        HgtDiscreteElevationModel a=new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        a.elevationSample( 7*3600, 46*3600 );
    }
    @Test(expected = IllegalArgumentException.class)
    public void outOfExtent5()  {
        HgtDiscreteElevationModel a=new HgtDiscreteElevationModel(new File("N46E006.hgt"));
        a.elevationSample( 7*3600+1, 46*3600 );
    }

}

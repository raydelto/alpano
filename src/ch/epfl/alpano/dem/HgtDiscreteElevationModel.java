/**
 * HgtDiscreteElevationModel
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

public final class HgtDiscreteElevationModel implements DiscreteElevationModel {
    
    static final int SAMPLES_PER_DEGREE_PLUS_ONE = 3601;
    
    private final Interval2D ext;
    private final int latitudeIndex, longitudeIndex;
    private ShortBuffer buffer;

    /**
     * Creates a HgtDiscreteElevationModel from the source file
     * 
     * @param file, the source file
     * @throws FileNotFoundException, if there are problems with the source file
     * @throws IOException, if there are problems with the source file
     * @throws IllegalArgumentException, if there are problems with the source file
     */
    public HgtDiscreteElevationModel(File file) {
        
        try (FileInputStream stream = new FileInputStream(file)) {
            Preconditions.checkArgument(this.checkName(file),"Problems with file name");
            long l = file.length();
            buffer = stream.getChannel().map(MapMode.READ_ONLY, 0, l).asShortBuffer();
        } catch (SecurityException | IOException e) {
            throw new IllegalArgumentException("Invalid file provided.");
        }
        
         String name = file.getName();
         int latitude= Integer.parseInt(name.substring(1, 3)), longitude = Integer.parseInt(name.substring(4, 7));
         char horizontal= name.charAt(3), vertical= name.charAt(0);

        if (horizontal == 'W')
            latitudeIndex = -latitude * SAMPLES_PER_DEGREE;
        else
            latitudeIndex = latitude * SAMPLES_PER_DEGREE;

        if (vertical == 'S')
            longitudeIndex = -longitude * SAMPLES_PER_DEGREE;
        else
            longitudeIndex = longitude * SAMPLES_PER_DEGREE;

        ext = new Interval2D(new Interval1D(longitudeIndex, longitudeIndex + SAMPLES_PER_DEGREE),
                new Interval1D(latitudeIndex, latitudeIndex + SAMPLES_PER_DEGREE));
    }

    /**
     * Checks if the file complies with the prerequisites
     * @param file, the file to be checked
     * @return true, only if the file complies with all prerequisites
     */
    private boolean checkName(File file) {
        
        String name = file.getName();
        if (name.length() != 11)// check length
            
            return false;

        char verticalTemp = name.charAt(0);
        if (!(verticalTemp == 'N' || verticalTemp == 'S'))// check first letter
            
            return false;

        int latitudeTemp;
        try {
            latitudeTemp = Integer.parseInt(name.substring(1, 3));// check latitude
        } catch (NumberFormatException e) {
            
            return false;
        } catch (Exception e) {
            
            return false;
        }
        if (verticalTemp == 'N'&&!(latitudeTemp >= 0 && latitudeTemp <= 89))//if we include 90 that means that the latitude will include until 91°, and we consider this wrong
            
            return false;
        if (verticalTemp == 'S'&&!(latitudeTemp >= 0 && latitudeTemp <= 90))
           
            return false;

       char horizontalTemp = name.charAt(3);
        if (!(horizontalTemp == 'W' || horizontalTemp == 'E'))
            
            return false;// check second letter

        int longitudeTemp;
        try {
            longitudeTemp = Integer.parseInt(name.substring(4, 7));// check longitude
        } catch (NumberFormatException e) {
           
            return false;
        } catch (Exception e) {
            
            return false;
        }
        if (horizontalTemp == 'W'&&!(longitudeTemp >= 0 && longitudeTemp <= 180))
            
            return false;
        if (horizontalTemp == 'E'&&!(longitudeTemp >= 0 && longitudeTemp <= 179))//if we include 180 that means that the longitude will include until 181°, and we consider this wrong
            
            return false;
        if (!name.substring(7, 11).equals(".hgt"))//check postfix
            
            return false;

        if (file.length() != 25934402)//check file length
            
            return false;

        return true;

    }

    @Override
    public void close(){
        
        buffer = null;
    }

    @Override
    public Interval2D extent() {
        
        return ext;

    }

    @Override
    public double elevationSample(int x, int y) {
        
        Preconditions.checkArgument((this.extent().contains(x, y)),"The extent doesn't contain the sample");

        int deltaX = Math.abs(x - longitudeIndex);
        int deltaY = Math.abs(y - latitudeIndex);
        int position = (SAMPLES_PER_DEGREE_PLUS_ONE * (SAMPLES_PER_DEGREE - deltaY)) + deltaX;
        
        return buffer.get(position);

    }

}

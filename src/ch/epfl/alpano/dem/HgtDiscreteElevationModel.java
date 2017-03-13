package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.security.KeyStore.PrivateKeyEntry;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

public final class HgtDiscreteElevationModel implements DiscreteElevationModel
{
    
    private Interval2D ext;
    private int latitude, longitude;
    private char horizontal, vertical;
    private int latitudeRad, longitudeRad;
    private final FileInputStream stream;
    private ShortBuffer buffer;
    public HgtDiscreteElevationModel(File file) throws FileNotFoundException, IOException
    {
        try{
            Preconditions.checkArgument(this.checkName(file));
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException();
        }
        long l = file.length();
        try  {
            stream = new FileInputStream(file);
           buffer = stream.getChannel()
            .map(MapMode.READ_ONLY, 0, l)
            .asShortBuffer();}
        catch(Exception e)
        {
            throw new IllegalArgumentException();//Po?, nqs po vej precondit.check
        }
       
        
        
        if(horizontal=='W') latitudeRad=-latitude*SAMPLES_PER_DEGREE;//ok konvertimi?
        else latitudeRad=latitude*SAMPLES_PER_DEGREE;
       
        if(vertical=='S') longitudeRad=-longitude*SAMPLES_PER_DEGREE;
        else longitudeRad=longitude*SAMPLES_PER_DEGREE;
       
       
        ext=new Interval2D(new Interval1D(longitudeRad, longitudeRad+SAMPLES_PER_DEGREE+1), new Interval1D(latitudeRad, latitudeRad+SAMPLES_PER_DEGREE+1));
        
        
        
    }
    /**
     *  Checks if the file complies with the prerequisites
     * @param file the file to be checked
     * @return true only if the file complies with all prerequisites
     */
    private boolean checkName(File file)
    {
        String name= file.getName();
        vertical=name.charAt(0);
        if(!(vertical=='N'||vertical=='S'))return false;
        if(name.length()!=11)return false;
        
        
        try{
            latitude=Integer.parseInt(name.substring(1, 3));
        }catch (NumberFormatException e) {
            return false;
        }
        catch (Exception e) {
            return false;
        }
        if(!(latitude>=0&&latitude<=90))return false;
        
        horizontal=name.charAt(3);
        if(!(horizontal=='W'||horizontal=='E'))return false;
        
        
        try{
            longitude=Integer.parseInt(name.substring(4, 7));
        }catch (NumberFormatException e) {
            return false;
        }
        catch (Exception e) {
            return false;
        }
        if(!(longitude>=0&&longitude<=180))return false;
        if(!name.substring(7, 11).equals(".hgt"))return false;
        
        if(file.length()!=25934402)return false;
        
        return true;
        
    }

    @Override
    public void close() throws Exception {
        
        buffer=null;
        stream.close();
    }

    @Override
    public Interval2D extent() {
       return ext;
       
    }

    @Override
    public double elevationSample(int x, int y) {
        if(this.extent().iX().contains(x)&&this.extent().iY().contains(y))//duhet?
            {
            int deltaX=x-longitudeRad;
            int deltaY=y-latitudeRad;
            int position=(3601*(3601-Math.abs(deltaY))-3601)+Math.abs(deltaX);//+1?
            return buffer.get(position);
            
            
            }
        else
        throw new IllegalArgumentException();
    }

}

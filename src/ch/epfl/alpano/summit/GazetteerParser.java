package ch.epfl.alpano.summit;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.dem.DiscreteElevationModel;

public class GazetteerParser {
    private GazetteerParser(){};
   
    /**
     * Extracts the information from a file to a list of Summits
     * @param file the source file
     * @return a list with all the information extracted from the file
     * @throws IOException if there are problems with the file, or if it is not formatted the correct way
     */
     public static List<Summit> readSummitsFrom(File file) throws IOException
    {
        ArrayList<Summit> tab=new ArrayList<Summit>();
        String name,longitude,latitude,elevation;
        BufferedReader br;
        try()
        {
            br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));//change it to try with resource
        }catch (Exception e) {
           throw new IOException();
        }
        if(file.length()==0)throw new IOException();
        String line= br.readLine();
        while(line!=null)
        {
            StringTokenizer token =new StringTokenizer(line);
            
            try{
                longitude=token.nextToken();
                
                latitude=token.nextToken();
                System.out.println(latitude);
                 elevation=token.nextToken();
               
                for(int i=0;i<3;i++)
                token.nextToken();
                name=token.nextToken("/n").trim();
             
                tab.add(new Summit(name, new GeoPoint(extractDegrees(longitude,"long"), extractDegrees(latitude,"lat")), Integer.parseInt(elevation)));
            }
            catch (Exception e) {
               
              throw new IOException();
            }   
           
            line=br.readLine();
        }
        br.close();
        return Collections.unmodifiableList(tab);
        
    }
     
     /**
      * extracts the information from a deg:min:sec format and returns the result in degrees
      * @param degree the raw deg:min:sec format string
      * @param direction 'long' for longitude and 'lat' for latitude
      * @return the result in degrees
      * @throws IOException if the string is not formatted the right way
      */
    private static double extractDegrees(String degree,String direction) throws IOException
    {
       
        IOException exception=new IOException();
      
      
        int counter = 0;
        for( int i=0; i<degree.length(); i++ ) {
            if( degree.charAt(i) == ':' ) 
                counter++;
            } 
     
        if(counter!=2)throw exception;
        
        String[] hms = degree.split(":");
        int deg, min, sec;
        try
        {
            deg=Integer.parseInt(hms[0]);
         
            if(direction.equals("long")&&!(deg>-180&&deg<180))throw exception;
            if(direction.equals("lat")&&!(deg>-90&&deg<90))throw exception;
           
            min=Integer.parseInt(hms[1]);
            if(!(min>=0&&min<=59))throw exception;
         
            sec=Integer.parseInt(hms[2]);
            if(!(sec>=0&&sec<=59))throw exception;
            
            if(direction.equals("lat")){
            System.out.println("g"+deg);
            System.out.println("g"+min);
            System.out.println("g"+sec);}
            

            
        }
        catch (Exception e) {
           throw exception;
        }

        if(hms[0].trim().charAt(0)=='-') return -(((deg*60)+min)*60+sec)/DiscreteElevationModel.SAMPLES_PER_RADIAN;
        return  (((deg)*60+min)*60+sec)/DiscreteElevationModel.SAMPLES_PER_RADIAN;
    }
   

}

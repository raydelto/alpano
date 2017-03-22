package ch.epfl.alpano.summit;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.dem.DiscreteElevationModel;

public class GazetteerParser {
    public GazetteerParser(){};//hiqe public!!!
    //public?
     public List<Summit> readSummitsFrom(File file) throws IOException
    {
        ArrayList<Summit> tab=new ArrayList<Summit>();
        String name,longitude,latitude,elevation;
        BufferedReader br;
        try{
           // br=new BufferedReader(new FileReader(file));
            br=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        }catch (Exception e) {
           throw new IOException();
        }
        if(file.length()==0)throw new IOException();
        String line= br.readLine();//IOException automatically throwed
        while(line!=null)
        {
            StringTokenizer token =new StringTokenizer(line);
            
            try{
                longitude=token.nextToken();
                // System.out.println(longitude);
                latitude=token.nextToken();
                //System.out.println(latitude);
                 elevation=token.nextToken();
                //System.out.println(elevation);
                for(int i=0;i<3;i++)
                token.nextToken();
                name=token.nextToken("/n").trim();
              //System.out.println(name);
               
                // System.out.println("in---------------------in");
                // System.out.println(extractDegrees(longitude)+","+ extractDegrees(latitude));
                tab.add(new Summit(name, new GeoPoint(extractDegrees(longitude,"long"), extractDegrees(latitude,"lat")), Integer.parseInt(elevation)));
            }
            catch (Exception e) {
                //System.out.println(e.getClass());
              throw new IOException();
            }   
            //duhet trajtu rasti nqs ka akoma gjona mas name te .txt file?
            line=br.readLine();
        }
        return Collections.unmodifiableList(tab);
        
    }
    private double extractDegrees(String degree,String direction) throws IOException
    {
        //System.out.println("-------------------------");
        IOException exception=new IOException();
      
       // if (degree.length()!=8) throw exception;
       // if(degree.charAt(2)!=':'||degree.charAt(5)!=':')throw exception;
        int counter = 0;
        for( int i=0; i<degree.length(); i++ ) {
            if( degree.charAt(i) == ':' ) 
                counter++;
            } 
       // System.out.println(counter);
        if(counter!=2)throw exception;
        
        String[] hms = degree.split(":");
        int deg, min, sec;
        try
        {
            deg=Integer.parseInt(hms[0]);
            System.out.println(deg);
            if(direction.equals("long")&&!(deg>-180&&deg<180))throw exception;
            if(direction.equals("lat")&&!(deg>-90&&deg<90))throw exception;
           
            min=Integer.parseInt(hms[1]);
            if(!(min>=0&&min<=59))throw exception;
            // System.out.println(min);
            sec=Integer.parseInt(hms[2]);
            if(!(sec>=0&&sec<=59))throw exception;
            
            // System.out.println(sec);
            
        }
        catch (Exception e) {
           throw exception;
        }
        //System.out.println("out------------------out");
        return indexToRadians(((deg*60)+min)*60+sec);
    }
    
    private double indexToRadians(double index)
    {
        return index*(1/DiscreteElevationModel.SAMPLES_PER_RADIAN);
    }

}

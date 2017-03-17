package ch.epfl.alpano.summit;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import ch.epfl.alpano.GeoPoint;

public class GazetteerParser {
    private GazetteerParser(){};
    //public?
    List<Summit> readSummitsFrom(File file) throws IOException
    {
        ArrayList<Summit> tab=new ArrayList<Summit>();
        String name,longitude,latitude,elevation;
        BufferedReader br;
        try{
            br=new BufferedReader(new FileReader(file));
        }catch (Exception e) {
           throw new IOException();
        }
        String line= br.readLine();//IOException automatically throwed
        while(line!=null)
        {
            StringTokenizer token =new StringTokenizer(line);
            
            try{
                longitude=token.nextToken();
                latitude=token.nextToken();
                elevation=token.nextToken();
                for(int i=0;i<3;i++)
                token.nextToken();
                name=token.nextToken();
                tab.add(new Summit(name, new GeoPoint(extractDegrees(longitude), extractDegrees(latitude)), Integer.parseInt(elevation)));
            }
            catch (Exception e) {
              throw new IOException();
            }   
            //duhet trajtu rasti nqs ka akoma gjona mas name te .txt file?
            line=br.readLine();
        }
        return Collections.unmodifiableList(tab);
        
    }
    private double extractDegrees(String degree) throws IOException
    {
        IOException exception=new IOException();
        if (degree.length()!=8) throw exception;
        if(degree.charAt(2)!=':'||degree.charAt(5)!=':')throw exception;
        int deg, min, sec;
        try
        {
            deg=Integer.parseInt(degree.substring(0, 2));
            min=Integer.parseInt(degree.substring(3, 5));
            sec=Integer.parseInt(degree.substring(6, 8));
            
        }
        catch (Exception e) {
           throw exception;
        }
        return ((deg*60)+min)*60+sec;
    }

}

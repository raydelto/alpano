package ch.epfl.alpano.gui;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.ls.LSInput;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public final class Labelizer {
    private final ContinuousElevationModel cev;
    private final List <Summit> summitList;
    public Labelizer(ContinuousElevationModel cev, List <Summit> summitList) {
        this.cev=cev;
        this.summitList=summitList;
    }
    public List<Node> labels(PanoramaParameters parameters)
    {
        List<Summit> visible=listOfVisibleSummit(parameters);
        
                
      
    }
    private boolean isVisible(PanoramaParameters p, Summit s)
    {
        double distX= s.position().distanceTo(p.observerPosition());
        //double arealDist=Math.sqrt(distX^distX+Math.pow(p.observerElevation()-s.elevation(), 2));
        if(distX>p.maxDistance())return false;
        
        double dH= distX*Math.tan(p.verticalFieldOfView()/2);
        if(dH+p.observerElevation()<s.elevation() || p.observerElevation()-dH>s.elevation()) return false;
        
        double dAzimuth=p.observerPosition().azimuthTo(s.position());
        if(dAzimuth>Azimuth.canonicalize(p.centerAzimuth()+p.horizontalFieldOfView()/2)||dAzimuth<Azimuth.canonicalize(p.centerAzimuth()-p.horizontalFieldOfView()/2))
        return false;
        
        
        
        
        
    }
    private List<Summit> listOfVisibleSummit ( PanoramaParameters parameters)
    {
        List<Summit> visible=new LinkedList<>();
       
       for( Summit s: summitList ) 
       {
           if(isVisible(parameters, s)){
               visible.add(s);
           }
       }
       return visible;
    }
    private List<Node> positionLabels(List<Summit> summitList,PanoramaParameters parameters)
    {
        for( Summit s: summitList ) 
        {
            if(parameters.yForAltitude(Math.toRadians(Math.atan((s.elevation()-parameters.observerElevation())/s.position().distanceTo(parameters.observerPosition()))))>=170)
                    {
                
                    }
        }
    }
    
    class visibleSummit
    {
        
    }

}

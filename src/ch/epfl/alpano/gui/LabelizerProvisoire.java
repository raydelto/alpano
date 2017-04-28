package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import org.w3c.dom.ls.LSInput;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public final class LabelizerProvisoire {
    private final ContinuousElevationModel cev;
    private final List <Summit> summitList;
    
    public LabelizerProvisoire(ContinuousElevationModel cev, List <Summit> summitList) {
        this.cev=cev;
        this.summitList=summitList;
    }
    public List<Node> labels(PanoramaParameters parameters)
    {
        List<Summit> visible=listOfVisibleSummit(parameters);
        return null;
                
      
    }
    
    private List<Summit> listOfVisibleSummit(PanoramaParameters p){
        List<Summit> visible=new LinkedList<>();
        
        for( Summit s: summitList ) 
        {
            double distX= s.position().distanceTo(p.observerPosition());
            double summitAzimuth=p.observerPosition().azimuthTo(s.position());
            double deltaAzimuth = angularDistance(p.centerAzimuth(), summitAzimuth);
            ElevationProfile ep = new ElevationProfile(cev, p.observerPosition(), summitAzimuth, p.maxDistance());
            double distY= (PanoramaComputer.rayToGroundDistance(ep, p.observerElevation(),  0)).applyAsDouble(distX);    // ??? distX ???     
            double summitAngle = Math.atan2(distY, distX);
            
            if(distX<=p.maxDistance()){
                if(summitAngle<=p.verticalFieldOfView()/2.0 && summitAngle>=p.verticalFieldOfView()/2.0){  
                    if(Math.abs(deltaAzimuth)<=p.horizontalFieldOfView()/2.0){                        
                        DoubleUnaryOperator op = PanoramaComputer.rayToGroundDistance(ep, p.observerElevation(), distY/distX);
                        double intersection = firstIntervalContainingRoot(op, 0, p.maxDistance(), 64);
                        if(intersection > distX-200){
                            visible.add(s);
                        }   
                        
                    }    
                }
            }               
        }
        
        return visible;
    }
}


package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;

import java.util.BitSet;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;


public final class Labelizer {
    private final ContinuousElevationModel cev;
    private final List <Summit> summitList;
    public Labelizer(ContinuousElevationModel cev, List <Summit> summitList) {
        this.cev=cev;
        this.summitList=summitList;
    }
    public List<Node> labels(PanoramaParameters parameters)
    {
        List<visibleSummit> visible=listOfVisibleSummit(parameters);
        Collections.sort(visible);
        return this.positionLabels(visible, parameters);
        
    }
   
    private List<visibleSummit> listOfVisibleSummit(PanoramaParameters p){
        List<visibleSummit> visible=new LinkedList<>();
        
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
                            
                            visible.add(new visibleSummit(s, (int)Math.round(p.xForAzimuth(summitAngle)), (int)Math.round(p.yForAltitude(summitAngle))));//possible mistake in the arguments here
                            //shif se te enonce e ka von AzimuthForX jo anasjelltas
                        }   
                        
                    }    
                }
            }               
        }
        
        return visible;
    }
    
    private List<Node> positionLabels(List<visibleSummit> summitList,PanoramaParameters parameters)
    {
        int printingY=-1;
        BitSet labelizable=new BitSet(parameters.width());
        List<Node> nList=new LinkedList<>();
        for( visibleSummit s: summitList ) 
        {
            if(s.yPixel<170)continue;
            if(s.xPixel<20||s.xPixel>parameters.width()-20)continue;
            if(labelizable.nextSetBit(s.xPixel)<s.xPixel+20)continue;
            if(printingY==-1)printingY=s.yPixel;
            
            labelizable.set(s.xPixel, s.xPixel+20);
            //nList.add(new Node) for the moment empty, right?
            
            
        }
        return nList;
    }
    
    class visibleSummit implements Comparable<visibleSummit>//can't extend summit
    {
        Summit summit;
        int xPixel, yPixel;
        public visibleSummit(Summit summit, int xPixel, int yPixel) 
        {
            this.summit=summit;
            this.xPixel=xPixel;
            this.yPixel=yPixel;
                
        }
    
        
        @Override
        public int compareTo(visibleSummit other) {
            if(this.yPixel>=other.yPixel) return 1;
            else return -1;
        }
        

    }

}

/**
 * Labelizer
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
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
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;


public final class Labelizer {
    private final ContinuousElevationModel cev;
    private final List <Summit> summitList;
    private final int STEP=64,TOLERANCE=200;
    public Labelizer(ContinuousElevationModel cev, List <Summit> summitList) {
        this.cev=cev;
        this.summitList=summitList;
    }
    /**
     * Checks all the Summits and returns the labels only of the ones that are visible and labelisable 
     * @param parameters the paramteters of the panorama
     * @return  a list of nodes representing the labels of the summits
     */
    public List<Node> labels(PanoramaParameters parameters)
    {
        List<visibleSummit> visible=listOfVisibleSummit(parameters);
        Collections.sort(visible);
        return this.positionLabels(visible, parameters);
        
    }
   
    /**
     * Checks all the summits, and returns only the ones that are visible 
     * @param p the parameters of the panorama 
     * @return a list of visibleSummits, after applying the conditions of a visible summit
     */
    private List<visibleSummit> listOfVisibleSummit(PanoramaParameters p){//maybe change the order for better efficiency?
        List<visibleSummit> visible=new LinkedList<>();
        
        for( Summit s: summitList ) 
        {
            double distX= s.position().distanceTo(p.observerPosition());
            double summitAzimuth=p.observerPosition().azimuthTo(s.position());
            double deltaAzimuth = angularDistance(p.centerAzimuth(), summitAzimuth);
            ElevationProfile ep = new ElevationProfile(cev, p.observerPosition(), summitAzimuth, distX);
           
            
            if(distX<=p.maxDistance()){
                 
                double distY= -(PanoramaComputer.rayToGroundDistance(ep, p.observerElevation(),  0)).applyAsDouble(distX);    
                double summitAngle = Math.atan2(distY, distX);
    
                if(summitAngle<=p.verticalFieldOfView()/2.0 && summitAngle>=(-p.verticalFieldOfView())/2.0){
                   
                    if(Math.abs(deltaAzimuth)<=p.horizontalFieldOfView()/2.0){  
                       
                        DoubleUnaryOperator op = PanoramaComputer.rayToGroundDistance(ep, p.observerElevation(), distY/distX);
                        double intersection = firstIntervalContainingRoot(op, 0, distX, STEP);
                        
                        if(intersection > distX-TOLERANCE){
                            
                            visible.add(new visibleSummit(s, (int)Math.round(p.xForAzimuth(summitAzimuth)), (int)Math.round(p.yForAltitude(summitAngle))));

                        }   
                        
                    }    
                }
            }               
        }
        return visible;
    }
    
    /**
     * Creates labels only for the visible summits that fulfill the conditions of being labelisable
     * @param summitList list of visible Summits
     * @param parameters the parameters of the panorama
     * @return the list of nodes of the labelisable summits
     */
    private List<Node> positionLabels(List<visibleSummit> summitList,PanoramaParameters parameters)
    {
        int printingY=-1;
        BitSet labelizable=new BitSet(parameters.width());
        List<Node> nList=new LinkedList<>();
        for( visibleSummit s: summitList ) 
        {
            
            if(s.yPixel<170)continue;
            
            if(s.xPixel<20||s.xPixel>parameters.width()-20)continue;
            
            int nextsSetBit=labelizable.nextSetBit(s.xPixel);
            if(nextsSetBit!=-1&&nextsSetBit<s.xPixel+20)continue;
            
            if(printingY==-1)printingY=s.yPixel;
           
            
            labelizable.set(s.xPixel, s.xPixel+20);
            Line line = new Line();
            line.setStartX(s.xPixel);
            line.setEndX(s.xPixel);
            line.setStartY(s.yPixel);
            line.setEndY(printingY);
            
            Text txt= new Text(s.summit.name()+" ("+s.summit.elevation()+"m)");
            txt.getTransforms().addAll(new Translate(s.xPixel, printingY), new Rotate(30, 0, 0));
            nList.add(line); 
            
        }
        return nList;
    }
    
    /**
     * Helper class that stores a summit, and it's coordinates in the panorama in pixels
     * 
     * Also implements Comparable so that it makes it easier to sort the visible Summits according to their y coordinate in pixels
     */
    class visibleSummit implements Comparable<visibleSummit>
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
            if(this.yPixel>other.yPixel) return 1;
            else if(this.yPixel<other.yPixel) return -1;
            else {
                if(this.summit.elevation()>other.summit.elevation()){
                    return -1;
                }
                else return 1;
            }
        }
        

    }

}

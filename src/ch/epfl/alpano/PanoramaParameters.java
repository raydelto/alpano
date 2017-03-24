/**
 * PanoramaParameters
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Math2.PI2;

public final class PanoramaParameters {
    
    private final GeoPoint OBSERVERPOSITION;
    private final int OBSERVERELEVATION;
    private final double centerAzimuth;
    private final double HORIZONTALFIELDOFVIEW;
    private final int MAXDISTANCE;
    private final int WIDTH;
    private final int HEIGHT;
    private final double DELTA;
 
    /**
     * 
     * @param observerPosition
     * @param observerElevation
     * @param centerAzimuth
     * @param horizontalFieldOfView
     * @param maxDistance
     * @param width
     * @param height
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, double horizontalFieldOfView, int maxDistance, int width, int height){
        
        OBSERVERPOSITION = requireNonNull(observerPosition);
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        Preconditions.checkArgument(horizontalFieldOfView>0 && horizontalFieldOfView <= PI2);
        Preconditions.checkArgument(maxDistance>0 && width >0 && height >0);
        
        OBSERVERELEVATION = observerElevation;
        this.centerAzimuth = centerAzimuth;
        HORIZONTALFIELDOFVIEW = horizontalFieldOfView;
        MAXDISTANCE = maxDistance;
        WIDTH = width;
        HEIGHT = height;
        DELTA = HORIZONTALFIELDOFVIEW/(WIDTH-1);
    
    }
    
    /**
     * 
     * @return
     */
    public GeoPoint observerPosition(){
        return OBSERVERPOSITION;
    }
    
    /**
     * 
     * @return
     */
    public int observerElevation(){
        return OBSERVERELEVATION;
    }
    
    /**
     * 
     * @return
     */
    public double centerAzimuth(){
        return centerAzimuth;
    }
    
    /**
     * 
     * @return
     */
    public double horizontalFieldOfView(){
        return HORIZONTALFIELDOFVIEW;
    }
    
    /**
     * 
     * @return
     */
    public int maxDistance(){
        return MAXDISTANCE;
    }
    
    /**
     * 
     * @return
     */
    public int width(){
        return WIDTH;
    }
    
    /**
     * 
     * @return
     */
    public int height(){
        return HEIGHT;
    }
    
    /**
     * 
     * @return
     */
    public double verticalFieldOfView(){
      
        return DELTA*(HEIGHT-1);
    }
    
    /**
     * 
     * @return
     */
    private double middleHeight(){
        return (HEIGHT-1)/2.0;
    }
    
    /**
     * 
     * @return
     */
    private double middleWidth(){
        return (WIDTH-1)/2.0;
    }
    
    /**
     * 
     * @param x
     * @return
     */
    public double azimuthForX(double x){
        Preconditions.checkArgument(x>=0 && x<=WIDTH-1);
        if(x==middleWidth()){
            return centerAzimuth;
        }
        else{
            return canonicalize((x-middleWidth())*DELTA+centerAzimuth);
        }
    }
    
    /**
     * 
     * @param a
     * @return
     */
    public double xForAzimuth(double a){
        double deltaAzimuth = Math2.angularDistance(centerAzimuth, a);
        Preconditions.checkArgument(Math.abs(deltaAzimuth)<=horizontalFieldOfView()/2.0);
        
         return (deltaAzimuth)/DELTA + middleWidth();
        
    }
    
    /**
     * 
     * @param y
     * @return
     */
    public double altitudeForY(double y){
        Preconditions.checkArgument(y>=0 && y<=HEIGHT-1);
        if(y==middleHeight()){
            return 0;
        }
        else{            
            return verticalFieldOfView()/2.0-y*DELTA;
        }
       
    }
    
    /**
     * 
     * @param a
     * @return
     */
    public double yForAltitude(double a){
        
        Preconditions.checkArgument(a>=-verticalFieldOfView()/2 && a <= verticalFieldOfView()/2);
        if(a == 0){
            return middleHeight();
        }
        else{
            return (verticalFieldOfView()/2.0 - a)/DELTA;
            
        }

    }
    
    /**
     * 
     * @param x
     * @param y
     * @return
     */
    boolean isValidSampleIndex(int x, int y){
        if(x>=0 && x<=WIDTH-1 && y >=0 && y<=HEIGHT-1){
            return true;
        }
        else{
            return false;
        }
        
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return
     */
    int linearSampleIndex(int x, int y){
        
        return WIDTH*y + x;
    }
}

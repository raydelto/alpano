package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static ch.epfl.alpano.Azimuth.canonicalize;
import static ch.epfl.alpano.Math2.PI2;

public final class PanoramaParameters {
    
    private final GeoPoint OBSERVERPOSITION;
    private final int OBSERVERELEVATION;
    private final double CENTERAZIMUTH;
    private final double HORIZONTALFIELDOFVIEW;
    private final int MAXDISTANCE;
    private final int WIDTH;
    private final int HEIGHT;
    private final double DELTA;
 
    
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, double horizontalFieldOfView, int maxDistance, int width, int height){
        
        OBSERVERPOSITION = requireNonNull(observerPosition);
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth));
        Preconditions.checkArgument(horizontalFieldOfView>0 && horizontalFieldOfView <= PI2);
        Preconditions.checkArgument(maxDistance>0 && width >0 && height >0);
        
        OBSERVERELEVATION = observerElevation;
        CENTERAZIMUTH = centerAzimuth;
        HORIZONTALFIELDOFVIEW = horizontalFieldOfView;
        MAXDISTANCE = maxDistance;
        WIDTH = width;
        HEIGHT = height;
        DELTA = HORIZONTALFIELDOFVIEW/(WIDTH-1);
    
    }
    
    public GeoPoint observerPosition(){
        return OBSERVERPOSITION;
    }
    
    public int observerElevation(){
        return OBSERVERELEVATION;
    }
    
    public double centerAzimuth(){
        return CENTERAZIMUTH;
    }
    
    public double horizontalFieldOfView(){
        return HORIZONTALFIELDOFVIEW;
    }
    
    public int maxDistance(){
        return MAXDISTANCE;
    }

    public int width(){
        return WIDTH;
    }
    
    public int height(){
        return HEIGHT;
    }
    
    public double verticalFieldOfView(){
      
        return DELTA*(HEIGHT-1);
    }
    
    private double middleHeight(){
        return (HEIGHT-1)/2.0;
    }
    
    private double middleWidth(){
        return (WIDTH-1)/2.0;
    }
    
    public double azimuthForX(double x){
        Preconditions.checkArgument(x>=0 && x<=WIDTH-1);
        if(x==middleWidth()){
            return CENTERAZIMUTH;
        }
        else{
            return canonicalize((x-middleWidth())*DELTA+CENTERAZIMUTH);
        }
    }
    
    public double xForAzimuth(double a){
        Preconditions.checkArgument(a>=canonicalize(CENTERAZIMUTH-DELTA*middleWidth()) && a <=canonicalize(CENTERAZIMUTH+DELTA*middleWidth()));
        if(a== CENTERAZIMUTH){
            return middleWidth();
        }
        else{
            return (a - CENTERAZIMUTH)/DELTA + middleWidth();
        }
    }
    
    public double altitudeForY(double y){
        Preconditions.checkArgument(y>=0 && y<=HEIGHT-1);
        if(y==middleHeight()){
            return 0;
        }
        else{            
            return verticalFieldOfView()/2.0-y*DELTA;
        }
       
    }
    
    public double yForAltitude(double a){
        
        Preconditions.checkArgument(a>=-verticalFieldOfView()/2 && a <= verticalFieldOfView()/2);
        if(a == 0){
            return middleHeight();
        }
        else{
            return (verticalFieldOfView()/2.0 - a)/DELTA;
            
        }

    }
    
    boolean isValidSampleIndex(int x, int y){
        if(x>=0 && x<=WIDTH-1 && y >=0 && y<=HEIGHT-1){
            return true;
        }
        else{
            return false;
        }
        
    }
    
    int linearSampleIndex(int x, int y){
        
        return WIDTH*y + x;
    }
}

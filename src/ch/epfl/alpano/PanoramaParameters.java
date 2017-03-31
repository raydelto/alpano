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
     * Constructs a new PanoramaParameters
     * @param observerPosition, a geopoint corresponding to the position of the observer 
     * @param observerElevation, the observer elevation in meters
     * @param centerAzimuth, the center azimuth in radians
     * @param horizontalFieldOfView, the horizontal field of view from the observer in radians
     * @param maxDistance, the maximum field of view from the observer, in meters
     * @param width, a sample that indicates the width of the panorama (a double)
     * @param height a sample that indicates the height of the panorama (a double)
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, double horizontalFieldOfView, int maxDistance, int width, int height){
        
        OBSERVERPOSITION = requireNonNull(observerPosition);
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth), "Azimuth must be canonical");
        Preconditions.checkArgument(horizontalFieldOfView>0 && horizontalFieldOfView <= PI2, "horizintalFieldOfView must be between 0 (excluded) and PI/2 (included)");
        Preconditions.checkArgument(maxDistance>0 && width >0 && height >0, "maxDistance, heigt and width must be positive");
        
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
     * Calculates the azimuth for a given sample x
     * @param x, the sample
     * @return the azimuth corresponding to the sample x
     */
    public double azimuthForX(double x){
        Preconditions.checkArgument(x>=0 && x<=WIDTH-1, "Invalid x for this panorama");
        if(x==middleWidth()){
            return centerAzimuth;
        }
        else{
            return canonicalize((x-middleWidth())*DELTA+centerAzimuth);
        }
    }
    
    /**
     * Calculates the sample x for a given azimuth
     * @param a, the azimuth in radians
     * @return the sample corresponding to the azimuth
     */
    public double xForAzimuth(double a){
        double deltaAzimuth = Math2.angularDistance(centerAzimuth, a);
        Preconditions.checkArgument(Math.abs(deltaAzimuth)<=horizontalFieldOfView()/2.0, "Invalid azimuth for this panorama");
        
         return (deltaAzimuth)/DELTA + middleWidth();
        
    }
    
    /**
     * Calculates the altitude for a given sample y
     * @param y, the sample
     * @return the altitude corresponding to the sample y
     */
    public double altitudeForY(double y){
        Preconditions.checkArgument(y>=0 && y<=HEIGHT-1, "Invalid y for this panorama");
        if(y==middleHeight()){
            return 0;
        }
        else{            
            return verticalFieldOfView()/2.0-y*DELTA;
        }
       
    }
    
    /**
     * Calculates the sample y for a given altitude
     * @param a, the altitude in radians
     * @return the sample corresponding to the altitude y
     */
    public double yForAltitude(double a){
        
        Preconditions.checkArgument(a>=-verticalFieldOfView()/2 && a <= verticalFieldOfView()/2, "Invalid altitude for this panoramas");
        if(a == 0){
            return middleHeight();
        }
        else{
            return (verticalFieldOfView()/2.0 - a)/DELTA;
            
        }

    }
    
    /**
     * Checks if a sample index is valid
     * @param x, sample
     * @param y, sample
     * @return true if the sample index is valid, false otherwise
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
     * Calculates the linearSampleIndex of 2 points from the panorama
     * @param x, sample
     * @param y, sample
     * @return an int that corresponds to the linearSampleIndex
     */
    int linearSampleIndex(int x, int y){
        
        return WIDTH*y + x;
    }
}

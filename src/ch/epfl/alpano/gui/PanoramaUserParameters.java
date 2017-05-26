/**
 * PanoramaUserParameters
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import static ch.epfl.alpano.gui.UserParameter.CENTER_AZIMUTH;
import static ch.epfl.alpano.gui.UserParameter.HEIGHT;
import static ch.epfl.alpano.gui.UserParameter.HORIZONTAL_FIELD_OF_VIEW;
import static ch.epfl.alpano.gui.UserParameter.MAX_DISTANCE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_ELEVATION;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LATITUDE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LONGITUDE;
import static ch.epfl.alpano.gui.UserParameter.SUPER_SAMPLING_EXPONENT;
import static ch.epfl.alpano.gui.UserParameter.WIDTH;
import static java.lang.Math.min;

import java.util.EnumMap;
import java.util.Map;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

public final class PanoramaUserParameters {
    private final Map<UserParameter, Integer> parameters;

    /**
     * Creates a new PanoramaUserParameters from a map
     * @param parameters a map composed of UserParameters and Integer to be stored
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> parameters) {
                
        this.parameters = new EnumMap<>(UserParameter.class);
        int maxH = min(HEIGHT.sanitize(parameters.get(HEIGHT)), (170*((WIDTH.sanitize(parameters.get(WIDTH))-1)/HORIZONTAL_FIELD_OF_VIEW.sanitize(parameters.get(HORIZONTAL_FIELD_OF_VIEW)))+1));
        
        for (Map.Entry<UserParameter, Integer> e : parameters.entrySet()) {
            if(e.getKey().equals(HEIGHT)){
                this.parameters.put(e.getKey(), e.getKey().sanitize(maxH));
            }
            
            else{
                this.parameters.put(e.getKey(), e.getKey().sanitize(e.getValue()));
            }
        }
    }

    /**
     * Creates a new PanoramaUserParameters from integer values
     * @param observerLongitude the longitude of the observer
     * @param observerLatitude the latitude of the observer
     * @param observerElevation the observer elevation
     * @param centerAzimuth the azimuth in the center of the panorama
     * @param horizonrtalFieldOfView the horizontal field of view
     * @param maxDistance the maximum distance of sight
     * @param width the width of the panorama
     * @param height the height of the panorama
     * @param samplingExponent the sampling exponent
     */
    public PanoramaUserParameters(int observerLongitude, int observerLatitude, int observerElevation, int centerAzimuth, int horizonrtalFieldOfView, int maxDistance, int width, int height, int samplingExponent) {

        this(createMap(observerLongitude, observerLatitude, observerElevation, centerAzimuth, horizonrtalFieldOfView, maxDistance, width, height, samplingExponent));
    }

    /**
     * Private method that creates a map from integer values
     * @param observerLongitude the longitude of the observer
     * @param observerLatitude the latitude of the observer
     * @param observerElevation the observer elevation
     * @param centerAzimuth the azimuth in the center of the panorama
     * @param horizonrtalFieldOfView the horizontal field of view
     * @param maxDistance the maximum distance of sight
     * @param width the width of the panorama
     * @param height the height of the panorama
     * @param samplingExponent the sampling exponent
     * @return a new EnumMap created from the integer values
     */
    private static EnumMap<UserParameter, Integer> createMap(int observerLongitude, int observerLatitude, int observerElevation, int centerAzimuth, int horizonrtalFieldOfView, int maxDistance, int width, int height, int samplingExponent) {
        
            EnumMap<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
            map.put(OBSERVER_LONGITUDE, observerLongitude);
            map.put(OBSERVER_LATITUDE, observerLatitude);
            map.put(OBSERVER_ELEVATION, observerElevation);
            map.put(CENTER_AZIMUTH, centerAzimuth);
            map.put(HORIZONTAL_FIELD_OF_VIEW,horizonrtalFieldOfView);
            map.put(MAX_DISTANCE, maxDistance);
            map.put(WIDTH, width);
            map.put(HEIGHT, height);
            map.put(SUPER_SAMPLING_EXPONENT, samplingExponent);
            return map;
        }
    
    /**
     *
     * @param p UserParameter of which we want to know the associated integer value
     * @return the integer value of the parameter p stored in the map
     */
    public int get(UserParameter p){
        return parameters.get(p);//what to do if p is not in map?
    }
    
    /**
     * 
     * @return the observer longitude
     */
    public int observerLongitude(){        
        return get(OBSERVER_LONGITUDE);
    }
    
    /**
     * 
     * @return the observer latitude
     */
    public int observerLatitude(){
        return get(OBSERVER_LATITUDE);
    }
    
    /**
     * 
     * @return
     */
    public int observerElevation(){
        return get(OBSERVER_ELEVATION);
    }
    
    /**
     * 
     * @return the center azimuth
     */
    public int centerAzimuth(){
        return get(CENTER_AZIMUTH);
    }
    
    /**
     * 
     * @return the horizontal field of view
     */
    public int horizontalFieldOfView(){
        return get(HORIZONTAL_FIELD_OF_VIEW);
    }
    
    /**
     * 
     * @return the maximum distance of sight
     */
    public int maxDistance(){
        return get(MAX_DISTANCE);
    }
    
    /**
     * 
     * @return the width of the panorama
     */
    public int width(){
        return get(UserParameter.WIDTH);
    }
    
    /**
     * 
     * @return the height of the panorama
     */
    public int height(){
        return get(UserParameter.HEIGHT);
    }
    
    /**
     * 
     * @return the super sampling exponent
     */
    public int superSamplingExponent(){
        return get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }
    
    /**
     * Creates a new PanoramaParameters considering the super sampling exponent
     * @return new PanoramaParameters 
     */
    public PanoramaParameters panoramaParameters(){
        return new PanoramaParameters(new GeoPoint(Math.toRadians((double)observerLongitude()/10000), Math.toRadians((double)observerLatitude()/10000)), observerElevation(), Math.toRadians((double)centerAzimuth()), Math.toRadians((double)horizontalFieldOfView()), maxDistance()*1000, applySuperSampling(width()), applySuperSampling(height()));
    }
    
    /**
     * Creates a new PanoramaParameters without considering the super sampling exponent
     * @return new PanoramaParameters
     */
    public PanoramaParameters panoramaDisplayParameters(){
        return new PanoramaParameters(new GeoPoint(Math.toRadians((double)observerLongitude()/10000), Math.toRadians((double)observerLatitude()/10000)), observerElevation(), Math.toRadians((double)centerAzimuth()), Math.toRadians((double)horizontalFieldOfView()), maxDistance()*1000, width(), height());
    }
    
    /**
     * Applies the super sampling exponent to 2 and multiply it by p
     * @param p integer that multiplies 2 to the power superSamplingExponent
     * @return 2 to the power superSamplingExponent multiplied by p
     */
    private int applySuperSampling(int p){
        return (int)Math.pow(2, superSamplingExponent())*p;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof PanoramaUserParameters){
            return (((PanoramaUserParameters) o).parameters.equals(parameters));
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return parameters.hashCode();
    }
}
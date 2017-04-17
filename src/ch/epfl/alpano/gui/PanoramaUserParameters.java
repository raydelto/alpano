/**
 * PanoramaUserParameters
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import java.util.EnumMap;
import java.util.Map;

public final class PanoramaUserParameters {
    private Map<UserParameter, Integer> parameters;
    
    public PanoramaUserParameters(Map<UserParameter, Integer> parameters){
        this.parameters = new EnumMap<>(UserParameter.class);
        for(Map.Entry<UserParameter, Integer> e : parameters.entrySet()){
            this.parameters.put(e.getKey(), e.getKey().sanitize(e.getValue()));
        }
        
    }
    public PanoramaUserParameters(int observerLongitude, int observerLatitude, int observerElevation, int centerAzimuth, int horizonrtalFieldOfView, int maxDistance, int width, int height, int samplingExponent){
//        Map<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
//        map.put(UserParameter.OBSERVER_LONGITUDE, observerLongitude);
//        map.put(UserParameter.OBSERVER_LATITUDE, observerLatitude);
//        map.put(UserParameter.OBSERVER_ELEVATION, observerElevation);
//        map.put(UserParameter.CENTER_AZIMUTH, centerAzimuth);
//        map.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW, horizonrtalFieldOfView);
//        map.put(UserParameter.MAX_DISTANCE, maxDistance);
//        map.put(UserParameter.WIDTH, width);
//        map.put(UserParameter.HEIGHT, height);
//        map.put(UserParameter.SUPER_SAMPLING_EXPONENT, samplingExponent);
        
        
    }
    

}

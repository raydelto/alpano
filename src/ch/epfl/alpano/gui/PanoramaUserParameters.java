/**
 * PanoramaUserParameters
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import java.util.EnumMap;
import java.util.Map;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

public final class PanoramaUserParameters {
    private Map<UserParameter, Integer> parameters;

    public PanoramaUserParameters(Map<UserParameter, Integer> parameters) {
                
        this.parameters = new EnumMap<>(UserParameter.class);
        int maxH = Math.min(UserParameter.HEIGHT.sanitize(parameters.get(UserParameter.HEIGHT)), (170*((UserParameter.WIDTH.sanitize(parameters.get(UserParameter.WIDTH))-1)/UserParameter.HORIZONTAL_FIELD_OF_VIEW.sanitize(parameters.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW)))+1));
        
        for (Map.Entry<UserParameter, Integer> e : parameters.entrySet()) {
            if(e.getKey().equals(UserParameter.HEIGHT)){
                this.parameters.put(e.getKey(), e.getKey().sanitize(maxH));
            }
            
            else{
                this.parameters.put(e.getKey(), e.getKey().sanitize(e.getValue()));
            }
        }
    }

    public PanoramaUserParameters(int observerLongitude, int observerLatitude, int observerElevation, int centerAzimuth, int horizonrtalFieldOfView, int maxDistance, int width, int height, int samplingExponent) {

        this(createMap(observerLongitude, observerLatitude, observerElevation, centerAzimuth, horizonrtalFieldOfView, maxDistance, width, height, samplingExponent));

    }

    public static EnumMap<UserParameter, Integer> createMap(int observerLongitude, int observerLatitude, int observerElevation, int centerAzimuth, int horizonrtalFieldOfView, int maxDistance, int width, int height, int samplingExponent) {
        
            EnumMap<UserParameter, Integer> map = new EnumMap<>(UserParameter.class);
            map.put(UserParameter.OBSERVER_LONGITUDE, observerLongitude);
            map.put(UserParameter.OBSERVER_LATITUDE, observerLatitude);
            map.put(UserParameter.OBSERVER_ELEVATION, observerElevation);
            map.put(UserParameter.CENTER_AZIMUTH, centerAzimuth);
            map.put(UserParameter.HORIZONTAL_FIELD_OF_VIEW,horizonrtalFieldOfView);
            map.put(UserParameter.MAX_DISTANCE, maxDistance);
            map.put(UserParameter.WIDTH, width);
            map.put(UserParameter.HEIGHT, height);
            map.put(UserParameter.SUPER_SAMPLING_EXPONENT, samplingExponent);
            return map;
        }
    public int get(UserParameter p)
    {
        return parameters.get(p);//what to do if p is not in map?
    }
    public int observerLongitude()//pas de meilleure idee pour l'instant
    {
//        for (Map.Entry<UserParameter, Integer> e : parameters.entrySet()) {
//           if(e.getKey().name().equals("OBSERVER_LONGITUDE"))
//           {
//               return e.getValue();
//           }
//        }
//        throw new NullPointerException();
        
        return get(UserParameter.OBSERVER_LONGITUDE);
    }
    public int observerLatitude(){
        return get(UserParameter.OBSERVER_LATITUDE);
    }
    public int observerElevation(){
        return get(UserParameter.OBSERVER_ELEVATION);
    }
    public int centerAzimuth(){
        return get(UserParameter.CENTER_AZIMUTH);
    }
    public int horizontalFieldOfView(){
        return get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }
    public int maxDistance(){
        return get(UserParameter.MAX_DISTANCE);
    }
    public int width(){
        return get(UserParameter.WIDTH);
    }
    public int height(){
        return get(UserParameter.HEIGHT);
    }
    public int superSamplingExponent(){
        return get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }
    public PanoramaParameters panoramaParameters()
    {
        return new PanoramaParameters(new GeoPoint(Math.toRadians((double)observerLongitude()/10000), Math.toRadians((double)observerLatitude()/10000)), observerElevation(), Math.toRadians((double)centerAzimuth()), Math.toRadians((double)horizontalFieldOfView()), maxDistance()*1000, applySuperSampling(width()), applySuperSampling(height()));
    }
    public PanoramaParameters panoramaDisplayParameters()
    {
        return new PanoramaParameters(new GeoPoint(Math.toRadians((double)observerLongitude()/10000), Math.toRadians((double)observerLatitude()/10000)), observerElevation(), Math.toRadians((double)centerAzimuth()), Math.toRadians((double)horizontalFieldOfView()), maxDistance()*1000, width(), height());
    }
    private int applySuperSampling(int p)
    {
        return (int)Math.pow(2, superSamplingExponent())*p;
    }

    @Override
    public boolean equals(Object o){
        
        if(o instanceof PanoramaUserParameters){
            for(Map.Entry<UserParameter, Integer> e : parameters.entrySet()){
               
                if(!(((Integer)((PanoramaUserParameters) o).get(e.getKey())).equals(e.getValue()))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return parameters.hashCode();
    }
}
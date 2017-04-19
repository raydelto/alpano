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
        for (Map.Entry<UserParameter, Integer> e : parameters.entrySet()) {
            this.parameters.put(e.getKey(), e.getKey().sanitize(e.getValue()));
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
        for (Map.Entry<UserParameter, Integer> e : parameters.entrySet()) {
           if(e.getKey().name().equals("OBSERVER_LONGITUDE"))
           {
               return e.getValue();
           }
        }
        throw new NullPointerException();
    }
    public int observerLatitude(){
        return 0;
    }
    public int observerElevation(){
        return 0;
    }
    public int centerAzimuth(){
        return 0;
    }
    public int horizontalFieldOfView(){
        return 0;
    }
    public int maxDistance(){
        return 0;
    }
    public int width(){
        return 0;
    }
    public int height(){
        return 0;
    }
    public int superSamplingExponent(){
        return 0;
    }
    public PanoramaParameters panoramaParameters()
    {
        return new PanoramaParameters(new GeoPoint(Math.toRadians((double)observerLongitude()), Math.toRadians((double)observerLatitude())), observerElevation(), Math.toRadians((double)centerAzimuth()), Math.toRadians((double)horizontalFieldOfView()), maxDistance()/1000, applySuperSampling(width()), applySuperSampling(height()));
    }
    public PanoramaParameters panoramaDisplayParameters()
    {
        return new PanoramaParameters(new GeoPoint(Math.toRadians((double)observerLongitude()), Math.toRadians((double)observerLatitude())), observerElevation(), Math.toRadians((double)centerAzimuth()), Math.toRadians((double)horizontalFieldOfView()), maxDistance()/1000, width(), height());
    }
    private int applySuperSampling(int p)
    {
        return (int)Math.pow(p, superSamplingExponent());
    }
    //todo equals, hashcode
    
}
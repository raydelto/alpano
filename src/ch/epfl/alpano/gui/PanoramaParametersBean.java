/**
 * PanoramaParametersBean
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import java.util.EnumMap;
import java.util.Map;
import static javafx.application.Platform.runLater;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PanoramaParametersBean {
    
    private EnumMap<UserParameter, ObjectProperty<Integer>> map;
    private ObjectProperty<PanoramaUserParameters> parameters;
    
    /**
     * Creates a new PanoramaParametersBean
     * Stores all the parameters in both a map and a PanoramaUserParameters
     * Adds a listener on every parameter in the map
     * @param parameters the parameters of the Panorama
     */
    public PanoramaParametersBean(PanoramaUserParameters parameters){
      
        this.parameters = new SimpleObjectProperty<>(parameters);
        map = new EnumMap<>(UserParameter.class);
        for(UserParameter p : UserParameter.values()){
            ObjectProperty<Integer> obj = new SimpleObjectProperty<>(parameters.get(p));
            obj.addListener((b, o, n) -> runLater(this::synchronizeParameters));
            map.put(p, obj);
        } 
    }
    
    /**
     * Private method that synchronizes the parameters entered by the user with the actual parameters of the Panorama
     */
    private void synchronizeParameters(){
        PanoramaUserParameters actualParameters = 
                new PanoramaUserParameters(observerLongitudeProperty().getValue(), observerLatitudeProperty().getValue(), 
                        observerElevationProperty().getValue(), centerAzimuthProperty().getValue(), horizontalFieldOfViewProperty().getValue(),
                        maxDistanceProperty().getValue(), widthProperty().getValue(), heightProperty().getValue(), superSamplingExponentProperty().getValue());
        
        parameters.set(actualParameters);
        
        for(UserParameter p : UserParameter.values()){
            map.get(p).set(actualParameters.get(p));
        }
        
    }

    /**
     * 
     * @return All the parameters of the Panorama
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
        return parameters;
    }
    
    /**
     * 
     * @return Longitude of the observer
     */
    public ObjectProperty<Integer> observerLongitudeProperty(){
        return map.get(UserParameter.OBSERVER_LONGITUDE);
    }
    
    /**
     * 
     * @return Latitude of the observer
     */
    public ObjectProperty<Integer> observerLatitudeProperty(){
        return map.get(UserParameter.OBSERVER_LATITUDE);
    }
    
    /**
     * 
     * @return Observer elevation
     */
    public ObjectProperty<Integer> observerElevationProperty(){
        return map.get(UserParameter.OBSERVER_ELEVATION);
    }
    
    /**
     * 
     * @return Center Azimuth of the Panorama
     */
    public ObjectProperty<Integer> centerAzimuthProperty(){
        return map.get(UserParameter.CENTER_AZIMUTH);
    }
    
    /**
     * 
     * @return Horizontal field of view of the Panorama
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
        return map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }
    
    /**
     * 
     * @return Maximum distance of the Panorama
     */
    public ObjectProperty<Integer> maxDistanceProperty(){
        return map.get(UserParameter.MAX_DISTANCE);
    }
    
    /**
     * 
     * @return Width of the Panorama
     */
    public ObjectProperty<Integer> widthProperty(){
        return map.get(UserParameter.WIDTH);
    }
    
    /**
     * 
     * @return Height of the Panorama
     */
    public ObjectProperty<Integer> heightProperty(){
        return map.get(UserParameter.HEIGHT);
    }
    
    /**
     * 
     * @return Super sampling exponent
     */
    public ObjectProperty<Integer> superSamplingExponentProperty(){
        return map.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }    
    
}

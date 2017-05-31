/**
 * PanoramaParametersBean
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
import static ch.epfl.alpano.gui.UserParameter.PAINTER;
import static ch.epfl.alpano.gui.UserParameter.SUPER_SAMPLING_EXPONENT;
import static ch.epfl.alpano.gui.UserParameter.WIDTH;
import static ch.epfl.alpano.gui.UserParameter.values;
import static javafx.application.Platform.runLater;

import java.util.EnumMap;

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
        
        for(UserParameter p : values()){
            ObjectProperty<Integer> obj = new SimpleObjectProperty<>(parameters.get(p));
            obj.addListener((b, o, n) -> runLater(this::synchronizeParameters));
            map.put(p, obj);
        } 
    }
    /**
     * Changes the values of the PanoramaUserParameters to the new ones
     * @param parameters the new user parameters
     */
    public void setBean(PanoramaUserParameters parameters)
    {
        this.parameters.set(parameters);
 
        for(UserParameter p : values()){
           
            map.get(p).setValue(parameters.get(p));
        } 
    }
    
    /**
     * Private method that synchronizes the parameters entered by the user with the actual parameters of the Panorama
     */
    private void synchronizeParameters(){
        
        PanoramaUserParameters actualParameters = new PanoramaUserParameters(observerLongitudeProperty().getValue(), observerLatitudeProperty().getValue(), 
                        observerElevationProperty().getValue(), centerAzimuthProperty().getValue(), horizontalFieldOfViewProperty().getValue(),
                        maxDistanceProperty().getValue(), widthProperty().getValue(), heightProperty().getValue(), superSamplingExponentProperty().getValue(),painterProperty().getValue(),parameters.getValue().toString());
        
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
        
        return map.get(OBSERVER_LONGITUDE);
    }
    
    /**
     * 
     * @return Latitude of the observer
     */
    public ObjectProperty<Integer> observerLatitudeProperty(){
        
        return map.get(OBSERVER_LATITUDE);
    }
    
    /**
     * 
     * @return Observer elevation
     */
    public ObjectProperty<Integer> observerElevationProperty(){
        
        return map.get(OBSERVER_ELEVATION);
    }
    
    /**
     * 
     * @return Center Azimuth of the Panorama
     */
    public ObjectProperty<Integer> centerAzimuthProperty(){
        
        return map.get(CENTER_AZIMUTH);
    }
    
    /**
     * 
     * @return Horizontal field of view of the Panorama
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
        
        return map.get(HORIZONTAL_FIELD_OF_VIEW);
    }
    
    /**
     * 
     * @return Maximum distance of the Panorama
     */
    public ObjectProperty<Integer> maxDistanceProperty(){
        
        return map.get(MAX_DISTANCE);
    }
    
    /**
     * 
     * @return Width of the Panorama
     */
    public ObjectProperty<Integer> widthProperty(){
        
        return map.get(WIDTH);
    }
    
    /**
     * 
     * @return Height of the Panorama
     */
    public ObjectProperty<Integer> heightProperty(){
        
        return map.get(HEIGHT);
    }
    
    /**
     * 
     * @return Super sampling exponent
     */
    public ObjectProperty<Integer> superSamplingExponentProperty(){
        
        return map.get(SUPER_SAMPLING_EXPONENT);
    }  
    /**
     * 
     * @return the number that correspond to the painter
     */
    public ObjectProperty<Integer> painterProperty(){
        
        return map.get(PAINTER);
    }  
    
}

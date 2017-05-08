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
    
    public PanoramaParametersBean(PanoramaUserParameters parameters){
      
        this.parameters = new SimpleObjectProperty<>(parameters);
        map = new EnumMap<>(UserParameter.class);
        for(UserParameter p : UserParameter.values()){
            ObjectProperty<Integer> obj = new SimpleObjectProperty<>(parameters.get(p));
            obj.addListener((b, o, n) -> runLater(this::synchronizeParameters));
            map.put(p, obj);
        } 
    }
    
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

    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
        return parameters;
    }
    public ObjectProperty<Integer> observerLongitudeProperty(){
        return map.get(UserParameter.OBSERVER_LONGITUDE);
    }
    public ObjectProperty<Integer> observerLatitudeProperty(){
        return map.get(UserParameter.OBSERVER_LATITUDE);
    }
    public ObjectProperty<Integer> observerElevationProperty(){
        return map.get(UserParameter.OBSERVER_ELEVATION);
    }
    public ObjectProperty<Integer> centerAzimuthProperty(){
        return map.get(UserParameter.CENTER_AZIMUTH);
    }
    public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
        return map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
    }
    public ObjectProperty<Integer> maxDistanceProperty(){
        return map.get(UserParameter.MAX_DISTANCE);
    }
    public ObjectProperty<Integer> widthProperty(){
        return map.get(UserParameter.WIDTH);
    }
    public ObjectProperty<Integer> heightProperty(){
        return map.get(UserParameter.HEIGHT);
    }
    public ObjectProperty<Integer> superSamplingExponentProperty(){
        return map.get(UserParameter.SUPER_SAMPLING_EXPONENT);
    }    
    
}

/**
 * UserParameter
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

public enum UserParameter {
    
    OBSERVER_LONGITUDE(60000, 120000),
    OBSERVER_LATITUDE(450000, 480000),
    OBSERVER_ELEVATION(300, 10000),
    CENTER_AZIMUTH(0, 359),
    HORIZONTAL_FIELD_OF_VIEW(1, 360),
    MAX_DISTANCE(10, 600),
    WIDTH(30, 16000),
    HEIGHT(10, 4000),
    SUPER_SAMPLING_EXPONENT(0, 2),
    PAINTER(0,3);
    
    int minValue;
    int maxValue;
    
    /**
     * Defines the minimum and maximum values acceptable by the enum
     * @param minValue minimal value of the enum
     * @param maxValue maximal value of the enum
     */
    private UserParameter(int minValue, int maxValue){
        
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    
    /**
     * Checks if the value is between the minimum and the maximum of this
     * @param value
     * @return the value sanitized
     */
    public int sanitize(int value){
        
        if(value> maxValue){
            
            return maxValue;
        }
        else if(value<minValue){
            
            return minValue;
        }
        else{
            
            return value;
        }
    }
}

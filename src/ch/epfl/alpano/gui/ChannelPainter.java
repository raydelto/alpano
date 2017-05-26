/**
 * ChannelPainter
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;

public interface ChannelPainter {
    
    public abstract float valueAt(int x, int y);
    /**
     * For a given Panorama, returns a channelPainter with the method valueAt()
     * valueAt() for this chanelPainter is the difference of the distance between a given point and the further neighbor of this point
     * @param p the Panorama
     * @return channelPainter with a lambda that defines the method valueAt()
     */
    public static ChannelPainter maxDistanceToNeighbors(Panorama p){
        
        return  (x, y) ->  max(max(p.distanceAt(x-1, y, 0), p.distanceAt(x, y-1, 0)), max(p.distanceAt(x+1, y, 0), p.distanceAt(x, y+1, 0))) - p.distanceAt(x, y, 0);
    }
    
    /**
     * This method transforms a ChannelPainter in another, it adds a given constant to the result of the method valueAt()
     * @param c the given constant
     * @return channelPainter with its new definition of the method valueAt()
     */
    public default ChannelPainter add(float c){
        
        return (x, y) -> (valueAt(x, y) + c);
    }
    
    /**
     * This method transforms a ChannelPainter in another, it subtracts a given constant to the result of the method valueAt()
     * @param c the given constant
     * @return channelPainter with its new definition of the method valueAt()
     */
    public default ChannelPainter sub(float c){
        
        return (x, y) -> (valueAt(x, y) - c);
    }
    
    /**
     * This method transforms a ChannelPainter in another, it multiplies by a given constant the result of the method valueAt()
     * @param c the given constant
     * @return channelPainter with its new definition of the method valueAt()
     */
    public default ChannelPainter mul(float c){
        
        return (x, y) -> (valueAt(x, y)*c);
    }
    
    /**
     * This method transforms a ChannelPainter in another, it divides by a given constant the result of the method valueAt()
     * @param c the given constant
     * @return channelPainter with its new definition of the method valueAt()
     * @throws IllegalArgumentException if the constant is 0
     */
    public default ChannelPainter div(float c){
        
        checkArgument((c!=0), "c cannot be 0");
        return (x, y) -> (valueAt(x, y)/c);
    }
    
    /**
     * This method transforms a ChannelPainter in another, its new method valueAt() applies a given DoubleUnaryOperator to the previous result of the method valueAt()
     * @param d the given DoubleUnaryOperator
     * @return channelPainter with its new definition of the method valueAt()
     */
    public default ChannelPainter map(DoubleUnaryOperator d){
        
        return (x, y) -> (float)d.applyAsDouble(valueAt(x, y));
    }
    
    /**
     * This method transforms a ChannelPainter in another, its new method valueAt() returns the inverse of the result of the previous method valueAt()
     * The inverse corresponds to 1 - the result of the method valueAt()
     * @return channelPainter with its new definition of the method valueAt()
     */
    public default ChannelPainter inverted(){
        
        return (x, y) -> (1 - valueAt(x, y));
    }
    
    /**
     * This method transforms a ChannelPainter in another, the result of the method valueAt() is clamped
     * @return channelPainter with its new definition of the method valueAt()
     */
    public default ChannelPainter clamped(){
        
        return (x, y) -> max(0, min(valueAt(x, y), 1));
        
    }
    
    /**
     * This method transforms a ChannelPainter in another, its new method valueAt() returns the mod 1 of the result of the previous method valueAt()
     * @return channelPainter with its new definition of the method valueAt()
     */
    public default ChannelPainter cycling(){
        
        return (x, y) -> valueAt(x, y)%1;
    }
}

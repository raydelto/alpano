package ch.epfl.alpano.gui;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static ch.epfl.alpano.Preconditions.checkArgument;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;


public interface ChannelPainter {
    
    public abstract float valueAt(int x, int y);
    
    
    public static ChannelPainter maxDistanceToNeighbors(Panorama p){
        return  (x, y) ->  max(max(p.distanceAt(x-1, y, 0), p.distanceAt(x, y-1, 0)), max(p.distanceAt(x+1, y, 0), p.distanceAt(x, y+1, 0))) - p.distanceAt(x, y, 0);
    }

    public default ChannelPainter add(float c){
        return (x, y) -> (valueAt(x, y) + c);
    }
    
    public default ChannelPainter sub(float c){
        return (x, y) -> (valueAt(x, y) - c);
    }
    
    public default ChannelPainter mul(float c){
        return (x, y) -> (valueAt(x, y)*c);
    }
    
    public default ChannelPainter div(float c){
        checkArgument((c!=0), "c cannot be 0");
        return (x, y) -> (valueAt(x, y)/c);
    }
    
    public default ChannelPainter map(DoubleUnaryOperator d){
        return (x, y) -> (float)d.applyAsDouble(valueAt(x, y));
    }
    
    public default ChannelPainter inverted(){
        return (x, y) -> (1 - valueAt(x, y));
    }
    
    public default ChannelPainter clamped(){
        return (x, y) -> max(0, min(valueAt(x, y), 1));
        
    }
    
    public default ChannelPainter cycling(){
        return (x, y) -> valueAt(x, y)%1;
    }
}

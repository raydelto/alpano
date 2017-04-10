package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import static java.lang.Math.max;

public interface ChannelPainter {
    
    public abstract float valueAt(int x, int y);
    
    public static ChannelPainter maxDistanceToNeighbors(Panorama p){
        return  (x, y) ->  max(max(p.distanceAt(x-1, y), p.distanceAt(x, y-1)), max(p.distanceAt(x+1, y, 0), p.distanceAt(x, y+1, 0))) - p.distanceAt(x, y, 0);
    }

    
}

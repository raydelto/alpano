/**
 * Summit
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano.summit;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;

public final class Summit {
    private String name;
    private GeoPoint position;
    private int elevation;
    /**
     *  Create a new Summit
     * @param name, the name of the summit (non null)
     * @param position, the position of the summit (non null)
     * @param elevation, the elevation in meters of the summit
     * @throws NullPointerException if name or position are null
     */
    public Summit(String name, GeoPoint position, int elevation){
        
        this.name=Objects.requireNonNull(name, "Null name");
        this.position=Objects.requireNonNull(position, "Null position");
        this.elevation=elevation;
    }
    /**
     * 
     * @return the elevation of the summit
     */
    public int elevation() {
        
        return elevation;
    }
    
    /**
     * 
     * @return the name of the summit
     */
    public String name() {
        
        return name;
    }
    
    /**
     * 
     * @return the position of the summit
     */
    public GeoPoint position() {
        
        return position;
    }
    
    @Override
    public String toString() {
        
        return name.toUpperCase()+" "+position.toString()+" "+elevation;
    }

}

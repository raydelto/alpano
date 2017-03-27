package ch.epfl.alpano.summit;

import java.util.Objects;

import ch.epfl.alpano.GeoPoint;

public final class Summit {
    private String name;
    private GeoPoint position;
    private int elevation;
    public Summit(String name, GeoPoint position, int elevation)
    {
        this.name=Objects.requireNonNull(name);
        this.position=Objects.requireNonNull(position);
        this.elevation=elevation;
        
    }
    public int elevation() {
        return elevation;
    }
    public String name() {
        return name;
    }
    public GeoPoint position() {
        return position;
    }
    @Override
    public String toString() {
        System.out.println(name.toUpperCase()+" ("+Math.toDegrees(position.longitude())+","+Math.toDegrees(position.latitude())+") "+elevation);
        return name.toUpperCase()+" "+position.toString()+" "+elevation;
    }

}

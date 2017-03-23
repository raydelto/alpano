package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;

public final class Summit {
    private String name;
    private GeoPoint position;
    private int elevation;
    public Summit(String name, GeoPoint position, int elevation)
    {
        this.name=name;
        this.position=position;
        this.elevation=elevation;
        
    }
    public int getElevation() {
        return elevation;
    }
    public String getName() {
        return name;
    }
    public GeoPoint getPosition() {
        return position;
    }
    @Override
    public String toString() {
        System.out.println(name.toUpperCase()+" ("+position.longitude()+","+position.latitude()+") "+elevation);
        return name.toUpperCase()+" ("+Math.toDegrees(position.longitude())+","+Math.toDegrees(position.latitude())+") "+elevation;
    }

}

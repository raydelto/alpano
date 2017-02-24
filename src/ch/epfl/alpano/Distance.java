package ch.epfl.alpano;

public interface Distance {
    public static double EARTH_RADIUS=6371000;
    
    /**
     * 
     * @param distanceInMeters distance to be converted in radians
     * @return the distance measured in radians
     */
    public static double toRadians(double distanceInMeters)
    {
        return distanceInMeters/EARTH_RADIUS;
    }
    /**
     * 
     * @param distanceInRadians radians to be converted to meters
     * @return the distance measured in meters
     */
    public static double toMeters(double distanceInRadians)
    {
        return distanceInRadians*EARTH_RADIUS;
    }

}

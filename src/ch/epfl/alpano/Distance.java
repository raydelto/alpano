/**
 * Distance
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

public interface Distance {
    public static final double EARTH_RADIUS = 6371000;

    /**
     * This method converts a distance in meters at the earth surface to the
     * corresponding angle in radians
     * 
     * @param distanceInMeters distance to be converted in radians
     * @return the distance converted in radians
     */
    public static double toRadians(double distanceInMeters) {

        return distanceInMeters / EARTH_RADIUS;
    }

    /**
     * This method converts an angle in radians to a distance in meters at the earth
     * surface
     * 
     * @param distanceInRadians radians to be converted to meters
     * @return the distance in radians converted in meters
     */
    public static double toMeters(double distanceInRadians) {

        return distanceInRadians * EARTH_RADIUS;
    }
}

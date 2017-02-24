/**
 * Azimuth
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

import static java.lang.Math.PI;

public interface Azimuth {

    /**
     * checks if azimuth is canonical
     * 
     * @param azimuth the angle in radians to be checked
     * @return true if the angle is included between 0 and (excluding)2PI
     */
    public static boolean isCanonical(double azimuth) {
        if (azimuth >= 0 && azimuth < Math2.PI2){
            return true;
        }
            
        else{
            return false;
        }
            
    }

    /**
     * canonicalizes azimuth
     * 
     * @param azimuth the angle in radians to be canonicalized
     * @return canonicalizes the angle between 0 and (excluding)2PI
     */
    public static double canonicalize(double azimuth) {
        return Math.abs(Math2.floorMod(azimuth, Math2.PI2));
    }

    /**
     * changes the orientation of the azimuth
     * 
     * @param azimuth the angle in radians to be converted in mathematical
     *            orientation
     * @return the converted azimuth in mathematical orientation
     */
    public static double toMath(double azimuth) {
        Preconditions.checkArgument(isCanonical(azimuth));
        if (azimuth == 0){
            return azimuth;
        }
        
        else{
            return Math2.PI2 - azimuth;
        }
            
        
    }

    /**
     * changes the orientation of the azimuth
     * 
     * @param angle the angle in mathematical orientation to be converted in
     *            reverse oriantation
     * @return the converted azimuth in reverse mathematical orientation
     */
    public static double fromMath(double azimuth) {

        Preconditions.checkArgument(isCanonical(azimuth));
        if (azimuth == 0){
            
            return azimuth;
        }
        
        else{
            return Math2.PI2 - azimuth;
        }
         
    }

    /**
     * 
     * @param azimuth the angle
     * @param n The String for the north direction
     * @param e The String for the east direction
     * @param s The String for the south direction
     * @param w The String for the west direction
     * @return
     */
    public static String toOctantString(double azimuth, String n, String e, String s, String w) {
       
        if(!(isCanonical(azimuth))){
            throw new IllegalArgumentException();
        }
        String direction = "";
        if ((azimuth >= 0 && azimuth <= 3 * PI / 8) || azimuth >= (13 * PI / 8)) {
            direction += n;
        } else if (azimuth >= (5 * PI / 8) && azimuth <= 11 * PI / 8) {
            direction += s;
        }
        if (azimuth >= PI / 8 && azimuth <= 7 * PI / 8) {
            direction += e;
        } else if (azimuth >= (9 * PI / 8) && azimuth <= (15 * PI / 8)) {
            direction += w;
        }

        System.out.println(direction);
        return direction;

    }

}

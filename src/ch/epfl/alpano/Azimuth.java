/**
 * Azimuth
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

import static ch.epfl.alpano.Math2.floorMod;
import static ch.epfl.alpano.Preconditions.checkArgument;
import static java.lang.Math.PI;
import static java.lang.Math.abs;

public interface Azimuth {

    /**
     * Checks if azimuth is canonical
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
     * Canonicalizes the azimuth
     * @param azimuth the angle in radians to be canonicalized
     * @return the angle canonicalized between 0 and (excluding) 2PI
     */
    public static double canonicalize(double azimuth) {
        
        return abs(floorMod(azimuth, Math2.PI2));
    }

    /**
     * Changes the orientation of the azimuth
     * @param azimuth the angle in radians to be converted in mathematical orientation
     * @return the converted azimuth in mathematical orientation, in radians
     * @throws IllegalArgumentException if the azimuth is not canonical
     */
    public static double toMath(double azimuth) {
        
        checkArgument(isCanonical(azimuth), "Azimuth must be canonical");
        
        if (azimuth == 0){
            return azimuth;
        }
        
        else{
            return Math2.PI2 - azimuth;
        }    
    }

    /**
     * Changes the orientation of the azimuth
     * @param azimuth the angle in mathematical orientation to be converted in reverse orientation
     * @return the converted azimuth in reverse mathematical orientation, in radians
     * @throws IllegalArgumentException if the azimuth is not canonical
     */
    public static double fromMath(double azimuth) {

        checkArgument(isCanonical(azimuth), "Azimuth must be canonical");
        
        if (azimuth == 0){
            return azimuth;
        }
        
        else{
            return Math2.PI2 - azimuth;
        } 
    }

    /**
     * Constructs a String that indicates the direction of the azimuth
     * @param azimuth the angle
     * @param n the String for the north direction
     * @param e the String for the east direction
     * @param s the String for the south direction
     * @param w the String for the west direction
     * @return a String that indicates the direction of the azimuth
     * @throws IllegalArgumentException if the azimuth is not canonical
     */
    public static String toOctantString(double azimuth, String n, String e, String s, String w) {
       
        checkArgument(isCanonical(azimuth), "Azimuth must be canonical");
        
        StringBuilder direction = new StringBuilder();
        if ((azimuth >= 0 && azimuth <= 3 * PI / 8) || azimuth >= (13 * PI / 8)) {
            direction.append(n);
        } else if (azimuth >= (5 * PI / 8) && azimuth <= 11 * PI / 8) {
            direction.append(s);
        }
        if (azimuth >= PI / 8 && azimuth <= 7 * PI / 8) {
            direction.append(e);
        } else if (azimuth >= (9 * PI / 8) && azimuth <= (15 * PI / 8)) {
            direction.append(w);
        }

        return direction.toString();
    }
}

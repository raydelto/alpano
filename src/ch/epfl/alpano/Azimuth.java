package ch.epfl.alpano;

import static java.lang.Math.PI;

public interface Azimuth {
    
    /**
     * 
     * @param azimuth the angle in radians to be checked
     * @return true if the angle is included between 0 and (excluding)2PI
     */
    public static boolean isCanonical(double azimuth)
    {
        if(azimuth>=0&&azimuth<Math2.PI2)return true;
        else return false;
    }
    /**
     * 
     * @param azimuth the angle in radians to be canonicalized
     * @return canonicalizes the angle between 0 and (excluding)2PI
     */
    public static double canonicalize(double azimuth)
    {
        return Math.abs(Math2.floorMod(azimuth, Math2.PI2));
        
    }
    /**
     * 
     * @param azimuth the angle in radians to be converted in mathematical orientation
     * @return the converted azimuth in mathematical orientation
     */
    public static double toMath(double azimuth)
    {
        Preconditions.checkArgument(isCanonical(azimuth));
        if(azimuth==0)return azimuth;
        return Math2.PI2-azimuth;
    }
    
    ////////////////////////
    /**
     * 
     * @param angle the angle in mathematical orientation to be converted in radians
     * @return
     */
    public static double fromMath(double azimuth)
    {
      
        Preconditions.checkArgument(isCanonical(azimuth));
        if(azimuth==0)return azimuth;
        return Math2.PI2-azimuth;
    }
   //////////////////////////////////////
    public static String toOctantString(double azimuth, String n, String e, String s, String w)
    {
        double mathAzimuth = toMath(azimuth);
        String direction = "";
        if((mathAzimuth>=0 && mathAzimuth <=3*PI/8) || mathAzimuth>=(13*PI/8)){
            direction+=n;
        }
        else if(mathAzimuth>=(5*PI/8) && mathAzimuth <=11*PI/8){
            direction += s;
        }
        if(mathAzimuth>=PI/8 && mathAzimuth <=7*PI/8){
            direction+=w;
        }
        else if(mathAzimuth>=(9*PI/8) && mathAzimuth <=(15*PI/8)){
            direction+=e;
        }
        
        System.out.println(direction);
        return direction;
    
    }

}

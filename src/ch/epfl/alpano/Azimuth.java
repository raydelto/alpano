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
        Preconditions.checkArgument(isCanonical(azimuth));
        if(azimuth>0&&azimuth<PI/2) return n+e;
        if(azimuth>PI/2&&azimuth<PI) return e+s;
        if(azimuth>PI&&azimuth<PI*3/2) return s+w;
        if(azimuth>PI*3/2&&azimuth<Math2.PI2) return n+w;
        return null;
    }

}

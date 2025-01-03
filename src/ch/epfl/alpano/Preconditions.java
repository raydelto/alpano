/**
 * Preconditions
 * 
 * @author Andrea Scalisi (259183)
 * @author Gerald Sula (257396)
 */
package ch.epfl.alpano;

public interface Preconditions {
    /**
     * if b is false an Illegal Argument Exception is thrown
     * 
     * @param b a boolean
     * @throws IllegalArgumentException is b is false
     */
    public static void checkArgument(boolean b) {

        if (!b)
            throw new IllegalArgumentException();
    }

    /**
     * if b is false an Illegal Argument Exception is thrown
     * 
     * @param b   a boolean
     * @param msg the message associated to the exception
     * @throws IllegalArgumentException is b is false
     */
    public static void checkArgument(boolean b, String msg) {

        if (!b)
            throw new IllegalArgumentException(msg);
    }
}

package ch.epfl.alpano;

public interface Preconditions {
    /**
     * 
     * @param b if b is false an Illegal Argument Exception is thrown
     */
    public static void checkArgument(boolean b)
    {
        if(!b) throw new IllegalArgumentException();
    }
    /**
     * 
     * @param b if b is false an Illegal Argument Exception is thrown
     * @param msg the message associated to the exception
     */
    public static void checkArgument(boolean b, String msg)
    {
        if(!b) throw new IllegalArgumentException(msg);
    }

}

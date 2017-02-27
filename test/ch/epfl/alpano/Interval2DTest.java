package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval2DTest {

    @Test(expected = NullPointerException.class)
    public void constructorThrowsExceptionForIx(){
        Interval1D iy = new Interval1D(12, 16);
        Interval2D i = new Interval2D(null, iy);
    }
    
    @Test(expected = NullPointerException.class)
    public void constructorThrowsExceptionForIy(){
        Interval1D ix = new Interval1D(-2, 4);
        Interval2D i = new Interval2D(ix, null);
    }
    
    @Test
    public void iXWorks(){
        Interval1D x = new Interval1D(-4, 6);
        Interval1D y = new Interval1D(-2, 8);
        Interval2D i = new Interval2D(x, y);
        assertTrue(x.equals(i.iX()));
        assertFalse(y.equals(i.iX()));
    }
    
    @Test
    public void iYWorks(){
        Interval1D x = new Interval1D(-4, 6);
        Interval1D y = new Interval1D(-2, 8);
        Interval2D i = new Interval2D(x, y);
        assertTrue(y.equals(i.iY()));
        assertFalse(x.equals(i.iY()));   
    }
    
    @Test
    public void containsWorks(){
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);
        Interval2D i = new Interval2D(x, y);
        assertTrue(i.contains(-4, 5));
        assertFalse(i.contains(-7, -2));
        assertFalse(i.contains(-5, 0));
        assertFalse(i.contains(4, 8));
        assertTrue(i.contains(0, 4));
    }
    
    @Test
    public void sizeWorks(){
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);
        Interval1D x1 = new Interval1D(-6, 4);
        Interval1D y1 = new Interval1D(-4, 8);
        Interval2D i = new Interval2D(x, y);
        Interval2D i2 = new Interval2D(x1, y1);
        
        assertEquals(11, i.size(), 0);
        assertEquals(15, i2.size(), 0);
        
    }
    
    
    

}

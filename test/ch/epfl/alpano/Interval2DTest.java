package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.test.ObjectTest;

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
        assertFalse(i.contains(0, 4));
    }
    
    @Test
    public void sizeWorks(){
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);
        Interval1D x1 = new Interval1D(-6, 4);
        Interval1D y1 = new Interval1D(-4, 8);
        Interval2D i = new Interval2D(x, y);
        Interval2D i2 = new Interval2D(x1, y1);
        
        assertEquals(30, i.size(), 0);
        assertEquals(143, i2.size(), 0);
        
    }
    
  
    @Test
    public void sizeOfIntersectioWithWorks(){
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);
        Interval1D x1 = new Interval1D(-6, 4);
        Interval1D y1 = new Interval1D(-4, 8);
        Interval1D x2 = new Interval1D(8, 12);
        Interval1D y2 = new Interval1D(-8, -5);
        Interval2D i = new Interval2D(x, y);
        Interval2D i2 = new Interval2D(x1, y1);
        Interval2D i3 = new Interval2D(x2, y2);
        
        assertEquals(25, i.sizeOfIntersectionWith(i2));
        assertEquals(0, i.sizeOfIntersectionWith(i3));
    }
    
    @Test
    public void boundingUnionWorks(){
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);

        Interval1D x1 = new Interval1D(-6, 4);
        Interval1D y1 = new Interval1D(-4, 8);
        
        Interval1D x2 = new Interval1D(4, 9);
        Interval1D y2 = new Interval1D(-8, -6);
        
        Interval1D b1 = new Interval1D(-7, 4);
        Interval1D b2 = new Interval1D(-4, 8);
        
        Interval1D b3 = new Interval1D(-7, 9);
        Interval1D b4 = new Interval1D(-8, 8);
        
        Interval2D i = new Interval2D(x, y);
        Interval2D i2 = new Interval2D(x1, y1);
        Interval2D i3 = new Interval2D(x2, y2);
        Interval2D bu = new Interval2D(b1, b2);
        Interval2D bu2 = new Interval2D(b3, b4);
       
        assertEquals(bu, i.boundingUnion(i2));
        assertEquals(bu2, i.boundingUnion(i3));
              
    }
    
    @Test
    public void inUnionableWithWorks(){
        
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);

        Interval1D x1 = new Interval1D(-6, 4);
        Interval1D y1 = new Interval1D(-4, 8);
        
        Interval1D x2 = new Interval1D(4, 9);
        Interval1D y2 = new Interval1D(-8, -6);
        
        Interval1D x3 = new Interval1D(-1, 2);
        Interval1D y3 = new Interval1D(0, 3);
        
        Interval1D x4 = new Interval1D(0, 2);
        Interval1D y4 = new Interval1D(0, 5);
        
        Interval1D x5 = new Interval1D(-3, 2);
        Interval1D y5 = new Interval1D(-4, 2);
        
        Interval2D i = new Interval2D(x, y);
        Interval2D i1 = new Interval2D(x1, y1);
        Interval2D i2 = new Interval2D(x2, y2);
        Interval2D i3 = new Interval2D(x3, y3);
        Interval2D i4 = new Interval2D(x4, y4);
        Interval2D i5 = new Interval2D(x5, y5);
        
        assertTrue(i.isUnionableWith(i1));
        assertFalse(i.isUnionableWith(i2));
        assertTrue(i.isUnionableWith(i3));
        assertFalse(i.isUnionableWith(i4));
                
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void unionThrowsException(){
        
        Interval1D x = new Interval1D(-7, 2);
        Interval1D y = new Interval1D(4, 8);

        Interval1D x1 = new Interval1D(4, 12);
        Interval1D y1 = new Interval1D(-4, 8);
         
        Interval2D i = new Interval2D(x, y);
        Interval2D i1 = new Interval2D(x1, y1);
       
        i.union(i1);
    }
    
    @Test
    public void unionWorks(){
        
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);

        Interval1D x1 = new Interval1D(-6, 4);
        Interval1D y1 = new Interval1D(-4, 8);
        
        Interval1D x2 = new Interval1D(4, 9);
        Interval1D y2 = new Interval1D(-8, -4);
                      
        Interval2D i = new Interval2D(x, y);
        Interval2D i1 = new Interval2D(x1, y1);
        Interval2D i2 = new Interval2D(x2, y2);
        
        Interval2D u = new Interval2D(new Interval1D(-7, 4), new Interval1D(-4, 8));
        Interval2D u1 = new Interval2D(new Interval1D(-6, 9), new Interval1D(-8, 8));
        assertEquals(u, i.union(i1));
        assertEquals(u1, i1.union(i2));
        
    }
    
    @Test
    public void equalsWorks(){
        
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);

        Interval1D x1 = new Interval1D(-6, 4);
        Interval1D y1 = new Interval1D(-4, 8);
        
        Interval1D x2 = new Interval1D(4, 9);
        Interval1D y2 = new Interval1D(-8, -6);
        
        Interval1D x3 = new Interval1D(-3, 9);
        Interval1D y3 = new Interval1D(-4, 6);
        
        Interval1D b1 = new Interval1D(-7, 4);
        Interval1D b2 = new Interval1D(-4, 8);
        
        Interval1D b3 = new Interval1D(-7, 9);
        Interval1D b4 = new Interval1D(-8, 8);
        
        Interval1D b5 = new Interval1D(-7, 8);
        Interval1D b6 = new Interval1D(-4, 8);
        
        Interval2D i = new Interval2D(x, y);
        Interval2D i2 = new Interval2D(x1, y1);
        Interval2D i3 = new Interval2D(x2, y2);
        Interval2D i4 = new Interval2D(x3, y3);
        
        Interval2D bu = new Interval2D(b1, b2);
        Interval2D bu2 = new Interval2D(b3, b4);
        Interval2D bu3 = new Interval2D(b5, b6);
        
       
        assertTrue(bu.equals(i.boundingUnion(i2)));
        assertTrue(bu2.equals(i.boundingUnion(i3)));
        assertFalse(bu3.equals(i.union(i4)));
    }
    
    @Test
    public void hashCodeTest(){
        
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8);

        Interval1D x1 = new Interval1D(-6, 4);
        Interval1D y1 = new Interval1D(-4, 8);
               
        Interval2D i = new Interval2D(x, y);
        Interval2D i2 = new Interval2D(x1, y1);
        
        ObjectTest.hashCodeIsCompatibleWithEquals(i, i2);
              
    }
    
    @Test
    public void toStringWorks(){
        
        Interval1D x = new Interval1D(-7, -2);
        Interval1D y = new Interval1D(4, 8); 
               
        Interval2D i = new Interval2D(x, y);
        String s = "[-7..-2]x[4..8]";
        
        assertTrue(s.equals(i.toString()));
        
    }
    
    
    

}

package ch.epfl.alpano;

import static org.junit.Assert.*;

import org.junit.Test;

public class Interval1DTest {

    @Test (expected = IllegalArgumentException.class)
    public void constructorThrowsException() {
        Interval1D i = new Interval1D(2, 1);
    }
    
    @Test
    public void includedFromWorksWithSmallInterval(){
        Interval1D i = new Interval1D(1, 1);
        Interval1D i2 = new Interval1D(-2, -1);
        assertEquals(1, i.includedFrom(), 0);
        assertEquals(-2, i2.includedFrom(), 0);
        
    }
    
    @Test
    public void includedToWorksWithSmallInterval(){
        Interval1D i = new Interval1D(1, 1);
        Interval1D i2 = new Interval1D(-2, -1);
        assertEquals(1, i.includedTo(), 0);
        assertEquals(-1, i2.includedTo(), 0);
    }
    
    @Test
    public void containsWorks(){
        Interval1D i = new Interval1D(-5, 5);
        assertTrue(i.contains(2));
        assertFalse(i.contains(6));
        assertTrue(i.contains(5));
        
    }
    
    @Test
    public void containsWorksWithPoint(){
        Interval1D i = new Interval1D(1, 1);
        assertTrue(i.contains(1));        
    }
    
    @Test
    public void sizeWorks(){
        Interval1D i = new Interval1D(0, 6);
        assertEquals(7, i.size(), 0);        
    }
    
    @Test
    public void sizeWorksWithPoint(){
        Interval1D i = new Interval1D(1, 1);
        assertEquals(1, i.size(), 0);        
    }
    
    @Test
    public void sizeOfIntersectionWithWorks(){
        Interval1D i = new Interval1D(-6, 4);
        Interval1D i2 = new Interval1D(-9, 1);
        assertEquals(8, i.sizeOfIntersectionWith(i2), 0);
        
        
    }
    
    @Test
    public void sizeOfIntersectionWithWorksWithEmptyIntersection(){
        Interval1D i = new Interval1D(-2, 4);
        Interval1D i2 = new Interval1D(5, 18);
        assertEquals(0, i.sizeOfIntersectionWith(i2), 0);
    }
    
    @Test
    public void sizeOfIntersectionWithWorksWithIntersectionOfOne(){
        Interval1D i = new Interval1D(-2, 4);
        Interval1D i2 = new Interval1D(4, 18);
        
        assertEquals(1, i.sizeOfIntersectionWith(i2), 0);
    }
    
    @Test
    public void boundingUnionWorks(){
        Interval1D i = new Interval1D(-2, 4);
        Interval1D i2 = new Interval1D(2, 18);
        Interval1D i3 = new Interval1D(-2, 18);
        assertTrue(i3.equals(i.boundingUnion(i2)));
        
        
    }
    
    @Test
    public void boundingUnionWorksWithIntersectionOfOne(){
        Interval1D i = new Interval1D(-2, 4);
        Interval1D i2 = new Interval1D(4, 18);
        Interval1D i3 = new Interval1D(-2, 18);
        assertTrue(i3.equals(i.boundingUnion(i2)));
    }
    
    @Test
    public void isUnionableWithWorks(){
        Interval1D i = new Interval1D(-2, 4);
        Interval1D i2 = new Interval1D(4, 18);
        Interval1D i3 = new Interval1D(-2, 6);
        Interval1D i4 = new Interval1D(5, 18);
        Interval1D i5 = new Interval1D(6, 7);
        Interval1D i6 = new Interval1D(1, 1);
        Interval1D i7 = new Interval1D(2, 2);
       /* System.out.println(i.isUnionableWith(i2));
        System.out.println(i.isUnionableWith(i3));
        System.out.println(i.isUnionableWith(i4));
        System.out.println(i.isUnionableWith(i5));
        System.out.println(i6.isUnionableWith(i7));
        */
        assertTrue(i.isUnionableWith(i2));
        assertTrue(i.isUnionableWith(i3));
        assertTrue(i.isUnionableWith(i4));
        assertFalse(i.isUnionableWith(i5));
        assertTrue(i6.isUnionableWith(i7));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void unionThrowsException(){
        
        Interval1D i = new Interval1D(-2, 4);
        Interval1D i2 = new Interval1D(7, 18);
        i.union(i2);
        
    }
    
    @Test
    public void unionWorks(){
        Interval1D i = new Interval1D(-12, -4);
        Interval1D i2 = new Interval1D(-5, 1);
        Interval1D i3 = i.union(i2);
        Interval1D united = new Interval1D(-12, 1);
        assertEquals(-12, i3.includedFrom(), 0);
        assertEquals(1, i3.includedTo(), 0);
        assertTrue(united.equals(i.union(i2)));
    }
    
    @Test
    public void equalsWorks(){
        Interval1D i = new Interval1D(-12, -4);
        Interval1D i2 = new Interval1D(-12, -4);
        Interval1D i3 = new Interval1D(-11, -4);
        Interval1D i4 = i.union(i3);
            
        assertTrue(i.equals(i2));
        assertFalse(i2.equals(i3));
        assertTrue(i4.equals(i));
    }
    
    
    
    
    
    
    
    
    

}

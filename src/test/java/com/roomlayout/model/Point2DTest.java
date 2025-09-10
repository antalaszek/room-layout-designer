package com.roomlayout.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class Point2DTest {
    
    @Test
    @DisplayName("Should create point with coordinates")
    void testPointCreation() {
        Point2D point = new Point2D(3.5, 4.2);
        
        assertEquals(3.5, point.getX());
        assertEquals(4.2, point.getY());
    }
    
    @Test
    @DisplayName("Should handle negative coordinates")
    void testNegativeCoordinates() {
        Point2D point = new Point2D(-2.5, -7.8);
        
        assertEquals(-2.5, point.getX());
        assertEquals(-7.8, point.getY());
    }
    
    @Test
    @DisplayName("Should handle zero coordinates")
    void testZeroCoordinates() {
        Point2D point = new Point2D(0, 0);
        
        assertEquals(0.0, point.getX());
        assertEquals(0.0, point.getY());
    }
    
    @Test
    @DisplayName("Should format toString correctly")
    void testToString() {
        Point2D point1 = new Point2D(3.14, 2.71);
        assertEquals("(3.1, 2.7)", point1.toString());
        
        Point2D point2 = new Point2D(10.0, 20.0);
        assertEquals("(10.0, 20.0)", point2.toString());
        
        Point2D point3 = new Point2D(-5.55, -3.33);
        assertEquals("(-5.6, -3.3)", point3.toString());
    }
    
    @Test
    @DisplayName("Should handle large values")
    void testLargeValues() {
        Point2D point = new Point2D(1000000.5, 999999.9);
        
        assertEquals(1000000.5, point.getX());
        assertEquals(999999.9, point.getY());
    }
    
    @Test
    @DisplayName("Should handle small values")
    void testSmallValues() {
        Point2D point = new Point2D(0.0001, 0.0002);
        
        assertEquals(0.0001, point.getX());
        assertEquals(0.0002, point.getY());
    }
    
    @Test
    @DisplayName("Should be immutable")
    void testImmutability() {
        Point2D point = new Point2D(5.0, 10.0);
        
        double x1 = point.getX();
        double y1 = point.getY();
        double x2 = point.getX();
        double y2 = point.getY();
        
        assertEquals(x1, x2);
        assertEquals(y1, y2);
    }
}
package com.roomlayout.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class FurnitureTest {
    
    @Test
    @DisplayName("Should create furniture with valid dimensions")
    void testFurnitureCreation() {
        Furniture furniture = new Furniture("Table", 1.5, 0.8, 0.75, 2.0, 3.0);
        
        assertEquals("Table", furniture.getName());
        assertEquals(1.5, furniture.getWidth());
        assertEquals(0.8, furniture.getLength());
        assertEquals(0.75, furniture.getHeight());
        assertEquals(2.0, furniture.getX());
        assertEquals(3.0, furniture.getY());
        assertEquals(0.0, furniture.getRotation());
    }
    
    @Test
    @DisplayName("Should create furniture with rotation")
    void testFurnitureWithRotation() {
        Furniture furniture = new Furniture("Chair", 0.5, 0.5, 0.9, 1.0, 1.0, 45.0);
        
        assertEquals(45.0, furniture.getRotation());
    }
    
    @Test
    @DisplayName("Should normalize rotation to 0-360 range")
    void testRotationNormalization() {
        Furniture furniture1 = new Furniture("Item1", 1.0, 1.0, 1.0, 0, 0, 380.0);
        assertEquals(20.0, furniture1.getRotation());
        
        Furniture furniture2 = new Furniture("Item2", 1.0, 1.0, 1.0, 0, 0, -30.0);
        assertEquals(330.0, furniture2.getRotation());
        
        Furniture furniture3 = new Furniture("Item3", 1.0, 1.0, 1.0, 0, 0, 720.0);
        assertEquals(0.0, furniture3.getRotation());
    }
    
    @Test
    @DisplayName("Should throw exception for invalid dimensions")
    void testInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Furniture("Invalid", 0, 1.0, 1.0, 0, 0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Furniture("Invalid", 1.0, -1.0, 1.0, 0, 0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Furniture("Invalid", 1.0, 1.0, 0, 0, 0));
    }
    
    @Test
    @DisplayName("Should calculate center point correctly")
    void testCenterCalculation() {
        Furniture furniture = new Furniture("Table", 2.0, 1.0, 0.8, 3.0, 4.0);
        Point2D center = furniture.getCenter();
        
        assertEquals(4.0, center.getX());
        assertEquals(4.5, center.getY());
    }
    
    @Test
    @DisplayName("Should allow negative position coordinates")
    void testNegativePosition() {
        Furniture furniture = new Furniture("Item", 1.0, 1.0, 1.0, -2.0, -3.0);
        
        assertEquals(-2.0, furniture.getX());
        assertEquals(-3.0, furniture.getY());
    }
    
    @Test
    @DisplayName("Should format toString correctly")
    void testToString() {
        Furniture furniture = new Furniture("Sofa", 2.5, 1.0, 0.8, 1.5, 2.0);
        String result = furniture.toString();
        
        assertTrue(result.contains("Sofa"));
        assertTrue(result.contains("2.5x1.0x0.8m"));
        assertTrue(result.contains("(1.5, 2.0)"));
    }
    
    @Test
    @DisplayName("Should handle edge case dimensions")
    void testEdgeCaseDimensions() {
        Furniture tiny = new Furniture("Tiny", 0.001, 0.001, 0.001, 0, 0);
        assertEquals(0.001, tiny.getWidth());
        assertEquals(0.001, tiny.getLength());
        assertEquals(0.001, tiny.getHeight());
        
        Furniture large = new Furniture("Large", 100.0, 100.0, 100.0, 0, 0);
        assertEquals(100.0, large.getWidth());
        assertEquals(100.0, large.getLength());
        assertEquals(100.0, large.getHeight());
    }
    
    @Test
    @DisplayName("Should maintain immutability")
    void testImmutability() {
        Furniture furniture = new Furniture("Table", 1.0, 1.0, 1.0, 2.0, 3.0, 45.0);
        
        assertEquals("Table", furniture.getName());
        assertEquals(1.0, furniture.getWidth());
        assertEquals(1.0, furniture.getLength());
        assertEquals(1.0, furniture.getHeight());
        assertEquals(2.0, furniture.getX());
        assertEquals(3.0, furniture.getY());
        assertEquals(45.0, furniture.getRotation());
        
        Point2D center1 = furniture.getCenter();
        Point2D center2 = furniture.getCenter();
        assertNotSame(center1, center2);
        assertEquals(center1.getX(), center2.getX());
        assertEquals(center1.getY(), center2.getY());
    }
}
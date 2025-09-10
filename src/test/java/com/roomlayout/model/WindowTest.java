package com.roomlayout.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class WindowTest {
    
    @Test
    @DisplayName("Should create window with valid parameters")
    void testWindowCreation() {
        Window window = new Window(Wall.EAST, 1.5, 2.0, 1.2, 0.9, "Bay");
        
        assertEquals(Wall.EAST, window.getWall());
        assertEquals(1.5, window.getPosition());
        assertEquals(2.0, window.getWidth());
        assertEquals(1.2, window.getHeight());
        assertEquals(0.9, window.getBottomHeight());
        assertEquals("Bay", window.getType());
    }
    
    @Test
    @DisplayName("Should create window with default type")
    void testWindowDefaultType() {
        Window window = new Window(Wall.WEST, 2.0, 1.5, 1.0, 1.0);
        
        assertEquals("Standard", window.getType());
    }
    
    @Test
    @DisplayName("Should throw exception for invalid dimensions")
    void testInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Window(Wall.NORTH, 1.0, 0, 1.0, 1.0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Window(Wall.NORTH, 1.0, 1.0, -1.0, 1.0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Window(Wall.NORTH, 1.0, -1.0, 1.0, 1.0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Window(Wall.NORTH, 1.0, 1.0, 1.0, -0.5));
    }
    
    @Test
    @DisplayName("Should reject floor and ceiling placement")
    void testInvalidWallPlacement() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Window(Wall.FLOOR, 1.0, 1.0, 1.0, 1.0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Window(Wall.CEILING, 1.0, 1.0, 1.0, 1.0));
    }
    
    @Test
    @DisplayName("Should handle various bottom heights")
    void testBottomHeights() {
        Window low = new Window(Wall.NORTH, 1.0, 1.5, 1.0, 0.5);
        Window medium = new Window(Wall.SOUTH, 1.0, 1.5, 1.0, 1.0);
        Window high = new Window(Wall.EAST, 1.0, 1.5, 1.0, 1.5);
        
        assertEquals(0.5, low.getBottomHeight());
        assertEquals(1.0, medium.getBottomHeight());
        assertEquals(1.5, high.getBottomHeight());
    }
    
    @Test
    @DisplayName("Should allow window at floor level")
    void testFloorLevelWindow() {
        Window floorWindow = new Window(Wall.NORTH, 1.0, 2.0, 2.5, 0.0);
        assertEquals(0.0, floorWindow.getBottomHeight());
    }
    
    @Test
    @DisplayName("Should format toString correctly")
    void testToString() {
        Window window = new Window(Wall.SOUTH, 2.5, 1.8, 1.3, 0.8, "Casement");
        String result = window.toString();
        
        assertTrue(result.contains("Casement Window"));
        assertTrue(result.contains("South wall"));
        assertTrue(result.contains("1.8m wide"));
        assertTrue(result.contains("1.3m high"));
        assertTrue(result.contains("position 2.5m"));
        assertTrue(result.contains("0.8m from floor"));
    }
    
    @Test
    @DisplayName("Should handle all wall types")
    void testAllWallTypes() {
        Window north = new Window(Wall.NORTH, 1.0, 1.0, 1.0, 1.0);
        Window south = new Window(Wall.SOUTH, 1.0, 1.0, 1.0, 1.0);
        Window east = new Window(Wall.EAST, 1.0, 1.0, 1.0, 1.0);
        Window west = new Window(Wall.WEST, 1.0, 1.0, 1.0, 1.0);
        
        assertEquals(Wall.NORTH, north.getWall());
        assertEquals(Wall.SOUTH, south.getWall());
        assertEquals(Wall.EAST, east.getWall());
        assertEquals(Wall.WEST, west.getWall());
    }
    
    @Test
    @DisplayName("Should allow negative position")
    void testNegativePosition() {
        Window window = new Window(Wall.NORTH, -1.0, 1.0, 1.0, 1.0);
        assertEquals(-1.0, window.getPosition());
    }
    
    @Test
    @DisplayName("Should handle various window types")
    void testDifferentWindowTypes() {
        Window standard = new Window(Wall.NORTH, 1.0, 1.5, 1.0, 1.0, "Standard");
        Window bay = new Window(Wall.SOUTH, 1.0, 2.0, 1.5, 0.8, "Bay");
        Window casement = new Window(Wall.EAST, 1.0, 1.0, 1.2, 0.9, "Casement");
        Window sliding = new Window(Wall.WEST, 1.0, 2.5, 1.0, 1.1, "Sliding");
        Window skylight = new Window(Wall.NORTH, 1.0, 1.0, 1.0, 2.0, "Skylight");
        
        assertEquals("Standard", standard.getType());
        assertEquals("Bay", bay.getType());
        assertEquals("Casement", casement.getType());
        assertEquals("Sliding", sliding.getType());
        assertEquals("Skylight", skylight.getType());
    }
    
    @Test
    @DisplayName("Should handle edge case dimensions")
    void testEdgeCaseDimensions() {
        Window tiny = new Window(Wall.NORTH, 0, 0.001, 0.001, 0);
        assertEquals(0.001, tiny.getWidth());
        assertEquals(0.001, tiny.getHeight());
        assertEquals(0.0, tiny.getBottomHeight());
        
        Window large = new Window(Wall.SOUTH, 0, 10.0, 5.0, 0);
        assertEquals(10.0, large.getWidth());
        assertEquals(5.0, large.getHeight());
    }
}
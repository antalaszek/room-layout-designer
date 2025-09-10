package com.roomlayout.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class DoorTest {
    
    @Test
    @DisplayName("Should create door with valid parameters")
    void testDoorCreation() {
        Door door = new Door(Wall.NORTH, 2.0, 1.0, 2.1, "French");
        
        assertEquals(Wall.NORTH, door.getWall());
        assertEquals(2.0, door.getPosition());
        assertEquals(1.0, door.getWidth());
        assertEquals(2.1, door.getHeight());
        assertEquals(0.0, door.getBottomHeight());
        assertEquals("French", door.getType());
    }
    
    @Test
    @DisplayName("Should create door with default type")
    void testDoorDefaultType() {
        Door door = new Door(Wall.SOUTH, 1.5, 0.9, 2.0);
        
        assertEquals("Standard", door.getType());
    }
    
    @Test
    @DisplayName("Should throw exception for invalid dimensions")
    void testInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Door(Wall.NORTH, 1.0, 0, 2.0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Door(Wall.NORTH, 1.0, 1.0, -1.0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Door(Wall.NORTH, 1.0, -1.0, 2.0));
    }
    
    @Test
    @DisplayName("Should reject floor and ceiling placement")
    void testInvalidWallPlacement() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Door(Wall.FLOOR, 1.0, 1.0, 2.0));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Door(Wall.CEILING, 1.0, 1.0, 2.0));
    }
    
    @Test
    @DisplayName("Should always have bottom height of 0")
    void testBottomHeight() {
        Door door1 = new Door(Wall.EAST, 0, 0.8, 2.0);
        Door door2 = new Door(Wall.WEST, 5.0, 1.2, 2.5, "Double");
        
        assertEquals(0.0, door1.getBottomHeight());
        assertEquals(0.0, door2.getBottomHeight());
    }
    
    @Test
    @DisplayName("Should format toString correctly")
    void testToString() {
        Door door = new Door(Wall.NORTH, 3.5, 1.2, 2.1, "Sliding");
        String result = door.toString();
        
        assertTrue(result.contains("Sliding Door"));
        assertTrue(result.contains("North wall"));
        assertTrue(result.contains("1.2m wide"));
        assertTrue(result.contains("2.1m high"));
        assertTrue(result.contains("position 3.5m"));
    }
    
    @Test
    @DisplayName("Should handle all wall types")
    void testAllWallTypes() {
        Door north = new Door(Wall.NORTH, 1.0, 1.0, 2.0);
        Door south = new Door(Wall.SOUTH, 1.0, 1.0, 2.0);
        Door east = new Door(Wall.EAST, 1.0, 1.0, 2.0);
        Door west = new Door(Wall.WEST, 1.0, 1.0, 2.0);
        
        assertEquals(Wall.NORTH, north.getWall());
        assertEquals(Wall.SOUTH, south.getWall());
        assertEquals(Wall.EAST, east.getWall());
        assertEquals(Wall.WEST, west.getWall());
    }
    
    @Test
    @DisplayName("Should allow negative position")
    void testNegativePosition() {
        Door door = new Door(Wall.NORTH, -2.0, 1.0, 2.0);
        assertEquals(-2.0, door.getPosition());
    }
    
    @Test
    @DisplayName("Should handle various door types")
    void testDifferentDoorTypes() {
        Door standard = new Door(Wall.NORTH, 1.0, 0.9, 2.0, "Standard");
        Door french = new Door(Wall.SOUTH, 1.0, 1.5, 2.1, "French");
        Door sliding = new Door(Wall.EAST, 1.0, 2.0, 2.0, "Sliding");
        Door pocket = new Door(Wall.WEST, 1.0, 0.8, 2.0, "Pocket");
        
        assertEquals("Standard", standard.getType());
        assertEquals("French", french.getType());
        assertEquals("Sliding", sliding.getType());
        assertEquals("Pocket", pocket.getType());
    }
}
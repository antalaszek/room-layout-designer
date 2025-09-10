package com.roomlayout.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    private Room room;
    
    @BeforeEach
    void setUp() {
        room = new Room(5.0, 6.0, 3.0);
    }
    
    @Test
    @DisplayName("Room should be created with valid dimensions")
    void testRoomCreation() {
        assertEquals(5.0, room.getWidth());
        assertEquals(6.0, room.getLength());
        assertEquals(3.0, room.getHeight());
    }
    
    @Test
    @DisplayName("Room should throw exception for invalid dimensions")
    void testInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Room(0, 5, 3));
        assertThrows(IllegalArgumentException.class, () -> new Room(5, -1, 3));
        assertThrows(IllegalArgumentException.class, () -> new Room(5, 5, 0));
    }
    
    @Test
    @DisplayName("Should add furniture within room bounds")
    void testAddValidFurniture() {
        Furniture table = new Furniture("Table", 1.0, 1.0, 0.8, 1.0, 1.0);
        room.addFurniture(table);
        assertEquals(1, room.getFurniture().size());
        assertEquals("Table", room.getFurniture().get(0).getName());
    }
    
    @Test
    @DisplayName("Should reject furniture outside room bounds")
    void testAddInvalidFurniture() {
        Furniture tooWide = new Furniture("Wide", 6.0, 1.0, 1.0, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> room.addFurniture(tooWide));
        
        Furniture tooLong = new Furniture("Long", 1.0, 7.0, 1.0, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> room.addFurniture(tooLong));
        
        Furniture tooTall = new Furniture("Tall", 1.0, 1.0, 4.0, 0, 0);
        assertThrows(IllegalArgumentException.class, () -> room.addFurniture(tooTall));
        
        Furniture outOfBoundsX = new Furniture("OutX", 1.0, 1.0, 1.0, 5.0, 0);
        assertThrows(IllegalArgumentException.class, () -> room.addFurniture(outOfBoundsX));
        
        Furniture outOfBoundsY = new Furniture("OutY", 1.0, 1.0, 1.0, 0, 6.0);
        assertThrows(IllegalArgumentException.class, () -> room.addFurniture(outOfBoundsY));
    }
    
    @Test
    @DisplayName("Should add multiple furniture items")
    void testAddMultipleFurniture() {
        room.addFurniture(new Furniture("Table", 1.0, 1.0, 0.8, 1.0, 1.0));
        room.addFurniture(new Furniture("Chair", 0.5, 0.5, 0.9, 3.0, 3.0));
        room.addFurniture(new Furniture("Sofa", 2.0, 0.8, 0.8, 0.5, 4.0));
        
        assertEquals(3, room.getFurniture().size());
    }
    
    @Test
    @DisplayName("Should add door on valid wall position")
    void testAddValidDoor() {
        Door door = new Door(Wall.NORTH, 1.0, 1.0, 2.0);
        room.addDoor(door);
        assertEquals(1, room.getDoors().size());
        assertEquals(Wall.NORTH, room.getDoors().get(0).getWall());
    }
    
    @Test
    @DisplayName("Should reject door outside wall bounds")
    void testAddInvalidDoor() {
        Door tooWideNorth = new Door(Wall.NORTH, 0, 6.0, 2.0);
        assertThrows(IllegalArgumentException.class, () -> room.addDoor(tooWideNorth));
        
        Door outOfBoundsNorth = new Door(Wall.NORTH, 5.0, 1.0, 2.0);
        assertThrows(IllegalArgumentException.class, () -> room.addDoor(outOfBoundsNorth));
        
        Door tooTall = new Door(Wall.NORTH, 1.0, 1.0, 4.0);
        assertThrows(IllegalArgumentException.class, () -> room.addDoor(tooTall));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Door(Wall.FLOOR, 1.0, 1.0, 2.0));
    }
    
    @Test
    @DisplayName("Should add window on valid wall position")
    void testAddValidWindow() {
        Window window = new Window(Wall.EAST, 1.0, 1.5, 1.0, 1.0);
        room.addWindow(window);
        assertEquals(1, room.getWindows().size());
        assertEquals(Wall.EAST, room.getWindows().get(0).getWall());
    }
    
    @Test
    @DisplayName("Should reject window outside wall bounds")
    void testAddInvalidWindow() {
        Window tooWideEast = new Window(Wall.EAST, 0, 7.0, 1.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> room.addWindow(tooWideEast));
        
        Window outOfBoundsEast = new Window(Wall.EAST, 5.5, 1.0, 1.0, 1.0);
        assertThrows(IllegalArgumentException.class, () -> room.addWindow(outOfBoundsEast));
        
        Window tooHighFromFloor = new Window(Wall.EAST, 1.0, 1.0, 1.0, 2.5);
        assertThrows(IllegalArgumentException.class, () -> room.addWindow(tooHighFromFloor));
        
        assertThrows(IllegalArgumentException.class, 
            () -> new Window(Wall.CEILING, 1.0, 1.0, 1.0, 1.0));
    }
    
    @Test
    @DisplayName("Should handle multiple doors and windows")
    void testMultipleDoorsAndWindows() {
        room.addDoor(new Door(Wall.NORTH, 1.0, 1.0, 2.0));
        room.addDoor(new Door(Wall.SOUTH, 2.0, 0.9, 2.1));
        
        room.addWindow(new Window(Wall.EAST, 1.0, 1.5, 1.0, 1.0));
        room.addWindow(new Window(Wall.WEST, 2.0, 2.0, 1.2, 0.8));
        room.addWindow(new Window(Wall.NORTH, 3.0, 1.0, 1.0, 1.2));
        
        assertEquals(2, room.getDoors().size());
        assertEquals(3, room.getWindows().size());
    }
    
    @Test
    @DisplayName("Should return defensive copies of lists")
    void testDefensiveCopies() {
        Furniture table = new Furniture("Table", 1.0, 1.0, 0.8, 1.0, 1.0);
        room.addFurniture(table);
        
        var furnitureList = room.getFurniture();
        furnitureList.clear();
        
        assertEquals(1, room.getFurniture().size());
    }
    
    @Test
    @DisplayName("Should format toString correctly")
    void testToString() {
        String result = room.toString();
        assertTrue(result.contains("5.0m x 6.0m x 3.0m"));
        assertTrue(result.contains("Room:"));
    }
}
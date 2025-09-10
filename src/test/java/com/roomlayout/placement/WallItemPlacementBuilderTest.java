package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WallItemPlacementBuilderTest {
    
    private Room room;
    
    @BeforeEach
    void setUp() {
        room = new Room(6.0, 4.0, 2.7);
    }
    
    @Test
    void testDoorPlacementCentered() {
        Door door = (Door) room.placeDoor("Main Door", 0.9, 2.1)
            .onWall(Wall.SOUTH)
            .centered()
            .build();
        
        assertEquals("Main Door", door.getType());
        assertEquals(0.9, door.getWidth(), 0.001);
        assertEquals(2.1, door.getHeight(), 0.001);
        assertEquals(Wall.SOUTH, door.getWall());
        assertEquals(2.55, door.getPosition(), 0.001); // (6.0 - 0.9) / 2
        assertTrue(room.getDoors().contains(door));
    }
    
    @Test
    void testDoorPlacementFromWest() {
        Door door = (Door) room.placeDoor("Entry Door", 0.8, 2.0)
            .onWall(Wall.NORTH)
            .fromWest(1.5)
            .build();
        
        assertEquals(1.5, door.getPosition(), 0.001);
        assertEquals(Wall.NORTH, door.getWall());
    }
    
    @Test
    void testDoorPlacementFromEast() {
        Door door = (Door) room.placeDoor("Balcony Door", 1.6, 2.1)
            .onWall(Wall.SOUTH)
            .fromEast(0.8)
            .build();
        
        assertEquals(3.6, door.getPosition(), 0.001); // 6.0 - 1.6 - 0.8
    }
    
    @Test
    void testDoorPlacementWithShift() {
        Door door = (Door) room.placeDoor("Side Door", 0.7, 2.0)
            .onWall(Wall.EAST)
            .centered()
            .shiftNorth(0.5)
            .build();
        
        assertEquals(1.15, door.getPosition(), 0.001); // (4.0 - 0.7) / 2 - 0.5
    }
    
    @Test
    void testWindowPlacementCentered() {
        Window window = (Window) room.placeWindow("Living Room Window", 1.2, 1.0, 1.0)
            .onWall(Wall.EAST)
            .centered()
            .build();
        
        assertEquals(1.2, window.getWidth(), 0.001);
        assertEquals(1.0, window.getHeight(), 0.001);
        assertEquals(1.0, window.getBottomHeight(), 0.001);
        assertEquals(Wall.EAST, window.getWall());
        assertEquals(1.4, window.getPosition(), 0.001); // (4.0 - 1.2) / 2
        assertTrue(room.getWindows().contains(window));
    }
    
    @Test
    void testWindowPlacementFromNorth() {
        Window window = (Window) room.placeWindow("Kitchen Window", 0.8, 0.6, 1.2)
            .onWall(Wall.WEST)
            .fromNorth(2.0)
            .build();
        
        assertEquals(2.0, window.getPosition(), 0.001);
        assertEquals(1.2, window.getBottomHeight(), 0.001);
    }
    
    @Test
    void testWindowPlacementFromSouth() {
        Window window = (Window) room.placeWindow("Bedroom Window", 1.0, 0.8, 0.9)
            .onWall(Wall.NORTH)
            .fromSouth(1.5)
            .build();
        
        assertEquals(3.5, window.getPosition(), 0.001); // 6.0 - 1.0 - 1.5
    }
    
    @Test
    void testMultipleWindowsOnSameWall() {
        Window window1 = (Window) room.placeWindow("Window 1", 0.6, 0.8, 1.1)
            .onWall(Wall.EAST)
            .fromNorth(0.5)
            .build();
            
        Window window2 = (Window) room.placeWindow("Window 2", 0.6, 0.8, 1.1)
            .onWall(Wall.EAST)
            .fromSouth(0.5)
            .build();
        
        assertEquals(2, room.getWindows().size());
        assertEquals(0.5, window1.getPosition(), 0.001);
        assertEquals(2.9, window2.getPosition(), 0.001); // 4.0 - 0.6 - 0.5
    }
    
    @Test
    void testDoorPlacementOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.placeDoor("Too Wide Door", 7.0, 2.1)
                .onWall(Wall.NORTH)
                .centered()
                .build();
        });
    }
    
    @Test
    void testWindowPlacementOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.placeWindow("Too Wide Window", 8.0, 1.0, 1.0)
                .onWall(Wall.SOUTH)
                .centered()
                .build();
        });
    }
    
    @Test
    void testDoorPlacementNearEdge() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.placeDoor("Edge Door", 1.0, 2.1)
                .onWall(Wall.WEST)
                .fromNorth(4.5) // Would place at position 4.5, but door is 1.0 wide, exceeding wall length 4.0
                .build();
        });
    }
    
    @Test
    void testWindowTooTall() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.placeWindow("Too Tall Window", 1.0, 3.0, 0.5) // height 3.0 + bottom 0.5 = 3.5 > room height 2.7
                .onWall(Wall.NORTH)
                .centered()
                .build();
        });
    }
    
    @Test
    void testValidationWithShifts() {
        // Should work - window fits after shift
        Window validWindow = (Window) room.placeWindow("Valid", 1.0, 0.8, 1.0)
            .onWall(Wall.NORTH)
            .centered()
            .shiftEast(2.0)
            .build();
        
        assertEquals(4.5, validWindow.getPosition(), 0.001); // center (2.5) + shift (2.0)
        
        // Should fail - window doesn't fit after shift
        assertThrows(IllegalArgumentException.class, () -> {
            room.placeWindow("Invalid", 1.0, 0.8, 1.0)
                .onWall(Wall.NORTH)
                .centered()
                .shiftEast(3.0) // Would place at 5.5, but wall is only 6.0 wide
                .build();
        });
    }
}
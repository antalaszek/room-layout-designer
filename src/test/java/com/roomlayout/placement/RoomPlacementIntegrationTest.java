package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RoomPlacementIntegrationTest {
    
    private Room room;
    
    @BeforeEach
    void setUp() {
        room = new Room(8.0, 6.0, 2.7);
    }
    
    @Test
    void testCompleteRoomLayoutScenario() {
        // Place sofa in corner
        Furniture sofa = room.place("Sofa", 2.5, 0.9, 0.8)
            .inCorner(Corner.NORTH_EAST)
            .withGap(0.3)
            .build();
        
        // Place TV on opposite wall
        Furniture tv = room.place("TV", 1.5, 0.4, 0.7)
            .onWall(Wall.SOUTH)
            .centered()
            .build();
        
        // Place coffee table between sofa and TV
        Furniture coffeeTable = room.place("Coffee Table", 1.2, 0.6, 0.4)
            .inCenter()
            .shiftNorth(1.0)
            .build();
        
        // Place chair next to sofa
        Furniture chair = room.place("Chair", 0.7, 0.7, 0.9)
            .nextTo(sofa)
            .onSide(Side.WEST)
            .withGap(0.2)
            .build();
        
        // Verify all furniture was added to room
        assertEquals(4, room.getFurniture().size());
        assertTrue(room.getFurniture().contains(sofa));
        assertTrue(room.getFurniture().contains(tv));
        assertTrue(room.getFurniture().contains(coffeeTable));
        assertTrue(room.getFurniture().contains(chair));
        
        // Verify positions
        assertEquals(5.2, sofa.getX(), 0.001); // 8.0 - 2.5 - 0.3
        assertEquals(0.3, sofa.getY(), 0.001);
        
        assertEquals(3.25, tv.getX(), 0.001); // (8.0 - 1.5) / 2
        assertEquals(5.6, tv.getY(), 0.001); // 6.0 - 0.4
        
        assertEquals(3.4, coffeeTable.getX(), 0.001); // (8.0 - 1.2) / 2
        assertEquals(1.7, coffeeTable.getY(), 0.001); // center - shift
        
        assertEquals(4.3, chair.getX(), 0.001); // sofa.x - chair.width - gap
        assertEquals(0.3, chair.getY(), 0.001); // same as sofa
    }
    
    @Test
    void testMultipleRelativePlacements() {
        // Create a chain of furniture
        Furniture desk = room.place("Desk", 1.5, 0.8, 0.75)
            .onWall(Wall.WEST)
            .fromNorth(2.0)
            .build();
        
        Furniture chair = room.place("Chair", 0.6, 0.6, 0.9)
            .nextTo(desk)
            .onSide(Side.EAST)
            .withGap(0.1)
            .build();
        
        Furniture lamp = room.place("Lamp", 0.3, 0.3, 1.2)
            .nextTo(chair)
            .onSide(Side.EAST)
            .withGap(0.05)
            .build();
        
        // Verify chain positioning
        assertEquals(0.0, desk.getX(), 0.001);
        assertEquals(2.0, desk.getY(), 0.001);
        
        assertEquals(1.6, chair.getX(), 0.001); // desk.x + desk.width + gap
        assertEquals(2.0, chair.getY(), 0.001);
        
        assertEquals(2.25, lamp.getX(), 0.001); // chair.x + chair.width + gap
        assertEquals(2.0, lamp.getY(), 0.001);
    }
    
    @Test
    void testWallPlacementWithShifts() {
        // Test all wall placement methods with shifts
        Furniture northItem = room.place("North", 1.0, 0.5, 0.8)
            .onWall(Wall.NORTH)
            .centered()
            .shiftEast(1.0)
            .build();
        
        Furniture eastItem = room.place("East", 0.5, 1.0, 0.8)
            .onWall(Wall.EAST)
            .fromNorth(1.5)
            .withGap(0.2)
            .build();
        
        Furniture southItem = room.place("South", 1.5, 0.3, 0.6)
            .onWall(Wall.SOUTH)
            .fromWest(2.0)
            .build();
        
        Furniture westItem = room.place("West", 0.4, 2.0, 1.5)
            .onWall(Wall.WEST)
            .centered()
            .shiftSouth(0.5)
            .build();
        
        // Verify positions
        assertEquals(4.5, northItem.getX(), 0.001); // centered + shift
        assertEquals(0.0, northItem.getY(), 0.001);
        
        assertEquals(7.3, eastItem.getX(), 0.001); // 8.0 - 0.5 - 0.2
        assertEquals(1.5, eastItem.getY(), 0.001);
        
        assertEquals(2.0, southItem.getX(), 0.001);
        assertEquals(5.7, southItem.getY(), 0.001); // 6.0 - 0.3
        
        assertEquals(0.0, westItem.getX(), 0.001);
        assertEquals(2.5, westItem.getY(), 0.001); // centered + shift
    }
    
    @Test
    void testRoomPlaceAndAddMethod() {
        // Test the simple placeAndAdd method
        Furniture furniture = room.placeAndAdd("Simple", 1.0, 1.0, 1.0);
        
        assertEquals("Simple", furniture.getName());
        assertEquals(3.5, furniture.getX(), 0.001); // centered
        assertEquals(2.5, furniture.getY(), 0.001); // centered
        assertTrue(room.getFurniture().contains(furniture));
    }
}
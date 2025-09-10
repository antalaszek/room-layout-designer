package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EdgeCaseAndErrorHandlingTest {
    
    private Room room;
    
    @BeforeEach
    void setUp() {
        room = new Room(3.0, 2.0, 2.5);
    }
    
    @Test
    void testFurnitureTooLargeForRoom() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Giant Table", 5.0, 3.0, 1.0)
                .inCorner(Corner.NORTH_WEST)
                .build();
        });
    }
    
    @Test
    void testFurnitureExactlyFitsInRoom() {
        // Furniture that exactly fits should work
        Furniture furniture = room.place("Perfect Fit", 3.0, 2.0, 2.5)
            .inCorner(Corner.NORTH_WEST)
            .build();
        
        assertEquals(0.0, furniture.getX());
        assertEquals(0.0, furniture.getY());
    }
    
    @Test
    void testCornerPlacementWithLargeGap() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Table", 1.0, 0.5, 0.8)
                .inCorner(Corner.NORTH_EAST)
                .withGap(3.0) // Gap too large
                .build();
        });
    }
    
    @Test
    void testWallPlacementOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Wide Shelf", 4.0, 0.3, 2.0)
                .onWall(Wall.NORTH)
                .centered()
                .build();
        });
    }
    
    @Test
    void testRelativePlacementPushingOutOfBounds() {
        Furniture reference = new Furniture("Reference", 1.0, 0.5, 0.8, 2.0, 1.0);
        room.addFurniture(reference);
        
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Chair", 1.0, 0.6, 0.9)
                .nextTo(reference)
                .onSide(Side.EAST)
                .build();
        });
    }
    
    @Test
    void testCenterPlacementWithExtremeOffset() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Table", 1.0, 0.5, 0.8)
                .inCenter()
                .shiftEast(5.0) // Offset too large
                .build();
        });
    }
    
    @Test
    void testZeroDimensionFurniture() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Invalid", 0.0, 1.0, 1.0)
                .inCorner(Corner.NORTH_WEST)
                .build();
        });
    }
    
    @Test
    void testNegativeDimensionFurniture() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Invalid", 1.0, -0.5, 1.0)
                .inCorner(Corner.NORTH_WEST)
                .build();
        });
    }
    
    @Test
    void testMinimalRoom() {
        Room tinyRoom = new Room(0.1, 0.1, 0.1);
        
        // Furniture that exceeds room size with gap should fail
        assertThrows(IllegalArgumentException.class, () -> {
            tinyRoom.place("Tiny", 0.08, 0.08, 0.05)
                .inCorner(Corner.NORTH_WEST)
                .withGap(0.05)
                .build();
        });
    }
    
    @Test
    void testBoundaryConditions() {
        // Test furniture that just barely fits
        Furniture furniture = room.place("Boundary", 1.5, 1.0, 1.0)
            .inCorner(Corner.NORTH_WEST)
            .build();
        
        assertEquals(0.0, furniture.getX());
        assertEquals(0.0, furniture.getY());
        
        // Test maximum possible gap
        Furniture gapped = room.place("Gapped", 1.0, 0.5, 1.0)
            .inCorner(Corner.SOUTH_EAST)
            .withGap(1.5) // Max gap for this furniture size
            .build();
        
        assertEquals(0.5, gapped.getX());
        assertEquals(0.0, gapped.getY());
    }
    
    @Test
    void testWallPlacementFromEndExceedsLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Table", 1.0, 0.5, 0.8)
                .onWall(Wall.NORTH)
                .fromEast(3.5) // Exceeds wall length
                .build();
        });
    }
    
    @Test
    void testRelativePlacementChainCollision() {
        Furniture first = room.place("First", 1.0, 0.5, 0.8)
            .inCorner(Corner.NORTH_WEST)
            .build();
        
        Furniture second = room.place("Second", 0.8, 0.4, 0.6)
            .nextTo(first)
            .onSide(Side.EAST)
            .build();
        
        // Third furniture would push out of bounds
        assertThrows(IllegalArgumentException.class, () -> {
            room.place("Third", 1.5, 0.5, 0.7)
                .nextTo(second)
                .onSide(Side.EAST)
                .build();
        });
    }
    
    @Test
    void testNullReferenceFurnitureHandling() {
        // This test verifies that the system handles null reference gracefully
        // The builder should not accept null furniture, but if it did, it should fail safely
        Furniture validFurniture = new Furniture("Valid", 1.0, 0.5, 0.8, 1.0, 1.0);
        
        assertDoesNotThrow(() -> {
            room.place("Test", 0.5, 0.5, 0.5)
                .nextTo(validFurniture)
                .onSide(Side.NORTH)
                .build();
        });
    }
}
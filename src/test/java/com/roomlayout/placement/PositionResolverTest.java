package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionResolverTest {
    
    private Room room;
    private Furniture furniture;
    
    @BeforeEach
    void setUp() {
        room = new Room(5.0, 4.0, 2.5);
        furniture = new Furniture("Test", 1.0, 0.5, 0.8, 0, 0);
    }
    
    @Test
    void testValidPositionResolution() {
        PlacementStrategy strategy = new CornerPlacementStrategy(Corner.NORTH_WEST);
        Point2D position = PositionResolver.resolve(strategy, room, furniture);
        
        assertEquals(0.0, position.getX());
        assertEquals(0.0, position.getY());
    }
    
    @Test
    void testNegativePositionThrowsException() {
        PlacementStrategy strategy = new CenterPlacementStrategy(-10.0, 0.0);
        
        assertThrows(IllegalArgumentException.class, 
            () -> PositionResolver.resolve(strategy, room, furniture));
    }
    
    @Test
    void testFurnitureOutOfBoundsXThrowsException() {
        PlacementStrategy strategy = new CenterPlacementStrategy(5.0, 0.0);
        
        assertThrows(IllegalArgumentException.class, 
            () -> PositionResolver.resolve(strategy, room, furniture));
    }
    
    @Test
    void testFurnitureOutOfBoundsYThrowsException() {
        PlacementStrategy strategy = new CenterPlacementStrategy(0.0, 4.0);
        
        assertThrows(IllegalArgumentException.class, 
            () -> PositionResolver.resolve(strategy, room, furniture));
    }
    
    @Test
    void testCreateFurnitureAtValidPosition() {
        PlacementStrategy strategy = new CornerPlacementStrategy(Corner.SOUTH_EAST);
        
        Furniture createdFurniture = PositionResolver.createFurnitureAt(
            "Created", 1.0, 0.5, 0.8, strategy, room);
        
        assertEquals("Created", createdFurniture.getName());
        assertEquals(1.0, createdFurniture.getWidth());
        assertEquals(0.5, createdFurniture.getLength());
        assertEquals(0.8, createdFurniture.getHeight());
        assertEquals(4.0, createdFurniture.getX());
        assertEquals(3.5, createdFurniture.getY());
    }
    
    @Test
    void testCreateFurnitureAtInvalidPosition() {
        Furniture largeFurniture = new Furniture("Large", 6.0, 5.0, 1.0, 0, 0);
        PlacementStrategy strategy = new CornerPlacementStrategy(Corner.NORTH_WEST);
        
        assertThrows(IllegalArgumentException.class, 
            () -> PositionResolver.createFurnitureAt("Large", 6.0, 5.0, 1.0, strategy, room));
    }
    
    @Test
    void testBoundaryPosition() {
        PlacementStrategy strategy = new CornerPlacementStrategy(Corner.SOUTH_EAST);
        Point2D position = PositionResolver.resolve(strategy, room, furniture);
        
        // Should be exactly at the boundary
        assertEquals(4.0, position.getX()); // 5.0 - 1.0
        assertEquals(3.5, position.getY()); // 4.0 - 0.5
    }
}
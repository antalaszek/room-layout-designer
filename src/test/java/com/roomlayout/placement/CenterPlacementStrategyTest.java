package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CenterPlacementStrategyTest {
    
    private Room room;
    private Furniture furniture;
    private PlacementContext context;
    
    @BeforeEach
    void setUp() {
        room = new Room(6.0, 4.0, 2.5);
        furniture = new Furniture("Test", 2.0, 1.0, 0.8, 0, 0);
        context = new PlacementContext(room, furniture);
    }
    
    @Test
    void testCenterPlacement() {
        CenterPlacementStrategy strategy = new CenterPlacementStrategy();
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(2.0, position.getX()); // (6.0 - 2.0) / 2
        assertEquals(1.5, position.getY()); // (4.0 - 1.0) / 2
    }
    
    @Test
    void testCenterPlacementWithOffsets() {
        CenterPlacementStrategy strategy = new CenterPlacementStrategy(1.0, -0.5);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(3.0, position.getX()); // center + xOffset
        assertEquals(1.0, position.getY()); // center + yOffset
    }
    
    @Test
    void testCenterPlacementWithNegativeOffsets() {
        CenterPlacementStrategy strategy = new CenterPlacementStrategy(-0.5, 0.5);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(1.5, position.getX());
        assertEquals(2.0, position.getY());
    }
    
    @Test
    void testCenterPlacementSmallFurniture() {
        Furniture smallFurniture = new Furniture("Small", 0.5, 0.3, 0.4, 0, 0);
        PlacementContext smallContext = new PlacementContext(room, smallFurniture);
        
        CenterPlacementStrategy strategy = new CenterPlacementStrategy();
        Point2D position = strategy.calculatePosition(smallContext);
        
        assertEquals(2.75, position.getX()); // (6.0 - 0.5) / 2
        assertEquals(1.85, position.getY()); // (4.0 - 0.3) / 2
    }
}
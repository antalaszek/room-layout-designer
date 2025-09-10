package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WallPlacementStrategyTest {
    
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
    void testNorthWallCentered() {
        WallPlacementStrategy strategy = new WallPlacementStrategy(Wall.NORTH, WallPlacementStrategy.WallPosition.CENTERED);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(2.0, position.getX()); // (6.0 - 2.0) / 2
        assertEquals(0.0, position.getY());
    }
    
    @Test
    void testSouthWallCentered() {
        WallPlacementStrategy strategy = new WallPlacementStrategy(Wall.SOUTH, WallPlacementStrategy.WallPosition.CENTERED);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(2.0, position.getX());
        assertEquals(3.0, position.getY()); // room.length - furniture.length
    }
    
    @Test
    void testEastWallCentered() {
        WallPlacementStrategy strategy = new WallPlacementStrategy(Wall.EAST, WallPlacementStrategy.WallPosition.CENTERED);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(4.0, position.getX()); // room.width - furniture.width - gap (6.0 - 2.0 - 0)
        assertEquals(1.5, position.getY()); // (4.0 - 1.0) / 2
    }
    
    @Test
    void testWestWallCentered() {
        WallPlacementStrategy strategy = new WallPlacementStrategy(Wall.WEST, WallPlacementStrategy.WallPosition.CENTERED);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(0.0, position.getX());
        assertEquals(1.5, position.getY());
    }
    
    @Test
    void testWallPlacementWithGap() {
        Gap gap = Gap.of(0.2);
        WallPlacementStrategy strategy = new WallPlacementStrategy(Wall.NORTH, WallPlacementStrategy.WallPosition.CENTERED, gap, 0.0);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(2.0, position.getX());
        assertEquals(0.2, position.getY());
    }
    
    @Test
    void testWallPlacementWithOffset() {
        WallPlacementStrategy strategy = new WallPlacementStrategy(Wall.NORTH, WallPlacementStrategy.WallPosition.CENTERED, Gap.NO_GAP, 1.0);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(3.0, position.getX()); // centered (2.0) + offset (1.0)
        assertEquals(0.0, position.getY());
    }
}
package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CornerPlacementStrategyTest {

    private PlacementContext context;
    
    @BeforeEach
    void setUp() {
        Room room = new Room(5.0, 4.0, 2.5);
        Furniture furniture = new Furniture("Test", 1.0, 0.5, 0.8, 0, 0);
        context = new PlacementContext(room, furniture);
    }
    
    @Test
    void testNorthWestCornerPlacement() {
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.NORTH_WEST);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(0.0, position.getX());
        assertEquals(0.0, position.getY());
    }
    
    @Test
    void testNorthEastCornerPlacement() {
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.NORTH_EAST);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(4.0, position.getX()); // room.width - furniture.width
        assertEquals(0.0, position.getY());
    }
    
    @Test
    void testSouthWestCornerPlacement() {
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.SOUTH_WEST);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(0.0, position.getX());
        assertEquals(3.5, position.getY()); // room.length - furniture.length
    }
    
    @Test
    void testSouthEastCornerPlacement() {
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.SOUTH_EAST);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(4.0, position.getX());
        assertEquals(3.5, position.getY());
    }
    
    @Test
    void testCornerPlacementWithGap() {
        Gap gap = Gap.of(0.2);
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.NORTH_WEST, gap);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(0.2, position.getX());
        assertEquals(0.2, position.getY());
    }
    
    @Test
    void testSouthEastCornerWithGap() {
        Gap gap = Gap.of(0.3);
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.SOUTH_EAST, gap);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(3.7, position.getX()); // 5.0 - 1.0 - 0.3
        assertEquals(3.2, position.getY()); // 4.0 - 0.5 - 0.3
    }
    
    @Test
    void testCornerPlacementWithShiftEast() {
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.SOUTH_WEST, Gap.NO_GAP, 0.3, 0.0);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(0.3, position.getX()); // 0.0 + 0.3 shift east
        assertEquals(3.5, position.getY()); // 4.0 - 0.5
    }
    
    @Test
    void testCornerPlacementWithShiftNorth() {
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.SOUTH_EAST, Gap.NO_GAP, 0.0, -0.4);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(4.0, position.getX()); // 5.0 - 1.0
        assertEquals(3.1, position.getY()); // 4.0 - 0.5 - 0.4 shift north
    }
    
    @Test
    void testCornerPlacementWithMultipleShifts() {
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.NORTH_WEST, Gap.NO_GAP, 0.2, 0.1);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(0.2, position.getX()); // 0.0 + 0.2 shift east
        assertEquals(0.1, position.getY()); // 0.0 + 0.1 shift south
    }
    
    @Test
    void testCornerPlacementWithGapAndShifts() {
        Gap gap = Gap.of(0.1);
        CornerPlacementStrategy strategy = new CornerPlacementStrategy(Corner.NORTH_EAST, gap, -0.3, 0.2);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(3.6, position.getX(), 0.001); // 5.0 - 1.0 - 0.1 - 0.3 shift west
        assertEquals(0.3, position.getY(), 0.001); // 0.0 + 0.1 + 0.2 shift south
    }
}
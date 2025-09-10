package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RelativePlacementStrategyTest {
    
    private Room room;
    private Furniture referenceFurniture;
    private Furniture targetFurniture;
    private PlacementContext context;
    
    @BeforeEach
    void setUp() {
        room = new Room(6.0, 4.0, 2.5);
        referenceFurniture = new Furniture("Reference", 2.0, 1.0, 0.8, 2.0, 1.5);
        targetFurniture = new Furniture("Target", 1.0, 0.5, 0.6, 0, 0);
        context = new PlacementContext(room, targetFurniture);
    }
    
    @Test
    void testPlaceToNorth() {
        RelativePlacementStrategy strategy = new RelativePlacementStrategy(referenceFurniture, Side.NORTH);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(2.0, position.getX()); // same as reference X
        assertEquals(1.0, position.getY()); // reference Y - target length
    }
    
    @Test
    void testPlaceToSouth() {
        RelativePlacementStrategy strategy = new RelativePlacementStrategy(referenceFurniture, Side.SOUTH);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(2.0, position.getX());
        assertEquals(2.5, position.getY()); // reference Y + reference length
    }
    
    @Test
    void testPlaceToEast() {
        RelativePlacementStrategy strategy = new RelativePlacementStrategy(referenceFurniture, Side.EAST);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(4.0, position.getX()); // reference X + reference width
        assertEquals(1.5, position.getY()); // same as reference Y
    }
    
    @Test
    void testPlaceToWest() {
        RelativePlacementStrategy strategy = new RelativePlacementStrategy(referenceFurniture, Side.WEST);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(1.0, position.getX()); // reference X - target width
        assertEquals(1.5, position.getY());
    }
    
    @Test
    void testRelativePlacementWithGap() {
        Gap gap = Gap.of(0.3);
        RelativePlacementStrategy strategy = new RelativePlacementStrategy(referenceFurniture, Side.EAST, gap);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(4.3, position.getX()); // reference X + reference width + gap
        assertEquals(1.5, position.getY());
    }
    
    @Test
    void testRelativePlacementWestWithGap() {
        Gap gap = Gap.of(0.2);
        RelativePlacementStrategy strategy = new RelativePlacementStrategy(referenceFurniture, Side.WEST, gap);
        Point2D position = strategy.calculatePosition(context);
        
        assertEquals(0.8, position.getX()); // reference X - target width - gap
        assertEquals(1.5, position.getY());
    }
}
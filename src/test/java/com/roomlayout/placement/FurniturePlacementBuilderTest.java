package com.roomlayout.placement;

import com.roomlayout.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FurniturePlacementBuilderTest {
    
    private Room room;
    
    @BeforeEach
    void setUp() {
        room = new Room(6.0, 4.0, 2.5);
    }
    
    @Test
    void testCornerPlacementBuilder() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Sofa", 2.0, 1.0, 0.8, room);
        
        Furniture furniture = builder.inCorner(Corner.NORTH_EAST).build();
        
        assertEquals("Sofa", furniture.getName());
        assertEquals(4.0, furniture.getX());
        assertEquals(0.0, furniture.getY());
        assertTrue(room.getFurniture().contains(furniture));
    }
    
    @Test
    void testCornerPlacementWithGap() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Table", 1.0, 0.5, 0.7, room);
        
        Furniture furniture = builder.inCorner(Corner.SOUTH_WEST).withGap(0.3).build();
        
        assertEquals(0.3, furniture.getX());
        assertEquals(3.2, furniture.getY()); // 4.0 - 0.5 - 0.3
    }
    
    @Test
    void testCornerPlacementWithShiftEast() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Sofa", 2.0, 1.0, 0.8, room);
        
        // South West corner + shift east by 0.2
        Furniture furniture = builder.inCorner(Corner.SOUTH_WEST).shiftEast(0.2).build();
        
        assertEquals(0.2, furniture.getX()); // 0.0 + 0.2 shift
        assertEquals(3.0, furniture.getY()); // 4.0 - 1.0 (furniture length)
    }
    
    @Test
    void testCornerPlacementWithShiftNorth() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Chair", 0.8, 0.6, 0.9, room);
        
        // South East corner + shift north by 0.5
        Furniture furniture = builder.inCorner(Corner.SOUTH_EAST).shiftNorth(0.5).build();
        
        assertEquals(5.2, furniture.getX()); // 6.0 - 0.8 (furniture width)
        assertEquals(2.9, furniture.getY()); // 4.0 - 0.6 - 0.5 shift
    }
    
    @Test
    void testCornerPlacementWithMultipleShifts() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Table", 1.0, 0.5, 0.7, room);
        
        // North West corner + shift south and east
        Furniture furniture = builder.inCorner(Corner.NORTH_WEST)
            .shiftSouth(0.3)
            .shiftEast(0.4)
            .build();
        
        assertEquals(0.4, furniture.getX()); // 0.0 + 0.4 shift east
        assertEquals(0.3, furniture.getY()); // 0.0 + 0.3 shift south
    }
    
    @Test
    void testCornerPlacementWithGapAndShift() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Bookshelf", 0.3, 1.8, 2.0, room);
        
        // North East corner with gap and shift west
        Furniture furniture = builder.inCorner(Corner.NORTH_EAST)
            .withGap(0.2)
            .shiftWest(0.5)
            .build();
        
        assertEquals(5.0, furniture.getX()); // 6.0 - 0.3 - 0.2 - 0.5 shift
        assertEquals(0.2, furniture.getY()); // gap from north wall
    }
    
    @Test
    void testWallPlacementCentered() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("TV", 1.5, 0.3, 0.6, room);
        
        Furniture furniture = builder.onWall(Wall.NORTH).centered().build();
        
        assertEquals(2.25, furniture.getX()); // (6.0 - 1.5) / 2
        assertEquals(0.0, furniture.getY());
    }
    
    @Test
    void testWallPlacementFromNorth() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Shelf", 0.3, 1.0, 2.0, room);
        
        Furniture furniture = builder.onWall(Wall.EAST).fromNorth(1.0).build();
        
        assertEquals(5.7, furniture.getX()); // room.width - furniture.width
        assertEquals(1.0, furniture.getY());
    }
    
    @Test
    void testWallPlacementWithShift() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Desk", 1.2, 0.6, 0.75, room);
        
        Furniture furniture = builder.onWall(Wall.SOUTH).centered().shiftEast(0.5).build();
        
        assertEquals(2.9, furniture.getX()); // centered (2.4) + shift (0.5)
        assertEquals(3.4, furniture.getY()); // 4.0 - 0.6
    }
    
    @Test
    void testRelativePlacement() {
        Furniture referenceFurniture = new Furniture("Reference", 1.0, 0.5, 0.8, 2.0, 1.0);
        room.addFurniture(referenceFurniture);
        
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Chair", 0.6, 0.6, 0.9, room);
        
        Furniture furniture = builder.nextTo(referenceFurniture).onSide(Side.EAST).build();
        
        assertEquals(3.0, furniture.getX()); // reference X + reference width
        assertEquals(1.0, furniture.getY()); // same as reference Y
    }
    
    @Test
    void testRelativePlacementWithGap() {
        Furniture referenceFurniture = new Furniture("Sofa", 2.0, 0.8, 0.8, 1.0, 1.5);
        room.addFurniture(referenceFurniture);
        
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Table", 0.8, 0.4, 0.5, room);
        
        Furniture furniture = builder.nextTo(referenceFurniture).onSide(Side.SOUTH).withGap(0.2).build();
        
        assertEquals(1.0, furniture.getX());
        assertEquals(2.5, furniture.getY()); // reference Y + reference length + gap
    }
    
    @Test
    void testCenterPlacement() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Rug", 2.0, 1.5, 0.1, room);
        
        Furniture furniture = builder.inCenter().build();
        
        assertEquals(2.0, furniture.getX()); // (6.0 - 2.0) / 2
        assertEquals(1.25, furniture.getY()); // (4.0 - 1.5) / 2
    }
    
    @Test
    void testCenterPlacementWithShift() {
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Table", 1.0, 0.6, 0.4, room);
        
        Furniture furniture = builder.inCenter().shiftNorth(0.5).shiftWest(0.3).build();
        
        assertEquals(2.2, furniture.getX()); // center (2.5) - shiftWest (0.3)
        assertEquals(1.2, furniture.getY()); // center (1.7) - shiftNorth (0.5)
    }
    
    @Test
    void testRelativePlacementWithoutSideThrowsException() {
        Furniture referenceFurniture = new Furniture("Reference", 1.0, 0.5, 0.8, 2.0, 1.0);
        room.addFurniture(referenceFurniture);
        
        FurniturePlacementBuilder builder = new FurniturePlacementBuilder("Chair", 0.6, 0.6, 0.9, room);
        
        assertThrows(IllegalStateException.class, 
            () -> builder.nextTo(referenceFurniture).build());
    }
}
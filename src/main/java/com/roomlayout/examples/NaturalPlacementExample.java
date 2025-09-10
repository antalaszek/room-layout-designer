package com.roomlayout.examples;

import com.roomlayout.model.*;
import com.roomlayout.placement.Corner;
import com.roomlayout.placement.Side;
import com.roomlayout.visualization.ConsoleVisualizer;

public class NaturalPlacementExample {
    public static void main(String[] args) {
        Room room = new Room(5.0, 4.0, 2.5);
        
        // Place sofa in North East corner with 0.2m gap
        Furniture sofa = room.place("Sofa", 2.0, 0.8, 0.8)
            .inCorner(Corner.NORTH_EAST)
            .withGap(0.2)
            .build();
        
        // Place TV on North wall, centered
        Furniture tv = room.place("TV", 1.2, 0.3, 0.6)
            .onWall(Wall.NORTH)
            .centered()
            .build();
        
        // Place coffee table in center, shifted slightly south
        Furniture coffeeTable = room.place("Coffee Table", 1.0, 0.6, 0.4)
            .inCenter()
            .shiftSouth(0.5)
            .build();
        
        // Place chair next to sofa on the west side
        Furniture chair = room.place("Chair", 0.6, 0.6, 0.9)
            .nextTo(sofa)
            .onSide(Side.WEST)
            .withGap(0.1)
            .build();
        
        // Place side table next to chair
        Furniture sideTable = room.place("Side Table", 0.4, 0.4, 0.5)
            .nextTo(chair)
            .onSide(Side.SOUTH)
            .withGap(0.05)
            .build();
        
        // Place bookshelf on East wall, 1m from North
        Furniture bookshelf = room.place("Bookshelf", 0.3, 1.5, 2.0)
            .onWall(Wall.EAST)
            .fromNorth(1.0)
            .withGap(0.1)
            .build();
        
        // Place desk in South West corner
        Furniture desk = room.place("Desk", 1.2, 0.6, 0.75)
            .inCorner(Corner.SOUTH_WEST)
            .withGap(0.15)
            .build();
        
        // Print room layout
        System.out.println("Natural Placement Example");
        System.out.println("========================");
        System.out.println(room);
        System.out.println();
        
        System.out.println("Furniture placed:");
        for (Furniture f : room.getFurniture()) {
            System.out.println("- " + f);
        }
        System.out.println();
        
        // Visualize
        ConsoleVisualizer visualizer = new ConsoleVisualizer(room);
        visualizer.visualizeFloorPlan();
    }
}
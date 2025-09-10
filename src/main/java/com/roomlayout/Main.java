package com.roomlayout;

import com.roomlayout.model.*;
import com.roomlayout.visualization.ImageVisualizer;

public class Main {
    public static void main(String[] args) {
        Room bedroom = createBedroom();

        System.out.println("IMAGE VISUALIZATION");
        System.out.println("=".repeat(60));

        String outputDir = "room-layouts";

        System.out.println("\nGenerating images for Bedroom...");
        ImageVisualizer imageVis3 = new ImageVisualizer(bedroom, outputDir + "/bedroom");
        imageVis3.visualizeAll();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Demo Complete!");
        System.out.println("Console output displayed above.");
        System.out.println("Images saved in: " + outputDir);
        System.out.println("=".repeat(60));
    }

    private static Room createBedroom() {
        Room bedroom = new Room(6, 3.5, 4);

        bedroom.addFurniture(new Furniture("Bed", 2.0, 1.4, 0.6, 1.0, 1.8));
        bedroom.addFurniture(new Furniture("Nightstand", 0.5, 0.4, 0.5, 0.3, 2.0));
        bedroom.addFurniture(new Furniture("Wardrobe", 1.5, 0.6, 2.2, 2.8, 0.2));
        bedroom.addFurniture(new Furniture("Dresser", 1.0, 0.5, 0.8, 0.2, 0.5));

        bedroom.addDoor(new Door(Wall.EAST, 1.5, 0.8, 2.1));
        bedroom.addWindow(new Window(Wall.SOUTH, 1.5, 1.5, 1.0, 1.0));

        return bedroom;
    }
}
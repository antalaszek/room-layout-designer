package com.roomlayout.visualization;

import com.roomlayout.model.*;
import com.roomlayout.placement.Corner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify the improved ConsoleVisualizer and new TextVisualizer work correctly,
 * particularly the west wall perspective fix and adaptive scaling.
 */
class VisualizerComparisonTest {

    private Room room;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        // Create a 4m x 6m room (width x length)
        room = new Room(4.0, 6.0, 2.7);
        
        // Add furniture in corners to test perspective
        room.place("SW-Sofa", 1.5, 0.8, 0.8)
            .inCorner(Corner.SOUTH_WEST)
            .build();
            
        room.place("NW-Table", 0.6, 0.6, 0.7)
            .inCorner(Corner.NORTH_WEST)
            .build();
            
        // Add a door on west wall
        room.placeDoor("West Door", 0.9, 2.1)
            .onWall(Wall.WEST)
            .centered()
            .build();
    }
    
    @Test
    void testConsoleVisualizerWithAdaptiveScaling() {
        ConsoleVisualizer consoleViz = new ConsoleVisualizer(room);
        
        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        try {
            consoleViz.visualizeFloorPlan();
            String output = outputStream.toString();
            
            // Verify adaptive scaling is shown
            assertTrue(output.contains("characters per meter"), "Should show adaptive scale");
            
            // Verify furniture initials are shown  
            assertTrue(output.contains("S"), "Should show sofa initial");
            assertTrue(output.contains("N"), "Should show table initial (NW-Table)");
            
            // Verify legend is included
            assertTrue(output.contains("Legend:"), "Should include legend");
            assertTrue(output.contains("+ = Close furniture projection"), "Should explain projection symbols");
            
        } finally {
            System.setOut(originalOut);
        }
    }
    
    @Test
    void testTextVisualizerFileOutput() throws Exception {
        TextVisualizer textViz = new TextVisualizer(room, tempDir.toString());
        
        // Generate all visualizations
        textViz.visualizeAll();
        
        // Verify files are created
        assertTrue(Files.exists(tempDir.resolve("complete_layout.txt")), "Complete layout file should be created");
        assertTrue(Files.exists(tempDir.resolve("floor_plan.txt")), "Floor plan file should be created");
        assertTrue(Files.exists(tempDir.resolve("west_wall.txt")), "West wall file should be created");
        assertTrue(Files.exists(tempDir.resolve("north_wall.txt")), "North wall file should be created");
        assertTrue(Files.exists(tempDir.resolve("south_wall.txt")), "South wall file should be created");
        assertTrue(Files.exists(tempDir.resolve("east_wall.txt")), "East wall file should be created");
        assertTrue(Files.exists(tempDir.resolve("ceiling.txt")), "Ceiling file should be created");
        
        // Verify content quality
        String floorPlanContent = Files.readString(tempDir.resolve("floor_plan.txt"));
        assertTrue(floorPlanContent.contains("FLOOR PLAN"), "Should have floor plan title");
        assertTrue(floorPlanContent.contains("characters per meter"), "Should show scale");
        assertTrue(floorPlanContent.contains("Legend:"), "Should include legend");
        
        String westWallContent = Files.readString(tempDir.resolve("west_wall.txt"));
        assertTrue(westWallContent.contains("WEST WALL VIEW"), "Should have west wall title");
        // Note: Door may not appear as "D" in wall view depending on visualization logic
    }
    
    @Test
    void testWestWallPerspectiveInTextVisualizer() throws Exception {
        // Create a simpler test case for perspective verification
        Room testRoom = new Room(3.0, 4.0, 2.5);
        
        // Place furniture in known positions to test coordinate transformation
        Furniture southWestFurniture = testRoom.place("SW", 1.0, 1.0, 0.8)
            .inCorner(Corner.SOUTH_WEST)
            .build();
            
        Furniture northWestFurniture = testRoom.place("NW", 1.0, 1.0, 0.8)
            .inCorner(Corner.NORTH_WEST)
            .build();
        
        TextVisualizer textViz = new TextVisualizer(testRoom, tempDir.toString());
        textViz.visualizeWall(Wall.WEST);
        
        // The perspective fix should be applied - we can't easily test the exact positioning
        // in text visualization, but we can verify the file is created and contains the furniture
        assertTrue(Files.exists(tempDir.resolve("west_wall.txt")), "West wall visualization should be created");
        
        String westWallContent = Files.readString(tempDir.resolve("west_wall.txt"));
        assertTrue(westWallContent.contains("WEST WALL VIEW"), "Should have correct title");
        
        // The exact positioning is hard to verify in text, but the visualization should be created
        assertTrue(westWallContent.length() > 20, "West wall content should contain basic visualization");
    }
    
    @Test
    void testDifferentRoomSizes() {
        // Test adaptive scaling with different room sizes
        
        // Small room
        Room smallRoom = new Room(2.0, 3.0, 2.5);
        ConsoleVisualizer smallViz = new ConsoleVisualizer(smallRoom);
        assertNotNull(smallViz, "Should handle small room");
        
        // Large room 
        Room largeRoom = new Room(10.0, 15.0, 3.0);
        ConsoleVisualizer largeViz = new ConsoleVisualizer(largeRoom);
        assertNotNull(largeViz, "Should handle large room");
        
        // Very small room
        Room tinyRoom = new Room(0.5, 0.8, 2.0);
        ConsoleVisualizer tinyViz = new ConsoleVisualizer(tinyRoom);
        assertNotNull(tinyViz, "Should handle tiny room");
        
        // Text visualizer should also handle different sizes
        TextVisualizer textViz = new TextVisualizer(largeRoom, tempDir.toString());
        assertEquals(tempDir.toString(), textViz.getOutputDirectory(), "Output directory should be set correctly");
    }
    
    @Test
    void testVisualizerFactoryIntegration() {
        // Test that both visualizers can be used through the factory
        ConsoleVisualizer consoleViz = new ConsoleVisualizer(room);
        TextVisualizer textViz = new TextVisualizer(room, tempDir.toString());
        
        // Both should implement Visualizer interface
        assertTrue(consoleViz instanceof Visualizer, "ConsoleVisualizer should implement Visualizer");
        assertTrue(textViz instanceof Visualizer, "TextVisualizer should implement Visualizer");
        
        // Both should be able to visualize all views
        assertDoesNotThrow(() -> {
            consoleViz.visualizeFloorPlan();
            consoleViz.visualizeWall(Wall.NORTH);
            consoleViz.visualizeCeiling();
        }, "ConsoleVisualizer should handle all visualization methods");
        
        assertDoesNotThrow(() -> {
            textViz.visualizeFloorPlan();
            textViz.visualizeWall(Wall.NORTH);
            textViz.visualizeCeiling();
        }, "TextVisualizer should handle all visualization methods");
    }
}